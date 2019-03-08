package  kaczmarek.notatki.di.services.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import kaczmarek.notatki.di.services.database.models.Page;
import kaczmarek.notatki.di.services.database.models.Section;


@Database(entities = {Section.class, Page.class}, version = 1, exportSchema = false)
public abstract class NotatkiDatabase extends RoomDatabase {
    public abstract SectionDao sectionDao();
    public abstract PageDao pageDao();
}