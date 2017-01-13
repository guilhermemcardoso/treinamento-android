package br.com.monitoratec.app.infraestructure.storage.manager;

import br.com.monitoratec.app.domain.entity.Status;
import br.com.monitoratec.app.domain.entity.User;
import br.com.monitoratec.app.domain.repository.GitHubRepository;
import br.com.monitoratec.app.domain.repository.GitHubStatusRepository;
import br.com.monitoratec.app.infraestructure.storage.service.GitHubService;
import br.com.monitoratec.app.infraestructure.storage.service.GitHubStatusService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GitHubStatusManager implements GitHubStatusRepository {

    private final GitHubStatusService mGitHubStatusService;

    public GitHubStatusManager(GitHubStatusService gitHubStatusService) {
        mGitHubStatusService = gitHubStatusService;
    }

    @Override
    public Observable<Status> lastMessage() {
        return mGitHubStatusService.lastMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
