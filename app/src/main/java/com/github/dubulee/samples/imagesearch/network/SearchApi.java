package com.github.dubulee.samples.imagesearch.network;

import com.github.dubulee.samples.imagesearch.BuildConfig;
import com.github.dubulee.samples.imagesearch.search.SearchChannel;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class SearchApi {

    private Retrofit retrofit;

    @Inject
    public SearchApi(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<SearchChannel> searchText(String text, int pageCount) {
        return retrofit.create(Api.class)
                .getSearch(text, pageCount);
    }

    interface Api {
        @GET("image?output=json&apikey=" + BuildConfig.DAUM_API_KEY)
        Observable<SearchChannel> getSearch(@Query("q") String seachText, @Query("pageno") int pageNo);
    }
}
