package com.david.ardfmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.david.ardfmanager.SI.CardReader;
import com.david.ardfmanager.SI.CardReaderBroadcastReceiver;
import com.david.ardfmanager.competitors.Competitor;
import com.david.ardfmanager.competitors.CompetitorAddActivity;
import com.david.ardfmanager.competitors.CompetitorsListAdapter;
import com.david.ardfmanager.competitors.competitors_fragment;
import com.david.ardfmanager.controlpoint.ControlPoint;
import com.david.ardfmanager.controlpoint.ControlPointAdapter;
import com.david.ardfmanager.event.Event;
import com.david.ardfmanager.event.EventsManagerActivity;
import com.david.ardfmanager.readouts.SIReadout;
import com.david.ardfmanager.readouts.SIReadoutListAdapter;
import com.david.ardfmanager.readouts.readouts_fragment;
import com.david.ardfmanager.tracks.Track;
import com.david.ardfmanager.tracks.TracksListAdapter;
import com.david.ardfmanager.tracks.trackAddActivity;
import com.david.ardfmanager.tracks.tracks_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    //ToDo: edit dialogs, saving right after object is created

    //public static ArrayList<Track> tracksList  = new ArrayList<>();
    //public static ArrayList<Competitor> competitorsList = new ArrayList<>();

    public static TracksListAdapter tracksListAdapter;
    public static CompetitorsListAdapter competitorsListAdapter;

    public static ArrayList<SIReadout> siReadoutList = new ArrayList<>();
    public static SIReadoutListAdapter siReadoutListAdapter;

    public static Event event;
    public static boolean EVENT_RUNNING = false;

    public static final int INTENT_ADD_TRACK = 1000;
    public static final int INTENT_ADD_COMPETITOR = 1001;

    String CHANNEL_ID = "notif_channel_1";

    public static String path = "";

   /* public String name;
    public float length;*/

    private Menu toolBarMenu; //menu in the top toolbar
    public static TextView SIStatusText; //bottom status text

    //notification shit
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    //toolbar nahore
    Toolbar toolbar;

    FloatingActionButton fab;
    BottomNavigationView navView;

    //competitor add dialog okynko promeny vole
    int ID, SINumber, gender, yearOfBirth, startNumber;
    String name, surname, category, callsign, country, index;


    //SI VOLE
    private CardReaderBroadcastReceiver mMessageReceiver = new CardReaderBroadcastReceiver(MainActivity.this);
    private CardReader cardReader;

    //rozmery displeje
    public static DisplayMetrics displayMetrics;

    public static Resources resources;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar at the top
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);

        //bottom si status text
        SIStatusText = (TextView) findViewById(R.id.SIStatusText);
        SIStatusText.setTextColor(Color.WHITE);
        SIStatusText.setBackgroundColor(Color.RED);
        SIStatusText.setText("SI disconnected");


        //Create SIReader
        cardReader = new CardReader(this, Calendar.getInstance());
        cardReader.execute();

        // Set up local broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(CardReader.EVENT_IDENTIFIER));

        //bottom fragment navigation controls
        navView = findViewById(R.id.nav_view);

        //rozmery displeje pro dialogy
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //resources
        resources = getResources();
        
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.tracks,
                R.id.competitors,
                R.id.readouts,
                R.id.results).build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener(){
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                System.out.println("Destination changed!");
                if(event != null) {
                    getSupportActionBar().setTitle(event.getTitle());
                }
                String label = destination.getLabel().toString();
                if(label == getResources().getString(R.string.title_results)){
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                }
            }
        });


        Intent intent = getIntent();
        event = (Event)intent.getSerializableExtra("event");
        toolbar.setTitle(event.getTitle());
        if(event.getTracksList().isEmpty()){
            Toast.makeText(MainActivity.this, "Nema trate", Toast.LENGTH_SHORT).show();
        }
        if(event.getCompetitorsList().isEmpty()){
            Toast.makeText(MainActivity.this, "Nema zavodniky", Toast.LENGTH_SHORT).show();
        }
        tracksListAdapter = new TracksListAdapter(this, R.layout.track_view_layout, event.getTracksList());
        competitorsListAdapter = new CompetitorsListAdapter(this, R.layout.competitor_view_layout, event.getCompetitorsList());
        siReadoutList.clear();
        siReadoutListAdapter = new SIReadoutListAdapter(this, R.layout.sireadout_view_layout, siReadoutList);
        setAllAdaptersAndSave();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentFragment = navController.getCurrentDestination().getLabel().toString();
                System.out.println(currentFragment);
                if(currentFragment == getResources().getString(R.string.title_tracks)){
                    //Intent intent = new Intent(MainActivity.this, trackAddActivity.class);
                    //startActivityForResult(intent, INTENT_ADD_TRACK);
                    tracks_fragment.showTrackAddDialog(MainActivity.this, null);
                }else if(currentFragment == getResources().getString(R.string.title_competitors)){
                    //Intent intent = new Intent(MainActivity.this, CompetitorAddActivity.class);
                    //startActivityForResult(intent, INTENT_ADD_COMPETITOR);
                    showCompetitorAddDialog();
                }else if(currentFragment == getResources().getString(R.string.title_readouts)){
                    SIReadout siReadout = new SIReadout(6969, 666666,666666666, 10);
                    siReadoutList.add(siReadout);
                }
                setAllAdaptersAndSave();
            }
        });

        ///////////////////////////////////////////////////////////////////////
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                .setContentTitle(event.getTitle() + getResources().getString(R.string.running))//getResources().getString(R.string.ardf_running))
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
    public boolean onOptionsItemSelected(MenuItem item) {   //handling of topbar buttons(start, stop, settings, help)
        switch (item.getItemId()) {
            case R.id.eventRunningToggleBtn:
                if(EVENT_RUNNING){
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertBuilder.setMessage("Opravdu chcete " + event.getTitle() + " ukončit?");
                    alertBuilder.setPositiveButton("Ano", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            toolBarMenu.getItem(0).setTitle(getResources().getString(R.string.start));
                            notificationManager.cancel(123);
                            Toast.makeText(MainActivity.this, "Event zastaven!", Toast.LENGTH_SHORT).show();
                            EVENT_RUNNING = !EVENT_RUNNING;
                        }
                    });
                    alertBuilder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }else{
                    toolBarMenu.getItem(0).setTitle(getResources().getString(R.string.stop));
                    notificationManager.notify(123, builder.build());
                    Toast.makeText(MainActivity.this, "Event spuštěn!", Toast.LENGTH_SHORT).show();
                    EVENT_RUNNING = !EVENT_RUNNING;
                }
                return true;

            case R.id.trackMacroButton:


                event.addTrack(new Track("D7", 0, new ArrayList<>()));
                event.addTrack(new Track("D9", 30, new ArrayList<>()));
                event.addTrack(new Track("D12", 35, new ArrayList<>()));
                setAllAdaptersAndSave();
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

        if (readouts_fragment.mListView != null) {
            readouts_fragment.mListView.setAdapter(siReadoutListAdapter);
        }else{
            Log.d("fragments", "Readout fragment object is null");
        }
        EventsManagerActivity.saveEventToFile(event);
    }

    @Override
    public void onBackPressed(){    //dismiss back button when event is running
        if(!EVENT_RUNNING) {
            EventsManagerActivity.saveEventToFile(event);
            MainActivity.super.onBackPressed();
        }else{
            Toast.makeText(MainActivity.this, "Event je spuštěn!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //handles results from intents
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case INTENT_ADD_TRACK:
                    Track t = (Track) data.getSerializableExtra("track");
                    event.addTrack(t);
                    setAllAdaptersAndSave();
                    break;

                case INTENT_ADD_COMPETITOR:
                    System.out.println("intent prisel");
                    Competitor c = (Competitor) data.getSerializableExtra("competitor");
                    event.addCompetitor(c);
                    setAllAdaptersAndSave();
                    break;

            }
        } else {
            Toast.makeText(MainActivity.this, "Zrušeno", Toast.LENGTH_SHORT).show();
        }
    }

    public static Competitor findCompBySI(long SINumber){
        ArrayList<Competitor> arrayList = event.getCompetitorsList();
        for(Competitor competitor : arrayList) {
            if(competitor.getSINumber() == SINumber){
                return competitor;
            }
        }
        return null;
    }



    private void showCompetitorAddDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_competitor_add, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_competitor);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText surnameEditText = dialogView.findViewById(R.id.surnameEditText);
        EditText tagNumberNumber = dialogView.findViewById(R.id.tagNumberNumber);
        EditText yearTextNumber = dialogView.findViewById(R.id.yearTextNumber);
        EditText countryEditText = dialogView.findViewById(R.id.countryEditText);
        EditText indexEditText = dialogView.findViewById(R.id.indexEditText);
        EditText startNumberNumber = dialogView.findViewById(R.id.startNumberNumber);
        EditText callSignEditText = dialogView.findViewById(R.id.callSignEditText);
        EditText startTimeEditText = dialogView.findViewById(R.id.startTimeEditText);

        Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);
        Spinner clubSpinner = dialogView.findViewById(R.id.clubSpinner);

        Switch genderSwitch = dialogView.findViewById(R.id.genderSwitch);
        Button confButt = dialogView.findViewById(R.id.confButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        genderSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(genderSwitch.isChecked()){
                    genderSwitch.setText(R.string.female);
                }else{
                    genderSwitch.setText(R.string.male);
                }
            }
        });

        confButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFilled(MainActivity.this, nameEditText) && checkFilled(MainActivity.this, surnameEditText)) {
                    String name = nameEditText.getText().toString();
                    String surname = surnameEditText.getText().toString();

                    int ID = 0;
                    String category = "kategorie";
                    int gender = genderSwitch.isActivated() ? 1 : 0;

                    if (!tagNumberNumber.getText().toString().equals("")) {
                        SINumber = Integer.parseInt(tagNumberNumber.getText().toString());
                    } else {
                        SINumber = -1;
                    }

                    if (!yearTextNumber.getText().toString().equals("")) {
                        yearOfBirth = Integer.parseInt(yearTextNumber.getText().toString());
                    } else {
                        yearOfBirth = -1;
                    }

                    if (!startNumberNumber.getText().toString().equals("")) {
                        startNumber = Integer.parseInt(startNumberNumber.getText().toString());
                    } else {
                        startNumber = -1;
                    }
                    callsign = callSignEditText.getText().toString();
                    country = countryEditText.getText().toString();
                    index = indexEditText.getText().toString();

                    Competitor competitor = new Competitor(ID, SINumber, name, surname, category, gender, yearOfBirth, callsign, country, startNumber, index);
                    event.addCompetitor(competitor);
                    setAllAdaptersAndSave();
                    alertDialog.dismiss();
                }
            }
        });

    }

    public static boolean checkFilled(Context c, EditText et){
        if(et.getText().toString().equals("")){
            et.setError(c.getResources().getString(R.string.required));
            return false;
        }else{
            return true;
        }
    }



}
