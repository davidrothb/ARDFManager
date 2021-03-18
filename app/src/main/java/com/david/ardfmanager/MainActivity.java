package com.david.ardfmanager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.david.ardfmanager.SI.CardReader;
import com.david.ardfmanager.SI.CardReaderBroadcastReceiver;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
    public static boolean EVENT_RUNNING = false;

    public static final int INTENT_ADD_TRACK = 1000;
    public static final int INTENT_ADD_COMPETITOR = 1001;

    String CHANNEL_ID = "notif_channel_1";

    public static String path = "";

    public String name;
    public float length;

    private Menu toolBarMenu; //menu in the top toolbar
    public static TextView SIStatusText; //bottom status text

    //notification shit
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    //SI VOLE
    private CardReaderBroadcastReceiver mMessageReceiver = new CardReaderBroadcastReceiver(MainActivity.this);
    private CardReader cardReader;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar at the top
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitle("pokus");
        setSupportActionBar(toolbar);

        //bottom si status text
        SIStatusText = (TextView) findViewById(R.id.SIStatusText);
        SIStatusText.setTextColor(Color.WHITE);
        SIStatusText.setBackgroundColor(Color.RED);
        SIStatusText.setText("SI disconnected");

        // Create SIReader
        cardReader = new CardReader(this, Calendar.getInstance());
        cardReader.execute();

        // Set up local broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(CardReader.EVENT_IDENTIFIER));

        //bottom fragment navigation controls
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
                    //competitorsList.add(0, c);
                    event.addCompetitor(c);
                    System.out.println("priadadadadadadad");
                }
                setAllAdaptersAndSave();
            }
        });

        ///////////////////////////////////////////////////////////////////////
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "Running notification", NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("Shows a notification when an event is running.");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(false);
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        //PendingIntent pokus = new PendingIntent(this, MainActivity.class);

         builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_run_circle_24)
                .setContentTitle(getResources().getString(R.string.ardf_running))
                .setContentText(getResources().getString(R.string.last_read_si))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line...Much longer text that cannot fit one line...Much longer text that cannot fit one line..."))
                .setOngoing(true);
                //.setContentIntent(pokus);

        //jmeno eventu
        //ardf m. je spusten, posl vyct. cip:, zavodniku vycteno: x z poctu zavodniku na trati, prepinac na print

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.toolBarMenu = menu;
        getMenuInflater().inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.eventRunningToggleBtn:
                EVENT_RUNNING = !EVENT_RUNNING;
                if(EVENT_RUNNING){
                    toolBarMenu.getItem(0).setTitle(getResources().getString(R.string.stop));
                    notificationManager.notify(123, builder.build());
                    Toast.makeText(this, "Event spuštěn!", Toast.LENGTH_SHORT).show();
                }else{
                    toolBarMenu.getItem(0).setTitle(getResources().getString(R.string.start));
                    notificationManager.cancel(123);
                    Toast.makeText(this, "Event zastaven!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.settingsBtn:
                Toast.makeText(this, "Settings!", Toast.LENGTH_LONG).show();
                return true;

            case R.id.helpBtn:
                Toast.makeText(this, "Napoveda!", Toast.LENGTH_LONG).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
