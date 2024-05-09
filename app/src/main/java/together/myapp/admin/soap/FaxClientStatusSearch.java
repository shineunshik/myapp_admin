package together.myapp.admin.soap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class FaxClientStatusSearch {

    private static final String BASE_URL = "http://testws.baroservice.com";
    //    private static final String BASE_URL = "http://ws.baroservice.com"; //운영 환경
    private static FaxResponseStatusSearchService faxResponseStatusSearchService;

    public static FaxResponseStatusSearchService getSoapService() {
        if (faxResponseStatusSearchService == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    // .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            faxResponseStatusSearchService = retrofit.create(FaxResponseStatusSearchService.class);
        }
        return faxResponseStatusSearchService;
    }

}
