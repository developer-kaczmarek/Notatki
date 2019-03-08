package kaczmarek.notatki.ui.sectionsList.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import kaczmarek.notatki.R;
import kaczmarek.notatki.ui.pagesList.activities.PagesListActivity;
import kaczmarek.notatki.ui.sectionsList.adapters.SectionsRecyclerViewAdapter;
import kaczmarek.notatki.ui.sectionsList.interfaces.ModifyDialog;
import kaczmarek.notatki.ui.sectionsList.interfaces.OnSectionClickListner;
import kaczmarek.notatki.ui.sectionsList.presenters.SectionsListPresenter;
import kaczmarek.notatki.ui.sectionsList.views.SectionsListView;
import kaczmarek.notatki.ui.settings.activities.SettingsActivity;

public class SectionsListActivity extends MvpAppCompatActivity implements SectionsListView, ModifyDialog {
    @InjectPresenter
    SectionsListPresenter mSectionsListPresenter;
    private static final int NUMBER_OF_REQUEST = 45401;
    private AlertDialog dialog;
    private RadioButton mRBIndigo, mRBBlue, mRBPurple, mRBPink, mRBGreen, mRBOrange, mRBDeepOrange, mRBRed;
    private FloatingActionButton fab;
    private SectionsRecyclerViewAdapter mSectionsRecyclerViewAdapter;
    private TextView infoText;
    private int countSections, selectModeAlertDialog = 0;
    private SearchView searchView;
    private String namePositiveButton = null;
    private ImageButton imageButtonSettings;
    String oldTitle;
    RecyclerView mRecyclerViewSections;
    OnSectionClickListner mOnSectionClickListner;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections_list);
        fab = findViewById(R.id.addSection);
        context = this;
        mRecyclerViewSections = findViewById(R.id.sectionsList);
        infoText = findViewById(R.id.infoCountSections);
        searchView = findViewById(R.id.searchSections);
        imageButtonSettings = findViewById(R.id.buttonSettings);
        fab.setOnClickListener(view -> openEditDialog(null, null, 0));
        mOnSectionClickListner = section -> {
            Intent intent = new Intent(SectionsListActivity.this, PagesListActivity.class);
            intent.putExtra(PagesListActivity.sectionTitle, section.getTitleSection());
            intent.putExtra(PagesListActivity.sectionId, section.getId());
            startActivity(intent);
        };
        imageButtonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(SectionsListActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
        });
        registerForContextMenu(mRecyclerViewSections);
        search(searchView);
        permissionCheck();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    //Проверка разрешения на запись в память смартфона
    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle(R.string.title_alert_dialog_permission);
                mBuilder.setMessage(R.string.text_alert_dialog_permission);
                mBuilder.setPositiveButton(android.R.string.yes, (dialog, which) ->
                        ActivityCompat.requestPermissions(SectionsListActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, NUMBER_OF_REQUEST));

                dialog = mBuilder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(SectionsListActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, NUMBER_OF_REQUEST);

            }
        } else {
            // Permission has already been granted
        }
    }

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case NUMBER_OF_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(),
                          //  R.string.permission_error,
                          //  Toast.LENGTH_LONG).show();
                }
            }
        }
    }*/

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSectionsRecyclerViewAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private View.OnClickListener mRadioButtonsClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRBIndigo.setChecked(false);
            mRBBlue.setChecked(false);
            mRBPurple.setChecked(false);
            mRBPink.setChecked(false);
            mRBGreen.setChecked(false);
            mRBOrange.setChecked(false);
            mRBDeepOrange.setChecked(false);
            mRBRed.setChecked(false);

            switch (v.getId()) {
                case R.id.radioIndigo:
                    mRBIndigo.setChecked(true);
                    break;
                case R.id.radioBlue:
                    mRBBlue.setChecked(true);
                    break;
                case R.id.radioRed:
                    mRBRed.setChecked(true);
                    break;
                case R.id.radioGreen:
                    mRBGreen.setChecked(true);
                    break;
                case R.id.radioOrange:
                    mRBOrange.setChecked(true);
                    break;
                case R.id.radioDeepOrange:
                    mRBDeepOrange.setChecked(true);
                    break;
                case R.id.radioPurple:
                    mRBPurple.setChecked(true);
                    break;
                case R.id.radioPink:
                    mRBPink.setChecked(true);
                    break;
            }

        }
    };

    private String selectedColor() {
        String color = getResources().getString(R.string.color_indigo);
        if (mRBIndigo.isChecked() == true)
            color = getResources().getString(R.string.color_indigo);
        if (mRBBlue.isChecked() == true)
            color = getResources().getString(R.string.color_blue);
        if (mRBRed.isChecked() == true)
            color = getResources().getString(R.string.color_red);
        if (mRBGreen.isChecked() == true)
            color = getResources().getString(R.string.color_green);
        if (mRBOrange.isChecked() == true)
            color = getResources().getString(R.string.color_orange);
        if (mRBDeepOrange.isChecked() == true)
            color = getResources().getString(R.string.color_deep_orange);
        if (mRBPurple.isChecked() == true)
            color = getResources().getString(R.string.color_purple);
        if (mRBPink.isChecked() == true)
            color = getResources().getString(R.string.color_pink);
        return color;
    }

    private void cheackedRadioButton(String color) {
        switch (color){
            case "#3f51b5":
                mRBIndigo.setChecked(true);
                break;
            case "#5677fc":
                mRBBlue.setChecked(true);
                break;
            case "#e51c23":
                mRBRed.setChecked(true);
                break;
            case "#259b24":
                mRBGreen.setChecked(true);
                break;
            case "#ff9800":
                mRBOrange.setChecked(true);
                break;
            case "#ff5722":
                mRBDeepOrange.setChecked(true);
                break;
            case "#9c27b0":
                mRBPurple.setChecked(true);
                break;
            case "#e91e63":
                mRBPink.setChecked(true);
                break;
        }
    }

    @Override
    public void updateRecyclerView() {
        this.countSections = mSectionsListPresenter.getCountSections();
        if (countSections != 0)
            infoText.setVisibility(View.INVISIBLE);
        else
            infoText.setVisibility(View.VISIBLE);
        mSectionsRecyclerViewAdapter = new SectionsRecyclerViewAdapter(this, mSectionsListPresenter.getList(), mOnSectionClickListner);
        mSectionsRecyclerViewAdapter.setDialog(this);
        mRecyclerViewSections.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewSections.setAdapter(mSectionsRecyclerViewAdapter);
    }

    @Override
    public void showErrorCreation() {
        Toast.makeText(this, R.string.warning_name_section, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openEditDialog(String title, String color, int mode) {
        AlertDialog.Builder sectionDialog = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.modify_dialog_sections, null);
        final EditText editTitle = dialogLayout.findViewById(R.id.editSectionTitle);
        this.selectModeAlertDialog = mode;
        mRBIndigo = dialogLayout.findViewById(R.id.radioIndigo);
        mRBBlue = dialogLayout.findViewById(R.id.radioBlue);
        mRBPurple = dialogLayout.findViewById(R.id.radioPurple);
        mRBPink = dialogLayout.findViewById(R.id.radioPink);
        mRBGreen = dialogLayout.findViewById(R.id.radioGreen);
        mRBOrange = dialogLayout.findViewById(R.id.radioOrange);
        mRBDeepOrange = dialogLayout.findViewById(R.id.radioDeepOrange);
        mRBRed = dialogLayout.findViewById(R.id.radioRed);

        mRBIndigo.setOnClickListener(mRadioButtonsClickListner);
        mRBBlue.setOnClickListener(mRadioButtonsClickListner);
        mRBPurple.setOnClickListener(mRadioButtonsClickListner);
        mRBPink.setOnClickListener(mRadioButtonsClickListner);
        mRBGreen.setOnClickListener(mRadioButtonsClickListner);
        mRBOrange.setOnClickListener(mRadioButtonsClickListner);
        mRBDeepOrange.setOnClickListener(mRadioButtonsClickListner);
        mRBRed.setOnClickListener(mRadioButtonsClickListner);
        sectionDialog.setView(dialogLayout);
        sectionDialog.setCancelable(false);

        if (mode == 0) {
            sectionDialog.setTitle(R.string.create_section_title_dialog);
            namePositiveButton = getResources().getString(R.string.text_create_button);
            mRBIndigo.setChecked(true);

        } else if (mode == 1) {
            sectionDialog.setTitle(R.string.edit_section_title_dialog);
            namePositiveButton = getResources().getString(R.string.text_edit_button);
            editTitle.setText(title);
            editTitle.setSelection(editTitle.getText().length());
            oldTitle = title;
            cheackedRadioButton(color);
        }

        sectionDialog.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTitle.getWindowToken(), 0);
        });

        sectionDialog.setPositiveButton(namePositiveButton, (dialog, which) -> {
            String newTitle = editTitle.getText().toString();
            String newColor = selectedColor();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTitle.getWindowToken(), 0);
            if (selectModeAlertDialog == 1) {
                mSectionsListPresenter.updateSection(oldTitle, newTitle, newColor);
            } else {
                mSectionsListPresenter.addNewSection(newTitle, newColor);
            }
        });
        dialog = sectionDialog.create();
        dialog.show();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        if (mode == 0)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                else
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void openDeleteDialog(String title) {
        AlertDialog.Builder sectionDialog = new AlertDialog.Builder(this);
        sectionDialog.setTitle(R.string.delete_section_title__dialog);
        sectionDialog.setMessage(R.string.delete_message);
        sectionDialog.setCancelable(true);
        sectionDialog.setNegativeButton(R.string.text_delete_button, (dialog, which) -> mSectionsListPresenter.deleteSection(title));
        sectionDialog.setPositiveButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        dialog = sectionDialog.create();
        dialog.show();
    }
}
