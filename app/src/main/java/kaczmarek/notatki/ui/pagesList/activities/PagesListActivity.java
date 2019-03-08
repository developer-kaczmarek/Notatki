package kaczmarek.notatki.ui.pagesList.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import kaczmarek.notatki.R;
import kaczmarek.notatki.ui.page.activities.PageActivity;
import kaczmarek.notatki.ui.pagesList.adapters.PagesRecyclerViewAdapter;
import kaczmarek.notatki.ui.pagesList.interfaces.ModifyPageDialog;
import kaczmarek.notatki.ui.pagesList.interfaces.OnPageClickListener;
import kaczmarek.notatki.ui.pagesList.presenters.PagesListPresenter;
import kaczmarek.notatki.ui.pagesList.views.PagesListView;


public class PagesListActivity extends MvpAppCompatActivity implements PagesListView, ModifyPageDialog {
    @InjectPresenter
    PagesListPresenter mPagesListPresenter;
    public static final String sectionTitle = "title";
    public static final String sectionId ="id";
    private TextView sectionTextView;
    private int countPages;
    private TextView mInfoCountPages;
    private PagesRecyclerViewAdapter mPagesRecyclerViewAdapter;
    private RecyclerView mRecyclerViewPages;
    private FloatingActionButton fab ;
    private int selectModeAlertDialog = 0;
    private String oldTitle;
    private AlertDialog dialog;
    private String nameSection;
    private SearchView searchView;
    private long idSection;
    private String namePositiveButton = null;
    private OnPageClickListener mOnPageClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages_list);
        sectionTextView = findViewById(R.id.titleSection);
        mInfoCountPages = findViewById(R.id.infoCountPages);
        mRecyclerViewPages = findViewById(R.id.listPages);
        searchView = findViewById(R.id.searchPages);
        nameSection = getIntent().getExtras().getString(sectionTitle);
        idSection = getIntent().getExtras().getLong(sectionId);
        sectionTextView.setText(nameSection);
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> onShowEditDialog(null,0));
        mOnPageClickListener = page -> {
            Intent intent = new Intent(PagesListActivity.this, PageActivity.class);
            intent.putExtra(PageActivity.pageId, page.getIdPage());
            startActivity(intent);
        };
        search(searchView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerViewPages();
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPagesRecyclerViewAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void updateRecyclerViewPages() {
        this.countPages = mPagesListPresenter.getCountPages(idSection);
        if(countPages!=0)
            mInfoCountPages.setVisibility(View.INVISIBLE);
        else
            mInfoCountPages.setVisibility(View.VISIBLE);
        mPagesRecyclerViewAdapter = new PagesRecyclerViewAdapter(this, mPagesListPresenter.getListPages(idSection), mOnPageClickListener);
        mPagesRecyclerViewAdapter.setDialog(this);
        mRecyclerViewPages.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewPages.setAdapter(mPagesRecyclerViewAdapter);
    }

    @Override
    public void showErrorCreation() {
        Toast.makeText(this, getResources().getString(R.string.warning_name_page), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onShowEditDialog(String title, int mode) {
        AlertDialog.Builder pageDialog = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.modify_dialog_page, null);
        final EditText mEditTextTitle = dialogLayout.findViewById(R.id.editPagesTitle);

        this.selectModeAlertDialog = mode;
        pageDialog.setView(dialogLayout);
        pageDialog.setCancelable(false);

        if(mode==0){
            pageDialog.setTitle(getResources().getString(R.string.create_page_title_dialog));
            namePositiveButton = getResources().getString(R.string.text_create_button);

        } else if(mode==1) {
            pageDialog.setTitle(getResources().getString(R.string.edit_page_title_dialog));
            mEditTextTitle.setText(title);
            mEditTextTitle.setSelection(mEditTextTitle.getText().length());
            oldTitle = title;
            namePositiveButton = getResources().getString(R.string.text_edit_button);
        }

        pageDialog.setNegativeButton(getResources().getString(android.R.string.cancel), (dialog, which) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditTextTitle.getWindowToken(),0);
        });

        pageDialog.setPositiveButton(namePositiveButton, (dialog, which) -> {
            String newTitlePage = mEditTextTitle.getText().toString();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditTextTitle.getWindowToken(),0);
            if(selectModeAlertDialog==1){
                mPagesListPresenter.updatePage(oldTitle,newTitlePage);
            } else {
                mPagesListPresenter.addNewPage(newTitlePage,idSection);
            }
        });
        dialog = pageDialog.create();
        dialog.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        if(mode==0)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0)
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                else
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });
    }

    @Override
    public void onShowDeleteDialog(String title) {
        AlertDialog.Builder sectionDialog = new AlertDialog.Builder(this);
        sectionDialog.setTitle(getResources().getString(R.string.delete_page_title_dialog));
        sectionDialog.setMessage(getResources().getString(R.string.delete_message));
        sectionDialog.setCancelable(true);
        sectionDialog.setNegativeButton(getResources().getString(R.string.text_delete_button), (dialog, which) -> mPagesListPresenter.deletePage(title));
        sectionDialog.setPositiveButton(getResources().getString(android.R.string.cancel), (dialog, which) -> dialog.dismiss());
        dialog = sectionDialog.create();
        dialog.show();
    }

}
