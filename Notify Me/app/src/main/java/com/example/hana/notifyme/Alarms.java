package com.example.hana.notifyme;

/**
 * Created by mantewari on 9/12/2015.
 */
public class Alarms {

    //private int _id;
    private Integer _alarmID;
    private String _inputString;
    private String _image;


    public Alarms(){};
    public Alarms(String inputString){
        this._inputString = inputString;
    };


    public void set_inputString(String inputString){
        this._inputString=inputString;

    };
    public void set_alarmID(Integer alarmID){
        this._alarmID = alarmID;
    };

    public void set_image(String image)  {
        this._image=image;
    };

    public Integer get_alarmID()
    {
        return this._alarmID;
    }
    public String get_inputString(){
        return this._inputString;
    }
    public String get_image(){
        return this._image;
    }

}