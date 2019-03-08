package kaczmarek.notatki.ui.page.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.lina.notatkidraft.ui.page.views.PageView;
import kaczmarek.notatki.R;
import kaczmarek.notatki.ui.page.presenters.PagePresenter;
import kaczmarek.notatki.utils.TextStyleEditor;
import kaczmarek.notatki.utils.UndoRedoAdapter;

public class PageActivity extends MvpAppCompatActivity implements PageView {

    @InjectPresenter
    PagePresenter mPagePresenter;
    public static final String pageId ="pageId";
    private Long idPage;
    private boolean flagDelete = false;
    private Context context;
    private AlertDialog dialog;
    private TextView titleTextView, dateTextView;
    private UndoRedoAdapter undoRedoAdapter;
    private EditText note;
    private ImageButton buttonUndo, buttonRedo, buttonBold, buttonItalic, buttonUnderline,
    buttonBullet, buttonQuote, buttonStrikethrough, buttonClear;
    private TextStyleEditor mTextStyleEditor;
    private HorizontalScrollView mHorizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        titleTextView = findViewById(R.id.titlePage);
        dateTextView = findViewById(R.id.datePage);
        note = findViewById(R.id.editor);
        mHorizontalScrollView = findViewById(R.id.toolsPanel);
        buttonUndo = findViewById(R.id.buttonUndo);
        buttonRedo = findViewById(R.id.buttonRedo);
        buttonBold = findViewById(R.id.buttonBold);
        buttonQuote = findViewById(R.id.buttonQuote);
        buttonClear = findViewById(R.id.buttonClear);
        buttonItalic = findViewById(R.id.buttonItalic);
        buttonBullet = findViewById(R.id.buttonBullet);
        buttonUnderline = findViewById(R.id.buttonUnderline);
        buttonStrikethrough = findViewById(R.id.buttonStrikethrough);
        undoRedoAdapter = new UndoRedoAdapter(note);
        buttonUndo.setOnClickListener(undoRedoListener);
        buttonRedo.setOnClickListener(undoRedoListener);
        buttonBold.setOnClickListener(toolsClickListener);
        buttonQuote.setOnClickListener(toolsClickListener);
        buttonClear.setOnClickListener(toolsClickListener);
        buttonItalic.setOnClickListener(toolsClickListener);
        buttonBullet.setOnClickListener(toolsClickListener);
        buttonUnderline.setOnClickListener(toolsClickListener);
        buttonStrikethrough.setOnClickListener(toolsClickListener);
        context = getApplicationContext();
        initButtonsToolPanel();
        note.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mHorizontalScrollView.setVisibility(View.VISIBLE);
                return true;
            }

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
                mHorizontalScrollView.setVisibility(View.INVISIBLE);
            }
        });

        idPage = getIntent().getExtras().getLong(pageId);
        titleTextView.setText( mPagePresenter.getTitlePage(idPage));
        dateTextView.setText(mPagePresenter.getDatePage(idPage));
        note.setText(Html.fromHtml(mPagePresenter.getContentPage(idPage)));
        note.setSelection(note.getText().length());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.page_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                AlertDialog.Builder sectionDialog = new AlertDialog.Builder(this);
                sectionDialog.setTitle(getResources().getString(R.string.delete_page_title_dialog));
                sectionDialog.setMessage(getResources().getString(R.string.delete_message));
                sectionDialog.setCancelable(true);
                sectionDialog.setNegativeButton(getResources().getString(R.string.text_delete_button), (dialog, which) -> {
                    mPagePresenter.deletePage(idPage);
                    flagDelete = true;
                    onBackPressed();
                });
                sectionDialog.setPositiveButton(getResources().getString(android.R.string.cancel), (dialog, which) -> dialog.dismiss());
                dialog = sectionDialog.create();
                dialog.show();
                return true;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, note.getText().toString());
                sendIntent.setType(getResources().getString(R.string.text_type));
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.item_context_menu_share)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initButtonsToolPanel(){
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        if(!prefs.getBoolean(getResources().getString(R.string.key_bold), true))
            buttonBold.setVisibility(View.GONE);
        else
            buttonBold.setVisibility(View.VISIBLE);
        if(!prefs.getBoolean(getResources().getString(R.string.key_italic), true))
            buttonItalic.setVisibility(View.GONE);
        else
            buttonItalic.setVisibility(View.VISIBLE);
        if(!prefs.getBoolean(getResources().getString(R.string.key_underline), true))
            buttonUnderline.setVisibility(View.GONE);
        else
            buttonUnderline.setVisibility(View.VISIBLE);
        if(!prefs.getBoolean(getResources().getString(R.string.key_strikethrough), true))
            buttonStrikethrough.setVisibility(View.GONE);
        else
            buttonStrikethrough.setVisibility(View.VISIBLE);
        if(!prefs.getBoolean(getResources().getString(R.string.key_clear),true))
            buttonClear.setVisibility(View.GONE);
        else
            buttonClear.setVisibility(View.VISIBLE);
        buttonQuote.setVisibility(View.GONE);
        buttonBullet.setVisibility(View.GONE);
            /*
            if(!prefs.getBoolean(getResources().getString(R.string.key_bullet), true))
            buttonBullet.setVisibility(View.GONE);
        else
            buttonBullet.setVisibility(View.VISIBLE);
            if(!prefs.getBoolean(getResources().getString(R.string.key_quote), true))
                buttonQuote.setVisibility(View.GONE);
            else
                buttonQuote.setVisibility(View.VISIBLE);
                */
    }
    View.OnClickListener undoRedoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttonUndo:
                    if(undoRedoAdapter.getCanUndo())
                        undoRedoAdapter.undo();
                    break;
                case R.id.buttonRedo:
                    if(undoRedoAdapter.getCanRedo())
                        undoRedoAdapter.redo();
                    break;
            }
        }
    };

    View.OnClickListener toolsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTextStyleEditor = new TextStyleEditor(note,
                    getResources().getColor(R.color.colorAccent));
            int start = note.getSelectionStart();
            int end = note.getSelectionEnd();
            switch (v.getId()){
                case R.id.buttonBold:
                    mTextStyleEditor.initTextStyleArray(start,end);
                    if(!mTextStyleEditor.isTextStyle()) {
                        mTextStyleEditor.setBoldStyle();
                    } else {
                        mTextStyleEditor.editBoldStyle();
                    }
                    note.setText(mTextStyleEditor.returnSpannableText());
                    note.setSelection(end);
                    break;
                case R.id.buttonItalic:
                    mTextStyleEditor.initTextStyleArray(start,end);
                    if(!mTextStyleEditor.isTextStyle()) {
                        mTextStyleEditor.setItalicStyle();
                    } else {
                        mTextStyleEditor.editItalicStyle();
                    }
                    note.setText(mTextStyleEditor.returnSpannableText());
                    note.setSelection(end);
                    break;
                case R.id.buttonUnderline:
                    mTextStyleEditor.initUnderlineStyleArray(start,end);
                    if(!mTextStyleEditor.isUnderlineStyle()) {
                        mTextStyleEditor.setUnderlineStyle();
                    } else {
                        mTextStyleEditor.removeUnderlineStyle();
                    }
                    note.setText(mTextStyleEditor.returnSpannableText());
                    note.setSelection(end);
                    break;
                case R.id.buttonStrikethrough:
                    mTextStyleEditor.initStrikethroughStyleArray(start,end);
                    if(!mTextStyleEditor.isStrikethroughStyle()) {
                        mTextStyleEditor.setStrikethroughStyle();
                    } else {
                        mTextStyleEditor.removeStrikethroughStyle();
                    }
                    note.setText(mTextStyleEditor.returnSpannableText());
                    note.setSelection(end);
                    break;
                case R.id.buttonBullet:
                    mTextStyleEditor.initBulletStyleArray(start,end);
                    if(!mTextStyleEditor.isBulletStyle()) {
                        mTextStyleEditor.setBulletStyle();
                    } else {
                        mTextStyleEditor.removeBulletStyle();
                    }
                    note.setText(mTextStyleEditor.returnSpannableText());
                    note.setSelection(note.getText().length());
                    break;
                case R.id.buttonQuote:
                    mTextStyleEditor.initOuoteStyleArray(start,end);
                    if(!mTextStyleEditor.isOuoteStyle()) {
                        mTextStyleEditor.setOuoteStyle();
                    } else {
                        mTextStyleEditor.removeOuoteStyle();
                    }
                    note.setText(mTextStyleEditor.returnSpannableText());
                    note.setSelection(end);
                    break;
                case R.id.buttonClear:
                    mTextStyleEditor.clearAllStylies();
                    note.setText(mTextStyleEditor.returnSpannableText());
                    note.setSelection(note.getText().length());
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        String contentForHtml = Html.toHtml(note.getText());
        if(!flagDelete){
                mPagePresenter.saveChanges(contentForHtml, idPage);
                Toast.makeText(getBaseContext(), getResources().getString(R.string.save_page),
                        Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }
}
