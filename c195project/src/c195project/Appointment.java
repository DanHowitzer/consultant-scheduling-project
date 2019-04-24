/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.time.LocalDateTime;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dhoward
 */
public class Appointment {
    private final SimpleStringProperty AppointmentID = new SimpleStringProperty("");
    private final SimpleStringProperty Type = new SimpleStringProperty("");
    private LocalDateTime start;
    private LocalDateTime end;

    private Customer customer;
    
    public Appointment() {
        
    }
    
    public Appointment(Customer customer, User user, String Type, LocalDateTime Start, LocalDateTime End) {
        
        setCustomer(customer);
        setType(Type);
        setStart(Start);
        setEnd(End);
        
    }
    
    public Appointment(String AppointmentID, Customer customer, User user,
            String Type, LocalDateTime Start, LocalDateTime End) {
        
        setCustomer(customer);
        setType(Type);
        setStart(Start);
        setEnd(End);
        
    }
    
    public void setAppointmentID(String appointmentID) {
        AppointmentID.set(appointmentID);
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setType(String Type) {
        this.Type.set(Type);
    }
    
    public void setStart(LocalDateTime Start) {
        this.start = Start;
    }
    
    public void setEnd(LocalDateTime End) {
        this.end = End;
    }
    
    public String getAppointmentID() {
        return AppointmentID.get();
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public String getType() {
        return Type.get();
    }
    
    public LocalDateTime getStart() {
        return start;
    }
    
    public LocalDateTime getEnd() {
        return end;
    }
    
}
