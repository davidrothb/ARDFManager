package com.david.ardfmanager.event;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.david.ardfmanager.MainActivity;
import com.david.ardfmanager.R;
import com.david.ardfmanager.competitors.Competitor;
import com.david.ardfmanager.controlpoint.ControlPoint;
import com.david.ardfmanager.tracks.Track;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EventsManagerActivity extends AppCompatActivity {


    public static ArrayList<Event> eventArrayList = new ArrayList<>();

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ARDFData";  //path to data folder
    public static File dir = new File(path);    //data folder
    File[] filelist = dir.listFiles();      //filelist

    ListView listView;
    EventListAdapter eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_manager);

        listView = (ListView) findViewById(R.id.eventsListView);
        eventArrayList.clear();
        eventListAdapter = new EventListAdapter(this, R.layout.event_view_layout, eventArrayList);

        dir.mkdir();


        if(filelist != null) {  //if there are any files
            for (int i = 0; i < filelist.length; i++) {
                if (compatibleFilename(filelist[i].getName())) {    //if the file matches ARDF-EVENT filename then show it in the list
                    try {
                        eventArrayList.add(eventFromFile(filelist[i].getName()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            Toast.makeText(EventsManagerActivity.this, "Zadny soubory", Toast.LENGTH_SHORT).show();
        }

        listView.setAdapter(eventListAdapter);


        FloatingActionButton btnEventAdd = findViewById(R.id.btn_event_add);
        btnEventAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventsManagerActivity.this);
                builder.setMessage(getResources().getString(R.string.new_event));
                final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
                builder.setView(customLayout);

                final EditText EtTitle = customLayout.findViewById(R.id.EtTitle);
                final Spinner SpLevel = customLayout.findViewById(R.id.spinner_level);
                final Spinner SpBand = customLayout.findViewById(R.id.spinner_band);
                final Spinner SpType = customLayout.findViewById(R.id.spinner_type);

                builder.setPositiveButton(R.string.ok, null);

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alert = builder.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(checkFilled(EtTitle)){
                            Event e = new Event(
                                    EtTitle.getText().toString(),
                                    SpLevel.getSelectedItemPosition(),
                                    SpBand.getSelectedItemPosition(),
                                    SpType.getSelectedItemPosition());
                            eventArrayList.add(e);
                            saveEventToFile(e);
                            listView.setAdapter(eventListAdapter);
                            alert.dismiss();
                        }
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(EventsManagerActivity.this, MainActivity.class);
                intent.putExtra("event", eventArrayList.get(i));
                startActivity(intent);
            }
        });

    }


    public Event eventFromFile(String filename) throws JSONException, IOException {
        JSONObject jsonEvent = jsonFromFile(path + "/" + filename);

        JSONObject jsonProperties = jsonEvent.getJSONObject("properties");
        String title = jsonProperties.getString("title");
        int level = jsonProperties.getInt("level");
        int band = jsonProperties.getInt("band");
        int type = jsonProperties.getInt("type");
        ArrayList<Track> trackArrayList = new ArrayList<>();
        ArrayList<Competitor> competitorArrayList = new ArrayList<>();

        if(jsonEvent.has("tracks")){
            JSONArray jsonTracksArray = jsonEvent.getJSONArray("tracks");
            for (int i = 0; i < jsonTracksArray.length(); i++) {
                JSONObject jsonTrack = jsonTracksArray.getJSONObject(i);
                String name = jsonTrack.getString("name");
                Float length = Float.parseFloat(jsonTrack.getString("length"));
                ArrayList<ControlPoint> controlPointArrayList = new ArrayList<>();

                if(jsonTrack.has("controlPoints")) {
                    JSONArray jsonControlPointsArray = jsonTrack.getJSONArray("controlPoints");
                    for (int y = 0; y < jsonControlPointsArray.length(); y++) {
                        JSONObject jsonControlPoint = jsonControlPointsArray.getJSONObject(i);
                        int number = jsonControlPoint.getInt("number");
                        int code = jsonControlPoint.getInt("code");
                        boolean isObligatory = jsonControlPoint.getBoolean("isObligatory");
                        boolean isBeacon = jsonControlPoint.getBoolean("isBeacon");
                        controlPointArrayList.add(new ControlPoint(number, code, isObligatory, isBeacon));
                    }
                }
                trackArrayList.add(new Track(name, length, controlPointArrayList));
            }
        }


        if(jsonEvent.has("competitors")){
            JSONArray jsonCompetitorsArray = jsonEvent.getJSONArray("competitors");
        }

        return new Event(title, level, band, type, trackArrayList, competitorArrayList);
    }

    public static JSONObject jsonFromEvent(Event e) throws JSONException {
        JSONArray jsonTracksArray = new JSONArray();
        for (int i = 0; i < e.getTracksList().size(); i++) {
            jsonTracksArray.put(e.getTracksList().get(i).trackToJSON());
        }

        JSONArray jsonCompetitorsArray = new JSONArray();
        for (int i = 0; i < e.getCompetitorsList().size(); i++) {
            jsonCompetitorsArray.put(e.getCompetitorsList().get(i).competitorToJSON());
        }

        JSONObject jsonEvent = new JSONObject();
        JSONObject jsonProperties = new JSONObject();

        jsonProperties.put("title", e.getTitle());
        jsonProperties.put("level", e.getLevel());
        jsonProperties.put("band", e.getBand());
        jsonProperties.put("type", e.getType());

        jsonEvent.put("properties", jsonProperties);
        jsonEvent.put("tracks", jsonTracksArray);
        jsonEvent.put("competitors", jsonCompetitorsArray);
        return jsonEvent;
    }

    public static void saveEventToFile(Event e) {
        try {
            FileWriter fileWriter = new FileWriter(path + "/ARDF-EVENT-" + e.getTitle() + ".json");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonFromEvent(e).toString());
            bufferedWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

    }

    private JSONObject jsonFromFile(String path) throws IOException, JSONException {
        File file = new File(path);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        String response = stringBuilder.toString();
        JSONObject jsonObject  = new JSONObject(response);
        return jsonObject;
    }

    public boolean checkFilled(EditText et){
        if(et.getText().toString().equals("")){
            et.setError(getResources().getString(R.string.required));
            return false;
        }else{
            return true;
        }
    }

    public boolean compatibleFilename(String filename){
        String[] parts = filename.split("-");
        return (parts[0].equals("ARDF") && parts[1].equals("EVENT"));
    }

}