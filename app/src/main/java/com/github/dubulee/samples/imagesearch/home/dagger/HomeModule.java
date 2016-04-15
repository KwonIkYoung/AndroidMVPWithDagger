package com.github.dubulee.samples.imagesearch.home.dagger;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.dubulee.samples.imagesearch.home.adapter.ImageAdapter;
import com.github.dubulee.samples.imagesearch.home.adapter.ImageAdapterDataModel;
import com.github.dubulee.samples.imagesearch.home.adapter.ImageAdapterDataView;
import com.github.dubulee.samples.imagesearch.home.presenter.HomePresenter;
import com.github.dubulee.samples.imagesearch.home.presenter.HomePresenterImpl;
import com.github.dubulee.samples.imagesearch.network.dagger.NetworkModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

//모듈은 우리가 주입할 필요가 있는 객체의 인스턴스들을 제공하는 클래스 입니다
//@Module로 어노테이팅함으로써 정의됩니다
//HomeModule은 NetworkModule을 포함하고 있습니다.
//HomeModule에서는 ImageAdapter, HomePresenter.View, Homepresenter를 제공합니다
//@Provides 어노테이팅을 사용 //내부에 주입될 객체들

//싱글톤이 필요하다면 @Singleton 어노테이팅을 통해서제공한다
//보통의 싱글톤들은 아마 프로젝트가 가질 수 있는 가장 위험한 의존 관계들일 것 입니다.
//무엇보다도 우리가 인스턴스를 생성하지 않는다는 사실로 인하여 그것을 어디서 사용하는지 매우 알기 힘듭니다.
//그래서 "숨겨진 의존 관계들"이 존재합니다.
//반대로 테스트를 위해 mock으로 대체 할 방법이 없으며 다른 모듈로 대체할 방법도 없습니다.
//그러므로 우리 코드의 유지보수와 테스트, 점진적인 개선이 어려워집니다.
//반면 주입된 싱글톤들은 싱글톤의 장점(유일한 인스턴스)을 가지면서도 어느 시점에서나 새로운 인스턴스를 생성할 수도 있습니다.
//따라서 서브클래스를 만들거나 공유할 인터페이스를 구현하도록 만들어 mock 또는 다른 코드 조각으로 교체하기 쉬워집니다.
@Module(includes = NetworkModule.class)
public class HomeModule {
    private Context activiyContext;
    private HomePresenter.View view;
    private ImageAdapter adapter;
    private LinearLayoutManager layoutManager;

    public HomeModule(Context ctx, HomePresenter.View view, ImageAdapter adapter, LinearLayoutManager layoutManager) {
        this.activiyContext = ctx;
        this.view = view;
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Provides
    ImageAdapterDataModel provideImageAdapterDataModel() {
        return adapter;
    }

    @Provides
    ImageAdapterDataView provideImageAdapterDataView() {
        return adapter;
    }

    @Provides
    HomePresenter provideHomePresenter(HomePresenterImpl homePresenter) {
        return homePresenter;
    }

    @Provides
    HomePresenter.View provideView() {
        return view;
    }

    @Provides
    LinearLayoutManager provideLayoutManager() {return layoutManager;}

//    @Singleton
    @Provides
    Context provideActiviyContext() {
        return this.activiyContext;
    }
}
