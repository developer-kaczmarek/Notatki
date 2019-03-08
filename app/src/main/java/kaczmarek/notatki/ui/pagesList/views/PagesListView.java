package kaczmarek.notatki.ui.pagesList.views;

import com.arellomobile.mvp.MvpView;

public interface PagesListView extends MvpView {

    void updateRecyclerViewPages();

    void showErrorCreation();
}
