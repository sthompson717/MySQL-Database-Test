
package databasetest;

import java.sql.*;
import org.json.simple.*;

/**
 * @author Stephen Thompson
 */

public class DatabaseTest {
    public JSONArray getJSONData(){
        
        Connection conn;
        ResultSetMetaData metaData;
        ResultSet resultSet = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        
        String query, key, value;
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        boolean hasResults;
        int resultCount, columnCount;
        
        try {
            
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password= "ko0@Ur83snvA";
            System.out.println("Connecting to " + server + "...");
            
            conn = DriverManager.getConnection(server, username, password);
            
            if (conn.isValid(0)){
                System.out.println("Connected successfully.");
                
                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);
                
                System.out.println("Submitting query...");
                
                hasResults = pstSelect.execute();
                
                System.out.println("Getting results...");
                
                while (hasResults || pstSelect.getUpdateCount() != -1){
                    if (hasResults){
                        resultSet = pstSelect.getResultSet();
                        metaData = resultSet.getMetaData();
                        columnCount = metaData.getColumnCount();
                        
                        while (resultSet.next()){
                            jsonObject = new JSONObject();
                            for (int i = 2; i <= columnCount; i++){
                                key = metaData.getColumnLabel(i);
                                value = resultSet.getString(i);
                                jsonObject.put(key, value);
                            }
                            jsonArray.add(jsonObject);
                        }
                    }
                    
                    else{
                        resultCount = pstSelect.getUpdateCount();
                        
                        if (resultCount == -1){
                            break;
                        }
                    }
                    
                    hasResults = pstSelect.getMoreResults();
                }
            }
            
            conn.close();
        }
        catch (Exception e) {e.printStackTrace();}
        
        finally {
            if (resultSet != null) {try {resultSet.close();} catch (Exception e){ e.printStackTrace();}}
            if (pstSelect != null) {try {pstSelect.close();} catch (Exception e){ e.printStackTrace();}}
            if (pstUpdate != null) {try {pstUpdate.close();} catch (Exception e){ e.printStackTrace();}}
        }
        return jsonArray;
    }
}
