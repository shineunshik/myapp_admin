package together.myapp.admin.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.net.MediaType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import together.myapp.admin.Object.Ob_Fax_Status;
import together.myapp.admin.R;
import together.myapp.admin.soap.FaxResponseStatusSearch;

public class CustomAdapter_Fax_Status_List extends RecyclerView.Adapter<CustomAdapter_Fax_Status_List.CustomViewHolder> {

    ArrayList<Ob_Fax_Status> arrayList;
    Context context;
    /** fax API **/
    String certKey = "D8AEAC73-ED16-43C8-89D3-322DC58C11F9";
    //  String certKey = "FF73D08B-C795-484E-85A6-56CEF1962E86";
    String corpNum = "2153401358";

    public CustomAdapter_Fax_Status_List(ArrayList<Ob_Fax_Status> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter_Fax_Status_List.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fax_status_list_layout,parent,false);
        CustomAdapter_Fax_Status_List.CustomViewHolder customViewHolder = new CustomAdapter_Fax_Status_List.CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_Fax_Status_List.CustomViewHolder holder, int position) {

        LocalDateTime inputDateTime1 = LocalDateTime.parse(arrayList.get(position).getSendDT().substring(0,12), DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String resultDateTimeStr1 = inputDateTime1.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
        holder.SendDT.setText(resultDateTimeStr1 + "에 보냄");

        holder.SendState.setText(arrayList.get(position).getSendState());
        holder.ReceiverNum.setText(arrayList.get(position).getReceiverNum());
        holder.SendResult.setText(arrayList.get(position).getSendResult());
        holder.SendPageCount.setText(arrayList.get(position).getSendPageCount()+"/"+arrayList.get(position).getSuccessPageCount());
        if (arrayList.get(position).getPayment_Status().equals("x")){
            holder.payment_Status.setText("미결제");
            holder.payment_Status.setTextColor(Color.parseColor("#FF0000"));
        }
        else if (arrayList.get(position).getPayment_Status().equals("o")){
            holder.payment_Status.setText("결제완료");
            holder.payment_Status.setTextColor(Color.parseColor("#336185"));
        }
        else if (arrayList.get(position).getPayment_Status().equals("z")){
            holder.payment_Status.setText("미결제");
            holder.payment_Status.setTextColor(Color.parseColor("#FF0000"));
        }


        holder.Request_Money.setText(arrayList.get(position).getRequest_Money()+"원");
        holder.user.setText(arrayList.get(position).getUser());
        holder.billing_key.setText(arrayList.get(position).getBilling_key());
        holder.SendKey.setText(arrayList.get(position).getSendKey());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView SendPageCount;  //전송 페이지 수 ㅇㅇ
        TextView SuccessPageCount;  //성공 페이지 수 ㅇㅇ
        TextView SendResult;  //전송결과 ㅇㅇ
        TextView ReceiverNum;  //수신번호 ㅇㅇ
        TextView SendDT;  //전송일시  ㅇㅇ
        TextView SendState;  //전송상태 ㅇㅇ
        TextView Request_Money;  //결제 금액
        TextView payment_Status;  //결제상태
        TextView user;
        TextView billing_key;
        TextView SendKey;
        TextView pay_request;
        TextView status_request;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.SendPageCount = itemView.findViewById(R.id.SendPageCount);
            //    this.SuccessPageCount = itemView.findViewById(R.id.SuccessPageCount);
            this.SendResult = itemView.findViewById(R.id.SendResult);
            this.ReceiverNum = itemView.findViewById(R.id.ReceiverNum);
            this.SendDT = itemView.findViewById(R.id.SendDT);
            this.SendState = itemView.findViewById(R.id.SendState);
            this.Request_Money = itemView.findViewById(R.id.Request_Money);
            this.payment_Status = itemView.findViewById(R.id.payment_Status);
            this.user = itemView.findViewById(R.id.user);
            this.billing_key = itemView.findViewById(R.id.billing_key);
            this.SendKey = itemView.findViewById(R.id.SendKey);
            this.pay_request = itemView.findViewById(R.id.pay_request);
            this.status_request = itemView.findViewById(R.id.status_request);
            view = itemView;

            status_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();

                    /** 팩스 전송상태를 실시간으로 계속 확인 **/
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true){
                                try {

                                    /** 팩스 조회 번호로 보낸 팩스 정보를 조회한다. **/
                                    String sendxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                                            "           <soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                            "                          xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                                            "                          xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                                            "  <soap:Body>" +
                                            "    <GetFaxMessageEx xmlns=\"http://ws.baroservice.com/\">" +
                                            "       <CERTKEY>"+certKey+"</CERTKEY>" +
                                            "       <CorpNum>"+corpNum+"</CorpNum>" +
                                            "       <SendKey>"+arrayList.get(position).getSendKey()+"</SendKey>" +
                                            "    </GetFaxMessageEx>" +
                                            "  </soap:Body>" +
                                            "</soap:Envelope>";

                                    /**  접수번호를 보내서 상태 조회하는 곳 **/
                                    FaxResponseStatusSearch faxResponseStatusSearch = new FaxResponseStatusSearch(sendxml,arrayList.get(position).getUser(),context);
                                    faxResponseStatusSearch.sendSoapRequest();

                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }

//                            try {
//                                /** 팩스 조회 번호로 보낸 팩스 정보를 조회한다. **/
//                                String sendxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                                        "           <soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
//                                        "                          xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
//                                        "                          xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
//                                        "  <soap:Body>" +
//                                        "    <GetFaxMessageEx xmlns=\"http://ws.baroservice.com/\">" +
//                                        "       <CERTKEY>"+certKey+"</CERTKEY>" +
//                                        "       <CorpNum>"+corpNum+"</CorpNum>" +
//                                        "       <SendKey>"+arrayList.get(position).getSendKey()+"</SendKey>" +
//                                        "    </GetFaxMessageEx>" +
//                                        "  </soap:Body>" +
//                                        "</soap:Envelope>";
//
//                                /**  접수번호를 보내서 상태 조회하는 곳 **/
//                                FaxResponseStatusSearch faxResponseStatusSearch = new FaxResponseStatusSearch(sendxml,arrayList.get(position).getUser(),context);
//                                faxResponseStatusSearch.sendSoapRequest();
//                            }
//                            catch (Exception e){
//                                e.printStackTrace();
//                            }
                        }
                    }).start();




                }
            });


            pay_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
