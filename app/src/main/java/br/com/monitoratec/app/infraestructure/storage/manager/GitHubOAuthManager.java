package br.com.monitoratec.app.infraestructure.storage.manager;

import br.com.monitoratec.app.domain.entity.AccessToken;
import br.com.monitoratec.app.domain.entity.User;
import br.com.monitoratec.app.domain.repository.GitHubOAuthRepository;
import br.com.monitoratec.app.domain.repository.GitHubRepository;
import br.com.monitoratec.app.infraestructure.storage.service.GitHubOAuthService;
import br.com.monitoratec.app.infraestructure.storage.service.GitHubService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GitHubOAuthManager implements GitHubOAuthRepository {

    private final GitHubOAuthService mGitHubOAuthService;

    public GitHubOAuthManager(GitHubOAuthService gitHubOAuthService) {
        mGitHubOAuthService = gitHubOAuthService;
    }

    @Override
    public Observable<AccessToken> accessToken(String clientId, String clientSecret, String code) {
        return mGitHubOAuthService.accessToken(clientId, clientSecret, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
