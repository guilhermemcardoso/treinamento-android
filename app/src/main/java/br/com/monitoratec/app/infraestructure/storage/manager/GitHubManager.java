package br.com.monitoratec.app.infraestructure.storage.manager;

import br.com.monitoratec.app.domain.entity.User;
import br.com.monitoratec.app.domain.repository.GitHubRepository;
import br.com.monitoratec.app.infraestructure.storage.service.GitHubService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GitHubManager implements GitHubRepository {

    private final GitHubService mGitHubService;

    public GitHubManager(GitHubService gitHubService) {
        mGitHubService = gitHubService;
    }

    @Override
    public Observable<User> basicAuth(String authorization) {
        return mGitHubService.basicAuth(authorization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
