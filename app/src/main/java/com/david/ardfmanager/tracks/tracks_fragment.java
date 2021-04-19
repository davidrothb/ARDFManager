package com.david.ardfmanager.tracks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.david.ardfmanager.DecimalDigitsInputFilter;
import com.david.ardfmanager.MainActivity;
import com.david.ardfmanager.R;
import com.david.ardfmanager.controlpoint.ControlPoint;
import com.david.ardfmanager.controlpoint.ControlPointAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tracks_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tracks_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ListView mListView;

    static View view;

    //track add dialog ui
    static Button confButton, addPunchButton;
    static EditText nameEditText, lengthEditText;
    static ListView cplv;
    private static ArrayList<ControlPoint> controlPointsList;
    private static ControlPointAdapter controlPointAdapter;


    public tracks_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tracks.
     */
    // TODO: Rename and change types and number of parameters
    public static tracks_fragment newInstance(String param1, String param2) {
        tracks_fragment fragment = new tracks_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*View*/ view = inflater.inflate(R.layout.fragment_tracks, container, false);
        //return inflater.inflate(R.layout.fragment_tracks, container, false);
        mListView = (ListView) view.findViewById(R.id.tracksListView);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        tracks_fragment.mListView.setAdapter(MainActivity.tracksListAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTrackAddDialog(getContext(), MainActivity.event.getTracksList().get(i));
                return false;
            }
        });

        return view;
    }

    public static void showTrackAddDialog(Context c, Track track){
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(c).inflate(R.layout.activity_track_add, viewGroup, false);    //set layout to view

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(R.string.add_track);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show(); //build and show the dialog

        //set the dialog size based on screen size
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.height = (int) (MainActivity.displayMetrics.heightPixels * 0.8f);
        alertDialog.getWindow().setAttributes(layoutParams);

        //assign ids to ui elements
        confButton = (Button) dialogView.findViewById(R.id.confButton);
        addPunchButton = (Button) dialogView.findViewById(R.id.addPunchButton);
        nameEditText = (EditText) dialogView.findViewById(R.id.nameEditText);
        lengthEditText = (EditText) dialogView.findViewById(R.id.lengthEditText);
        lengthEditText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        cplv = (ListView) dialogView.findViewById(R.id.control_points_lv);
        controlPointsList = new ArrayList<ControlPoint>();
        controlPointAdapter = new ControlPointAdapter(c, R.layout.controlpoint_view_layout, controlPointsList);

        /*/make the control point list show empty item when empty
        TextView emptyText = (TextView)dialogView.findViewById(R.id.empty_list_item);
        cplv.setEmptyView(emptyText);*/

        if(track != null){ //if valid track is in function arguments
            nameEditText.setText(track.getName());
            lengthEditText.setText(String.valueOf(track.getLength()));
            controlPointsList = track.getControlPoints();
            ControlPoint casd = new ControlPoint(1,2,0);
            controlPointsList.add(casd);
            sortAndSet();
        }

        confButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.checkFilled(c, nameEditText) && MainActivity.checkFilled(c, lengthEditText)) {
                    if(track == null){
                        Track t = new Track(
                                nameEditText.getText().toString(),
                                Float.parseFloat(lengthEditText.getText().toString()),
                                controlPointsList);
                        MainActivity.event.addTrack(t);
                    }else{
                        MainActivity.event.getTracksList().get(MainActivity.event.getTracksList().indexOf(track)).setName(nameEditText.getText().toString());
                        MainActivity.event.getTracksList().get(MainActivity.event.getTracksList().indexOf(track)).setLength(Float.parseFloat(lengthEditText.getText().toString()));
                        MainActivity.event.getTracksList().get(MainActivity.event.getTracksList().indexOf(track)).setControlPoints(controlPointsList);
                    }
                    MainActivity.setAllAdaptersAndSave();
                    alertDialog.dismiss();
                }
            }
        });

        addPunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //add dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setMessage(R.string.new_control_point);
                final View customLayout = LayoutInflater.from(c).inflate(R.layout.dialog_add_punch, null);
                builder.setView(customLayout);


                final NumberPicker punchNumPick = customLayout.findViewById(R.id.punchNumPick);
                final NumberPicker punchCodePick = customLayout.findViewById(R.id.punchCodePick);
                final RadioGroup typePicker = customLayout.findViewById(R.id.typePicker);
                final RadioButton rb_basic_cp = customLayout.findViewById(R.id.rb_basic_cp);
                final RadioButton rb_spec_cp = customLayout.findViewById(R.id.rb_spec_cp);
                final RadioButton rb_beacon_cp = customLayout.findViewById(R.id.rb_beacon_cp);

                punchNumPick.setMinValue(0);
                punchNumPick.setMaxValue(99);

                punchCodePick.setMinValue(0);
                punchCodePick.setMaxValue(99);

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
                            int number = punchNumPick.getValue();
                            int code = punchCodePick.getValue();
                            int type;
                            if(typePicker.getCheckedRadioButtonId() == rb_basic_cp.getId()){
                                type = 0;
                            }else if(typePicker.getCheckedRadioButtonId() == rb_beacon_cp.getId()){
                                type = 1;
                            }else if(typePicker.getCheckedRadioButtonId() == rb_spec_cp.getId()){
                                type = 2;
                            }else{
                                type = -1;
                            }

                            ControlPoint cp = new ControlPoint(number, code, type);
                            controlPointsList.add(cp);
                            sortAndSet();
                            alert.dismiss();
                    }

                });
            }
        });
    }

    public static void sortAndSet(){
        Collections.sort(controlPointsList, new Comparator<ControlPoint>() {
            public int compare(ControlPoint cp1, ControlPoint cp2) {
                return Integer.compare(cp1.getNumber(), cp2.getNumber());
            }
        });
        cplv.setAdapter(controlPointAdapter);
    }

}
