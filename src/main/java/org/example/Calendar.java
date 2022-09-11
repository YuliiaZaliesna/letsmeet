package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Calendar {
    public ArrayList getDataFromJson(String filename) throws ParseException {
        String content = null;
        ArrayList<ArrayList> arrayList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
        try {
            content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONObject obj = new JSONObject(content);
        ArrayList<LocalTime> workingHours = new ArrayList<>();
        String start_wh = obj.getJSONObject("working_hours").getString("start");
        workingHours.add(LocalTime.parse(start_wh));
        String end_wh = obj.getJSONObject("working_hours").getString("end");
        workingHours.add(LocalTime.parse(end_wh));
        arrayList.add(workingHours);
        JSONArray arr = obj.getJSONArray("planned_meeting");
        for (int i = 0; i < arr.length(); i++)
        {
            ArrayList<LocalTime> plannedMeetings = new ArrayList<>();
            String start_m = arr.getJSONObject(i).getString("start");
            plannedMeetings.add(LocalTime.parse(start_m));
            String end_m = arr.getJSONObject(i).getString("end");
            plannedMeetings.add(LocalTime.parse(end_m));
            arrayList.add(plannedMeetings);
        }
        return arrayList;
    }
}