package together.myapp.admin.Object;

public class Ob_Fax_Status {
    String ReceiverNum;  //수신번호   ooo
    String Request_Money;  //결제 금액 ooo
    String SendDT;  //전송일시   oo
    String SendKey; //접수번호 oo
    String SendPageCount;  //전송 페이지 수  ooo
    String SendResult;  //전송결과  ooo
    String SendState;  //전송상태  ooo
    String SuccessPageCount;  //성공 페이지 수 ooo
    String billing_key;  //빌링키 oo
    String payment_Status;  //결제 상태 ooo
    String user; //사용자


    public String getReceiverNum() {
        return ReceiverNum;
    }

    public void setReceiverNum(String receiverNum) {
        ReceiverNum = receiverNum;
    }

    public String getRequest_Money() {
        return Request_Money;
    }

    public void setRequest_Money(String request_Money) {
        Request_Money = request_Money;
    }

    public String getSendDT() {
        return SendDT;
    }

    public void setSendDT(String sendDT) {
        SendDT = sendDT;
    }

    public String getSendKey() {
        return SendKey;
    }

    public void setSendKey(String sendKey) {
        SendKey = sendKey;
    }

    public String getSendPageCount() {
        return SendPageCount;
    }

    public void setSendPageCount(String sendPageCount) {
        SendPageCount = sendPageCount;
    }

    public String getSendResult() {
        return SendResult;
    }

    public void setSendResult(String sendResult) {
        SendResult = sendResult;
    }

    public String getSendState() {
        return SendState;
    }

    public void setSendState(String sendState) {
        SendState = sendState;
    }

    public String getSuccessPageCount() {
        return SuccessPageCount;
    }

    public void setSuccessPageCount(String successPageCount) {
        SuccessPageCount = successPageCount;
    }

    public String getBilling_key() {
        return billing_key;
    }

    public void setBilling_key(String billing_key) {
        this.billing_key = billing_key;
    }

    public String getPayment_Status() {
        return payment_Status;
    }

    public void setPayment_Status(String payment_Status) {
        this.payment_Status = payment_Status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
