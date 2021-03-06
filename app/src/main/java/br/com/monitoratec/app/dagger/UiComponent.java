package br.com.monitoratec.app.dagger;

import br.com.monitoratec.app.dagger.module.presentation.PresenterModule;
import br.com.monitoratec.app.dagger.scope.PerActivity;
import br.com.monitoratec.app.presentation.ui.auth.AuthActivity;
import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = {PresenterModule.class})
public interface UiComponent {
    void inject(AuthActivity activity);
}
