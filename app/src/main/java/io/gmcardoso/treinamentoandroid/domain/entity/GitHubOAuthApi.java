package io.gmcardoso.treinamentoandroid.domain.entity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by guilherme on 11/01/2017.
 */

public interface GitHubOAuthApi {
    String BASE_URL = "https://github.com/login/oauth/";

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("access_token")
    Call<AccessToken> accessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);
}
