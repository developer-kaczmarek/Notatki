package kaczmarek.notatki.ui.pagesList.interfaces;

import kaczmarek.notatki.di.services.database.models.Page;

public interface OnPageClickListener {
    void onPageClick(Page page);
}
