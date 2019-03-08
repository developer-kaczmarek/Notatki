package kaczmarek.notatki.ui.sectionsList.views;

import com.arellomobile.mvp.MvpView;

public interface SectionsListView extends MvpView {
    void updateRecyclerView();
    void showErrorCreation();
}
