package  kaczmarek.notatki.di.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import kaczmarek.notatki.di.services.database.models.Page;


@Dao
public interface PageDao {

    @Insert
    void insert(Page page);

    @Query("SELECT * from Pages WHERE idSection = :receivedSection")
    Flowable<List<Page>> getAllPagesForSection(long receivedSection);

    @Query("SELECT COUNT(*) from Pages WHERE idSection = :idSection")
    int countPages(long idSection);

    @Query("SELECT COUNT(*) from Pages WHERE titlePage = :newTitle")
    int getCountOfCopies(String newTitle);

    @Query("DELETE FROM Pages WHERE titlePage = :title")
    void deleteByTitle(String title);

    @Query("UPDATE Pages Set titlePage = :newTitle WHERE titlePage=:oldTitle")
    void updateTitlePage(String oldTitle, String newTitle);

    @Query("SELECT titlePage from Pages WHERE idPage = :id")
    String getTitlePage(Long id);

    @Query("SELECT datePage from Pages WHERE idPage = :id")
    String getDatePage(long id);

    @Query("SELECT textPage from Pages WHERE idPage = :id")
    String getContentPage(long id);

    @Query("UPDATE Pages Set textPage = :text where idPage = :id")
    void saveContentPage(String text, Long id);

    @Query("DELETE FROM Pages WHERE idPage = :id")
    void deleteById(Long id);
}
