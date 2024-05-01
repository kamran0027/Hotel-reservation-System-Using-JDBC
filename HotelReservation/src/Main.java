import java.sql.*;
import java.util.Scanner;
import java.util.Scanner;


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
            System.out.println("current reservations:");

            while (true){
                System.out.println();
                Scanner scanner=new Scanner(System.in);
                System.out.println("press 1 for reservaion");
                System.out.println("press 2 for view reservation");
                System.out.println("press 3 for get room number");
                System.out.println("press  for delete resrvation");
                System.out.println("press 0 for exiting");
                System.out.println("enter your choice: ");

                int key=scanner.nextInt();
                boolean flag=true;
                switch (key){
                    case 1:
                        reserveRoom(connection,scanner);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,scanner);
                        break;
                    case 4:
                        deletereservayion(connection,scanner);
                        break;
                    case 0:
                        flag=false;
                        break;


                    default:
                        System.out.println("invalid choice");
                }
                if (!flag){
                    System.out.println("exiting..........");
                    connection.close();
                    break;
                }

            }

        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    private  static void viewReservation(Connection connection){
        String query="select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations";

        try {
            Statement stmt=connection.createStatement();
            ResultSet set=stmt.executeQuery(query);
            System.out.println("+----------------+------------------+---------------+------------------+--------------------+");
            System.out.println("| Reservation Id | Guest            |  Room Number  |  contact Number  | Reservation Date   |");
            System.out.println("+----------------+------------------+---------------+------------------+--------------------+");
            while (set.next()){
                int reservationId=set.getInt("reservation_id");
                String guestName=set.getString("guest_name");
                int roomNumber=set.getInt("room_number");
                String contactNumber=set.getString("contact_number");
                String reservationDate= set.getTimestamp("reservation_date").toString();
                //formate to display the reservation data in atable like formate

                System.out.printf("| %-14d | %-15s | %-13d | %-20s |  %-19s\n",
                        reservationId,guestName,roomNumber,contactNumber,reservationDate);
            }
            System.out.println("+----------------+------------------+---------------+------------------+--------------------+");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void reserveRoom(Connection connection,Scanner scanner){
        System.out.println("enter the guest name:");
        String guestName=scanner.next();
        scanner.nextLine();
        System.out.println("enter room no:");

        int roomNumber=scanner.nextInt();
        System.out.println("enter contact no:");

        String contactNumber=scanner.next();

        String sql="insert into reservations (guest_name,room_number,contact_number)"+
                "values ('" + guestName +"'," + roomNumber + ",'" + contactNumber + "')";

        try {
            Statement stmt=connection.createStatement();

            int effectRows=stmt.executeUpdate(sql);

            if (effectRows > 0) {

                System.out.println("reservation succsesfull...");
            }
            else {
                System.out.println("resrvation failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getRoomNumber(Connection connection,Scanner scanner){

        System.out.println("enter reservation id:");
        int reservationId=scanner.nextInt();
        System.out.println("enter guest name");
        String guestname=scanner.next();

        //prepared statement query
        String query="select room_number from reservations where reservation_id=? AND guest_name =?";

        try {
            //preparesd statement
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,reservationId);
            preparedStatement.setString(2,guestname);
            ResultSet resultSet=preparedStatement.executeQuery();

            if (resultSet.next()){
                int roomnumber=resultSet.getInt("room_number");
                System.out.println("Room no for reservation id: "+reservationId+" and guest name:"+guestname+" is: "+roomnumber);
            }
            else {
                System.out.println("reservation not found for the given guest name and id:");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deletereservayion(Connection connection,Scanner scanner){
        System.out.println("enter reservation id:");
        int reservationId= scanner.nextInt();

        if(!resrvationExist(connection,reservationId)){
            System.out.println("resrvation not found at given id...");
            return;
        }
        String sql="delete from reservations where reservation_id=?";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,reservationId);
            int affect=preparedStatement.executeUpdate();

            if(affect>0){
                System.out.println("reservation delete");
            }
            else {
                System.out.println("deletion fail");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static  boolean resrvationExist(Connection connection,int reservationid){
        String query="select reservation_id from reservations where reservation_id=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,reservationid);
            ResultSet resultSet=preparedStatement.executeQuery();
            return resultSet.next();//if there is a result, then reservation exixt
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
}