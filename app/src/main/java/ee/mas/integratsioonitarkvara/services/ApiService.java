package ee.mas.integratsioonitarkvara.services;

import ee.mas.integratsioonitarkvara.models.CommonConfig;
import ee.mas.integratsioonitarkvara.models.DesktopCommand;
import ee.mas.integratsioonitarkvara.models.DesktopLayout;
import ee.mas.integratsioonitarkvara.models.Edition;
import ee.mas.integratsioonitarkvara.models.MarkuStationConfig;
import ee.mas.integratsioonitarkvara.models.MarkuStationGame;
import ee.mas.integratsioonitarkvara.models.Scheme;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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

    @PUT("config")
    Call<CommonConfig> saveCommonConfig(@Body CommonConfig newConfig);

    @PUT("desktop")
    Call<DesktopLayout> saveDesktopLayout(@Body DesktopLayout newLayout);

    @PUT("markustation/config")
    Call<MarkuStationConfig> saveMarkuStationConfig(@Body MarkuStationConfig newConfig);

    @PUT("markustation/games")
    Call<MarkuStationGame[]> saveMarkuStationGames(@Body MarkuStationGame[] newGames);

    @PUT("scheme")
    Call<Scheme> saveScheme(@Body Scheme scheme);

    @POST("desktop/command")
    Call<DesktopCommand> sendDesktopCommand(@Body DesktopCommand command);
}
