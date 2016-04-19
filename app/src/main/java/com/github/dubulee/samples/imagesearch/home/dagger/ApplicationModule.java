package com.github.dubulee.samples.imagesearch.home.dagger;

import android.content.Context;

import com.github.dubu.runtimepermissionshelper.rxver.RxPermissions;
import com.github.dubulee.samples.imagesearch.network.dagger.NetworkModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

//@Module(includes = NetworkModule.class)
@Module
public class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return context;
    }

    @Provides
    @Singleton
    public RxPermissions provideRxPermissions() {return RxPermissions.getInstance(context);}
}
