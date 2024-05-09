package together.myapp.admin.soap;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaxResponseStatusSearch {

    Context context;

    String soapXml;

    String resultValue;
    String statusValue="";

    String sendResultValue;
    String sendValue;
    String user_nickname;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_paymentCheck;
    DatabaseReference databaseReference_paymentList;
    DatabaseReference databaseReference_billingkey;
    DatabaseReference databaseReference_paymentList_admin;
    String money = "0";
    public FaxResponseStatusSearch(String soapXml,String user_nickname ,Context context ){
        this.soapXml=soapXml;
        this.context = context;
        this.user_nickname = user_nickname;
    }

    public void sendSoapRequest(){
        RequestBody body = RequestBody.create(soapXml,MediaType.parse("text/xml; charset=utf-8"));

        Call<ResponseBody> call = FaxClientStatusSearch.getSoapService().sendFaxFromFTP(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {

                        String responseString = response.body().string();
                        Log.d("SOAP Response", responseString);


                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();

                        Document document = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(responseString)));
                        Element envelopeElement = document.getDocumentElement();
                        Element bodyElement = (Element) envelopeElement.getElementsByTagName("soap:Body").item(0);
                        Element GetFaxMessageExResponse = (Element) bodyElement.getElementsByTagName("GetFaxMessageExResponse").item(0);
                        Element GetFaxMessageExResult = (Element) GetFaxMessageExResponse.getElementsByTagName("GetFaxMessageExResult").item(0);
                        Element SendKey = (Element) GetFaxMessageExResult.getElementsByTagName("SendKey").item(0); //접수번호
                        Element ID = (Element) GetFaxMessageExResult.getElementsByTagName("ID").item(0); //바로빌 회원 아이디
                        Element SendFileName = (Element) GetFaxMessageExResult.getElementsByTagName("SendFileName").item(0);  //전송 파일명
                        Element SendPageCount = (Element) GetFaxMessageExResult.getElementsByTagName("SendPageCount").item(0);  //전송 페이지 수
                        Element SuccessPageCount = (Element) GetFaxMessageExResult.getElementsByTagName("SuccessPageCount").item(0);  //성공 페이지 수
                        Element SendResult = (Element) GetFaxMessageExResult.getElementsByTagName("SendResult").item(0);  //전송결과
                        Element SenderNum = (Element) GetFaxMessageExResult.getElementsByTagName("SenderNum").item(0); //발신번호
                        Element ReceiveCorp = (Element) GetFaxMessageExResult.getElementsByTagName("ReceiveCorp").item(0); //수신자 회사명
                        Element ReceiverName = (Element) GetFaxMessageExResult.getElementsByTagName("ReceiverName").item(0);  //수신자명
                        Element ReceiverNum = (Element) GetFaxMessageExResult.getElementsByTagName("ReceiverNum").item(0);  //수신번호
                        Element SendDT = (Element) GetFaxMessageExResult.getElementsByTagName("SendDT").item(0);  //전송일시
                        Element RefKey = (Element) GetFaxMessageExResult.getElementsByTagName("RefKey").item(0);  //관리번호 (파트너가 직접 부여하는 고유키)
                        Element SendState = (Element) GetFaxMessageExResult.getElementsByTagName("SendState").item(0);  //전송상태

                        if (SendState.getTextContent().length()==0||SendState.getTextContent()==null){
                            statusValue ="";
                        }
                        else {
                            resultValue = SendState.getTextContent();
                            if (Integer.parseInt(resultValue) < 0) { // API 호출 실패
                                statusValue = "전송실패";
                            } else if (Integer.parseInt(resultValue) == 0) { // 전송중 (파일 변환 대기중)
                                statusValue = "전송중(파일 변환 대기중)";
                            } else if (Integer.parseInt(resultValue) == 1) { // 전송중 (파일 변환중)
                                statusValue = "전송중(파일 변환중)";
                            } else if (Integer.parseInt(resultValue) == 2) { // 전송중 (파일 변환완료)
                                statusValue = "전송중(파일 변환완료)";
                            } else if (Integer.parseInt(resultValue) == 3) { //전송완료
                                statusValue = "전송완료";
                            } else if (Integer.parseInt(resultValue) == 4) { // 전송오류 (DB항목 오류)
                                statusValue = "전송실패";
                            } else if (Integer.parseInt(resultValue) == 5) { // 전송오류 (파일 변환중 오류)
                                statusValue = "전송실패";
                            }
                        }



                        if (SendResult.getTextContent().length()==0||SendResult.getTextContent()==null){
                            sendResultValue="";
                        }
                        else {
                            sendValue = SendResult.getTextContent();
                            if (Integer.parseInt(sendValue) < 100) { // API 호출 실패
                                sendResultValue = "과금실패";
                            } else if (Integer.parseInt(sendValue) == 101) { // 전송중 (파일 변환 대기중)
                                sendResultValue = "변환실패";
                            } else if (Integer.parseInt(sendValue) == 102) { // 전송중 (파일 변환중)
                                sendResultValue = "변환실패 (30분 타임아웃)";
                            } else if (Integer.parseInt(sendValue) == 801) { // 전송중 (파일 변환완료)
                                sendResultValue = "부분완료 (부분환불)";
                            } else if (Integer.parseInt(sendValue) == 802) { //전송완료
                                sendResultValue = "완료";
                            } else if (Integer.parseInt(sendValue) == 803) { // 전송오류 (DB항목 오류)
                                sendResultValue = "통화중 (재전송시도)";
                            } else if (Integer.parseInt(sendValue) == 804) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "잘못된 수신번호 (환불)";
                            } else if (Integer.parseInt(sendValue) == 805) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "응답없음 (환불)";
                            }else if (Integer.parseInt(sendValue) == 806) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "수화기들음";
                            }else if (Integer.parseInt(sendValue) == 807) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "수신거부 (환불)";
                            }else if (Integer.parseInt(sendValue) == 808) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "알수없는 오류 (환불)";
                            }else if (Integer.parseInt(sendValue) == 809) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "콜 혼잡 (환불)";
                            }else if (Integer.parseInt(sendValue) == 810) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "파일변환 오류 (환불)";
                            }else if (Integer.parseInt(sendValue) == 811) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "전송데이터 없음 (환불)";
                            }else if (Integer.parseInt(sendValue) == 812) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "파일저장오류 (환불)";
                            }else if (Integer.parseInt(sendValue) == 813) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "입력항목 오류 (환불)";
                            }else if (Integer.parseInt(sendValue) == 814) { // 전송오류 (파일 변환중 오류)
                                sendResultValue = "소켓통신오류 (환불)";
                            }
                        }

                        //팩스 id가 null이 아닐때
                        if (SendKey.getTextContent()!=null&&SendKey.getTextContent().length()!=0&&user_nickname!=null&&user_nickname.length()!=0){
                            /** 팩스 전송 내역 디비 실시간 저장 **/
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            /** 사용자  **/
                            databaseReference = firebaseDatabase.getReference().child("oojung_fax").child("send_list").child(user_nickname).child(SendKey.getTextContent());
                            /** 관리자 결제 관리 리스트 **/
                            databaseReference_paymentList_admin = firebaseDatabase.getReference().child("oojung_fax_admin").child(SendKey.getTextContent());
                            /** 팩스 전송 결제 내역 저장 **/
                            databaseReference_paymentList = firebaseDatabase.getReference().child("oojung_fax_paymend_request_List").child(SendKey.getTextContent());

                            databaseReference.child("SendPageCount").setValue(SendPageCount.getTextContent());
                            databaseReference.child("SuccessPageCount").setValue(SuccessPageCount.getTextContent());
                            databaseReference.child("SendResult").setValue(sendResultValue);
                            databaseReference.child("SendState").setValue(statusValue);

                            databaseReference_paymentList_admin.child("SendPageCount").setValue(SendPageCount.getTextContent());
                            databaseReference_paymentList_admin.child("SuccessPageCount").setValue(SuccessPageCount.getTextContent());
                            databaseReference_paymentList_admin.child("SendResult").setValue(sendResultValue);
                            databaseReference_paymentList_admin.child("SendState").setValue(statusValue);

                            databaseReference_paymentList.child("SendPageCount").setValue(SendPageCount.getTextContent());
                            databaseReference_paymentList.child("SuccessPageCount").setValue(SuccessPageCount.getTextContent());
                            databaseReference_paymentList.child("SendResult").setValue(sendResultValue);
                            databaseReference_paymentList.child("SendState").setValue(statusValue);

                            /** HashMap<String, Object> value = new HashMap<>(); 는 처음 데이터를 넣을떄 쓰고 만약 한번더 쓰면 기존 값을 다 덮어쓴다 **/
                            /** 그래서 두번쨰부터 값을 넣을떄는 위에처럼 넣는다 **/


                            /** 팩스 전송 완료 후 미결제일때 결제금액 계산해서 보내주기 **/
                            databaseReference_paymentCheck = firebaseDatabase.getReference().child("oojung_fax").child("send_list").child(user_nickname).child(SendKey.getTextContent()).child("payment_Status");
                            databaseReference_paymentCheck.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {
                                        String value = snapshot.getValue().toString();
                                        if (value.equals("x")){ //사용자 팩스 리스트에서 x표시는 최초 팩스 전송할떄만 표시되는 부분으로 결제 요청이되면 z로 바뀜 (한번만 호출됨 ) 하지만 결제 요청이 실패되면 x로 바뀜

                                            if (statusValue.equals("전송완료")){
                                                /** 결제 금액 보내주기 **/
                                                databaseReference_billingkey = firebaseDatabase.getReference().child("oojung_fax").child("send_list").child(user_nickname).child(SendKey.getTextContent()).child("billing_key");
                                                databaseReference_billingkey.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        try {
                                                            String billingKey = snapshot.getValue().toString();
                                                            money = String.valueOf(110*Integer.parseInt(SuccessPageCount.getTextContent()));

                                                            databaseReference_paymentList.child("SendPageCount").setValue(SendPageCount.getTextContent());
                                                            databaseReference_paymentList.child("SuccessPageCount").setValue(SuccessPageCount.getTextContent());
                                                            databaseReference_paymentList.child("SendResult").setValue(sendResultValue);
                                                            databaseReference_paymentList.child("SendState").setValue(statusValue);
                                                            databaseReference_paymentList.child("Request_Money").setValue(money);

                                                            //   databaseReference_paymentList.child("SendKey").setValue(SendKey.getTextContent());
                                                            //  databaseReference_paymentList.child("ReceiverNum").setValue(ReceiverNum.getTextContent());
                                                            //  databaseReference_paymentList.child("SendDT").setValue(SendDT.getTextContent());
                                                            //  databaseReference_paymentList.child("billing_key").setValue(billingKey);
                                                            //  databaseReference_paymentList.child("user").setValue(user_nickname);
                                                            //  databaseReference_paymentList.child("payment_Status").setValue("x"); //여기가 x로 되어있어야 서버에서 결제 요청이 된다
                                                            //  databaseReference_paymentList.child("response").setValue(""); //서버에서 호출받은 값

                                                            /** 관리자 관리 리스트에 업데이트 **/
                                                            databaseReference_paymentList_admin.child("SendPageCount").setValue(SendPageCount.getTextContent());
                                                            databaseReference_paymentList_admin.child("SuccessPageCount").setValue(SuccessPageCount.getTextContent());
                                                            databaseReference_paymentList_admin.child("SendResult").setValue(sendResultValue);
                                                            databaseReference_paymentList_admin.child("SendState").setValue(statusValue);
                                                            databaseReference_paymentList_admin.child("Request_Money").setValue(money); //o


                                                            /** 사용자 리스트에도 결제금액 업데이트 **/
                                                            databaseReference.child("payment_Money").setValue(money);
                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });



                                            }


                                        }
                                        else if (value.equals("결제완료")){

                                        }


                                        System.out.println("테스트 확인 : "+value);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }




                        // 여기서 응답을 처리합니다.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("SOAP Request", "Failed", t);
            }
        });
    }



}
