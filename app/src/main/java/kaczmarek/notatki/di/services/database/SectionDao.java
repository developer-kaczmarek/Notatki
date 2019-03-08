package  kaczmarek.notatki.di.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import kaczmarek.notatki.di.services.database.models.Section;

@Dao
public interface SectionDao {
    //Добавление в бд
    @Insert
    void insert(Section section);

    @Query("SELECT * FROM Sections")
    Flowable<List<Section>> getAllSections();

    @Query("SELECT COUNT(*) from Sections")
    int count();

    @Query("SELECT COUNT(*) from Sections WHERE titleSection = :newTitle")
    int getCountOfCopies(String newTitle);

    @Query("DELETE FROM Sections WHERE titleSection = :title")
    void deleteByTitle(String title);

    @Query("UPDATE Sections Set titleSection = :newName, colorSection=:color where titleSection=:oldName")
    void update(String oldName, String newName, String color);
}
