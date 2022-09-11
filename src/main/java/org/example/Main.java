package org.example;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParseException {
        Calendar calendar1 = new Calendar();
        Calendar calendar2 = new Calendar();
        ArrayList<ArrayList> arrayList1 = calendar1.getDataFromJson("calendar1.json");
        ArrayList<ArrayList> arrayList2 = calendar2.getDataFromJson("calendar2.json");
        List<LocalTime> wh1 = arrayList1.get(0), wh2 = arrayList2.get(0);
        List<ArrayList> pm1 = arrayList1.subList(1,arrayList1.size());
        List<ArrayList> pm2 = arrayList2.subList(1,arrayList2.size());
        pm1.addAll(pm2);
        List<ArrayList> pm_combined = pm1;
        LocalTime max_start = wh1.get(0).isAfter(wh2.get(0)) ? wh1.get(0) : wh2.get(0);
        LocalTime min_end = wh1.get(1).isAfter(wh2.get(1)) ? wh2.get(1) : wh1.get(1);
        int index = 0;
        while (index<pm_combined.size()) {
            boolean changeMade = false;
            for (int i = index+1; i < pm_combined.size(); i++) {
                List<LocalTime> list1 = pm_combined.get(index), list2 = pm_combined.get(i);
                if(!list1.get(1).isBefore(list2.get(0)) && !list1.get(1).isAfter(list2.get(1))){
                    LocalTime smallest = list1.get(0).isBefore(list2.get(0)) ? list1.get(0) : list2.get(0);
                    list1.set(0, smallest);
                    list1.set(1, list2.get(1));
                    pm_combined.remove(i);
                    changeMade = true;
                    break;
                } else if(!list2.get(1).isBefore(list1.get(0)) && !list2.get(1).isAfter(list1.get(1))){
                    LocalTime smallest = list1.get(0).isBefore(list2.get(0)) ? list1.get(0) : list2.get(0);
                    list1.set(0, smallest);
                    list1.set(1, list1.get(1));
                    pm_combined.remove(i);
                    changeMade = true;
                    break;
                }

            }
            if(!changeMade)
                index++;
        }

        pm_combined.sort((a, b)->{
            LocalTime aa = (LocalTime) a.get(0);
            LocalTime bb = (LocalTime) b.get(0);
            return aa.compareTo(bb);
        });

        List<List<LocalTime>> free_intervals = new ArrayList<>();
        List<LocalTime> interval = new ArrayList<>();

        if(max_start.isBefore((LocalTime) pm_combined.get(0).get(0))){
            interval.add(max_start);
            interval.add( (LocalTime) pm_combined.get(0).get(0));
            if(((LocalTime) pm_combined.get(0).get(0)).until(max_start, ChronoUnit.MINUTES) >= 30){
                free_intervals.add(interval);
            }
        }
        for(int i = 0; i < pm_combined.size()-1; i++){
            interval = new ArrayList<>();
            List<LocalTime> list1 = pm_combined.get(i), list2 = pm_combined.get(i+1);
            interval.add(list1.get(1));
            interval.add(list2.get(0));
            if(list1.get(1).until(list2.get(0), ChronoUnit.MINUTES) >= 30){
                free_intervals.add(interval);
            }
        }
        if(min_end.isAfter((LocalTime) pm_combined.get(pm_combined.size()-1).get(1))){
            interval = new ArrayList<>();
            interval.add((LocalTime) pm_combined.get(pm_combined.size()-1).get(1));
            interval.add(min_end);
            if(((LocalTime) pm_combined.get(pm_combined.size()-1).get(1)).until(min_end, ChronoUnit.MINUTES) >= 30){
                free_intervals.add(interval);
            }
        }
        System.out.println(free_intervals);
    }
}