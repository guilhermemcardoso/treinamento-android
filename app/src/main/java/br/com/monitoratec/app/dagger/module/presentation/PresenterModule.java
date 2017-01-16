package br.com.monitoratec.app.dagger.module.presentation;

import br.com.monitoratec.app.dagger.scope.PerActivity;
import br.com.monitoratec.app.domain.repository.GitHubOAuthRepository;
import br.com.monitoratec.app.domain.repository.GitHubRepository;
import br.com.monitoratec.app.domain.repository.GitHubStatusRepository;
import br.com.monitoratec.app.presentation.ui.auth.AuthContract;
import br.com.monitoratec.app.presentation.ui.auth.AuthPresenter;
import br.com.monitoratec.app.presentation.ui.profile.ProfileContract;
import br.com.monitoratec.app.presentation.ui.profile.ProfilePresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @PerActivity
    @Provides
    AuthContract.Presenter provideMainPresenter(
            GitHubRepository gitHubRepository,
            GitHubStatusRepository gitHubStatusRepository,
            GitHubOAuthRepository gitHubOAuthRepository) {
        return new AuthPresenter(gitHubRepository,
                gitHubStatusRepository,
                gitHubOAuthRepository);
    }

    @PerActivity
    @Provides
    ProfileContract.Presenter provideProfilePresenter(
            GitHubRepository gitHubRepository) {
        return new ProfilePresenter(gitHubRepository);
    }
}
