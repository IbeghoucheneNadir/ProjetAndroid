package mbds.api;

public class ApiUtils {

    private ApiUtils() {}

    public static ApiService getAPIService() {
        return RetrofitClient.getClient().create(ApiService.class);
    }
}