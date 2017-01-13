package br.com.monitoratec.app.domain.repository;

import br.com.monitoratec.app.domain.entity.AccessToken;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface GitHubOAuthRepository {

    Observable<AccessToken> accessToken(String clientId, String clientSecret, String code);
}
