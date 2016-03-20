package com.example.hana.notifyme;

import android.app.Activity;
import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


/**
 * Created by mantewari on 8/23/2015.
 */
public class Timer extends DialogFragment {
    EditText textHour;
    EditText textMinutes;
    EditText textSeconds;

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.timerfragment, container, false);

        //getDialog().setTitle("Choose Time");
        //Need to push the dialog fragment slightly above than current position
        //getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Button buttonOK = (Button) v.findViewById(R.id.btnOK);
        Button buttonCancel = (Button) v.findViewById(R.id.btnCancel);
        textHour = (EditText)v.findViewById(R.id.textHours);
        textMinutes = (EditText)v.findViewById(R.id.textMinutes);
        textSeconds = (EditText) v.findViewById(R.id.textSeconds);
        // textHour.setFilters(new InputFilter[]{new InputFilterMinMax("0", "23"), new InputFilterMinMax.LengthFilter(2)});
        // textMinutes.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59"), new InputFilterMinMax.LengthFilter(2)});
        // textSeconds.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59"), new InputFilterMinMax.LengthFilter(2)});
       /* textHour.setSelectAllOnFocus(true);
        textMinutes.setSelectAllOnFocus(true);
        textSeconds.setSelectAllOnFocus(true);*/
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimer(v);
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer(v);
            }
        });

        /*textHour.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showListTimer(v);
                return false;
            }
        });*/
        textHour.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textHour.length() == 2) {
                    textHour.clearFocus();
                    textMinutes.requestFocus();
                    if(Integer.parseInt(s.toString())>23)
                    {
                        textHour.setText("23");
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        textMinutes.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textMinutes.length() == 2) {
                    textMinutes.clearFocus();
                    textSeconds.requestFocus();
                }
                if (textMinutes.length() == 1) {
                    if (Integer.parseInt(s.toString()) > 5) {
                        textMinutes.setText("0");
                        textMinutes.setSelection(1);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
      /*  textMinutes.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 67 && textMinutes.length() == 0) {
                    textMinutes.clearFocus();
                    textHour.requestFocus();
                }
                return false;
            }
        });*/
        textSeconds.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textSeconds.length() == 1) {
                    if (Integer.parseInt(s.toString()) > 5) {
                        textSeconds.setText("0");
                        textSeconds.setSelection(1);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
       /* textSeconds.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //do something here
                if (keyCode == 67 && textSeconds.length() == 0) {
                    textSeconds.clearFocus();
                    textMinutes.requestFocus();
                }
                //    return true;
                //}
                return false;
            }
        });*/
        return v;
    }

    public void setTimer(View view)
    {
        //Toast.makeText(view.getContext(),"from timer fragment",Toast.LENGTH_LONG).show();

        long hour =  textHour.getText().toString() != ""?Long.parseLong(textHour.getText().toString())*60*60*1000:0;
        long seconds = textSeconds.getText().toString() != ""?Long.parseLong(textSeconds.getText().toString())*1000:0;
        long minutes = textMinutes.getText().toString() != ""?Long.parseLong(textMinutes.getText().toString())*60000:0;
        long miliseconds = hour + seconds + minutes;
        String string = String.valueOf(miliseconds);
        Toast.makeText(view.getContext(), string, Toast.LENGTH_LONG).show();
        callback.setAlarm(miliseconds);

    }

    public void cancelTimer(View view)
    {
        callback.cancelTimer();
    }

    public interface MyCallback{
        void setAlarm(long miliseconds);
        void cancelTimer();
    }

    MyCallback callback;
    Activity sample;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MyCallback) activity;
        sample= activity;

    }
    public void showListTimer(View view){
        ScrollView sv = new ScrollView(view.getContext());
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout ll = new LinearLayout(view.getContext());
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll); // adding a new LinearLayout inside your ScrollView

        for(int i = 0; i < 20; i++)
        {
            Button b = new Button(view.getContext()); //instead of Button you can add anything you want to add inside the LinearLayout of the ScrollView
            b.setText("Button "+i);
            ll.addView(b); //adding the Button to the LinearLayout inside ScrollView
        }

        // linLayout.addView(sv);//finally adding the ScrollView to the parent LinearLayout of the layout which you have posted in the question
        //Activity host =  ((Activity)view.getContext());

        view.isFocused();
        sample.setContentView(sv);
    }
}
