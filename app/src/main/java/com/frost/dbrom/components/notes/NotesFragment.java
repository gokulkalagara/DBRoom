package com.frost.dbrom.components.notes;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frost.dbrom.MainActivity;
import com.frost.dbrom.R;
import com.frost.dbrom.database.DBRoomProvider;
import com.frost.dbrom.database.Note;
import com.frost.dbrom.database.NoteDao;
import com.frost.dbrom.databinding.FragmentNotesBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment implements INotesAdapter {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentNotesBinding binding;
    private NoteDao noteDao;
    private List<Note> list;
    private NotesAdapter notesAdapter;
    public NotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notes,container,false);

        setUp();
        return binding.getRoot();
    }

    private void setUp()
    {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(notesAdapter = new NotesAdapter(list=new ArrayList<>(),this));

        setUpDao();
        getUserNotes();
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            getUserNotes();
        });

        binding.fabCreate.setOnClickListener(view -> {
            if(getActivity() instanceof MainActivity){
                ((MainActivity)getActivity()).addNote();
            }
        });




    }

    private void setUpDao()
    {
        noteDao = DBRoomProvider.getInstance(getActivity()).getNoteDao();
    }

    public void getUserNotes()
    {
//        Note n1 = new Note();
//        n1.setTitle("NSC");
//        n1.setDescription("Notional Saving Certificate");
////        n1.setTimeCreated(new Date(System.currentTimeMillis()));
//
//
//        Note n2 = new Note();
//        n2.setTitle("PPF");
//        n2.setDescription("Public Provident Fund");
////        n2.setTimeCreated(new Date(System.currentTimeMillis()));
//
//
//        Note n3 = new Note();
//        n3.setTitle("TD");
//        n3.setDescription("Time Deposit Account");
////        n3.setTimeCreated(new Date(System.currentTimeMillis()));
//
//        Note n4 = new Note();
//        n4.setTitle("VK");
//        n4.setDescription("Vision Kit Account");
////        n4.setTimeCreated(new Date(System.currentTimeMillis()));

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                list = noteDao.getAllNotes();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                binding.swipeRefreshLayout.setRefreshing(false);
                notesAdapter.updatedNotes(list==null?list= new ArrayList<>():list);
            }
        }.execute();

    }


    @Override
    public void deleteNote(Note note,int position) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                noteDao.deleteNoteById(note.getId());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                list.remove(position);
                notesAdapter.notifyItemRemoved(position);
                notifyNoteDelete(note);
            }
        }.execute();
        getUserNotes();
    }

    @Override
    public void updateNote(Note note, int position) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity)getActivity()).updateNote(note);
        }
    }


    public void notifyNoteDelete(Note note)
    {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"INOTES_CHANNEL")
                                            .setContentTitle("Deleted Note")
                                            .setContentText(note.getTitle() + " note is deleted successfully")
                                            .setSmallIcon(R.drawable.notification)
                                            .setColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("INOTES_CHANNEL","Notes",NotificationManager.IMPORTANCE_HIGH));
        }

        notificationManager.notify(new Random().nextInt(9999)+1,builder.build());
    }
}
