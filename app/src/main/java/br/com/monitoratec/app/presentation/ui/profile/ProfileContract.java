package br.com.monitoratec.app.presentation.ui.profile;

import br.com.monitoratec.app.domain.entity.User;

public interface ProfileContract {

    interface View {
        void onLoadZenMessageComplete(String zenMessage);

        void onGetUserComplete(User user);

        void onError(String message);
    }

    interface Presenter {

        void setView(ProfileContract.View view);

        void getUser(String authorization);

        void loadZenMessage();

    }
}
