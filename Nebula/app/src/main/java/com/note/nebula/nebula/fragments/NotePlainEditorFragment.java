package com.note.nebula.nebula.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.note.nebula.nebula.R;
import com.note.nebula.nebula.activities.MainActivity;
import com.note.nebula.nebula.apps_security.SecureNote;
import com.note.nebula.nebula.data.NoteManager;
import com.note.nebula.nebula.models.Note;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotePlainEditorFragment extends Fragment {
    private View mRootView;
    private EditText mTitleEditText;
    private EditText mContentEditText;
    private Note mCurrentNote = null;


    public NotePlainEditorFragment() {
        // Required empty public constructor
    }

    public static NotePlainEditorFragment newInstance(long id){
        NotePlainEditorFragment fragment = new NotePlainEditorFragment();

        if (id > 0){
            Bundle bundle = new Bundle();
            bundle.putLong("id", id);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private void getCurrentNote(){
        Bundle args = getArguments();
        if (args != null && args.containsKey("id")){
            long id = args.getLong("id", 0);
            Log.d("getCurrentNote()", "id = " + id);
            if (id > 0){
                mCurrentNote = NoteManager.newInstance(getActivity()).getNote(id);
                Log.d("mCurrentNote", "" + mCurrentNote);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("OnCreate", "NotePlainEditorFrag");
        getCurrentNote();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_note_plain_editor, container, false);
        mTitleEditText = (EditText)mRootView.findViewById(R.id.edit_text_title);
        mContentEditText = (EditText)mRootView.findViewById(R.id.edit_text_note);

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("OnResume", "NotePlainEditorFragment" + mCurrentNote);
        if (mCurrentNote != null){
            populateFields();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_note_edit_plain, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
             //delete note
                if (mCurrentNote != null){
                    promptForDelete();
                }else {
                    makeToast("Cannot delete note that has not been saved");
                }
                break;
            case R.id.action_save:
                //save note
                String message = mCurrentNote !=  null ? "Note updated" : "Note saved";
                if (saveNote()){
                    makeToast(message);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void populateFields() {
//        Log.d("mCurrentNote", "Title" + mCurrentNote.getTitle());
//        Log.d("mCurrentNote", "Content" + mCurrentNote.getContent());
        mTitleEditText.setText(mCurrentNote.getTitle());
//        Log.d("DECRYPT MESSAGE", "" + mCurrentNote.getContent().length);
//        Log.d("DECRYPT MESSAGE", "" + SecureNote.decryptMessage(mCurrentNote.getContent()));
        Log.d("NOTE CONTENT", mCurrentNote.getTitle() + " : " + new String(mCurrentNote.getContent()));
        mContentEditText.setText(new String(SecureNote.getInstance().decryptMessage(mCurrentNote.getContent())));
    }


    private boolean saveNote(){

        String title = mTitleEditText.getText().toString();
        if (TextUtils.isEmpty(title)){
            mTitleEditText.setError("Title is required");
            return false;
        }
        if (title.length() > 24){
            mTitleEditText.setError("Title can not longer than 24");
            return false;
        }

        String content = mContentEditText.getText().toString();
        if (TextUtils.isEmpty(content)){
            mContentEditText.setError("Content is required");
            return false;
        }

        byte[] message = SecureNote.getInstance().encryptMessage(content.getBytes());

        if (mCurrentNote != null){
            mCurrentNote.setContent(message);
            mCurrentNote.setTitle(title);
            NoteManager.newInstance(getActivity()).update(mCurrentNote);

        }else {
            Note note = new Note();
            note.setTitle(title);
            note.setContent(message);
            long id = NoteManager.newInstance(getActivity()).create(note);
            note.setId(id);
            mCurrentNote = note;
        }

//        getActivity().finish();

        return true;
    }

    private void makeToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void promptForDelete(){
        final String titleOfNoteTobeDeleted = mCurrentNote.getTitle();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Delete " + titleOfNoteTobeDeleted + " ?");
        alertDialog.setMessage("Are you sure you want to delete the note " + titleOfNoteTobeDeleted + "?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NoteManager.newInstance(getActivity()).delete(mCurrentNote);
                makeToast(titleOfNoteTobeDeleted + " is deleted");
//                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
