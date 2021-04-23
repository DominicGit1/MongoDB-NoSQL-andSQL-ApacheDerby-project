import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.bson.DATE;
import java.lang.Integer;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase; 
import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential;
import com.mongodb.ConnectionString; 
import com.mongodb.*;
public class App {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:derby:c:\\Users\\INSERTUSER\\Desktop\\Derby\\db-derby-10.14.2.0-bin\\hr;create=true");
        System.out.println("Connected to Derby Database.");
        //MongoClientURI uri = new MongoClientURI(
        //"mongodb+srv://no spamming/schoolNOSQLversion?retryWrites=true&w=majority");
        //MongoClient mongoClient = new MongoClient(uri); //THIS MESSES UP DONT KNOW WHY EXACTLY AS MONGO SAYS to do
        //MongoDatabase database = mongoClient.getDatabase("test");
        ArrayList<Statement> statements = new ArrayList<Statement>();
        Statement why = conn.createStatement();
        //mongo.close();
        statements.add(why);
        why.execute("create table therapists(therapist_id int primary key, First_name varchar(30), Last_name varchar(30), Street varchar(30), City varchar(30), State varchar(30), Zip varchar(30))");
        why.execute("create table patients(patient_number int primary key, First_name varchar(30), Last_name varchar(30), Street varchar(30), City varchar(30), State varchar(30), Zip varchar(30), Balance int)");
        why.execute("create table therapies(therapist_code int primary key, description varchar(30), billable_unit varchar(30))");
        why.execute("create table sessions(therapist_id int constraint therapist_id REFERENCES therapists, patient_number int constraint patient_number REFERENCES patients, Session_date DATE, Session_length varchar(30), Session_number int)");
        System.out.println("Created tables");
        why.execute("CREATE TRIGGER trig1 AFTER INSERT ON patients REFERENCING NEW AS updatedrow FOR EACH ROW MODE DB2SQL INSERT INTO therapies VALUES(updatedrow.patient_number,'What happened.','500$')");
        System.out.println("Created trigger that does the therapies table after a new row is inserted into patients.");
        //The trigger command. the referencing new is referencing the new data that was inserted into the patients table. inserting doesn't allow
        //you to reference old. Since the therapies table basically didn't have any different values besides the therapist code, it was easy
        //to write the trigger command to take the patient_number and put that in the therapist_code, and just insert the generic
        //2 hours long session and 500$ for the bill.
        //Note: Only row triggers (see Statement versus row triggers) can use the transition variables. INSERT row triggers cannot reference an OLD row. DELETE row triggers cannot reference a NEW row.
        //https://docs.oracle.com/javadb/10.8.3.0/ref/rrefsqlj43125.html#rrefsqlj43125__sqlj54276
        //several notes about triggers and when and where you can and cannot use them
        why.execute("insert into therapists values(1,'John','Smith','Middle of nowhere 12345','Dustville','Oklahoma','12345')");
        why.execute("insert into therapists values(2,'Abe','Newell','Middle of nowhere 12345','Dustville','Oklahoma','12345')");
        why.execute("insert into therapists values(3,'Erik','Fielder','Middle of nowhere 12345','Dustville','Oklahoma','12345')");
        why.execute("insert into patients values(1,'Sociopath','Evil','Middle of nowhere 12345','Dustville','Oklahoma','12345', 1000)");
        why.execute("insert into patients values(2,'Mental','Issues','Middle of nowhere 12345','Dustville','Oklahoma','12345', 1500)");
        why.execute("insert into patients values(3,'Seek','Help','Middle of nowhere 12345','Dustville','Oklahoma','12345', 500)");
        //why.execute("insert into therapies values(1,'What happened.','500$')");
        //why.execute("insert into therapies values(2,'What happened.','500$')"); //commented these out due to the trigger
        //why.execute("insert into therapies values(3,'What happened.','500$')");
        why.execute("insert into sessions values(1,1,CURRENT_DATE,'2 hours',1)");
        why.execute("insert into sessions values(2,2,CURRENT_DATE,'2 hours',2)");
        why.execute("insert into sessions values(3,3,CURRENT_DATE,'2 hours',3)");
        System.out.println("Inserted data into tables");
        String query = "SELECT * FROM therapists";
        ResultSet rs = why.executeQuery(query);
        while(rs.next())
        {
            System.out.print("therapist_id: "+rs.getInt("therapist_id"));
            System.out.print(" First_name: "+rs.getString("First_name"));
            System.out.print(" Last_name: "+rs.getString("Last_name"));
            System.out.print(" Street: "+rs.getString("Street"));
            System.out.print(" City: "+rs.getString("City"));
            System.out.print(" State: "+rs.getString("State"));
            System.out.print(" Zip: "+rs.getString("Zip"));
            System.out.println("");
        }
        query = "SELECT * FROM patients";
        rs = why.executeQuery(query);
        while(rs.next())
        {
            System.out.print("patient_number: "+rs.getInt("patient_number"));
            System.out.print(" First_name: "+rs.getString("First_name"));
            System.out.print(" Last_name: "+rs.getString("Last_name"));
            System.out.print(" Street: "+rs.getString("Street"));
            System.out.print(" City: "+rs.getString("City"));
            System.out.print(" State: "+rs.getString("State"));
            System.out.print(" Zip: "+rs.getString("Zip"));
            System.out.print(" Balance: "+rs.getInt("Balance"));
            System.out.println("");
        }
        query = "SELECT * FROM therapies";
        rs = why.executeQuery(query);
        while(rs.next())
        {
            System.out.print("therapist_code: "+rs.getInt("therapist_code"));
            System.out.print(" description: "+rs.getString("description"));
            System.out.print(" billable_unit: "+rs.getString("billable_unit"));
            System.out.println("");
        }
        query = "SELECT * FROM sessions";
        rs = why.executeQuery(query);
        while(rs.next())
        {
            System.out.print("therapist_id: "+rs.getInt("therapist_id"));
            System.out.print(" patient_number: "+rs.getInt("patient_number"));
            System.out.print(" Session_date: "+rs.getDate("Session_date"));
            System.out.print(" Session_length: "+rs.getString("Session_length"));
            System.out.print(" Session_number: "+rs.getInt("Session_number"));
            System.out.println("");
        }
        System.out.println("Printed out complete tables in the SQL Derby database.");
        query = "SELECT * FROM sessions WHERE therapist_id = 1";
        rs = why.executeQuery(query);
        while(rs.next())
        {
            System.out.print("therapist_id: "+rs.getInt("therapist_id"));
            System.out.print(" patient_number: "+rs.getInt("patient_number"));
            System.out.print(" Session_date: "+rs.getDate("Session_date"));
            System.out.print(" Session_length: "+rs.getString("Session_length"));
            System.out.print(" Session_number: "+rs.getInt("Session_number"));
            System.out.println("");
        }
        System.out.println("Printed out rows from the sessions table where therapist_id equals 1.");
        query = "SELECT * FROM therapists WHERE Last_name LIKE '%e%'"; //LIKE is the operator to search for things that contain this specified string or character
        rs = why.executeQuery(query);//if you did '%e' it would search last names that have a e on the end
        while(rs.next())//likewise, if you did 'e%' it would search last names that have a e on the end
        {// note, e is a different value than E, won't work because names are capitalized.
            System.out.print("therapist_id: "+rs.getInt("therapist_id"));
            System.out.print(" First_name: "+rs.getString("First_name"));
            System.out.print(" Last_name: "+rs.getString("Last_name"));
            System.out.print(" Street: "+rs.getString("Street"));
            System.out.print(" City: "+rs.getString("City"));
            System.out.print(" State: "+rs.getString("State"));
            System.out.print(" Zip: "+rs.getString("Zip"));
            System.out.println("");
        }
        System.out.println("Printed out rows from the therapists table where therapist Last_name has a e in it.");
        query = "SELECT * FROM patients WHERE patient_number IN (1,3)";
        rs = why.executeQuery(query);
        while(rs.next())
        {
            System.out.print("patient_number: "+rs.getInt("patient_number"));
            System.out.print(" First_name: "+rs.getString("First_name"));
            System.out.print(" Last_name: "+rs.getString("Last_name"));
            System.out.print(" Street: "+rs.getString("Street"));
            System.out.print(" City: "+rs.getString("City"));
            System.out.print(" State: "+rs.getString("State"));
            System.out.print(" Zip: "+rs.getString("Zip"));
            System.out.print(" Balance: "+rs.getInt("Balance"));
            System.out.println("");
        }
        System.out.println("Printed out rows from the patients table that had the patient_number of 1 or 3.");
        query = "SELECT * FROM patients WHERE Balance BETWEEN 1000 AND 1500";
        rs = why.executeQuery(query);
        while(rs.next())
        {
            System.out.print("patient_number: "+rs.getInt("patient_number"));
            System.out.print(" First_name: "+rs.getString("First_name"));
            System.out.print(" Last_name: "+rs.getString("Last_name"));
            System.out.print(" Street: "+rs.getString("Street"));
            System.out.print(" City: "+rs.getString("City"));
            System.out.print(" State: "+rs.getString("State"));
            System.out.print(" Zip: "+rs.getString("Zip"));
            System.out.print(" Balance: "+rs.getInt("Balance"));
            System.out.println("");
        }
        System.out.println("Printed out rows from the patients table that had a Balance between 1000 and 1500.");
        query = "SELECT * FROM patients ORDER BY Balance"; //If you want descending, you just add DESC after Balance
        rs = why.executeQuery(query);
        while(rs.next())
        {
            System.out.print("patient_number: "+rs.getInt("patient_number"));
            System.out.print(" First_name: "+rs.getString("First_name"));
            System.out.print(" Last_name: "+rs.getString("Last_name"));
            System.out.print(" Street: "+rs.getString("Street"));
            System.out.print(" City: "+rs.getString("City"));
            System.out.print(" State: "+rs.getString("State"));
            System.out.print(" Zip: "+rs.getString("Zip"));
            System.out.print(" Balance: "+rs.getInt("Balance"));
            System.out.println("");
        }
        System.out.println("Printed out rows from the patients table with their Balance in ascending order.");
        rs.close();
        why.execute("drop table sessions");
        why.execute("drop table therapists");
        why.execute("drop table patients");
        why.execute("drop table therapies");
        System.out.println("Dropped tables");
        conn.close();
    }
}
