package com.github.dubulee.samples.imagesearch.home.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.github.dubulee.samples.imagesearch.R;
import com.github.dubulee.samples.imagesearch.home.adapter.ImageAdapter;
import com.github.dubulee.samples.imagesearch.home.adapter.ImageAdapterDataView;
import com.github.dubulee.samples.imagesearch.home.dagger.DaggerHomeComponent;
import com.github.dubulee.samples.imagesearch.home.dagger.HomeModule;
import com.github.dubulee.samples.imagesearch.home.presenter.HomePresenter;
import com.github.dubulee.samples.imagesearch.views.EndlessRecyclerViewScrollListener;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class HomeActivity extends AppCompatActivity implements HomePresenter.View{

    @Inject
    Context context;

    @Inject
    HomePresenter homePresenter;

    @Inject
    ImageAdapterDataView imageAdapterDataView;

    @Inject
    LinearLayoutManager layoutManager;

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
        DaggerHomeComponent.builder()
                .homeModule(new HomeModule(this, this, adapter, new LinearLayoutManager(HomeActivity.this)))
                .build()
                .inject(this);

        ButterKnife.bind(this);

        rvSearchResult.setAdapter(adapter);
        rvSearchResult.setLayoutManager(layoutManager);

        imageAdapterDataView.setOnRecyclerItemClickListener((adapter1, position) -> {
            homePresenter.onItemClick(position);
        });

        rvSearchResult.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                homePresenter.plusPageCount(etSearch.getText().toString());
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
