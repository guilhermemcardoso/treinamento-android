package io.gmcardoso.treinamentoandroid.domain.entity;

import android.net.Credentials;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * Created by guilherme on 11/01/2017.
 */

public interface GitHubApi {

    String BASE_URL = "https://api.github.com/";

    @GET("user")
    Call<User> login(@Header("Authorization") String credential);
}
