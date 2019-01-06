package mbds.api;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/createUser")
    @Headers({"Content-Type:application/json"})
    Call<CreateUser> createUser(@Body Map<String, String> params);

    @POST("/api/login")
    @Headers({"Content-Type:application/json"})
    Call<Login> login(@Body Map<String, String> params);

    @GET("/api/fetchMessages")
    Call<List<Message>> fetchMessages(@Header("Authorization") String authKey);
}
