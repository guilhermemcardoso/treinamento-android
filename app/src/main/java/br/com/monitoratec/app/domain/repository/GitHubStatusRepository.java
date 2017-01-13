package br.com.monitoratec.app.domain.repository;

import br.com.monitoratec.app.domain.entity.Status;
import retrofit2.http.GET;
import rx.Observable;

public interface GitHubStatusRepository {

    Observable<Status> lastMessage();
}
