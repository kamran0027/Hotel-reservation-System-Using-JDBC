import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    final static String user="root";
    final static String url="jdbc:mysql://localhost:3306/hotel_reservation";
    final static String password="Kamran098@";
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("driver loaded.....");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection= DriverManager.getConnection(url,user,password);
            System.out.println("connection establish.......");
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}