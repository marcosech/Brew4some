package com.brewforsome.etxekakatua.brewforsome.Utils;

import android.content.Context;
import androidx.multidex.MultiDexApplication;


/**
 * Created by etxekakatua on 27/1/18.
 */

public class Application extends MultiDexApplication {

    public static Boolean logged;
    public static String email;
    public static String password;
    public static String uid;
    public static String phone;
    public static String name;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        final PreferencesManager preferencesManager = new PreferencesManager(this);
        this.email = preferencesManager.getUserEmail();
        this.name = preferencesManager.getUserName();
        this.logged = (!preferencesManager.getUserEmail().equals("")) ? true : false;
        this.password = preferencesManager.getUserPassword();
        this.phone = preferencesManager.getUserPhone();
        this.uid = preferencesManager.getUserUid();

    }

    public static void reloadDatas(Context context) {
        final PreferencesManager preferencesManager = new PreferencesManager(context);
        Application.email = preferencesManager.getUserEmail();
        Application.name = preferencesManager.getUserName();
        Application.logged = (!preferencesManager.getUserEmail().equals("")) ? true : false;
        Application.password = preferencesManager.getUserPassword();
        Application.phone = preferencesManager.getUserPhone();
        Application.uid = preferencesManager.getUserUid();

    }

    public static Context getContext() {
        return context;
    }
}
