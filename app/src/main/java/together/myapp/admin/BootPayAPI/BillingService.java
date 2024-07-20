package together.myapp.admin.BootPayAPI;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;


public class BillingService {


    static public HttpResponse requestSubscribe(Bootpay bootpay, SubscribePayload payload) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(payload.billingKey == null || payload.billingKey.isEmpty()) throw new Exception("billing_key 값을 입력해주세요.");
        if(payload.itemName == null || payload.itemName.isEmpty()) throw new Exception("item_name 값을 입력해주세요.");
        if(payload.price <= 0) throw new Exception("price 금액을 설정을 해주세요.");
        if(payload.orderId == null || payload.orderId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("subscribe/billing", new StringEntity(gson.toJson(payload), "UTF-8"));
        post.setHeader("Authorization", bootpay.token);
        return client.execute(post);
    }

}
