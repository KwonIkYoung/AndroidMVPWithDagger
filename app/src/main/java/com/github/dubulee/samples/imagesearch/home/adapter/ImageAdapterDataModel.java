package com.github.dubulee.samples.imagesearch.home.adapter;

import com.github.dubulee.samples.imagesearch.search.ImageItem;

public interface ImageAdapterDataModel {
    void add(ImageItem imageItem);
    int getSize();
    ImageItem getItem(int position);
    void clear();
}
