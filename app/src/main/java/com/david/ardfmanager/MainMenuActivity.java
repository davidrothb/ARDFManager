package com.david.ardfmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.david.ardfmanager.event.EventsManagerActivity;

public class MainMenuActivity extends AppCompatActivity {

    Button eventsBtn, settingsBtn, helpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        eventsBtn = (Button) findViewById(R.id.eventsBtn);
        settingsBtn = (Button) findViewById(R.id.settingsBtn);
        helpBtn = (Button) findViewById(R.id.helpBtn);

        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissions()) {
                    Intent intent = new Intent(MainMenuActivity.this, EventsManagerActivity.class);
                    startActivity(intent);
                }else{
                    askForStoragePermission();
                }
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenuActivity.this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenuActivity.this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkPermissions(){
        return ContextCompat.checkSelfPermission(MainMenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askForStoragePermission(){
        ActivityCompat.requestPermissions(MainMenuActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainMenuActivity.this, EventsManagerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainMenuActivity.this, R.string.storage_permission_not_granted, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }



}