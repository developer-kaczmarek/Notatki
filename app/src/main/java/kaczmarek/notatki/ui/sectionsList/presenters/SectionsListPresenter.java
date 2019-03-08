package kaczmarek.notatki.ui.sectionsList.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import kaczmarek.notatki.BaseApplication;
import kaczmarek.notatki.di.services.database.SectionDao;
import kaczmarek.notatki.di.services.database.models.Section;
import kaczmarek.notatki.ui.sectionsList.views.SectionsListView;

@InjectViewState
public class SectionsListPresenter extends MvpPresenter<SectionsListView> {
    private List<Section> sectionsList =new ArrayList<>();
    @Inject
    SectionDao mSectionDao;

    public SectionsListPresenter() {
       BaseApplication.getComponent().inject(this);
    }

    public List<Section> getList(){
       mSectionDao.getAllSections().subscribe(studentModels -> {
            sectionsList.clear();
            sectionsList.addAll(studentModels);
        });
        return sectionsList;
    }

    public void addNewSection(String title, String color) {
        if(mSectionDao.getCountOfCopies(title)==0){
            Section newSection = new Section();
            newSection.setTitleSection(title);
            newSection.setColorSection(color);

            mSectionDao.insert(newSection);
            getViewState().updateRecyclerView();
        } else
            getViewState().showErrorCreation();

    }

    public int getCountSections() {
        return mSectionDao.count();
    }

    public void deleteSection(String title) {
        mSectionDao.deleteByTitle(title);
        getViewState().updateRecyclerView();
    }

    public void updateSection(String oldTitle, String title, String color) {
                mSectionDao.update(oldTitle, title, color);
        getViewState().updateRecyclerView();
    }
}
