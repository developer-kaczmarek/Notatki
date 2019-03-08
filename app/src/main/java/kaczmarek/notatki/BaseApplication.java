package kaczmarek.notatki;

import android.app.Application;

import kaczmarek.notatki.di.components.AppComponent;
import kaczmarek.notatki.di.components.DaggerAppComponent;
import kaczmarek.notatki.di.modules.ContextModule;
import kaczmarek.notatki.di.modules.DatabaseModule;

public class BaseApplication extends Application {
    public static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .databaseModule(new DatabaseModule(this))
                .build();

    }
    public static AppComponent getComponent(){
        return mAppComponent;
        }
}
