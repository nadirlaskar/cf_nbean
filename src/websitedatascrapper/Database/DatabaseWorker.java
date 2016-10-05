/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websitedatascrapper.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import websitedatascrapper.Practice;
import websitedatascrapper.ProblemData;
import websitedatascrapper.Problems;

/**
 *
 * @author Nadir
 */
public class DatabaseWorker {
    
    private static Connection sqlconnection;
    
    private static final String InsertProblemSql="INSERT INTO `problems` "
                                          +"(`id`, `code`, `name`, `type`, `sucessful_submission`, `accuracy`, `problem_url`, `submit_url`, `status_url`)"
                                          +"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    
    private static final  String InsertProblemDataSql="INSERT INTO `problems_data` "
                                             + "(`problem_code`, `problem_name`, `body`, `max_timelimit`, `source_sizelimit`, `problem_author`, `problem_tester`, `date_added`, `language_supported`) "
                                             + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    
    private static final String InsertContestProblemSql="INSERT INTO `contest_problems` (`contest_code`, `problem_code`) "
                                                 + "VALUES (?, ?);";
   
    
    
    private static final String countProblemTypeSQL="select count(*) from problems where type = ? AND id > ? AND id < ?;";
    private static final String selectProblemsListSQL="select * from problems where type = ? AND id > ? AND id < ?;";
    
    private static final String selectProblemsDataSQL="select * from problems_data where problem_code = ?;";
    private static final String selectProblemsSQL="select * from problems where code = ?;";
    
    private static final String countProblemsSQL="select count(*) from problems";
    
    public static Connection getConnection()
    {
        if(sqlconnection==null) createConnection();
        return sqlconnection;
    }
    
