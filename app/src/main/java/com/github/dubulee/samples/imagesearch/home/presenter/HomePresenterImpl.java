package com.github.dubulee.samples.imagesearch.home.presenter;

import android.text.TextUtils;

import com.github.dubulee.samples.imagesearch.home.adapter.ImageAdapterDataModel;
import com.github.dubulee.samples.imagesearch.network.SearchApi;
import com.github.dubulee.samples.imagesearch.search.ImageItem;
import com.github.dubulee.samples.imagesearch.search.SearchChannel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class HomePresenterImpl implements HomePresenter {

    // DI Object
    private View view;
    private SearchApi searchApi;
    private ImageAdapterDataModel imageAdapterDataModel;

    //구독자이자 발행자
    PublishSubject<String> searchSubject;
    //구독자
    Subscription searchSubscription;
    private int pageCount = 1;

    @Inject
    public HomePresenterImpl(View view, SearchApi searchApi, ImageAdapterDataModel imageAdapterDataModel) {
        this.view = view;
        this.searchApi = searchApi;
        this.imageAdapterDataModel = imageAdapterDataModel;

        searchSubject = PublishSubject.create();
        initSubscription();
    }

    //Reactive Extensions => ReactiveX
    //비동기처리와 이벤트 기반의 프로그램 개발을 위해 유용한 기능을 제공합니다
    //Observer패턴과 Iterator패턴 그리고 함수형 프로그래밍의 아이디어를 조합한형태입니다
    //그러므로 Observer패턴 Iterator패턴을 알고 계시면 더 잘 활용이 가능합니다

    //Observer패턴(발행/구독 모델): 객체의 상태변화를 관찰하는 관찰자들 즉 옵저버들의 목록을 객체에 등록하여
    //상태의 변화가 있때마다 메서드 등을 통해 객체가 직접 목록의 각 옵저버에게 통지하는 디자인패턴
    //주로 이벤트 핸들링 시스템을 구현하는데 사용
    //Subject는 이벤트 발생의 주체 ----RX ---> Observable(또는 Subject) :: 이벤트를 발생시킵니다
    //Observer는 이벤트 구독의 주체 ----RX ---> Subscriber :: 발생된 이벤트를 받아서 처리합니다


    //넘어오는 string을 발행합니다.
    //throttleWithTimeout으로 200 millisecond 간격을 줍니다
    //UI작업이 들어가기에 observeOn에서 Schedulers.io()로 설정합니다.
    private void initSubscription() {
        searchSubscription = searchSubject
                .onBackpressureBuffer()
                .throttleWithTimeout(200, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(text -> {
                    loadSearchResult(text);

                }, Throwable::printStackTrace);
    }

    //searchApi.serchText(text, pageCount)를 수행하면 Observable 형태의 결과값이 넘어옵니다.
    //filter, map, flatMap을 통해 데이터를 가공해 줍니다
    //filter에서 channel의 null 여부를 체크합니다
    //map에서는 SearchChannel의 getChannel함수를 통해 필요한 값을 가져옵니다
    //flatMap에서는 ImageResult의 item만 가져옵니다. 아마도 배열이 되겠죠
    //subscribe의 onNext에서 ImageAdapterDataModel의 add를 통해 최종 데이터를 집어넣습니다
    //subscribe의 onComplete에서는 Presenter.View의 refresh를 실행시켜 activity의 imageAdapterDataView.refresh();가 실행되게합니다
    //UI작업이 들어가기에 observeOn에서 AndroidSchedulers.mainThread()로 설정합니다.
    void loadSearchResult(String text) {
        searchApi.searchText(text, pageCount)
                .filter(channel -> channel != null && channel.getChannel() != null)
                .map(SearchChannel::getChannel)
                .filter(result -> result != null && result.getResult() > 0)
                .flatMap(imageResult -> Observable.from(imageResult.getItem()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageAdapterDataModel::add,
                        Throwable::printStackTrace,
                        view::refresh);
    }

    @Override
    public void inputSearchText(String searchText, boolean isStart) {
        //이전의searchSubject를 unsubscribe시켜준다
        if (!searchSubscription.isUnsubscribed()) {
            searchSubscription.unsubscribe();
        }

        if(isStart) {
            imageAdapterDataModel.clear();
        }

        initSubscription();

        if (!TextUtils.isEmpty(searchText)) {
            //searchSubject는 PublishSubject형태이므로 발행구독 모두 가능하며, 여기서는 발행중
            searchSubject.onNext(searchText);
        } else {
            //Presenter.View의 refresh를 실행시켜 activity의 imageAdapterDataView.refresh();가 실행
            view.refresh();
        }
    }

    @Override
    public void unSubscribeSearch() {
        searchSubscription.unsubscribe();
    }

    @Override
    public void onItemClick(int position) {
        ImageItem item = imageAdapterDataModel.getItem(position);
        String link = item.getLink();
        view.onMoveLink(link);
    }

    @Override
    public void plusPageCount(String searchText) {
        pageCount++;
        inputSearchText(searchText, false);
    }
}
