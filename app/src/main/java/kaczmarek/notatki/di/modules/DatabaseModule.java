package kaczmarek.notatki.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kaczmarek.notatki.di.services.database.NotatkiDatabase;
import kaczmarek.notatki.di.services.database.PageDao;
import kaczmarek.notatki.di.services.database.SectionDao;

@Module
public class DatabaseModule {

    private NotatkiDatabase notatkiDatabase;

    public DatabaseModule(Application mApplication) {
        notatkiDatabase = Room.databaseBuilder(mApplication,
                NotatkiDatabase.class,
                "notatki-db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    NotatkiDatabase providesRoomDatabase() {
        return notatkiDatabase;
    }

    @Singleton
    @Provides
    SectionDao providesSectionDao(NotatkiDatabase notatkiDatabase) {
        return notatkiDatabase.sectionDao();
    }

    @Singleton
    @Provides
    PageDao providesPageDao(NotatkiDatabase notatkiDatabase) {
        return notatkiDatabase.pageDao();
    }

}