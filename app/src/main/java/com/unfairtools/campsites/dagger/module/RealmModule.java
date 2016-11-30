package com.unfairtools.campsites.dagger.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.unfairtools.campsites.R;
import com.unfairtools.campsites.util.ApiService;
import com.unfairtools.campsites.util.OpenHelper;

import java.security.KeyStore;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by brianroberts on 10/28/16.
 */

@Module
public class RealmModule {

    private Context context;


    public RealmModule(Context c) {
        context = c;
    }

    @Provides
    @Singleton
    ApiService provideApiService() {
        Retrofit retrofit = null;
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            KeyStore ks = KeyStore.getInstance("BKS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            ks.load(context.getResources().openRawResource(R.raw.mykeystore), "mysecret".toCharArray());
            trustManagerFactory.init(ks);
            sslcontext.init(null, trustManagerFactory.getTrustManagers(), null);


            X509TrustManager c = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslcontext.getSocketFactory(), c)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            Log.e("verify", s + " " + sslSession.toString());
                            if (s.equals("unfairtools.com")) {
                                Log.e("Trust", "Trust verified: " + s);
                                return true;
                            } else {
                                Log.e("DENIED", "denied trust verifier: " + s);
                                return false;
                            }
                        }
                    })
                    .build();

            Log.e("RealmModule", "New realm module");
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://unfairtools.com/campsites/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retrofit.create(ApiService.class);

    }
}
