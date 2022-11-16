import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public void main(String args[])
    {
        if(!init()) return;

        Scanner menu = new Scanner(System.in);
        Scanner id_string = new Scanner(System.in);
        int number = 0;

        while(true)
        {
            printMenu();

            System.out.print("Enter an integer: ");

            if(menu.hasNextInt()){
                number = menu.nextInt();
            }else {
                System.out.println("concentration!");
                break;
            }


            String instance_id = "";

            switch(number) {
                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    break;

                case 4:
                    break;

                case 5:
                    break;

                case 6:
                    break;

                case 7:
                    break;

                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;

                case 99:
                    System.out.println("bye!");
                    menu.close();
                    id_string.close();
                    return;
                default: System.out.println("concentration!");
            }

        }

    }


    public boolean init(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://192.168.56.101:4567/project",
                    "hslee","1234");
            Statement stmt=con.createStatement();
        }
        catch(Exception e){
            System.out.println("fail to connect database");
            return false;
        }
        return true;
    }

    public void printMenu(){
        System.out.println("                                                                               ");
        System.out.println("                                                                               ");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("                              동아리 인원 관리 프로그램                            ");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("  1. connection                         2.  find movement                      ");
        System.out.println("  3. confirmation enterprise            4.  confirmation region                ");
        System.out.println("  5. insert enterprise                  6.  insert movement                    ");
        System.out.println("  7. insert Quarantine information      8.  comparison with movement           ");
        System.out.println("  9. find not quarantined enterprise    10. Check distance to movement         ");
        System.out.println("                                        99. quit                               ");
        System.out.println("-------------------------------------------------------------------------------");
    }

    public void findMovement(){

    }

    public void confirmationEnterprise(){

    }

    public void insertEnterprise(){

    }

    public void insertMovement(){

    }
    public void insertQuarantineInformation(){

    }
    public void ComparisonWithMovement(){

    }
    public void findNotQuarantinedEnterprise(){

    }
    public void CheckDistanceToMovement(){

    }
}
