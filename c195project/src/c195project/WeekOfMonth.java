/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

/**
 *
 * @author dhoward
 * 
 * object meant to represent a week within a month
 */
public class WeekOfMonth {
    
    private LocalDate start;
    private LocalDate end;
    
    public void WeekOfMonth() {
        
    }
    
    public void WeekOfMonth(LocalDate start, LocalDate end) {
        setStart(start);
        setEnd(end);
    }
    
    // returns range of week in a string when toString is called
    @Override
    public String toString() {
        if(start != null && end != null) {
            return start.toString() + " to " + end.toString();
        } else {
            return null;
        }
        
    }
    
    public void setStart(LocalDate start) {
        this.start = start;
    }
    
    public void setEnd(LocalDate end) {
        this.end = end;
    }
    
    public LocalDate getStart() {
        return this.start;
    }
    
    public LocalDate getEnd() {
        return this.end;
    }
    
    // get arraylist of WeekOfMonth objects for a given month of a localdate
    public static ArrayList<WeekOfMonth> getWeeksOfMonth(LocalDate date) {
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        
        // create arraylist of WeekOfMonth objects to return later
        ArrayList<WeekOfMonth> weeks = new ArrayList();
        
        // iterate through each day of the monh
        // create a WeekOfMonth object for each complete week in month and add to arraylist
        for(int i = 0; i < lastDay.getDayOfMonth(); i++) {
            
            // current date of month being looked at
            LocalDate iterateDate = firstDay.plusDays(i);
            
            //  if day is the first day of the month or if it's a Monday, create a
            //   new DayOfMonth object and set the start to the date being looked at
            if(iterateDate.getDayOfWeek().getValue() == 1 || i == 0) {
               
                // create temporary WeekOfMonth object to add to arraylist
                WeekOfMonth tempWeek = new WeekOfMonth();
                
                // set the start date to the current iterated date
                tempWeek.setStart(iterateDate);
                
                // add it to the arraylist
                weeks.add(tempWeek);
                
            // if the day is Sunday or the last day of the month, set the newest
            // WeekOfMonth end to this date
            } else if(iterateDate.getDayOfWeek().getValue() == 7 || iterateDate.equals(lastDay)) {
                weeks.get(weeks.size() - 1).setEnd(iterateDate);
            }
            
        }
        
        // return the finished list
        return weeks;
    }
}
