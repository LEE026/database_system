import java.sql.*;
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
        init("hslee","1234");//테스트 편의를 위한 문장, 제작종료후 삭제 예정
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

                    String user=inputString(input,"유저 이름: ");
                    String pw=inputString(input, "비밀 번호: ");

                    if(!user.isEmpty()&&!pw.isEmpty()) {
                        init(user,pw);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }


                case 2: {
                    String confirmation_id=inputString(input,"확진자 식별 번호: ");
                    if(!confirmation_id.isEmpty()) {
                        findMovement(confirmation_id);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }

                case 3:
                    break;

                case 4: {
                    String region=inputString(input,"확인을 원하는 지역: ");
                    if(!region.isEmpty()) {
                        confirmationRegion(region);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }

                case 5: {
                    System.out.println("추가할 업체 정보");
                    String name=inputString(input,"업체 이름: ");
                    String address=inputString(input,"업체 주소: ");
                    String longitude=inputString(input,"경도: ");
                    String latitude=inputString(input,"위도: ");
                    if(!name.isEmpty()&&!address.isEmpty()) {
                        insertEnterprise(name,address,longitude,latitude);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }

                case 6:{
                    System.out.println("추가할 동선 정보");
                    String confirmation_id=inputString(input,"확진자 식별번호: ");
                    String name=inputString(input,"업체 이름: ");
                    String address=inputString(input,"업체 주소: ");
                    String date=inputString(input,"방문 일자: ");
                    if(!confirmation_id.isEmpty()&&!name.isEmpty()&&!address.isEmpty()&&!date.isEmpty()) {
                        insertMovement(confirmation_id,name,address,date);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }

                case 7:
                    break;

                case 8:
                    break;
                case 9: {
                    showNotQuarantinedEnterprise();
                    break;
                }
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


    public static void printOneResult(ResultSet rs,ResultSetMetaData column, int cnt) {
        for(int i=1;i<=cnt;i++){
            try {
                System.out.printf("%s: %s\n",column.getColumnLabel(i),rs.getString(i));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }

    public static void printResult(ResultSet rs){
        try {
            ResultSetMetaData column=rs.getMetaData();
            int cnt= column.getColumnCount();

            int num=1;
            while (rs.next()) {
                System.out.printf("#%d\n",num++);
                printOneResult(rs,column,cnt);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Query print fail");
        }
    }

    //확진자의 동선
    public static void findMovement(String confirmation_id){
        String Query="select "+
                "name as \"업체\", address as \"업체 주소\", dateOfVisit as \"방문 일자\", confirmation_disease_code as \"확진된 질병\" \n" +
                "from Movement natural join Confirmed_case \n" +
                "where confirmation_id = "+confirmation_id+" \n" +
                "order by dateOfVisit;\n";
        try {
            ResultSet rs=stmt.executeQuery(Query);
            System.out.println("\n확진자 "+confirmation_id+"의 동선\n");
            printResult(rs);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Query fail");
        }
    }

    //특정 업체의 기록
    public static void confirmationEnterprise(String enterpriseName, String address){
        String Query1="select name as \"업체\", address as \"업체 주소\", date as \"방역 일자\",  disinfection_company as \"방역 업체\"\n" +
                "from Enterprise natural join Quarantine_information \n" +
                "where name like \"%김포공항%\" and address like \"%방화동%\"\n" +
                "order by date;";
        String Query2="select name as \"업체\", address as \"업체 주소\", dateOfVisit as \"방문 일자\", confirmation_disease_code as \"확진된 질병\" \n" +
                "from Movement natural join Confirmed_case \n" +
                "where name like \"%김포공항%\" and address like \"%방화동%\"\n" +
                "order by dateOfVisit;";
        try {
            ResultSet rs1=stmt.executeQuery(Query1);
            ResultSet rs2=stmt.executeQuery(Query2);

            rs1.close();
            rs2.close();
        } catch (SQLException e) {
            System.out.println("Query fail");
        }
    }

    //지역에서 발생중인 질병 리스트
    public static void confirmationRegion(String region){
        String Query="select confirmation_disease_code as \"질병\" \n" +
                "from Movement natural join Confirmed_case \n" +
                "where address like \"%"+region+"%\";";
        try {
            ResultSet rs=stmt.executeQuery(Query);
            System.out.println(region+"에서 발생중인 질병 목록");
            printResult(rs);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Query fail");
        }
    }

    //업체추가
    public static void insertEnterprise(String name,String address,String longitude, String latitude){
        String Query="INSERT INTO Enterprise VALUES ('"+name+"',  '"+address+"', "+longitude+","+latitude+");";
        try {
            stmt.executeUpdate(Query);
        } catch (SQLException e) {
            System.out.println("insert fail");
        }
    }

    // 동선추가
    public static void insertMovement(String confirmation_id ,String name,String address,String dateOfVisit){
        String Query="INSERT INTO Movement VALUES ('"+confirmation_id+"', '"+name+"', '"+address+"', '"+dateOfVisit+"');";
        try {
            stmt.executeUpdate(Query);
        } catch (SQLException e) {
            System.out.println("insert fail");
        }
    }
    //방역정보 추가
    public static void insertQuarantineInformation(){

    }

    //확진자 추가
    public static void insertConfirmedCase(){

    }

    //방역되지 않은 업체
    public static void showNotQuarantinedEnterprise(){
        String Query="select name as \"업체\", address as \"업체 주소\"\n" +
                "from Movement m natural join Quarantine_information q\n" +
                "where m.dateOfVisit > q.date\n" +
                "order by dateOfVisit;";
        try {
            ResultSet rs=stmt.executeQuery(Query);
            System.out.println("위험 업체 목록");
            printResult(rs);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Query fail");
        }
    }

    //위험 업체와의 거리
    public static void CheckDistanceToMovement(){

    }
}
