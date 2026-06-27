package ee.mas.integratsioonitarkvara.services;

import ee.mas.integratsioonitarkvara.models.CommonConfig;
import ee.mas.integratsioonitarkvara.models.DesktopLayout;
import ee.mas.integratsioonitarkvara.models.Edition;
import ee.mas.integratsioonitarkvara.models.MarkuStationConfig;
import ee.mas.integratsioonitarkvara.models.MarkuStationGame;
import ee.mas.integratsioonitarkvara.models.Scheme;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("edition")
    Call<Edition> getEdition();

    @GET("config")
    Call<CommonConfig> getConfig();

    @GET("markustation/config")
    Call<MarkuStationConfig> getMarkuStationConfig();

    @GET("markustation/games")
    Call<MarkuStationGame[]> getMarkuStationGames();

    @GET("scheme")
    Call<Scheme> getScheme();

    @GET("desktop")
    Call<DesktopLayout> getDesktopLayout();

    @POST("config")
    Call<CommonConfig> saveCommonConfig(@Body CommonConfig newConfig);

    @POST("markustation/config")
    Call<MarkuStationConfig> saveMarkuStationConfig(@Body MarkuStationConfig newConfig);

    @POST("scheme")
    Call<Scheme> saveScheme(@Body Scheme scheme);
}
