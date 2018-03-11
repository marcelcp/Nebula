package com.note.nebula.nebula.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.note.nebula.nebula.models.Note;
import com.note.nebula.nebula.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 9/29/2015.
 */
public class NoteManager {
    //instance variables
    private Context mContext;
    private static NoteManager sNoteManagerInstance = null;

    public static NoteManager newInstance(Context context){

        if (sNoteManagerInstance == null){
            sNoteManagerInstance = new NoteManager(context.getApplicationContext());
        }

        return sNoteManagerInstance;
    }

    //private constructor, cannot be instantiated from the outside
    private NoteManager(Context context){
        this.mContext = context.getApplicationContext();
    }

    public long create(Note note) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, note.getTitle());
        values.put(Constants.COLUMN_CONTENT, note.getContent());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());
        Uri result = mContext.getContentResolver().insert(NoteContentProvider.CONTENT_URI, values);
        long id = Long.parseLong(result.getLastPathSegment());
        return id;
    }

    public void update(Note note) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, note.getTitle());
        values.put(Constants.COLUMN_CONTENT, note.getContent());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());
        mContext.getContentResolver().update(NoteContentProvider.CONTENT_URI,
                values, Constants.COLUMN_ID  + "=" + note.getId(), null);

    }

    public void delete(Note note) {
        mContext.getContentResolver().delete(
                NoteContentProvider.CONTENT_URI, Constants.COLUMN_ID + "=" + note.getId(), null);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<Note>();
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI, Constants.COLUMNS, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                notes.add(Note.getNotefromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notes;
    }

    public Note getNote(Long id) {
        Note note;
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI,
                Constants.COLUMNS, Constants.COLUMN_ID + " = " + id, null, null);
        Log.d("Cursor", "" + cursor.getCount());
        if (cursor != null){
            cursor.moveToFirst();
            note = Note.getNotefromCursor(cursor);
            cursor.close();
            return note;
        }
        return null;
    }





}
