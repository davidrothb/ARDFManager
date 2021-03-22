package com.david.ardfmanager.SI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import com.david.ardfmanager.MainActivity;


public class CardReaderBroadcastReceiver extends BroadcastReceiver {

    Activity activity;
    private long deviceId = 0;

    public CardReaderBroadcastReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        CardReader.Event event = (CardReader.Event)intent.getSerializableExtra("Event");
        switch(event) {
            case DeviceDetected:
                deviceId = intent.getLongExtra("Serial", 0);
                MainActivity.SIStatusText.setText("Device (" + deviceId + ") online");
                MainActivity.SIStatusText.setBackgroundColor(Color.GREEN);
                break;
            case ReadStarted:
                MainActivity.SIStatusText.setText("Device (" + deviceId + ") reading card " + intent.getLongExtra("CardId", 0) + "...");
                break;
            case ReadCanceled:
                MainActivity.SIStatusText.setText("Device (" + deviceId + ") online");
                break;
            case Readout:
                CardReader.CardEntry cardEntry = (CardReader.CardEntry)intent.getParcelableExtra("Entry");
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                if (cardEntry.startTime != 0) {
                    long timeDiff = cardEntry.finishTime - cardEntry.startTime;
                    long minutes = timeDiff / (60*1000);
                    long seconds = (timeDiff - minutes * 60 * 1000) / 1000;
                    long hundreds = (timeDiff - minutes * 60 * 1000 - seconds * 1000);
                    StringBuilder tmpPunches = new StringBuilder();
                    for (int i=0; i<cardEntry.punches.size(); i++) {
                        if (i > 0)
                            tmpPunches.append(", ");
                        tmpPunches.append(cardEntry.punches.get(i).code);
                    }
                    alertDialog.setTitle("Vyčteno: " + String.format("%d:%02d.%02d", minutes, seconds, hundreds));
                    alertDialog.setMessage(tmpPunches);
                }else{
                    alertDialog.setTitle("Lol nejde neni cas");
                    alertDialog.setMessage("");
                }
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                MainActivity.SIStatusText.setText(String.format("Device (%d) card %d read", deviceId, cardEntry.cardId));
                break;
        }
    }
}