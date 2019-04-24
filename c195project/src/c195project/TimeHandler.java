/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author dhoward
 */
public class TimeHandler {
    
    private static final int timeIncrement = 15;
    
    
    // returns an arraylist of LocalTIme in 15 minute blocks between the hours of 8am and 5pm
    public static ArrayList<LocalTime> getBusinessHours() {
        
        ArrayList<LocalTime> businessHours = new ArrayList();
        
        // iterate through each hour and 15 minute block between 8:00 and 16:45 (8am and 4:45pm)
        for(int h = 8; h < 17; h++) {
            // iterate through each 15 minute block from :00 to :45
            for(int m = 0; m < 60; m = m + timeIncrement ) {
                businessHours.add(LocalTime.parse(twoDigitString(Integer.toString(h)) + ":" +
                        twoDigitString(Integer.toString(m))));
            }
        }
        
        // add the time 15:00 (5:00pm) since the for loops skip that time
        // this will be used for the end time dialog
        businessHours.add(LocalTime.parse("17:00"));
        
        return businessHours;
    }
    
    
    // adds a leading zero to any single digit strings for parsing time
    private static String twoDigitString(String numberString) {
        if(numberString.length() < 2) {
            while(numberString.length() < 2) {
                numberString = "0" + numberString;
            }
        }
        return numberString;
    }
    
    
    // returns the next timeslot (incremental unit after the time provided)
    public static LocalTime nextTimeSlot(LocalTime time) {
        return time.plusMinutes(timeIncrement);
    }
    
    
    // returns the previous timeslot (incremental unit before the time provided)
    public static LocalTime previousTimeSlot(LocalTime time) {
        return time.minusMinutes(timeIncrement);
    }
    
    
    // returns Month enums for each of the 12 months of the year in an arraylist
    public static ArrayList<Month> getMonths() {
        
        // create an arraylist for the months
        ArrayList<Month> months = new ArrayList();
        
        // create an array of integers for the month numbers (1-12)
        int[] monthNumbers = new int[12];
        
        // set array of month numbers from 1 to 12
        // lambda expression makes it succinct and easy
        Arrays.setAll(monthNumbers, i -> i + 1);
        
        // loop through each month number from 1 to 12 to add Month enum (January, February, etc.)
        // to the arraylist
        // lambda expression makes this succinct and easy to implement
        Arrays.stream(monthNumbers).forEach(i -> months.add(Month.of(i)));
        
        // return the 12 month enum arraylist
        return months;
    }
    
    
    // converts localdatetime from UTC to user's localdatetime
    public static LocalDateTime utcToLocalDateTime(LocalDateTime time) {
        
        // instantiate localdatetime using UTC timezone 
        ZonedDateTime timeUTCZoned = time.atZone(ZoneId.of("UTC"));
        
        // instantiate new localdatetime using user's timezone
        ZonedDateTime timeUserZoned = timeUTCZoned.withZoneSameInstant(ZoneId.systemDefault());
        
        System.out.println("UTC datetime " + timeUTCZoned.toString() + " converted to localtime " + timeUserZoned.toString());
        
        // return UTC datetime converted into user's local datetime
        return timeUserZoned.toLocalDateTime();
        
    }
    
    
    // converts user's localdatetime to utc localdatetime
    public static LocalDateTime localToUTCDateTime(LocalDateTime time) {
        
        // instantiate localdatetime using user's timezone
        ZonedDateTime timeUserZoned = time.atZone(ZoneId.systemDefault());
        
        // instantiate localdatetime using UTC timezone
        ZonedDateTime timeUTCZoned = timeUserZoned.withZoneSameInstant(ZoneId.of("UTC"));
        
        System.out.println("Local datetime " + timeUserZoned.toString() + " converted to UTC " + timeUTCZoned.toString());
        
        return timeUTCZoned.toLocalDateTime();
        
    }
    
    
    
    
}