    private static void createConnection()
    {
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
  
            sqlconnection=DriverManager.getConnection("jdbc:mysql://"+DatabaseInfo.DB_SERVER+":"+
                                                       DatabaseInfo.DB_PORT+"/"+DatabaseInfo.DB_DATABASE,
                                                       DatabaseInfo.DB_USER,DatabaseInfo.DB_PASS);
            }catch(ClassNotFoundException | SQLException e){ System.out.println("Error Connecting Database... \nError:\n"+e);}  
  
    }  
    
    
    public static Practice getPracticeProblems(String Type,int from,int to)
    {
        
        Practice practice =new Practice(Type,from,to);
        
        if(!practice.fetchedFromDatabase)
        {
        int index=getProblemsCount();    
        PreparedStatement Insert;
        int size=practice.problems.size();
        for(int i=0;i<size;i++)
        try { 
            Insert = sqlconnection.prepareStatement(InsertProblemSql);
            Insert.setInt(1, index+i);
            Insert.setString(2, practice.problems.get(i).code);
            Insert.setString(3, practice.problems.get(i).name);
            Insert.setInt(4, Type=="school"?0:Type=="easy"?1:Type=="medium"?2:Type=="hard"?3:4);
            Insert.setInt(5, toInt(practice.problems.get(i).successful_submission));
            Insert.setFloat(6, toFloat(practice.problems.get(i).accuracy));
            Insert.setString(7, practice.problems.get(i).problem_url);
            Insert.setString(8, practice.problems.get(i).submit_url);
            Insert.setString(9, practice.problems.get(i).status_url);
            Insert.executeUpdate();
            
            Insert = sqlconnection.prepareStatement(InsertProblemDataSql);
            
            Insert.setString(1, practice.problems.get(i).problem_data.problem_code);
            Insert.setString(2, practice.problems.get(i).problem_data.problem_name);
            Insert.setString(3, practice.problems.get(i).problem_data.body);
            Insert.setString(4, practice.problems.get(i).problem_data.max_timelimit);
            Insert.setInt(5, toInt(practice.problems.get(i).problem_data.source_sizelimit));
            Insert.setString(6, practice.problems.get(i).problem_data.problem_author);
            Insert.setString(7, practice.problems.get(i).problem_data.problem_tester);
            Insert.setString(8, toDate(practice.problems.get(i).problem_data.date_added));
            Insert.setString(9,practice.problems.get(i).problem_data.languages);
            
            Insert.executeUpdate();
             
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseWorker.class.getName()).log(Level.SEVERE, practice.problems.get(i).problem_data.problem_code);
            index--;
        }
    }
    return practice;
    }
    
    private static int getProblemsCount()
    {
        int count=0;
        try{
            ResultSet r= sqlconnection.createStatement().executeQuery(countProblemsSQL);
            while(r.next())
            {
                count=r.getInt("count(*)");
            }
        }catch(SQLException e){
        }
        return count;
    }
    
    
    public static void closeConnection()
    {
        try {
            sqlconnection.close();
        } catch (SQLException e) {
            System.out.println("Error Closing Database... \nError:\n"+e);
        }
    }
    
    private static int toInt(String value)
    {
        try
        {
            return Integer.parseInt(value);
        }catch(Exception e){return -1;}
    }
    
     private static float toFloat(String value)
    {
        try
        {
            return Float.parseFloat(value);
        }catch(Exception e){return -1;}
    }
     
     private static String toDate(String value)
     {
         String[] date=value.split("-");
         if(date.length!=3) return "00000000";
         if(date[0].length()==1) date[0]="0"+date[0];
         if(date[1].length()==1) date[1]="0"+date[1];
         return date[2]+date[1]+date[0];
     }
     
     
     public static  int getCount(String Type , int from, int to) {
     ResultSet r;
     try {
        PreparedStatement select = sqlconnection.prepareStatement(countProblemTypeSQL);
        select.setString(1,""+(Type=="school"?0:Type=="easy"?1:Type=="medium"?2:Type=="hard"?3:4));
        select.setInt(2,from);
        select.setInt(3,to);
        r=select.executeQuery();
        while(r.next())
        return r.getInt("count(*)");        
        } catch(SQLException e){
            System.out.print(e);
        }
    
    return 0;
    }
     
    public static ArrayList<Problems> getPracticeProblemsList(String Type,int from,int to)
    {
        ArrayList<Problems> Problems = new ArrayList<>();
        ResultSet r=null;
     try {
        PreparedStatement select = sqlconnection.prepareStatement(selectProblemsListSQL);
        //select.setString(1,"problems");
        //select.setString(2,"type");
        select.setString(1,""+(Type=="school"?0:Type=="easy"?1:Type=="medium"?2:Type=="hard"?3:4));
        select.setString(2,""+from);
        select.setString(3,""+to);
        r=select.executeQuery();
        
        while(r.next())
        {
           Problems.add(getProblem(r.getString("code")));
        }
              
        } catch(SQLException e){}
     
        try {r.close();} catch (SQLException e) {}
    
    return Problems;
           
    }
    
    public static ProblemData getProblemData(String problem_code)
    {
         ProblemData data = new ProblemData();
         ResultSet r=null;
     try {
        PreparedStatement select = sqlconnection.prepareStatement(selectProblemsDataSQL);
        //select.setString(1,"problems_data");
        //select.setString(2,"problem_code");
        select.setString(1,""+problem_code);
        r=select.executeQuery();
      
        while(r.next())
        {
            data.body=r.getString("body");
            data.problem_code=r.getString("problem_code");
            data.problem_author=r.getString("problem_author");
            data.problem_name=r.getString("problem_name");
            data.problem_tester=r.getString("problem_tester");
            data.languages=r.getString("language_supported");
            data.max_timelimit=r.getString("max_timelimit");
            data.source_sizelimit=""+r.getInt("source_sizelimit");
            data.date_added=""+r.getDate("date_added");
            data.contest_name="";
            
        }
             
        } catch(SQLException e){}
     
        try {r.close();} catch (SQLException e) {}
       
     
        return  data;
    }
    
     public static Problems getProblem(String problem_code)
    {
         Problems problem= new Problems();
         ResultSet r=null;
     try {
        PreparedStatement select = sqlconnection.prepareStatement(selectProblemsSQL);
        //select.setString(1,"problems");
        //select.setString(2,"code");
        select.setString(1,""+problem_code);
        r=select.executeQuery();
 
          
        while(r.next())
        {
           problem.code = r.getString("code");
           problem.accuracy = ""+r.getInt("accuracy");
           problem.name = r.getString("name");
           problem.problem_url = r.getString("problem_url");
           problem.status_url = r.getString("status_url");
           problem.successful_submission = ""+r.getInt("sucessful_submission");
           problem.submit_url=r.getString("submit_url");
           problem.problem_data = getProblemData(problem.code);
        } 
        
        } catch(SQLException e){}
     
        try {r.close();} catch (SQLException e) {}
     
        return  problem;
    }
}  

