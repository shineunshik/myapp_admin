package together.myapp.admin.soap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FaxResponseStatusSearchService {
    @Headers({
            "Content-Type: text/xml;charset=utf-8",
            "SOAPAction: http://ws.baroservice.com/GetFaxMessageEx"
    })
    @POST("/FAX.asmx")
    Call<ResponseBody> sendFaxFromFTP(@Body RequestBody body);

}