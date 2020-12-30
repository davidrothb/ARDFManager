package com.david.ardfmanager.event;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.david.ardfmanager.MainActivity;
import com.david.ardfmanager.R;
import com.david.ardfmanager.controlpoint.ControlPoint;
import com.david.ardfmanager.tracks.trackAddActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EventsManagerActivity extends AppCompatActivity {

    ListView listView;
    EventListAdapter eventListAdapter;
    public static ArrayList<Event> eventArrayList;

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ARDFData";  //path to data folder
    public static File dir = new File(path);    //data folder
    File[] filelist = dir.listFiles();      //filelist

    private JSONObject jsonEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_manager);

        listView = (ListView) findViewById(R.id.eventsListView);
        eventArrayList = new ArrayList<>();
        dir.mkdir();


        for (int i = 0; i < filelist.length; i++) {     //if the file matches ARDF-EVENT filename then show it in the list
            String[] parts = filelist[i].getName().split("-");
            if(parts[0].equals("ARDF") && parts[1].equals("EVENT")){
            Event e = new Event(parts[2],0,0,0);
            eventArrayList.add(e);
            }
        }

        eventListAdapter = new EventListAdapter(this, R.layout.event_view_layout, eventArrayList);
        listView.setAdapter(eventListAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                            listView.setAdapter(eventListAdapter);
                            alert.dismiss();
                        }
                    }

                });
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

    public boolean checkFilled(EditText et){
        if(et.getText().toString().equals("")){
            et.setError(getResources().getString(R.string.required));
            return false;
        }else{
            return true;
        }
    }

}