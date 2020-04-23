package com.example.meruvents.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.meruvents.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    String CategoryName, Name, startDate, startTime, endDate, endTime, category, Location;;

    DatabaseReference eventsRef;

    int hour,min;

    int GalleryPick = 1;

    Uri ImageUri;

    ProgressDialog loadingBar;

    TextView ticket;

    StorageReference eventImageRef;


    public CreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_create, container, false);
        loadingBar = new ProgressDialog(getActivity());
        eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");
        ticket = view.findViewById(R.id.addticket);
        final EditText editText = view.findViewById(R.id.eventName);
        final EditText location = view.findViewById(R.id.eventLocation);
        Button button = view.findViewById(R.id.submitevent);
        final Calendar myCalendar = Calendar.getInstance();
        hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        min = myCalendar.get(Calendar.MINUTE);
        final TextView begin = view.findViewById(R.id.starttime);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime(hour,min);
            }

            private void showTime(int hour, int min) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        begin.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, min, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        min = myCalendar.get(Calendar.MINUTE);
        final TextView endtime = view.findViewById(R.id.endtime);
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTime(hour,min);
            }

            private void endTime(int hour, int min) {
                TimePickerDialog xTimePicker;
                xTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endtime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, min, false);//Yes 24 hour time
                xTimePicker.setTitle("Select Time");
                xTimePicker.show();
            }
        });


        final TextView textView = view.findViewById(R.id.startdate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                textView.setText(sdf.format(myCalendar.getTime()));
                String startDate = sdf.format(myCalendar.getTime());


            }
        };
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final TextView end = view.findViewById(R.id.enddate);

        final DatePickerDialog.OnDateSetListener enddate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEnddate();
            }

            private void updateEnddate() {
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                end.setText(sdf.format(myCalendar.getTime()));
                String endDate = sdf.format(myCalendar.getTime());
            }


        };

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), enddate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Spinner spinner = view.findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.Spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = editText.getText().toString();
                startDate = textView.getText().toString();
                endDate = end.getText().toString();
                startTime = begin.getText().toString();
                endTime = endtime.getText().toString();
                Location = location.getText().toString();

                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(getActivity(), "Please provide an event name...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Location)) {
                    Toast.makeText(getActivity(), "Please provide an event location...", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Add New Event");
                    loadingBar.setMessage("Please wait while we are adding the new event.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    SaveEventInfoToDatabase();
                }
            }
            private void SaveEventInfoToDatabase() {
                HashMap<String, Object> eventMap = new HashMap<>();
                eventMap.put("id", Name);
                eventMap.put("category", CategoryName);
                eventMap.put("name", Name);
                eventMap.put("startDate", startDate);
                eventMap.put("endDate", endDate);
                eventMap.put("startTime", startTime);
                eventMap.put("endTime", endTime);
                eventMap.put("location", Location);

                eventsRef.child(Name).updateChildren(eventMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    loadingBar.dismiss();
                                    ticket.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(),"Event is added Successfully please edit your ticket ",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    loadingBar.dismiss();
                                    String message = task.getException().toString();
                                    Toast.makeText(getActivity(),"Error: "+ message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });
        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        CategoryName = adapterView.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
