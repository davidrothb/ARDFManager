package com.david.ardfmanager.competitors;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.david.ardfmanager.R;

public class CompetitorAddActivity extends AppCompatActivity {

    EditText nameEditText, surnameEditText, tagNumberNumber, yearTextNumber, countryEditText, indexEditText, startNumberNumber, callSignEditText, startTimeEditText;
    Spinner categorySpinner, clubSpinner;
    Switch genderSwitch;
    Button confButt;

    int ID;
    int SINumber;
    String name;
    String surname;
    String category;
    int gender;
    int yearOfBirth;
    String callsign;
    String country;
    int startNumber;
    String index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_add_unused);

        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        tagNumberNumber = findViewById(R.id.tagNumberNumber);
        yearTextNumber = findViewById(R.id.yearTextNumber);
        countryEditText = findViewById(R.id.countryEditText);
        indexEditText = findViewById(R.id.indexEditText);
        startNumberNumber = findViewById(R.id.startNumberNumber);
        callSignEditText = findViewById(R.id.callSignEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);

        categorySpinner = findViewById(R.id.categorySpinner);
        clubSpinner = findViewById(R.id.clubSpinner);

        genderSwitch = findViewById(R.id.genderSwitch);

        confButt = findViewById(R.id.confButton);

        confButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkFilled(nameEditText) && checkFilled(surnameEditText)){
                    String name = nameEditText.getText().toString();
                    String surname = surnameEditText.getText().toString();

                    int ID = 0;
                    String category = "kategorie";
                    int gender = genderSwitch.isActivated() ? 1 : 0;

                    if(!tagNumberNumber.getText().toString().equals("")){
                       SINumber = Integer.parseInt(tagNumberNumber.getText().toString());
                    }else{
                       SINumber = -1;
                    }

                    if(!yearTextNumber.getText().toString().equals("")){
                        yearOfBirth = Integer.parseInt(yearTextNumber.getText().toString());
                    }else{
                        yearOfBirth = -1;
                    }

                    if(!startNumberNumber.getText().toString().equals("")){
                        startNumber = Integer.parseInt(startNumberNumber.getText().toString());
                    }else{
                        startNumber = -1;
                    }

                String callsign = callSignEditText.getText().toString();
                String country = countryEditText.getText().toString();
                String index = indexEditText.getText().toString();

                Competitor competitor = new Competitor(ID, SINumber, name, surname, category, gender, yearOfBirth, callsign, country, startNumber, index);

                Intent intent = new Intent();
                intent.putExtra("competitor", competitor);
                setResult(Activity.RESULT_OK, intent);
                    System.out.println("intent zacal");
                finish();
                }
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
}