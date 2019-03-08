package  kaczmarek.notatki.di.services.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Pages", foreignKeys = @ForeignKey(entity = Section.class, parentColumns = "id", childColumns = "idSection", onDelete = CASCADE))
public class Page {

    @PrimaryKey(autoGenerate = true)
    private long idPage;
    private String titlePage;
    private String textPage;
    private long idSection;
    private String datePage;

    public long getIdPage() {
        return idPage;
    }

    public void setIdPage(long idPage) {
        this.idPage = idPage;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }

    public String getTextPage() {
        return textPage;
    }

    public void setTextPage(String textPage) {
        this.textPage = textPage;
    }

    public long getIdSection() {
        return idSection;
    }

    public void setIdSection(long idSection) {
        this.idSection = idSection;
    }

    public String getDatePage() {
        return datePage;
    }

    public void setDatePage(String datePage) {
        this.datePage = datePage;
    }


}
