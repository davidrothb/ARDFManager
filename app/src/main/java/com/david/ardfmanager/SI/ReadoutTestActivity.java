package com.david.ardfmanager.SI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.david.ardfmanager.R;
import com.david.ardfmanager.SI.CardReader;

import java.util.Calendar;

public class ReadoutTestActivity extends AppCompatActivity {

        private TextView mContentView;
        private TextView mPunchesView;
        private TextView mStatusView;
        private long deviceId = 0;


        /*public class CardReaderBroadcastReceiver extends BroadcastReceiver {
            ReadoutTestActivity activity;

            public CardReaderBroadcastReceiver(ReadoutTestActivity activity) {
                this.activity = activity;
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                CardReader.Event event = (CardReader.Event)intent.getSerializableExtra("Event");
                switch(event) {
                    case DeviceDetected:
                        activity.deviceId = intent.getLongExtra("Serial", 0);
                        activity.mStatusView.setText("Device (" + activity.deviceId + ") online");
                        break;
                    case ReadStarted:
                        activity.mPunchesView.setText("");
                        activity.mContentView.setText("");
                        activity.mStatusView.setText("Device (" + activity.deviceId + ") reading card " + intent.getLongExtra("CardId", 0) + "...");
                        break;
                    case ReadCanceled:
                        activity.mStatusView.setText("Device (" + activity.deviceId + ") online");
                        break;
                    case Readout:
                        CardReader.CardEntry cardEntry = (CardReader.CardEntry)intent.getParcelableExtra("Entry");
                        if (cardEntry.startTime != 0) {
                            long timeDiff = cardEntry.finishTime - cardEntry.startTime;
                            long minutes = timeDiff / (60*1000);
                            long seconds = (timeDiff - minutes * 60 * 1000) / 1000;
                            long hundreds = (timeDiff - minutes * 60 * 1000 - seconds * 1000);
                            activity.mContentView.setText(String.format("%d:%02d.%02d", minutes, seconds, hundreds));
                            StringBuilder tmpPunches = new StringBuilder();
                            for (int i=0; i<cardEntry.punches.size(); i++) {
                                if (i > 0)
                                    tmpPunches.append(", ");
                                tmpPunches.append(cardEntry.punches.get(i).code);
                            }
                            activity.mPunchesView.setText(tmpPunches);
                        }
                        else {
                            activity.mContentView.setText("Ingen startstämpel");
                        }
                        activity.mStatusView.setText(String.format("Device (%d) card %d read", activity.deviceId, cardEntry.cardId));
                        break;
                }
            }
        }*/
        private CardReaderBroadcastReceiver mMessageReceiver = new CardReaderBroadcastReceiver(this);
        private CardReader cardReader;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_readout_test);

            //mControlsView = findViewById(R.id.fullscreen_content_controls);
            mContentView = findViewById(R.id.fullscreen_content);
            mPunchesView = findViewById(R.id.punches_content);
            mStatusView = findViewById(R.id.statusView);

            // Create SIReader
            cardReader = new CardReader(this, Calendar.getInstance());
            cardReader.execute();

            // Set up local broadcast receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(CardReader.EVENT_IDENTIFIER));
        }

    }