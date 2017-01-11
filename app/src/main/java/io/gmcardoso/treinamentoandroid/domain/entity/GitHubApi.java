package io.gmcardoso.treinamentoandroid.domain.entity;

import android.net.Credentials;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by guilherme on 11/01/2017.
 */

public interface GitHubApi {

    String BASE_URL = "https://api.github.com/";

    Retrofit RETROFIT = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build();

    @GET("user")
    Call<User> basicAuth(@Header("Authorization") String credential);
}
