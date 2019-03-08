package kaczmarek.notatki.di.components;

import javax.inject.Singleton;

import dagger.Component;
import kaczmarek.notatki.di.modules.ContextModule;
import kaczmarek.notatki.di.modules.DatabaseModule;
import kaczmarek.notatki.ui.page.presenters.PagePresenter;
import kaczmarek.notatki.ui.pagesList.presenters.PagesListPresenter;
import kaczmarek.notatki.ui.sectionsList.presenters.SectionsListPresenter;

@Singleton
@Component(modules = {ContextModule.class, DatabaseModule.class})
public interface AppComponent {
    void inject(SectionsListPresenter sectionsListPresenter);
    void inject(PagesListPresenter pagesListPresenter);
    void inject(PagePresenter pagePresenter);
}
