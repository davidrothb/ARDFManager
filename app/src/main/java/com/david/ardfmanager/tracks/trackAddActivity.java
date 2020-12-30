package com.david.ardfmanager.tracks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.david.ardfmanager.MainActivity;
import com.david.ardfmanager.controlpoint.ControlPoint;
import com.david.ardfmanager.DecimalDigitsInputFilter;
import com.david.ardfmanager.R;
import com.david.ardfmanager.controlpoint.ControlPointAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class trackAddActivity extends AppCompatActivity {

    Button confButton, addPunchButton;
    EditText nameEditText, lengthEditText;
    ListView cplv;

    public static ArrayList<ControlPoint> controlPointsList;
    public static ControlPointAdapter controlPointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.add_track);
        setContentView(R.layout.activity_track_add);

        confButton = (Button) findViewById(R.id.confButton);
        addPunchButton = (Button) findViewById(R.id.addPunchButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        lengthEditText = (EditText) findViewById(R.id.lengthEditText);
        lengthEditText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        cplv = (ListView) findViewById(R.id.control_points_lv);
        controlPointsList = new ArrayList<>();
        controlPointAdapter = new ControlPointAdapter(this, R.layout.controlpoint_view_layout, controlPointsList);

        final Intent intent = getIntent();
        if(intent.hasExtra("name") && intent.hasExtra("length") && intent.hasExtra("controlPoints")){
            System.out.println("ma extraaa");
            nameEditText.setText(intent.getStringExtra("name"));
            lengthEditText.setText(String.valueOf(intent.getFloatExtra("length", 0)));
            ArrayList<ControlPoint> controlPointsFromIntent = (ArrayList<ControlPoint>) intent.getSerializableExtra("controlPoints");
            for(int i = 0; i < controlPointsFromIntent.size(); i++){
                controlPointsList.add(controlPointsFromIntent.get(i));
            }
            sortAndSet();
        }

        confButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(intent.hasExtra("name") && intent.hasExtra("length") && intent.hasExtra("controlPoints")) {
                    System.out.println("potvrzeni z editace");
                    int trackIndex = intent.getIntExtra("trackIndex", -1);
                    MainActivity.tracksList.get(trackIndex).setName(nameEditText.getText().toString());
                    MainActivity.tracksList.get(trackIndex).setLength(Float.parseFloat(lengthEditText.getText().toString()));
                    MainActivity.tracksList.get(trackIndex).setControlPoints(controlPointsList);
                    MainActivity.setAllAdapters();
                    finish();
                }


                if (checkFilled(nameEditText) && checkFilled(lengthEditText)) {
                Intent intent = new Intent();
                intent.putExtra("name", nameEditText.getText().toString());
                intent.putExtra("length", lengthEditText.getText().toString());
                intent.putExtra("cplist", controlPointsList);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            }
        });

        addPunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //add dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(trackAddActivity.this);
                builder.setMessage(getResources().getString(R.string.new_control_point));
                final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_punch, null);
                builder.setView(customLayout);

                final EditText EtNumber = customLayout.findViewById(R.id.number);
                final EditText EtCode = customLayout.findViewById(R.id.code);
                final CheckBox CbIsBeacon = customLayout.findViewById(R.id.isBeacon);
                final CheckBox CbIsObligatory = customLayout.findViewById(R.id.isObligatory);

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
                        if(checkFilled(EtNumber) && checkFilled(EtCode)){
                            int number = Integer.parseInt(EtNumber.getText().toString());
                            int code = Integer.parseInt(EtCode.getText().toString());
                            boolean isBeacon = CbIsBeacon.isChecked();
                            boolean isObligatory = CbIsObligatory.isChecked();

                            ControlPoint cp = new ControlPoint(number, code, isBeacon, isObligatory);
                            controlPointsList.add(cp);
                            sortAndSet();
                            alert.dismiss();
                        }
                    }

                });
            }
        });


        cplv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {  //edit dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(trackAddActivity.this);
                builder.setMessage(getResources().getString(R.string.edit_control_point));
                final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_punch, null);
                builder.setView(customLayout);

                final EditText EtNumber = customLayout.findViewById(R.id.number);
                final EditText EtCode = customLayout.findViewById(R.id.code);
                final CheckBox CbIsBeacon = customLayout.findViewById(R.id.isBeacon);
                final CheckBox CbIsObligatory = customLayout.findViewById(R.id.isObligatory);

                EtNumber.setText(String.valueOf(controlPointsList.get(i).getNumber()));
                EtCode.setText(String.valueOf(controlPointsList.get(i).getCode()));
                CbIsBeacon.setChecked(controlPointsList.get(i).isBeacon());
                CbIsObligatory.setChecked(controlPointsList.get(i).isObligatory());

                builder.setPositiveButton(R.string.ok, null);

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controlPointsList.remove(i);
                        sortAndSet();
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
                        if(checkFilled(EtNumber) && checkFilled(EtCode)){
                            int number = Integer.parseInt(EtNumber.getText().toString());
                            int code = Integer.parseInt(EtCode.getText().toString());
                            boolean isBeacon = CbIsBeacon.isChecked();
                            boolean isObligatory = CbIsObligatory.isChecked();

                            controlPointsList.get(i).setNumber(number);
                            controlPointsList.get(i).setCode(code);
                            controlPointsList.get(i).setBeacon(isBeacon);
                            controlPointsList.get(i).setObligatory(isObligatory);

                            sortAndSet();
                            alert.dismiss();
                        }
                    }

                });

                return false;
            }
        });

    }

    public boolean checkFilled(EditText et){
        if(et.getText().toString().equals("")){
            et.setError(getResources().getString(R.string.required));
            return false;
        }else{
            return true;
        }
    }

    public void sortAndSet(){
        Collections.sort(controlPointsList, new Comparator<ControlPoint>() {
            public int compare(ControlPoint cp1, ControlPoint cp2) {
                return Integer.compare(cp1.getNumber(), cp2.getNumber());
            }
        });

        cplv.setAdapter(controlPointAdapter);
    }

}
