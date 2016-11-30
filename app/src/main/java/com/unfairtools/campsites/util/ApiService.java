package com.unfairtools.campsites.util;

/**
 * Created by brianroberts on 10/28/16.
 */
        import java.util.List;

        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.GET;
        import retrofit2.http.Header;
        import retrofit2.http.POST;
        import retrofit2.http.Path;
        import retrofit2.http.Query;

/**
 * Created by brianroberts on 10/12/16.
 */

public interface ApiService {

    @GET("boundsformarkers_get")
    Call<InfoObject> postBoundsForMarkersGet(@Query("lat_ne") String latNorthEast,
                                             @Query("lng_ne") String longNorthEast,
                                             @Query("lat_sw") String latSouthWest,
                                             @Query("lng_sw") String lngSouthWest);

    @POST ("boundsformarkers")
    Call<InfoObject> postBoundsForMarkers(@Body InfoObject info);

    @POST ("idformarkerinfo")
    Call<MarkerInfoObject> postIdForMarkerInfo(@Body InfoObject info);

    @POST("postforsearchinfo")
    Call<InfoObject> postForSearchResults(@Body InfoObject info);

//    @POST ("loginauth")
//    public Call<InfoObject> postLoginAuth(@Body InfoObject info);

    //
    @POST("login")
    public Call<InfoObject> postLogin(@Header("username") String username,
                                      @Header("password") String password);

//
}