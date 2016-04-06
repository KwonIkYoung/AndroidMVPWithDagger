package com.github.dubulee.samples.imagesearch.network.dagger;

import com.github.dubulee.samples.imagesearch.network.SearchApi;
import com.github.dubulee.samples.imagesearch.network.retrofit.RetrofitCreator;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

//네트워크 모듈에서는 Retrofit과 SearchApi를 생성하여 제공합니다.
@Module
public class NetworkModule {

    @Provides
    public Retrofit provideRetrofit() {
        return RetrofitCreator.createRetrofit();
    }

    @Provides
    public SearchApi provideSearchApi(Retrofit retrofit) {
        return new SearchApi(retrofit);
    }
}
