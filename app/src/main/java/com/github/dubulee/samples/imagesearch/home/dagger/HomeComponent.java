package com.github.dubulee.samples.imagesearch.home.dagger;

import com.github.dubulee.samples.imagesearch.home.dagger.qualifier.PerActivity;
import com.github.dubulee.samples.imagesearch.home.view.HomeActivity;

import dagger.Component;

//Module과 Inject간의 브릿지 역활을 합니다
//@Component 어노테이팅을 통해서 컴포넌트를 만들어줍니다
//모듈로는 HomeModule를 가집니다
//inject는 객체를 주입합니다

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = HomeModule.class)
public interface HomeComponent {
    void inject(HomeActivity homeActivity);
}
