package  kaczmarek.notatki.di.services.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Sections")
public class Section {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String titleSection;
    private String colorSection;

    @NonNull
    public String getTitleSection() {
        return titleSection;
    }

    public void setTitleSection(@NonNull String titleSection) {
        this.titleSection = titleSection;
    }

    public String getColorSection() {
        return colorSection;
    }

    public void setColorSection(String colorSection) {
        this.colorSection = colorSection;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
