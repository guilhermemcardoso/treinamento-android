package br.com.monitoratec.app.infraestructure.storage.service;

import br.com.monitoratec.app.domain.entity.User;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

public interface GitHubService {

    String BASE_URL = "https://api.github.com/";

    @GET("user")
    Observable<User> getUser(@Header("Authorization") String credential);

    @GET("zen")
    Observable<String> getZenMessage();

    @GET("users/{userName}/repos")
    Observable<String> getReposOf(@Path("userName") String userName);

    @GET("user/repos")
    Observable<String> getMyRepos(@Header("Authorization") String credential);
}
