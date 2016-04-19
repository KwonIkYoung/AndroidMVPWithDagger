package com.github.dubulee.samples.imagesearch.home.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.github.dubu.runtimepermissionshelper.rxver.RxPermissions;
import com.github.dubulee.samples.imagesearch.R;
import com.github.dubulee.samples.imagesearch.home.adapter.ImageAdapter;
import com.github.dubulee.samples.imagesearch.home.adapter.ImageAdapterDataView;
import com.github.dubulee.samples.imagesearch.home.dagger.ApplicationComponent;
import com.github.dubulee.samples.imagesearch.home.dagger.DaggerHomeComponent;
import com.github.dubulee.samples.imagesearch.home.dagger.HomeModule;
import com.github.dubulee.samples.imagesearch.home.dagger.SampleApplication;
import com.github.dubulee.samples.imagesearch.home.presenter.HomePresenter;
import com.github.dubulee.samples.imagesearch.views.EndlessRecyclerViewScrollListener;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import rx.Subscriber;

public class HomeActivity extends AppCompatActivity implements HomePresenter.View{

    @Inject
    HomePresenter homePresenter;

    @Inject
    ImageAdapterDataView imageAdapterDataView;

    @Inject
    LinearLayoutManager layoutManager;

    @Inject
    RxPermissions rxPermissions;

    @Bind(R.id.et_home_search)
    EditText etSearch;

    @Bind(R.id.rv_home_search_result)
    RecyclerView rvSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageAdapter adapter = new ImageAdapter(HomeActivity.this);

        //Dagger로 Inject
        ApplicationComponent applicationComponent = ((SampleApplication) getApplication()).getApplicationComponent();
        DaggerHomeComponent.builder()
                .applicationComponent(applicationComponent)
                .homeModule(new HomeModule(this, adapter, new LinearLayoutManager(HomeActivity.this)))
                .build()
                .inject(this);

        ButterKnife.bind(this);

        rvSearchResult.setAdapter(adapter);
        rvSearchResult.setLayoutManager(layoutManager);

        imageAdapterDataView.setOnRecyclerItemClickListener((adapter1, position) -> {
            setPermission(position);
//            homePresenter.onItemClick(position);
        });

        rvSearchResult.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                homePresenter.plusPageCount(etSearch.getText().toString());
            }
        });
    }

    private void setPermission(int position) {
        rxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean) {
                            Toast.makeText(HomeActivity.this, "RX Permissions OK", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(HomeActivity.this,
                                    "Permission denied, can't enable the camera ", Toast.LENGTH_SHORT).show();
                        }

                        homePresenter.onItemClick(position);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        homePresenter.unSubscribeSearch();
        super.onDestroy();
    }

    @OnTextChanged(R.id.et_home_search)
    void onChangedSearchText(CharSequence text) {
        homePresenter.inputSearchText(text.toString(), true);
    }

    @Override
    public void refresh() {
        imageAdapterDataView.refresh();
    }

    @Override
    public void onMoveLink(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}
