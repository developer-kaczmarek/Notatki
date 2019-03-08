package kaczmarek.notatki.ui.sectionsList.interfaces;

import kaczmarek.notatki.di.services.database.models.Section;

public interface OnSectionClickListner {

    void onSectionClick(Section section);
}
