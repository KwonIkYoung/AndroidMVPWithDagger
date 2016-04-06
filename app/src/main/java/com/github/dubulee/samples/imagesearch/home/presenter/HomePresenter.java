package com.github.dubulee.samples.imagesearch.home.presenter;

//Model-Presenter-View
//View는 어떠한 presenter와 연결될 지를 결정하여 스스로 제어될 곳을 결정합니다
//View 로부터 제어권을 할당받은 Presenter 는 Model 을 통해서 필요한 로직을 수행한 후 View 에게 다시 보여질 화면을 구현하도록 요청합니다.
//View:Presenter = 1:1
//Vie와 Presenter는 서로 참조하고 있습니다
//View와 모델은 서로의 존재를 몰라야 합니다
//View는 실제 View에 직접 접근하여 화면을 갱신합니다. 그리고 View에서 발생하는 이벤트를 Prewenter로 전달합니다

public interface HomePresenter {

    void inputSearchText(String searchText);

    void unSubscribeSearch();

    void onItemClick(int position);

    interface View {

        void refresh();

        void onMoveLink(String link);
    }
}
