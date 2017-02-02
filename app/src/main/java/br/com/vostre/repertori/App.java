package br.com.vostre.repertori;

import android.app.Application;

import br.com.vostre.repertori.utils.GoogleApiHelper;

public class App extends Application {
    private GoogleApiHelper googleApiHelper;
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        googleApiHelper = new GoogleApiHelper(mInstance);
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }
    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }
}