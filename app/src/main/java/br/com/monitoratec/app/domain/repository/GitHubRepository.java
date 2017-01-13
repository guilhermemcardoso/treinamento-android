package br.com.monitoratec.app.domain.repository;

import br.com.monitoratec.app.domain.entity.User;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

public interface GitHubRepository {

    Observable<User> basicAuth(String credential);
}
