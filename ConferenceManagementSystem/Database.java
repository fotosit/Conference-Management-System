package com.fotosit;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Database {

    private  static Database instance;

    private static final String DB_URL = "jdbc:mysql://localhost/ConferenceManagementSystemDB";
    private static final String USER = "root";
    private static final String PASS = "Password1234*";

    private Connection conn;
    private Statement stmt;


    private Database()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        } catch (SQLException throwables) {
            throwables.printStackTrace ();
        }
    }

    public Connection getConnection()
    {
        return conn;
    }

    //Singleton Pattern Design
    public static Database getInstance()
    {
        if (instance == null) {
            instance = new Database();
        } else
            {
            try {
                if (instance.getConnection().isClosed()) {
                    instance = new Database();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace ();
            }
        }

        return instance;
    }


    public void closeConnection()
    {
        try
        {
            if ((conn != null) || (stmt != null))
                conn.close();
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
    }


    /*--------------------------------------------------AUTHOR METHODS------------------------------------------------*/


    public boolean addPaper(String authorNames, String authorMail, String authorUniversityName, String paperName, String paperAbstract, String keywords, String paperTopics, FileReader paper, int submitPerson)
    {
        String submitDate = new SimpleDateFormat ("dd.MM.yyyy").format(new Date());
        boolean isEvaluationComplete=false;

        try {
            String sql="INSERT INTO Papers (authorName,authorMail,authorUniversityName,paperName,paperAbstract,keywords,paperTopics,paper,submitPerson,submitDate,isEvaluationComplete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preStmt = conn.prepareStatement (sql);
            preStmt.setString ( 1,authorNames );
            preStmt.setString ( 2,authorMail );
            preStmt.setString ( 3,authorUniversityName );
            preStmt.setString ( 4,paperName );
            preStmt.setString ( 5,paperAbstract );
            preStmt.setString ( 6,keywords );
            preStmt.setString ( 7,paperTopics );
            preStmt.setClob ( 8,paper );
            preStmt.setInt ( 9,submitPerson );
            preStmt.setString ( 10,submitDate );
            preStmt.setBoolean ( 11,false );
            preStmt.executeUpdate();

            return true;
        }
        catch (Exception se)
        {
            se.printStackTrace();
            return false;
        }
    }

    public ArrayList<Integer> getApprovalPaperId(int authorId)
    {
        ArrayList<Integer> paperIdArray = new ArrayList<>();

        try{
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Papers WHERE submitPerson=" + authorId + " AND publishDate IS NOT NULL";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                paperIdArray.add(rs.getInt ( "paperId" ));
            }
        }
        catch(Exception se)
        {
            se.printStackTrace();
        }
        return paperIdArray;
    }

    public ArrayList<Evaluation> getEvaluationByPaperId(int paperId)
    {
        ArrayList<Evaluation> evaluationList = new ArrayList<Evaluation>();

        try{
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Evaluation WHERE paperId=" + paperId;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                Evaluation eva = new Evaluation ( rs.getInt ( "evaluationId" ),rs.getInt ( "paperId" ),rs.getString ( "paperName" ),rs.getString ( "paperAuthor" ),rs.getInt ( "refereeId" ),rs.getString ( "refereeName" ),rs.getString ( "refereeComment" ),rs.getInt ( "refereeScore" ),rs.getString ( "evaluationDate" ) );
                evaluationList.add(eva);
            }
        } catch(Exception se)
        {
            se.printStackTrace();
        }

        return evaluationList;
    }


    public ArrayList<Paper> getPaperByUserId(int userId)
    {
        ArrayList<Paper> papers = new ArrayList<Paper>();

        try{
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Papers WHERE submitPerson=" + userId;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                Paper getPaper = new Paper ( rs.getInt ( "paperId" ),rs.getString ( "authorName" ),rs.getString ( "authorMail" ),rs.getString ( "authorUniversityName" ),rs.getString ( "paperName" ),rs.getString ( "paperAbstract" ),rs.getString ( "keywords" ),rs.getString ( "paperTopics" ),rs.getClob ( "paper" ),rs.getInt ( "submitPerson" ),rs.getString ( "submitDate" ),rs.getString ( "referees" ),rs.getDouble ( "meanScore" ),rs.getBoolean ( "isEvaluationComplete" ),rs.getString ( "deadline" ),rs.getString ( "publishDate" ),rs.getBoolean ( "paperStatus" ) );
                papers.add(getPaper);
            }
        } catch(Exception se)
        {
            se.printStackTrace();
        }

        return papers;
    }


    /*--------------------------------------------------REFEREE METHODS------------------------------------------------*/


    public ArrayList<Paper> getWaitingPapersByReferee(int refereeId)
    {
        ArrayList<Paper> waiting = new ArrayList<Paper>();

        try {
            stmt = conn.createStatement ();
            String sql=String.format ( "SELECT * from Papers WHERE referees LIKE '%%%d%%' AND isEvaluationComplete IS NOT TRUE",refereeId);
            ResultSet rs = stmt.executeQuery ( sql );

            while(rs.next())
            {
                Paper getPaper = new Paper ( rs.getInt ( "paperId" ),rs.getString ( "authorName" ),rs.getString ( "authorMail" ),rs.getString ( "authorUniversityName" ),rs.getString ( "paperName" ),rs.getString ( "paperAbstract" ),rs.getString ( "keywords" ),rs.getString ( "paperTopics" ),rs.getClob ( "paper" ),rs.getInt ( "submitPerson" ),rs.getString ( "submitDate" ),rs.getString ( "referees" ),rs.getDouble ( "meanScore" ),rs.getBoolean ( "isEvaluationComplete" ),rs.getString ( "deadline" ),rs.getString ( "publishDate" ),rs.getBoolean ( "paperStatus" ) );

                if (!existIsRefereeEvaluation ( getPaper.getPaperId (), refereeId ))
                {
                    waiting.add(getPaper);
                }
            }
        }
        catch(Exception se)
        {
            se.printStackTrace();
        }
        return waiting;
    }


    public boolean checkAllEvaluationIsComplete(int paperId)
    {
        int allReferees=0;
        int evaluatedReferees=0;
        boolean returnStatement=false;

        try{

            stmt = conn.createStatement();
            String sql = "SELECT referees FROM Papers WHERE paperId=" + paperId;
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                String assignedRef=rs.getString ( "referees" );
                allReferees=assignedRef.split ( "," ).length;
            }

            stmt = conn.createStatement();
            String sql2 = "SELECT count(*) from Evaluation WHERE paperId=" + paperId;
            ResultSet rs2 = stmt.executeQuery ( sql2 );

            while(rs2.next())
            {
                evaluatedReferees = rs2.getInt("count(*)");
            }

            if(evaluatedReferees<allReferees)
            {
                returnStatement=false;
            }
            else if (evaluatedReferees==allReferees)
            {
                returnStatement=true;
            }
        }
        catch(Exception se)
        {
            se.printStackTrace();
        }
        return returnStatement;
    }


    public void setAllEvaluationIsComplete(int paperId)
    {
        double meanScore=0;

        try{
            stmt = conn.createStatement();
            String sql = "SELECT AVG(refereeScore) FROM Evaluation WHERE paperId=" + paperId;
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                meanScore=rs.getDouble ( 1);
            }

            String sql2 = "UPDATE Papers SET meanScore = ? WHERE paperId =?";
            PreparedStatement stmt2 = conn.prepareStatement (sql2);
            stmt2.setDouble (1,meanScore );
            stmt2.setInt ( 2,paperId );
            stmt2.executeUpdate ();
        }
        catch(Exception se)
        {
            se.printStackTrace();
        }
    }

    public boolean evaluationPaper(int paperId, String paperName, String paperAuthor, int refereeId, String refereeName, String comment, int score)
    {
        String submitDate = new SimpleDateFormat ("dd.MM.yyyy").format(new Date());
        boolean returnStatement=false;

        try {
            stmt = conn.createStatement();
            String sql=String.format("INSERT INTO Evaluation (paperId,paperName,paperAuthor,refereeId,refereeName,refereeComment,refereeScore,evaluationDate) VALUES (%d, '%s', '%s', %d, '%s','%s',%d,'%s')",paperId,paperName,paperAuthor,refereeId,refereeName,comment,score,submitDate);
            stmt.executeUpdate(sql);

            returnStatement=true;
        }
        catch (Exception se)
        {
            se.printStackTrace();
        }
        return returnStatement;
    }


    /*------------------------------------------------PRESIDENT OF THE COMMISSION METHODS-------------------------------------------------*/

    public void evaluationByPresident(int paperId, int decision)
    {
        String submitDate = new SimpleDateFormat ("dd.MM.yyyy").format(new Date());

        try{
            stmt = conn.createStatement();
            String sql = String.format("UPDATE Papers SET isEvaluationComplete=%d, paperStatus=%d, publishDate='%s' WHERE paperId=%d",1,decision,submitDate,paperId );
            stmt.executeUpdate (sql);
        } catch(Exception se)
        {
            se.printStackTrace();
        }

    }

    public ArrayList<Paper> getWaitingApprovePaper()
    {
        ArrayList<Paper> papers = new ArrayList<Paper>();

        try{
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Papers WHERE meanScore IS NOT NULL AND publishDate IS NULL AND paperStatus IS NULL ORDER BY meanScore DESC";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                //Paper getPaper=new Paper (  )
                Paper getPaper = new Paper ( rs.getInt ( "paperId" ),rs.getString ( "authorName" ),rs.getString ( "authorMail" ),rs.getString ( "authorUniversityName" ),rs.getString ( "paperName" ),rs.getString ( "paperAbstract" ),rs.getString ( "keywords" ),rs.getString ( "paperTopics" ),rs.getClob ( "paper" ),rs.getInt ( "submitPerson" ),rs.getString ( "submitDate" ),rs.getString ( "referees" ),rs.getDouble ( "meanScore" ),rs.getBoolean ( "isEvaluationComplete" ),rs.getString ( "deadline" ),rs.getString ( "publishDate" ),rs.getBoolean ( "paperStatus" ) );
                papers.add(getPaper);
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return papers;
    }

    public ArrayList<Paper> getWaitForAssignPapers()
    {
        ArrayList<Paper> papers = new ArrayList<Paper>();

        try{
            stmt = conn.createStatement();
            String sql = "SELECT * from Papers WHERE referees IS NULL";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                Paper getPaper = new Paper ( rs.getInt ( "paperId" ),rs.getString ( "authorName" ),rs.getString ( "authorMail" ),rs.getString ( "authorUniversityName" ),rs.getString ( "paperName" ),rs.getString ( "paperAbstract" ),rs.getString ( "keywords" ),rs.getString ( "paperTopics" ),rs.getClob ( "paper" ),rs.getInt ( "submitPerson" ),rs.getString ( "submitDate" ),rs.getString ( "referees" ),rs.getDouble ( "meanScore" ),rs.getBoolean ( "isEvaluationComplete" ),rs.getString ( "deadline" ),rs.getString ( "publishDate" ),rs.getBoolean ( "paperStatus" ));
                papers.add(getPaper);
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return papers;

    }

    public ArrayList<Paper> getAllPapers()
    {
        ArrayList<Paper> papers = new ArrayList<Paper>();

        try{
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Papers";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                Paper getPaper = new Paper ( rs.getInt ( "paperId" ),rs.getString ( "authorName" ),rs.getString ( "authorMail" ),rs.getString ( "authorUniversityName" ),rs.getString ( "paperName" ),rs.getString ( "paperAbstract" ),rs.getString ( "keywords" ),rs.getString ( "paperTopics" ),rs.getClob ( "paper" ),rs.getInt ( "submitPerson" ),rs.getString ( "submitDate" ),rs.getString ( "referees" ),rs.getDouble ( "meanScore" ),rs.getBoolean ( "isEvaluationComplete" ),rs.getString ( "deadline" ),rs.getString ( "publishDate" ),rs.getBoolean ( "paperStatus" ) );
                papers.add(getPaper);
            }
        }
        catch(Exception se)
        {
            se.printStackTrace();
        }

        return papers;
    }

    public ArrayList<Referee> getReferees()
    {
        ArrayList<Referee> referee = new ArrayList<Referee>();

        try{
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Users WHERE role=2 OR role=3";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                Referee ref = new Referee (rs.getInt ( "userId" ),rs.getString("name"),rs.getString("surname"), rs.getString("mail"),rs.getString("universityName"),rs.getInt ( "role" ),rs.getString("userName"),rs.getString("userPassword"),rs.getString("refereeInterests") );
                referee.add(ref);
            }
        } catch(Exception se)
        {
            se.printStackTrace();
        }

        return referee;
    }


    public boolean assignReferee(String referee, String deadline, int paperId)
    {
        boolean returnStatement=false;

        try{
            stmt = conn.createStatement();
            String sql = String.format("UPDATE Papers SET referees='%s', deadline='%s' WHERE paperId=%d",referee,deadline,paperId );
            stmt.executeUpdate (sql);

            returnStatement=true;
        }
        catch(Exception se)
        {
            se.printStackTrace();
        }

        return returnStatement;
    }


    /*------------------------------------------------REGISTRATION METHODS-------------------------------------------------*/

    public boolean addPerson(String name, String surname, String mail,String university, String userName, String userPassword, int role, String interests)
    {
        try {
            stmt = conn.createStatement();
            String sql="";

            if (role==1) // Author
            {
                sql = String.format("INSERT INTO Users (name,surname,mail,universityName,userName,userPassword,role) VALUES ('%s', '%s', '%s', '%s', '%s','%s', %d)", name, surname,mail,university,userName,userPassword,role);
            }
            else if (role==2 || role==3) //Referee or Author&Referee
            {
                sql = String.format("INSERT INTO Users (name,surname,mail,universityName,userName,userPassword,role, refereeInterests) VALUES ('%s', '%s', '%s', '%s', '%s','%s', %d, '%s')", name, surname,mail,university,userName,userPassword,role,interests);
            }

            stmt.executeUpdate(sql);

            return true;
        }
        catch (Exception se)
        {
            se.printStackTrace();
            return false;
        }
    }

    public Person searchByUserId(int userId)
    {
        Person user=null;

        try{
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Users WHERE userId=" + userId;
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next ())
            {
                user = new Person (rs.getInt ( "userId" ),rs.getString("name"),rs.getString("surname"), rs.getString("mail"),rs.getString("universityName"),rs.getInt ( "role" ),rs.getString("userName"),rs.getString("userPassword") );
            }
        } catch(Exception se)
        {
            se.printStackTrace();

        }
        return user;
    }

    public int isExistUsers(String userName, String userPassword)
    {
        int returnVal=-1;
        try{
            stmt = conn.createStatement();
            String sql = "SELECT * FROM Users WHERE userName='" + userName + "' AND userPassword='" + userPassword + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next ()) {

                returnVal= -1;
            }
            else
            {
                returnVal=rs.getInt ( "userId" );
            }

        } catch(Exception se)
        {
            se.printStackTrace();
        }
        return returnVal;
    }

    /*------------------------------------------------SYSTEM METHODS-------------------------------------------------*/

    public String getRefereeNameSurname(int refereeId)
    {
        String nameSurname="";

        try {
            stmt = conn.createStatement ();
            String sql=String.format ( "SELECT name,surname from Users WHERE userId=%d",refereeId);
            ResultSet rs = stmt.executeQuery ( sql );

            while(rs.next())
            {
                nameSurname=rs.getString ( "name" ) + " " + rs.getString ( "surname" );
            }
        }
        catch(Exception se)
        {
            se.printStackTrace();
        }

        return nameSurname;
    }

    public boolean existIsRefereeEvaluation(int paperId, int refereeId)
    {
        boolean returnStatement=false;
        int count=0;

        try{
            stmt = conn.createStatement();
            String sql = "SELECT count(*) FROM Evaluation WHERE paperId=" + paperId + " AND refereeId=" + refereeId;
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                count = rs.getInt("count(*)");
            }

            if (count==0)
            {
                returnStatement=false;
            }
            else
            {
                returnStatement=true;
            }

        }
        catch(Exception se)
        {
            se.printStackTrace();
        }

        return returnStatement;
    }


}
