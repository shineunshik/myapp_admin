package together.myapp.admin.BootPayAPI;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class BootpayObject {
    public String token;
    public String application_id;
    public String private_key;
    public String baseUrl;

    public BootpayObject() {}
    public BootpayObject(String rest_application_id, String private_key) {
        this.application_id = rest_application_id;
        this.private_key = private_key;
        this.baseUrl = DevMode.PRODUCTION;
    }

    public BootpayObject(String rest_application_id, String private_key, String devMode) {
        this.application_id = rest_application_id;
        this.private_key = private_key;
        this.baseUrl = devMode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HttpGet httpGet(String url) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl + url);
        URI uri = new URIBuilder(get.getURI()).build();
        get.setURI(uri);
        return get;
    }

    public HttpGet httpGet(String url, List<NameValuePair> nameValuePairList) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl +url);
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Accept-Charset", "utf-8");
        URI uri = new URIBuilder(get.getURI()).addParameters(nameValuePairList).build();
        get.setURI(uri);
        return get;
    }

    public HttpPost httpPost(String url, StringEntity entity) {
        HttpPost post = new HttpPost(this.baseUrl + url);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept-Charset", "utf-8");
        post.setEntity(entity);
        return post;
    }

    public HttpDelete httpDelete(String url) {
        HttpDelete delete = new HttpDelete(this.baseUrl + url);
        delete.setHeader("Accept", "application/json");
        delete.setHeader("Content-Type", "application/json");
        delete.setHeader("Accept-Charset", "utf-8");
        return delete;
    }

    public HttpPut httpPut(String url, StringEntity entity) {
        HttpPut put = new HttpPut(this.baseUrl + url);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-Type", "application/json");
        put.setHeader("Accept-Charset", "utf-8");
        put.setEntity(entity);
        return put;
    }

    public String  getAccessToken() throws Exception {
        if(this.application_id == null || this.application_id.isEmpty()) throw new Exception("application_id 값이 비어있습니다.");
        if(this.private_key == null || this.private_key.isEmpty()) throw new Exception("private_key 값이 비어있습니다.");

        Token token = new Token();
        token.application_id = this.application_id;
        token.private_key = this.private_key;




        // API 요청을 위한 URL 설정
        URL url = new URL("https://api.bootpay.co.kr/v2/request/token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 요청 메서드 설정
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        // JSON 데이터를 요청 바디에 쓰기
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(new Gson().toJson(token).getBytes("UTF-8"));
        }

        // 서버로부터 응답 코드 확인
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 서버 응답 데이터 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(response.toString());

            System.out.println("토큰 반환 : "+response.toString());
            System.out.println("토큰 반환 : "+jsonObject.getString("access_token"));

            // JSON 데이터를 객체로 변환
            ResToken resToken = new Gson().fromJson(response.toString(), ResToken.class);

            // 응답이 성공적이면 토큰 저장
            if (resToken.status == 200) {
                this.token = resToken.data.token;
            }

            // HttpResponse 객체 반환 (실제로 사용하는 경우에 맞게 반환)
            // 여기서는 HttpResponse를 반환했으나, 실제 사용하는 HttpResponse 객체를 반환하도록 수정 필요
            return jsonObject.getString("access_token");
        } else {
            throw new Exception("HTTP 응답 코드: " + responseCode);
        }
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPost post = httpPost("request/token.json", new StringEntity(new Gson().toJson(token), "UTF-8"));
//
//        HttpResponse res = client.execute(post);
//        String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
//        ResToken resToken = new Gson().fromJson(str, ResToken.class);
//
//        if(resToken.status == 200)
//            this.token = resToken.data.token;
//
//        return res;
    }
}
