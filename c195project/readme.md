Consultant Scheduling Project
=============================

This was a school project built on Java 8 with a JavaFX GUI using FXML for views, CSS for styling, and a MySQL database to store and retrieve data. The purpose of the program is to provide a way for consultants in different locations around the world to schedule appointments with customers. The program detects the user's default location and can display the login screen in either English or Traditional Chinese (Taiwan) depending on where they are. It also automatically displays all dates and times in local time but saves to the database in UTC. The program prevents collisions between different appointments by showing the user only times which will not coincide or overlap with existing appointments or result in an appointment less than 15 minutes long. CRUD operations are available on appointments and customers.

A:Log-in Form
---------------
When the program is launched, the user is presented with a login form prompting for a username and password. There are two languages availble, English and Traditional Chinese (Taiwan) that are determined by the user's default location in their JVM. To test this, change the user's default locale to Taiwan by uncommententing the code on line 33 of C195project.java.

Error messages will be displayed in the user's native language if the username and/or password do not match what's in the database. The following username and password combinations are in the database:

*username*	*password*
agent1		pass
coolagent	pass
dhoward		pass
otheragent	pass
uncoolagent	pass

B:Customer Records
-----------------------
To add, edit, or remove customers click on the "Customers" button once you're logged in. A list of customers from the database will be displayed in a table view. To add a customer click "Add Customer" in the lower left. To edit a customer, select one of the existing customers in the table view and click "Edit Customer" in the lower middle. To delete a customer, select one of the existing customers and click "Delete Customer". The user will be warned that all of that customer's appointments will also be deleted and to confirm. Editing an existing customer or adding a customer will take the user to another screen where the customer's information can be added or changed. There is a combo box with a list of every country in the world and once a country is selected a city combo box will automatically show a list of cities available to choose for the selected country. Click on the button at the bottom of the menu to add or update your customer. Error messages will be shown if required fields are not filled in.

C:Appointments
-----------------------
To add, edit, or remove appointments click on the "Appointments" button once you're logged in. A list of existing appointments will be displayed in a table view. To add an appointment click "Add Appointment". To edit an appointment click on the existing appointment in the table view and click "Edit Appointment". The add/edit appointment screen has a combo box with a list of existing customers, a combo box for the valid appointment types you can choose, a date picker to choose the date, and combo boxes for the start time and end time for the appointment. Once a date is chosen by the user, the system fills in valid start and end times that do not conflict with existing appointments. The system also understands that the end time must be at least 15 minutes after the start time chosen and that the appointment cannot overlap with another existing appointment. Therefore only valid times are presented to the user and it is impossible for the user to select a time and date that is invlalid or conflicts with other appointments.

To link to the customer associated with an appointment, click on the appointment in the table view and click on the "Edit Customer" button. This takes the user to the edit customer screen for the customer that belongs to that appointment. The user can then see every detail about that customer.

To delete an appointment, click on one of the appointments in the table view and click "Delete Appointment" and the system will show a dialog to confirm the deletion. If the user clicks "OK" the appointment will be deleted otherwise if the user selects "Cancel" or the "X" in the upper right nothing happens and the dialog closes.

D:Calendar Views
-----------------------
To see the calendar views click on the "Calendar" button after logging in. By default the calendar will display a list of appointments for the current month of the current year. The user can select a different year and a different month using the combo boxes above the list view to see the appointments for that month.

To see appointments by week, choose the "By Week" radio button which clears the table view and adds a new combo box so the user can select the week. Each week shows the date range of that week which starts either on Monday or the first day of the month and ends on the following Sunday or the last day of the month. Once a week is chosen the table view will display any appointments made for that week. If there are no appointments the table view will display a "No appointments in selected time period." message. Changing the year and/or month will clear the week combo box and the user must once again choose a week. Switch back to month view by choosing the "By Month" radio button.

E:Time Zones
-----------------------
The program automatically converts and saves all local times chosen by the user in the database as UTC time. It also retrieves all UTC time from the database and converts it into local time. It takes daylights savings time into account automatically using Java 8's built in java.time LocalDate, LocalTime, LocaldateTime, and ZonedDateTime classes.

F:Exception Control
----------------------
The program only presents valid appointment times to the user therefore it's impossible for the user to select a time outside business hours, overlapping existing appointments, or end times that are equal to or less than start times. The user must use a combo box with the existing valid users therefore it's impossible for the user to enter an invalid customer for an appointment. Entering an incorrect username or password will result in the program displaying an error message in either English or Traditional Chinese (Taiwan) in red at the bottom of the window depending on which default locale is set for the user's JVM.

G:Lambda Expressions
----------------------
The program makes use of numerous lambda functions that make for easier to write and more succienct code. Lambda expressions are used for event listeners, forEach() expressions are used for loops, stream().anyMatch() is used to find objects in arraylists, setAll() is used to set up arraylists, and removeIf() is used to remove invalid times from combo boxes. All lambda functions are commented to justify their use.

H:Alerts
----------------------
When the user logs in the program if there is an appointment within the next 15 minutes it will display a dialog box informing the user that there is an appointment in the next 15 minutes.

I:Reports
----------------------
To see reports, log in and click on the "Reports" button and choose from one of the three options. Appointment Types by Month displays a count of the different appointment types scheduled by month and year. Consultant Schedules shows a list of appointments and the appointment details per consultant (user) in the system. Consultant Listing shows a list of all the consultants (users) in the system and the time and date they were created.

J:Activity Log
----------------------
Activity by the user, especially when the user adds, edits, or deletes any entries are recorded in a log file called log.txt which is contained in the main directory for the program. New entries are appended to the bottom of the text file and contain the username of the logged in user making the change, a description of the change being made, and a timestamp with the user's localdatetime. To open the log file, log in to the program and click on "Open Log File" in the upper right.

K. Professional Communication
----------------------
This documentation uses correct grammar and professional language to convey the details necessary to understand how to operate the program, how it works, and how it fulfills the requirements set for the project.



