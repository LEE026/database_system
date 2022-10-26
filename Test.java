import java.sql.*;
public class Test
{
    public static void main(String args[])
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://192.168.56.101:4567/madang",
                    "hslee","1234");
            Statement stmt=con.createStatement();

            stmt.executeUpdate("insert into Book values (100,'test','test',3215)");
            ResultSet rs=stmt.executeQuery("SELECT * FROM Book");
            while(rs.next())
                System.out.println(rs.getInt(1)+" "+rs.getString(2)+
                        " "+rs.getString(3));
            System.out.println("===============================");

            stmt.executeUpdate("delete from Book where bookid = 100");

            rs=stmt.executeQuery("SELECT * FROM Book");
            while(rs.next())
                System.out.println(rs.getInt(1)+" "+rs.getString(2)+
                        " "+rs.getString(3));
            con.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}