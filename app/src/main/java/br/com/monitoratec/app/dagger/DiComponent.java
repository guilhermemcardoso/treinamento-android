package br.com.monitoratec.app.dagger;

import javax.inject.Singleton;

import br.com.monitoratec.app.dagger.module.infraestructure.ManagerModule;
import br.com.monitoratec.app.dagger.module.presentation.HelperModule;
import br.com.monitoratec.app.presentation.base.BaseActivity;
import br.com.monitoratec.app.presentation.ui.auth.AuthActivity;
import br.com.monitoratec.app.dagger.module.ApplicationModule;
import br.com.monitoratec.app.dagger.module.infraestructure.NetworkModule;
import br.com.monitoratec.app.dagger.module.PreferenceModule;
import br.com.monitoratec.app.dagger.module.infraestructure.ServiceModule;
import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        PreferenceModule.class,
        NetworkModule.class,
        ServiceModule.class,
        ManagerModule.class,
        HelperModule.class
})
public interface DiComponent {
    UiComponent uiComponent();
}
