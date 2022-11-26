import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void printMenu(){
        System.out.println("                                                                               ");
        System.out.println("                                                                               ");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("                              동아리 인원 관리 프로그램                            ");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("  1. connection                         2.  find movement                      ");
        System.out.println("  3. confirmation enterprise            4.  confirmation region                ");
        System.out.println("  5. insert enterprise                  6.  insert movement                    ");
        System.out.println("  7. insert Quarantine information      8.  insert confirmed case              ");
        System.out.println("  9. show not quarantined enterprise    10. Check distance to movement         ");
        System.out.println("                                        99. quit                               ");
        System.out.println("-------------------------------------------------------------------------------");
    }



    static Statement stmt;
    static String mysqlAddress="192.168.56.101:4567";
    static String databaseName="disease_notice_service";

    public static void main(String args[])
    {

        Scanner menu = new Scanner(System.in);
        Scanner input = new Scanner(System.in);
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




            switch(number) {
                case 1: {

                    String user=inputString(input,"enter user name: ");
                    String pw=inputString(input, "enter user name: ");

                    if(!user.isEmpty()&&!pw.isEmpty()) {
                        init(user,pw);
                    }else{
                        System.out.println("concentration!");
                    }
                }
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
                    input.close();
                    return;
                default: System.out.println("concentration!");
            }

        }

    }

    public static String inputString(Scanner scanner, String description){
        System.out.print(description);
        String input="";
        if(scanner.hasNextLine())
            input=scanner.nextLine();
        return input;
    }


    public static boolean init(String user, String pw){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://"+mysqlAddress+"/"+databaseName+"",
                    user,pw);
            stmt=con.createStatement();
            System.out.println("success to connect database");
        }
        catch(Exception e){
            System.out.println("fail to connect database");
            return false;
        }
        return true;
    }



    //확진자의 동선
    public static void findMovement(String confirmation_id){

    }

    //특정 업체의 기록
    public static void confirmationEnterprise(){

    }

    //업체추가
    public static void insertEnterprise(){

    }

    //업체추가
    public static void insertRegion(){

    }

    // 동선추가
    public static void insertMovement(){

    }
    //방역정보 추가
    public static void insertQuarantineInformation(){

    }

    //확진자 추가
    public static void insertConfirmedCase(){

    }

    //방역되지 않은 업체
    public static void showNotQuarantinedEnterprise(){

    }

    //위험 업체와의 거리
    public static void CheckDistanceToMovement(){

    }
}