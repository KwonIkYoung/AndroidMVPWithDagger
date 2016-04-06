package com.github.dubulee.samples.imagesearch.home.adapter;

import com.github.dubulee.samples.imagesearch.views.OnRecyclerItemClickListener;

public interface ImageAdapterDataView {
    void refresh();

    void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener);
}
