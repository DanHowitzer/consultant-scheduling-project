/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhoward
 */
public class AppointmentDAO {
    
    
    private static final String selectSQL = "select a.appointmentid, "
                    + "c.customerid, c.customername, d.addressid, d.address, d.address2,"
                    + "d.postalcode, d.phone, i.cityid, i.city, o.countryid, o.country,"
                    + "a.title, a.start, a.end "
                    + "from appointment a inner join customer c on a.customerid = c.customerid "
                    + "inner join address d "
                    + "on c.addressid = d.addressid inner join city i "
                    + "on d.cityid = i.cityid inner join country o "
                    + "on i.countryid = o.countryid ";
    
    public static ArrayList<Appointment> getAllAppointments() {
        
        // create arraylist to store appointments returned from database
        ArrayList<Appointment> appointments = new ArrayList();
        
        // try with resources to get full data for each customer in database
        // including name, address, city, and country
        try(Connection connection = ConnectionManager.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(selectSQL + " order by a.start");
            
            // process resultset into arraylist of appointments
            appointments = processAppointments(results);
            
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // return completed arraylist, could be empty if database is empty or connection errors out
        return appointments;
    }
    
    
    public static void addAppointment(Appointment appointment, User user) throws SQLException {
        
        // start prepared statement
        PreparedStatement appointmentStatement = null;
        
        // insert query for inserting new appointment
        String appointmentSQL = "insert into appointment(customerid, title,"
                + "start, end, createdate, createdby, lastupdate, lastupdateby,"
                + "description, location, contact, url) "
                + "values(?, ?, ?, ?, now(), ?, now(), ?, ?, ?, ?, ?)";
        
        // try with resources to connect to the database
        try ( Connection connection = ConnectionManager.getConnection();) {
            
            // instantiate prepared statement using the sql statement above
            appointmentStatement = connection.prepareStatement(appointmentSQL);
            
            // insert attributes for appointment into statement
            appointmentStatement.setString(1, appointment.getCustomer().getCustomerID());
            appointmentStatement.setString(2, appointment.getType());
            
            // jdbc library will automatically convert localdatetime to UTC
            appointmentStatement.setObject(3, appointment.getStart());
            appointmentStatement.setObject(4, appointment.getEnd());
            
            appointmentStatement.setString(5, user.getName());
            appointmentStatement.setString(6, user.getName());
            appointmentStatement.setString(7, "");
            appointmentStatement.setString(8, "");
            appointmentStatement.setString(9, "");
            appointmentStatement.setString(10, "");
            
            // execute update to insert appointment
            appointmentStatement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close prepared statement
            if(appointmentStatement != null) {
                appointmentStatement.close();
            }
            
        }
    }
    
    
    public static void updateAppointment(Appointment appointment, User user) throws SQLException {
        PreparedStatement appointmentStatement = null;
        
        // query for updating address
        String appointmentSQL = "update appointment set customerid = ?, "
                + "title = ?, start = ?, end = ?, lastupdate = now(),"
                + "lastupdateby = ? where appointmentid = ?";
        
        // try catch block with resources
        try (Connection connection = ConnectionManager.getConnection();) {
            
            // instantiate prepared statement to insert customer into database
            appointmentStatement = connection.prepareStatement(appointmentSQL);
            
            // set attributes for prepared statement
            // update address in database
            appointmentStatement.setString(1, appointment.getCustomer().getCustomerID());
            appointmentStatement.setString(2, appointment.getType());
            appointmentStatement.setObject(3, appointment.getStart());
            appointmentStatement.setObject(4, appointment.getEnd());
            appointmentStatement.setString(5, user.getName());
            appointmentStatement.setString(6, appointment.getAppointmentID());
            
            System.out.println("Running statement: " + appointmentStatement.toString());
            
            // execute the update
            appointmentStatement.executeUpdate();
            System.out.println("Appointment updated!");
        }
    }
    
    
    public static void deleteAppointment(Appointment appointment) throws SQLException {
        
        // create prepared statement
        PreparedStatement statement = null;
        
        // create sql query to run
        String sqlQuery = "delete from appointment where appointmentid = ?";
        
        // try with resources to get connection
        try(Connection connection = ConnectionManager.getConnection();) {
            
            // get prepared statement using sql query
            statement = connection.prepareStatement(sqlQuery);
            
            // get appointmentid from appointment to be deleted
            statement.setString(1, appointment.getAppointmentID());
            
            // execute delete
            statement.execute();
            
        }
        
    }
    
    
    // gets a list of appointments by localdate month
    public static ArrayList<Appointment> getAppointmentsByMonth(LocalDate date) {
        
        // get the month of the date provided
        Month month = date.getMonth();
        
        // get the year of the date provided
        int year = date.getYear();
        
        // create arraylist of appointments to send back
        ArrayList<Appointment> appointments = new ArrayList();
        
        // create parepared statement
        PreparedStatement statement = null;
        
        // construct sql query
        String sqlQuery = selectSQL + "where year(a.start) = ? and month(a.start) = ? order by a.start";
        
        // try with resources to query database
        // closes connection automatically
        try(Connection connection = ConnectionManager.getConnection();) {
            
            // creat prepared statement based of sql query
            statement = connection.prepareStatement(sqlQuery);
            
            // set parameters of statement
            statement.setInt(1, year);
            statement.setInt(2, month.getValue());
            
            System.out.println(statement.toString());
            
            // execute finished preparedstatement
            ResultSet result = statement.executeQuery();
            
            // process results into appointment arraylist
            appointments = processAppointments(result);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // return arraylist of appointments
        return appointments;
        
    }
    

    // gets a list of appointments on the localdate passed
    public static ArrayList<Appointment> getAppointmentsByLocalDate(LocalDate date) {
        
        // create arraylist of appointments to send back
        ArrayList<Appointment> appointments = new ArrayList();
        
        // get beginning of day for provided localdate in UTC time and the end of the day a minute before midnight
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        
        // create parepared statement
        PreparedStatement statement = null;
        
        // create sql query string
        // we are assuming all appointments in database start and end on the same date
        String sqlQuery = selectSQL + "where start between ? and ? and end between ? and ? order by a.start";
        
        // try with resources to get appointments on the specified date
        try(Connection connection = ConnectionManager.getConnection();) {
            
            // fully instantiate statement
            statement = connection.prepareStatement(sqlQuery);
            
            // insert parameters
            statement.setObject(1, start);
            statement.setObject(2, end);
            statement.setObject(3, start);
            statement.setObject(4, end);
            
            // query database and get results
            ResultSet results = statement.executeQuery();
            
            // turn resultset into an arraylist of appointments
            appointments = processAppointments(results);
            
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // return completed arraylist, could be empty if database is empty or connection errors out
        return appointments;
        
    }
    
    
    // gets a list of appointments on the weekofmonth passed
    public static ArrayList<Appointment> getAppointmentsByWeek(WeekOfMonth week) {
        
        // create arraylist of appointments to send back
        ArrayList<Appointment> appointments = new ArrayList();
        
        // create parepared statement
        PreparedStatement statement = null;
        
        // create sql query string
        // we are assuming all appointments in database start and end on the same date
        String sqlQuery = selectSQL + "where start between ? and ? order by a.start";
        
        // try with resources to get appointments on the specified date
        try(Connection connection = ConnectionManager.getConnection();) {
            
            // fully instantiate statement
            statement = connection.prepareStatement(sqlQuery);
            
            // insert parameters
            statement.setObject(1, week.getStart());
            statement.setObject(2, week.getEnd());
            
            // query database and get results
            ResultSet results = statement.executeQuery();
            
            // turn resultset into an arraylist of appointments
            appointments = processAppointments(results);
            
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // return completed arraylist, could be empty if database is empty or connection errors out
        return appointments;
        
    }
    
    
    // get report of number of appointments per month
    public static String getAppointmentCount() {
        
        StringBuilder message = new StringBuilder();
        
        // create parepared statement
        PreparedStatement statement = null;
        
        // create sql query string
        // we are assuming all appointments in database start and end on the same date
        String sqlQuery = "select year(start) as 'year', monthname(start) as 'month', title as 'type', count(*) as 'count' " +
                                "from appointment " +
                                "group by year(start), month(start), title " +
                                "order by year(start), month(start), title";
        
        // try with resources to get appointments on the specified date
        try(Connection connection = ConnectionManager.getConnection();) {
            
            // fully instantiate statement
            statement = connection.prepareStatement(sqlQuery);
            
            // query database and get results
            ResultSet results = statement.executeQuery();
            
            message.append("Report: Number of Appointments by Type by Month and Year\n");
            message.append("---------------------------\n\n");
            
            // turn resultset into an arraylist of appointments
            while(results.next()) {
                message.append(results.getString("year") + " - " +
                        results.getString("month") + " - " + results.getString("type") + " - (" + 
                        results.getString("count") + ")\n");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // return completed string, could be empty if database is empty or connection errors out
        return message.toString();        
        
    }
 
    
        // get report of number of appointments per month
    public static String getSchedules() {
        
        StringBuilder message = new StringBuilder();
        
        // create parepared statement
        PreparedStatement statement = null;
        
        // create sql query string
        // we are assuming all appointments in database start and end on the same date
        String sqlQuery = "select a.createdby as 'consultant', c.customerName as 'customer', a.title as 'type', a.start as 'start', a.end as 'end' " +
                                "from appointment a " +
                                "inner join customer c " +
                                "on a.customerId = c.customerId " +
                                "order by a.createdby, a.start";
        
        // try with resources to get appointments on the specified date
        try(Connection connection = ConnectionManager.getConnection();) {
            
            // fully instantiate statement
            statement = connection.prepareStatement(sqlQuery);
            
            // query database and get results
            ResultSet results = statement.executeQuery();
            
            message.append("Report: Schedule for each consultant \n");
            message.append("---------------------------\n\n");
            
            // turn resultset into an arraylist of appointments
            while(results.next()) {
                // create datetimes associated with appointmentby parsing datetimes from database
                // convert UTC datetimes from the database to user's local datetime
                LocalDateTime startTime = TimeHandler.utcToLocalDateTime(results.getObject("a.start", LocalDateTime.class));
                LocalDateTime endTime = TimeHandler.utcToLocalDateTime(results.getObject("a.end", LocalDateTime.class));
                
                message.append(startTime.toLocalDate().toString() + " - Consultant " +
                        results.getString("consultant") + " with customer " +
                        results.getString("customer") + " for " +
                        results.getString("type") + " from " + 
                        startTime.toLocalTime().toString() + " to " +
                        endTime.toLocalTime().toString() + "\n");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // return completed string, could be empty if database is empty or connection errors out
        return message.toString(); 
    }
    
    
    // processes a resultset and returns an arraylist of appointments
    private static ArrayList<Appointment> processAppointments(ResultSet results) throws SQLException {
        
        // arraylist for returning appointments
        ArrayList<Appointment> appointments = new ArrayList();

        // loop through resultset and add every appointment to the arraylist
        while(results.next()) {

            // create address associated with customer associated with appointment
            Country country = new Country();
            country.setCountryID(results.getString("o.countryid"));
            country.setCountry(results.getString("o.country"));

            City city = new City();
            city.setCountry(country);
            city.setCityID(results.getString("i.cityid"));
            city.setCity(results.getString("i.city"));

            Address address = new Address();
            address.setCity(city);
            address.setAddress1(results.getString("d.address"));
            address.setAddress2(results.getString("d.address2"));
            address.setAddressID(results.getString("d.addressid"));
            address.setPhone(results.getString("d.phone"));
            address.setPostalCode(results.getString("d.phone"));

            // create customer associated with appointment
            Customer customer = new Customer();
            customer.setCustomerID(results.getString("c.customerid"));
            customer.setCustomerName(results.getString("c.customername"));
            customer.setAddress(address);

            // create datetimes associated with appointmentby parsing datetimes from database
            // convert UTC datetimes from the database to user's local datetime
            LocalDateTime startTime = TimeHandler.utcToLocalDateTime(results.getObject("a.start", LocalDateTime.class));
            LocalDateTime endTime = TimeHandler.utcToLocalDateTime(results.getObject("a.end", LocalDateTime.class));

            // create appointment
            Appointment appointment = new Appointment();
            appointment.setAppointmentID(results.getString("a.appointmentid"));
            appointment.setCustomer(customer);
            appointment.setType(results.getString("a.title"));
            appointment.setStart(startTime);
            appointment.setEnd(endTime);

            // add appointment to arraylist
            appointments.add(appointment);

        }
        
        // return appointment arraylist
        return appointments;
    }
    
    
    
}
