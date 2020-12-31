package com.david.ardfmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.david.ardfmanager.competitors.Competitor;
import com.david.ardfmanager.competitors.CompetitorsListAdapter;
import com.david.ardfmanager.competitors.competitors_fragment;
import com.david.ardfmanager.event.Event;
import com.david.ardfmanager.event.EventsManagerActivity;
import com.david.ardfmanager.tracks.Track;
import com.david.ardfmanager.tracks.TracksListAdapter;
import com.david.ardfmanager.tracks.trackAddActivity;
import com.david.ardfmanager.tracks.tracks_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Track> tracksList  = new ArrayList<>();;
    public static TracksListAdapter tracksListAdapter;
    public static ArrayList<Competitor> competitorsList = new ArrayList<>();;
    public static CompetitorsListAdapter competitorsListAdapter;

    public static Event event;

    public static final int INTENT_ADD_TRACK = 1000;
    public static final int INTENT_ADD_COMPETITOR = 1001;

    public static String path = "";

    public String activeFragment;
    public String name;
    public float length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.tracks,
                R.id.competitors,
                R.id.readouts,
                R.id.results).build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent intent = getIntent();
        event = (Event)intent.getSerializableExtra("event");
        if(event.getTracksList().isEmpty()){
            Toast.makeText(MainActivity.this, "Nema trate", Toast.LENGTH_SHORT).show();
        }
        if(event.getCompetitorsList().isEmpty()){
            Toast.makeText(MainActivity.this, "Nema zavodniky", Toast.LENGTH_SHORT).show();
        }
        tracksListAdapter = new TracksListAdapter(this, R.layout.track_view_layout, event.getTracksList());
        competitorsListAdapter = new CompetitorsListAdapter(this, R.layout.competitor_view_layout, event.getCompetitorsList());
        setAllAdaptersAndSave();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentFragment = navController.getCurrentDestination().getLabel().toString();
                if(currentFragment == getResources().getString(R.string.title_tracks)){
                    Intent intent = new Intent(MainActivity.this, trackAddActivity.class);
                    startActivityForResult(intent, INTENT_ADD_TRACK);
                }else if(currentFragment == getResources().getString(R.string.title_competitors)){
                    Competitor c = new Competitor("pokus2");
                    competitorsList.add(0, c);
                    System.out.println("priadadadadadadad");
                }
                setAllAdaptersAndSave();
            }
        });

    }


    public static void setAllAdaptersAndSave() {
        if (tracks_fragment.mListView != null) {
            tracks_fragment.mListView.setAdapter(tracksListAdapter);
        }else{
            Log.d("fragments", "Tracks fragment object is null");
        }
        if (competitors_fragment.mListView != null) {
            competitors_fragment.mListView.setAdapter(competitorsListAdapter);
        }else{
            Log.d("fragments", "Competitors fragment object is null");
        }
        EventsManagerActivity.saveEventToFile(event);
    }

    @Override
    public void onBackPressed(){
        EventsManagerActivity.saveEventToFile(event);
        MainActivity.super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case INTENT_ADD_TRACK:
                    Track t = (Track) data.getSerializableExtra("track");
                    event.addTrack(t);
                    setAllAdaptersAndSave();
                    break;

                case INTENT_ADD_COMPETITOR:

                    break;

            }
        } else {
            Toast.makeText(MainActivity.this, "Zrušeno", Toast.LENGTH_SHORT).show();
        }
    }
}
