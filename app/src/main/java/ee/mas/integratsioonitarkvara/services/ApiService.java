package ee.mas.integratsioonitarkvara.services;

import ee.mas.integratsioonitarkvara.models.CommonConfig;
import ee.mas.integratsioonitarkvara.models.Edition;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("edition")
    Call<Edition> getEdition();

    @GET("config")
    Call<CommonConfig> getConfig();
}
