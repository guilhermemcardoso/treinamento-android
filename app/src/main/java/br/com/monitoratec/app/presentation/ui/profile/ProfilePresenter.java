package br.com.monitoratec.app.presentation.ui.profile;

import android.provider.ContactsContract;

import br.com.monitoratec.app.domain.repository.GitHubRepository;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View mView;
    private GitHubRepository mGitHubRepository;

    public ProfilePresenter(GitHubRepository gitHubRepository) {
        mGitHubRepository = gitHubRepository;
    }

    @Override
    public void setView(ProfileContract.View view) {
        mView = view;
    }

    @Override
    public void getUser(String authorization) {
        mGitHubRepository.getUser(authorization)
                .subscribe(user -> {
                    mView.onGetUserComplete(user);
                }, error -> {
                    mView.onError(error.getMessage());
                });
    }

    @Override
    public void loadZenMessage() {
        mGitHubRepository.getZenMessage()
                .subscribe(zenMessage -> {
                    mView.onLoadZenMessageComplete(zenMessage);
                }, error -> {
                    mView.onError(error.getMessage());
                });
    }
}
