package kaczmarek.notatki.ui.page.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import com.example.lina.notatkidraft.ui.page.views.PageView;
import javax.inject.Inject;
import kaczmarek.notatki.BaseApplication;
import kaczmarek.notatki.di.services.database.PageDao;

@InjectViewState
public class PagePresenter extends MvpPresenter<PageView> {
     @Inject
     PageDao mPageDao;
     public PagePresenter() {
         BaseApplication.getComponent().inject(this);
     }

     public String getTitlePage(long id) {
         return mPageDao.getTitlePage(id);
     }

     public String getDatePage(long id) {
         return mPageDao.getDatePage(id);
     }

     public String getContentPage(long id) {
         return mPageDao.getContentPage(id);
     }

     public void saveChanges(String text, Long id){
         mPageDao.saveContentPage(text, id);
     }

     public void deletePage(Long idPage) {
         mPageDao.deleteById(idPage);
     }
 }
