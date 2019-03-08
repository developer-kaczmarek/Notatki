package kaczmarek.notatki.ui.settings.activities;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.DocumentsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import kaczmarek.notatki.R;

public class SettingsActivity extends AppCompatPreferenceActivity {
    private static final int pickfileResultCode = 0;
    private static Context mContext;
    private static View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = getApplicationContext();
        mView = findViewById(R.id.fragmentContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new MainPreferenceFragment())
                .commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);
            Preference exportPreference = findPreference(getString(R.string.pref_key_export));
            Preference importPreference = findPreference(getString(R.string.pref_key_import));

            exportPreference.setOnPreferenceClickListener(preference -> {
                if(checkPermission()){
                    try {
                        exportDatabase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    showSnackbar(getResources().getString(R.string.alert_dialog_denim_permission));
                }

                return true;
            });

            importPreference.setOnPreferenceClickListener(preference -> {
                if(checkPermission()){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, pickfileResultCode);
                } else{
                    showSnackbar(getResources().getString(R.string.alert_dialog_denim_permission));
                }
                return true;
            });

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case pickfileResultCode:
                    if (resultCode == RESULT_OK) {
                        String absolutePath = getPath(mContext, data.getData());
                        try {
                            importDatabase(absolutePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }

        private void showSnackbar(String text){
            final Snackbar snackBar = Snackbar.make(mView, text, Snackbar.LENGTH_INDEFINITE);
            snackBar.setAction(android.R.string.yes, v -> snackBar.dismiss());
            snackBar.setActionTextColor(getResources().getColor(android.R.color.white));
            snackBar.show();
        }

        private boolean checkPermission(){
            boolean permission;
            if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED){
                permission = true;
            } else {
                permission = false;
            }
            return permission;
        }

        public static String getPath(final Context context, final Uri uri) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        public static String getDataColumn(Context context, Uri uri, String selection,
                                           String[] selectionArgs) {
            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {
                    column
            };
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        private void exportDatabase() throws IOException {
            File source = new File(mContext.getDatabasePath(getResources().getString(R.string.name_database)).getAbsolutePath());
            File saveFolder = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.name_directory));
            if (!saveFolder.exists())
                saveFolder.mkdir();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(getResources().getString(R.string.date_pattern));
            Date myDate = new Date();
            File dest = new File(saveFolder.getPath() + "/database" + mSimpleDateFormat.format(myDate));
            FileInputStream inStream = null;
            FileOutputStream outStream = null;
            try {
                inStream = new FileInputStream(source);
                outStream = new FileOutputStream(dest);
                FileChannel inChannel = inStream.getChannel();
                FileChannel outChannel = outStream.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
                showSnackbar(getResources().getString(R.string.message_success_save_datebase)
                        + saveFolder.getPath());
            }
        }

        private void importDatabase(String selectSource) throws IOException {
            File source = new File(selectSource);
            File dest = new File(mContext.getDatabasePath(getResources()
                    .getString(R.string.name_database)).getAbsolutePath());

            FileInputStream inStream = null;
            FileOutputStream outStream = null;
            try {
                inStream = new FileInputStream(source);
                outStream = new FileOutputStream(dest);
                FileChannel inChannel = inStream.getChannel();
                FileChannel outChannel = outStream.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
                showSnackbar(getResources().getString(R.string.message_success_update_datebase));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
