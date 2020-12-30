package com.david.ardfmanager.event;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.david.ardfmanager.MainActivity;
import com.david.ardfmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EventsManagerActivity extends AppCompatActivity {

    ListView rv;
    EventListAdapter ela;
    public static ArrayList<Event> filenames;

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ARDFData";  //path to data folder
    public static File dir = new File(path);    //data folder
    File[] filelist = dir.listFiles();      //filelist

    private JSONObject jsonEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_manager);

        rv = (ListView) findViewById(R.id.eventsListView);
        filenames  = new ArrayList<>();
        dir.mkdir();


        for (int i = 0; i < filelist.length; i++) {     //if the file matches ARDF-EVENT filename then show it in the list
            String[] parts = filelist[i].getName().split("-");
            if(parts[0].equals("ARDF") && parts[1].equals("EVENT")){
            Event e = new Event(parts[2],0,0,0);
            filenames.add(e);
            }
        }

        ela = new EventListAdapter(this, R.layout.event_view_layout, filenames);
        rv.setAdapter(ela);


        rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

                try {
                    jsonEvent = readJSONFromFile(path + "/" + filelist[i].getName());   //get json from the selected file
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(jsonEvent != null) {     //if the JSONEvent is not empty then execute the intent to MainActivity with it
                    Intent intent = new Intent(EventsManagerActivity.this, MainActivity.class);
                    intent.putExtra("path", path + "/" + filelist[i].getName());
                    intent.putExtra("jsonEvent", jsonEvent.toString());
                    startActivity(intent);
                }else{      //else print an err message
                    Toast.makeText(EventsManagerActivity.this, getString(R.string.errFileRead), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private JSONObject readJSONFromFile(String path) throws IOException, JSONException {
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
}