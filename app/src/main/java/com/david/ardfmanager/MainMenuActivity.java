package com.david.ardfmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
                Intent intent = new Intent(MainMenuActivity.this, EventsManagerActivity.class);
                startActivity(intent);
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
}