package com.brewforsome.etxekakatua.brewforsome.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by etxekakatua on 1/11/17.
 */

public class Utils {

    public FirebaseAuth.AuthStateListener mAuthListener;
    public static KProgressHUD progress;
    public static ACProgressFlower dialog;

    public static void createToast(Activity activity, String errorMessage) {
        Toast.makeText(activity, errorMessage,
                Toast.LENGTH_SHORT).show();
    }

    public static boolean isPhoneValid(String phoneNumberTxt) {
        if (phoneNumberTxt.equals("") || phoneNumberTxt.length() != 9 || phoneNumberTxt.substring(0, 1).equals("1") || phoneNumberTxt.substring(0, 1).equals("2") || phoneNumberTxt.substring(0, 1).equals("3") || phoneNumberTxt.substring(0, 1).equals("4") ||
                phoneNumberTxt.substring(0, 1).equals("5") || phoneNumberTxt.substring(0, 1).equals("8") || phoneNumberTxt.substring(0, 1).equals("9") || phoneNumberTxt.substring(0, 1).equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEmpty(String text) {
        if (TextUtils.isEmpty(text)) {
            return true;
        }
        return false;
    }



    public static boolean checkEmailFormat(String email) {
        String expression = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPassValid(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void showProgress(Context context){
        dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.YELLOW)
                .text("Loading...")
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
         /*progress = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Cargando...")
                .show();*/
    }

    public static void hideProgress(){
        dialog.dismiss();
        //progress.dismiss();
    }
}
