package com.github.dubulee.samples.imagesearch.home.dagger;

import com.github.dubulee.samples.imagesearch.home.view.HomeActivity;

import dagger.Component;

//Module과 Inject간의 브릿지 역활을 합니다
//@Component 어노테이팅을 통해서 컴포넌트를 만들어줍니다
//모듈로는 HomeModule를 가집니다
//inject는 객체를 주입합니다

//해당 Component를 정의하고 컴파일하면 DaggerHomeComponent가 만들어지고 실지리적으로 객체 주입을 진행할수 있습니다
//DaggerHomeComponent.builder()...homeModule(new HomeModule()).build();
//AS 에서 shift x 2 를하여 DaggerHomeComponent를 검색해보자
@Component(modules = HomeModule.class)
public interface HomeComponent {
    void inject(HomeActivity homeActivity);
}
