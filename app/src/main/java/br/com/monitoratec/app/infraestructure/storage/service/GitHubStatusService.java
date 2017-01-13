package br.com.monitoratec.app.infraestructure.storage.service;

import br.com.monitoratec.app.domain.entity.Status;
import retrofit2.http.GET;
import rx.Observable;

public interface GitHubStatusService {

    String BASE_URL = "https://status.github.com/api/";

    @GET("last-message.json")
    Observable<Status> lastMessage();
}
