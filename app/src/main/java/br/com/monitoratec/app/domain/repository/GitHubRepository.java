package br.com.monitoratec.app.domain.repository;

import br.com.monitoratec.app.domain.entity.User;
import rx.Observable;

public interface GitHubRepository {

    Observable<User> getUser(String credential);

    Observable<String> getZenMessage();
}
