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
    static Statement stmt2;
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

                case 3:{
                    System.out.println("확진자 방문 및 방역조치 기록을 보고 싶은 업체");
                    String name=inputString(input,"업체 이름: ");
                    String address=inputString(input,"업체 주소: ");
                    if(!name.isEmpty()&&!address.isEmpty()) {
                        confirmationEnterprise(name,address);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }

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

                case 7:{
                    System.out.println("추가할 방역 정보");
                    String name=inputString(input,"업체 이름: ");
                    String address=inputString(input,"업체 주소: ");
                    String date=inputString(input,"방역 일자: ");
                    String disinfection_company=inputString(input,"방역 업체: ");
                    if(!name.isEmpty()&&!address.isEmpty()&&!date.isEmpty()) {
                        insertQuarantineInformation(name,address,date,disinfection_company);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }

                case 8:{
                    System.out.println("추가할 확진자 정보");
                    String id=inputString(input,"확진자 식별번호: ");
                    String disease_code=inputString(input,"질병 코드: ");
                    String date=inputString(input,"확진 일자: ");

                    if(!id.isEmpty()) {
                        insertConfirmedCase(id,disease_code,date);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }
                case 9: {
                    showNotQuarantinedEnterprise();
                    break;
                }
                case 10:{
                    System.out.println("현재 gps위치를 입력해주세요");
                    Double longitude=-1.0;
                    Double latitude=-1.0;
                    System.out.print("경도: ");
                    if(input.hasNextDouble())
                        longitude=input.nextDouble();
                    System.out.print("위도: ");
                    if(input.hasNextDouble())
                        latitude=input.nextDouble();

                    if(longitude>0.0&&latitude>0.0) {
                            CheckDistanceToMovement(longitude,latitude);
                    }else{
                        System.out.println("concentration!");
                    }
                    break;
                }

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
            stmt2= con.createStatement();
            System.out.println("success to connect database");
        }
        catch(Exception e){
            System.out.println("fail to connect database");
            return false;
        }
        return true;
    }


    public static void printOneResult(ResultSet rs,ResultSetMetaData column) {
        int cnt= 0;
        try {
            cnt = column.getColumnCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

            int num=1;
            while (rs.next()) {
                System.out.printf("#%d\n",num++);
                printOneResult(rs,column);
            }

        } catch (Exception e) {
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
        String Query1="select name as \"업체\", address as \"업체 주소\", dateOfVisit as \"방문 일자\", confirmation_disease_code as \"확진된 질병\" \n" +
                "from Movement natural join Confirmed_case \n" +
                "where name like \""+enterpriseName+"\" and address like \""+address+"\"\n" +
                "order by dateOfVisit;";

        String Query2="select name as \"업체\", address as \"업체 주소\", date as \"방역 일자\",  disinfection_company as \"방역 업체\"\n" +
                "from Enterprise natural join Quarantine_information \n" +
                "where name like \""+enterpriseName+"\" and address like \""+address+"\"\n" +
                "order by date;";

        try {
            ResultSet rs1=stmt.executeQuery(Query1);
            ResultSet rs2=stmt2.executeQuery(Query2);
            ResultSetMetaData rsMeta1 = null;
            ResultSetMetaData rsMeta2 = null;
            boolean next1=rs1.next();
            boolean next2=rs2.next();

            System.out.printf("\n%s %s의 확진자 방문 및 방역 조치 기록\n",address,enterpriseName);
            if(next1)
                rsMeta1= rs1.getMetaData();
            if (next2)
                rsMeta2= rs2.getMetaData();;
            int cnt=1;
            while(next1 && next2){
                System.out.println("#"+cnt);
                if(rs1.getDate(3).compareTo(rs2.getDate(3))<0){
                    System.out.println("확진자 방문");
                    printOneResult(rs1,rsMeta1);
                    next1=rs1.next();
                }
                else {
                    System.out.println("방역조치");
                    printOneResult(rs2,rsMeta2);
                    next2=rs2.next();
                }
                cnt++;
            }

            while(next1){
                System.out.println("확진자 방문");
                System.out.println("#"+cnt);
                printOneResult(rs1,rsMeta1);
                next1=rs1.next();
                cnt++;
            }
            while(next2){
                System.out.println("방역조치");
                System.out.println("#"+cnt);
                printOneResult(rs2,rsMeta2);
                next2=rs2.next();
                cnt++;
            }

            rs1.close();
            rs2.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
    public static void insertQuarantineInformation(String name,String address,String date, String disinfection_company){
        String Query="INSERT INTO Quarantine_information VALUES ('"+name+"', '"+address+"','"+date+"', '"+disinfection_company+"');";

        try {
            stmt.executeUpdate(Query);
        } catch (SQLException e) {
            System.out.println("insert fail");
        }
    }

    //확진자 추가
    public static void insertConfirmedCase(String id,String disease_code, String date){
        String Query="INSERT INTO Confirmed_case VALUES ('"+id+"', '"+disease_code+"','"+date+"');";

        try {
            stmt.executeUpdate(Query);
        } catch (SQLException e) {
            System.out.println("insert fail");
        }
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

    private static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    private static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;

        return dist; //단위 meter
    }
    //500m내 위험 업체와의 거리
    public static void CheckDistanceToMovement(Double longitude,Double latitude){
        String Query="select name as \"업체\", address as \"업체 주소\", longitude, latitude\n" +
                "from (select  * from Enterprise e \n" +
                "where abs(e.longitude-"+longitude+")<=0.005 \n" +
                "and abs(e.latitude-"+latitude+")<=0.005) e \n" +
                "natural join (select name, address\n" +
                "from Movement m natural join Quarantine_information q\n" +
                "where m.dateOfVisit > q.date\n" +
                "order by dateOfVisit) c;\n";

        try {
            ResultSet rs=stmt.executeQuery(Query);
            System.out.println("\n500 미터 내 위험업체 \n");
            ResultSetMetaData column=rs.getMetaData();
            int cnt=column.getColumnCount();
            while(rs.next()){
                int num=1;
                double dist=distance(longitude,latitude,rs.getDouble(3),rs.getDouble(4));
                if(dist<=500.0) {
                    System.out.printf("#%d\n", num++);
                    System.out.printf("%s: %s\n", column.getColumnLabel(1),rs.getString(1));
                    System.out.printf("%s: %s\n", column.getColumnLabel(2),rs.getString(2));
                    System.out.printf("위험 업체와의 거리: %.2fm\n\n", dist);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Query fail");
        }

    }
}


