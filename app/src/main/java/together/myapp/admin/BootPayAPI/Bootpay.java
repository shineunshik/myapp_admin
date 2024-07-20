package together.myapp.admin.BootPayAPI;

import org.apache.http.HttpResponse;



public class Bootpay extends BootpayObject {


    public Bootpay() { }

    public Bootpay(String rest_application_id, String private_key) {
        super(rest_application_id, private_key);
    }

    public Bootpay(String rest_application_id, String private_key, String devMode) {
        super(rest_application_id, private_key, devMode);
    }

    //billing

    public HttpResponse requestSubscribe(SubscribePayload payload) throws Exception {
        return BillingService.requestSubscribe(this, payload);
    }

}
