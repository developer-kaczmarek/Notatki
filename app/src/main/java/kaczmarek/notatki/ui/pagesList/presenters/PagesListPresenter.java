package kaczmarek.notatki.ui.pagesList.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import kaczmarek.notatki.BaseApplication;
import kaczmarek.notatki.di.services.database.PageDao;
import kaczmarek.notatki.di.services.database.models.Page;
import kaczmarek.notatki.ui.pagesList.views.PagesListView;

@InjectViewState
public class PagesListPresenter extends MvpPresenter<PagesListView> {
    private List<Page> listPages = new ArrayList<>();
    @Inject
    PageDao mPageDao;
    public PagesListPresenter() {
        BaseApplication.getComponent().inject(this);
    }

    public List<Page> getListPages(long receivedTitle) {
        mPageDao.getAllPagesForSection(receivedTitle).subscribe(pages -> {
            listPages.clear();
            listPages.addAll(pages);
        });
        return listPages;
    }

    public int getCountPages(long section) {
        return mPageDao.countPages(section);
    }

    public void addNewPage(String newTitlePage, long idSection) {
        if(mPageDao.getCountOfCopies(newTitlePage) == 0){
            Page newPage = new Page();
            newPage.setTitlePage(newTitlePage);
            newPage.setIdSection(idSection);
            newPage.setTextPage("");
            Date now = new Date();
            DateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            newPage.setDatePage(mDateFormat.format(now));
            mPageDao.insert(newPage);
            getViewState().updateRecyclerViewPages();
        } else
            getViewState().showErrorCreation();
    }

    public void deletePage(String title) {
        mPageDao.deleteByTitle(title);
        getViewState().updateRecyclerViewPages();
    }

    public void updatePage(String oldTitle, String title) {
        mPageDao.updateTitlePage(oldTitle, title);
        getViewState().updateRecyclerViewPages();
    }
}
