package com.note.nebula.nebula.apps_security;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.note.nebula.nebula.data.DatabaseHelper;
import com.note.nebula.nebula.utilities.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Steven Albert on 12/3/2017.
 */

public class Authenticator {

    private static Context context;

    public Authenticator(Context context) {
        this.context = context;
    }

    private static Cursor getUser() {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.CREDENTIALS_TABLE, null);
        return cursor;
    }

    public static boolean hasUser() {
        return getUser().getCount() == 1;
    }

    public static boolean authenticate(String password) {
        if(password == null || password.length() < 8 || password.length() > 12) return false;
        MessageDigest digest;
        byte[] processedPassword;
        byte[] salt;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            salt = digest.digest(password.getBytes());
            processedPassword = concat(salt, password.getBytes());
            for(int i=0; i<4; i++) {
                salt = digest.digest(processedPassword);
                processedPassword = digest.digest(concat(salt, processedPassword));
            }

            Cursor cursor = getUser();
            if(hasUser()) {
                return isEquals(cursor, processedPassword);
            }
            else {
                return create(processedPassword);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean isEquals(Cursor cursor, byte[] password) {
        cursor.moveToFirst();
        byte[] _password = cursor.getBlob(0);
        cursor.close();
        return Arrays.equals(password, _password);
    }

    private static boolean create(byte[] password) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_PASSWORD, password);
        return db.insert(Constants.CREDENTIALS_TABLE, null, cv) > -1;
    }

    public static byte[] concat(byte[] a, byte[] b) {
        if(a == null || b == null) return null;
        byte[] newBytes = new byte[a.length + b.length];
        for(int i=0; i<a.length; i++) {
            newBytes[i] = a[i];
        }
        for(int i=0; i<b.length; i++) {
            newBytes[i + a.length] = b[i];
        }
        return newBytes;
    }
}
