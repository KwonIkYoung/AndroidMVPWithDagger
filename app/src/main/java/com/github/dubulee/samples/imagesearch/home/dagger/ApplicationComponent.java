package com.github.dubulee.samples.imagesearch.home.dagger;

import android.content.Context;

import com.github.dubu.runtimepermissionshelper.rxver.RxPermissions;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(SampleApplication app);

    //하위 component 에서 dependencies 로 잡혀 놓으면 context 가 필요하면 모듈에서 땡겨서 쓰도록 가능
    Context getContext();
    RxPermissions getRxPermissions();
}
