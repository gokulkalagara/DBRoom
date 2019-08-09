package com.frost.dbrom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.frost.dbrom.broadcasts.AlarmBroadcastReceiver;
import com.frost.dbrom.components.notes.AddNoteFragment;
import com.frost.dbrom.components.notes.NotesFragment;
import com.frost.dbrom.database.Note;
import com.frost.dbrom.databinding.ActivityMainBinding;
import com.frost.dbrom.services.AppIntentService;
import com.frost.dbrom.services.AppService;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpFragment();


        //addAlarm();
        //addIntentService();
        addServices();
    }

    private void addServices()
    {
        Intent intent = new Intent(this, AppService.class);
        intent.putExtra("Url","https://www.facebook.com/");
        startService(intent);

        intent.putExtra("Url","https://www.amazon.in/");
        startService(intent);
        //finish();
    }

    private void addIntentService() {
        Intent intent = new Intent(this, AppIntentService.class);
        intent.putExtra("ResultReceiver", new ResultReceiver(new Handler()) {



            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode == 1) {
                    showSnackBar(resultData.getString("Result"));
                }
                else
                    showSnackBar("Something went wrong!");
            }
        });
        intent.putExtra("Url","https://www.facebook.com/");
        startService(intent);

        intent.putExtra("Url","https://www.amazon.in/");
        startService(intent);
        finish();
    }

    private void showSnackBar(String result) {
        Snackbar snackbar = Snackbar.make(binding.frameLayout, result, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void addAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra("text", "Gokul Kalagara is on duty");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, new Random().nextInt(999) + 1, intent, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, pendingIntent);
        }

    }

    private void setUpFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, NotesFragment.newInstance(null, null)).addToBackStack("NotesFragment").commit();
    }

    public void addNote() {
        getSupportActionBar().setTitle("Add Note");
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, AddNoteFragment.newInstance(null, null)).addToBackStack("AddNoteFragment").commit();
    }

    public void updateNote(Note note) {
        getSupportActionBar().setTitle("Update Note");
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, AddNoteFragment.newInstance(note, null)).addToBackStack("AddNoteFragment").commit();

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 2) {
            getSupportActionBar().setTitle("My Notes");
            getSupportFragmentManager().popBackStack();
            if (getSupportFragmentManager().getFragments().get(0) instanceof NotesFragment) {
                NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager().getFragments().get(0);
                notesFragment.getUserNotes();
            }
        } else {
            finish();
        }

    }
}
