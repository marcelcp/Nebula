package com.note.nebula.nebula.utilities;

/**
 * Created by Valentine on 9/28/2015.
 */
public class Constants {

    public static final String NOTES_TABLE = "notes";
    public static final String CREDENTIALS_TABLE = "credentials";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_MODIFIED_TIME = "modified_time";
    public static final String COLUMN_CREATED_TIME = "created_time";


    public static final String[] COLUMNS = {
            Constants.COLUMN_ID,
            Constants.COLUMN_TITLE,
            Constants.COLUMN_CONTENT,
            Constants.COLUMN_MODIFIED_TIME,
            Constants.COLUMN_CREATED_TIME
    };
}
