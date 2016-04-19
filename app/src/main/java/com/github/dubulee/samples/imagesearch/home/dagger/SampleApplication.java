package com.github.dubulee.samples.imagesearch.home.dagger;

import android.app.Application;

public class SampleApplication extends Application {
    private static final String TAG = SampleApplication.class.getSimpleName();

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule((this)))
                .build();
        applicationComponent.inject(this);

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
