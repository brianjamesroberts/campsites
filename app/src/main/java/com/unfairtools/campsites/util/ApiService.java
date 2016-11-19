package com.unfairtools.campsites.util;

/**
 * Created by brianroberts on 10/28/16.
 */
        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.Header;
        import retrofit2.http.POST;

/**
 * Created by brianroberts on 10/12/16.
 */

public interface ApiService {

    @POST ("boundsformarkers")
    Call<InfoObject> postBoundsForMarkers(@Body InfoObject info);

    @POST ("idformarkerinfo")
    Call<MarkerInfoObject> postIdForMarkerInfo(@Body InfoObject info);

//    @POST ("loginauth")
//    public Call<InfoObject> postLoginAuth(@Body InfoObject info);

    //
    @POST("login")
    public Call<InfoObject> postLogin(@Header("username") String username, @Header("password") String password);

//
}