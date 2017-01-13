package br.com.monitoratec.app.dagger.module.presentation;

import android.content.Context;

import br.com.monitoratec.app.presentation.helper.AppHelper;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public class HelperModule {
    @Provides
    @Reusable
    AppHelper provideTextHelper(Context context) {
        return new AppHelper(context);
    }
}
