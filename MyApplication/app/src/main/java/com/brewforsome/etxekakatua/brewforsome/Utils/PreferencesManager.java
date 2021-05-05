package com.brewforsome.etxekakatua.brewforsome.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by etxekakatua on 1/12/17.
 */

public class PreferencesManager {

    private static final String prefs_name = "myDatas";

    private static final String userUid = "uid";
    public static final String userEmail = "userEmail";
    public static final String userPassword = "password";
    public static final String userName = "password";
    public static final String userPhone = "phone";
    public static final String userLogged = "logged";
    private Context context;

    public PreferencesManager(Context context) {
        this.context = context;
    }

    public void saveUserDatas(String email, String password, String name, String phone, String uid) {

        final SharedPreferences sharedPreferences = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (email != null)
            editor.putString(userEmail, email);
        else
            editor.putString(userEmail, "");

        if (password != null)
            editor.putString(userPassword, password);
        else
            editor.putString(userPassword, "");

        if (name != null)
            editor.putString(userName, name);
        else
            editor.putString(userName, "");

        if (phone != null)
            editor.putString(userPhone, phone);
        else
            editor.putString(userPhone, "");

        if (uid != null)
            editor.putString(userUid, uid);
        else
            editor.putString(userUid, "");

        editor.commit();
    }

    public static String getPrefs_name() {
        return prefs_name;
    }

    public String getUserUid() {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(userUid, "").equals(""))
            return sharedPreferences.getString(userUid, "");
        else
            return "";
    }

    public String getUserEmail() {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(userEmail, "").equals(""))
            return sharedPreferences.getString(userEmail, "");
        else
            return "";
    }

    public String getUserPassword() {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(userPassword, "").equals(""))
            return sharedPreferences.getString(userPassword, "");
        else
            return "";
    }


    public String getUserName() {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(userName, "").equals(""))
            return sharedPreferences.getString(userName, "");
        else
            return "";
    }

    public String getUserPhone() {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(userPhone, "").equals(""))
            return sharedPreferences.getString(userPhone, "");
        else
            return "";
    }

    public boolean getUserLogged() {

        final SharedPreferences sharedPreferences = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(userLogged, true);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
