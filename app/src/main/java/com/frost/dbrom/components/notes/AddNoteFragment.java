package com.frost.dbrom.components.notes;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.frost.dbrom.R;
import com.frost.dbrom.database.DBRoomProvider;
import com.frost.dbrom.database.Note;
import com.frost.dbrom.database.NoteDao;
import com.frost.dbrom.databinding.FragmentAddNoteBinding;
import com.frost.dbrom.databinding.FragmentNotesBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAddNoteBinding binding;
    private NoteDao noteDao;
    private Note note;
    public AddNoteFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddNoteFragment newInstance(Note note, String param2) {
        AddNoteFragment fragment = new AddNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable("Note", note);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable("Note");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_note,container,false);

        setUp();
        return binding.getRoot();
    }

    private void setUp() {
        setUpNoteDao();
        binding.progressBar.setVisibility(View.GONE);
        binding.frameLayout.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        binding.tvSubmit.setOnClickListener(view -> {
            if(TextUtils.isEmpty(binding.etTitle.getText().toString()))
            {
                showSnackBar("Please add title");
                return;
            }
            if(TextUtils.isEmpty(binding.etDescription.getText().toString()))
            {
                showSnackBar("Please add description");
                return;
            }
            if(note==null)
                addNote();
            else
                updateNote();
        });

        if(note!=null){
            binding.etTitle.setText(note.getTitle());
            binding.etDescription.setText(note.getDescription());
            binding.tvSubmit.setText("UPDATE");
        }
    }



    private void setUpNoteDao() {
        noteDao = DBRoomProvider.getInstance(getActivity()).getNoteDao();
    }

    private void addNote()
    {
        Note note = new Note();
        note.setTitle(binding.etTitle.getText().toString());
        note.setDescription(binding.etDescription.getText().toString());

        new AsyncTask<Note,Void,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Note... notes) {
                try {
                    noteDao.insert(notes[0]);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                binding.progressBar.setVisibility(View.GONE);

                if(result==null){
                    showSnackBar("Note add successfully!");
                    addNoteNotify(note);
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    View v = getActivity().getCurrentFocus();
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                    getActivity().onBackPressed();
                }
                else
                {
                    showSnackBar(result);
                }
            }
        }.execute(note);
    }

    private void updateNote()
    {
        note.setTitle(binding.etTitle.getText().toString());
        note.setDescription(binding.etDescription.getText().toString());

        new AsyncTask<Note,Void,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Note... notes) {
                try {
                    noteDao.updateNote(notes[0].getTitle(),notes[0].getDescription(),notes[0].getId());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                binding.progressBar.setVisibility(View.GONE);

                if(result==null){
                    showSnackBar("Note updated successfully!");
                    updateNoteNotify(note);
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    View v = getActivity().getCurrentFocus();
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                    getActivity().onBackPressed();
                }
                else
                {
                    showSnackBar(result);
                }
            }
        }.execute(note);

    }


    private void addNoteNotify(Note note){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"INOTES")
                                                    .setContentTitle("Note Created")
                                                    .setContentText(note.getTitle() + " note created successfully")
                                                    .setSmallIcon(R.drawable.notification)
                                                    .setColor(Color.parseColor("#009900"));


        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("INOTES","Notes",NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(new Random().nextInt(9999)+1,builder.build());

    }


    private void updateNoteNotify(Note note){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"INOTES")
                .setContentTitle("Note Updated")
                .setContentText(note.getTitle() + " note updated successfully")
                .setSmallIcon(R.drawable.notification)
                .setColor(Color.parseColor("#999900"));


        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("INOTES","Notes",NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(new Random().nextInt(9999)+1,builder.build());

    }

    private void showSnackBar(String message)
    {
        Snackbar snackbar = Snackbar.make(binding.llLayout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
