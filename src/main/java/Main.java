import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application
{
    private static boolean check = true;


    public static void main(String[] args) throws SQLException, IOException
    {
     /*   beginParsing();
        HeapSizeChecker checker = new HeapSizeChecker();
        checker.start();*/
        launch(args);

//        String selectTableSQL = "SELECT * FROM test";
//        ResultSet rs = statement.executeQuery(selectTableSQL);
//
//        while (rs.next())
//        {
//            String userid = rs.getString("id");
//            String username = rs.getString("testcol");
//
//            System.out.println("id : " + userid);
//            System.out.println("testcol : " + username);
//        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
            primaryStage.setTitle("Choose a file to parse");
            primaryStage.setScene(new Scene(root, 305, 150));
            primaryStage.setResizable(false);
            root.requestFocus();
            primaryStage.show();
            ClickController.init(primaryStage);
            establishConnection(primaryStage);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void establishConnection(Stage pStage) throws IOException
    {
        Stage stage = new Stage();
        Parent conn = FXMLLoader.load(getClass().getResource("fxml/connection.fxml"));
        stage.setResizable(false);
        stage.setScene(new Scene(conn));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(pStage.getScene().getWindow());
        conn.requestFocus();
        stage.show();
    }

    private static void beginParsing() throws SQLException
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

        XmlParser parser = new XmlParser();
        java.sql.Connection connection = null;
        try
        {
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/dbbig", "root", "root");
        } catch (SQLException e)
        {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        Statement statement = connection.createStatement();
        try
        {
            /*newTable(statement, "Users", XmlParser.attributesUsers);
            ArrayList<HashMap<String, String>> data = parser.parseXmlAttributes("C:\\Users\\Wingfly\\Desktop\\DB\\Users.xml", XmlParser.attributesUsers);
            addToDB(statement, "Users", data);
            System.out.println("Users finished");

            newTable(statement, "Badges", XmlParser.attributesBadges);
            addToDB(statement, "Badges", parser.parseXmlAttributes("C:\\Users\\Wingfly\\Desktop\\DB\\Badges.xml", XmlParser.attributesBadges));
            System.out.println("Badges finished");

            newTable(statement, "Comments", XmlParser.attributesComments);
            addToDB(statement, "Comments", parser.parseXmlAttributes("C:\\Users\\Wingfly\\Desktop\\DB\\Comments.xml", XmlParser.attributesComments));
            System.out.println("Comments finished");

            newTable(statement, "History", XmlParser.attributesHistory);
            addToDB(statement, "History", parser.parseXmlAttributes("C:\\Users\\Wingfly\\Desktop\\DB\\PostHistory.xml", XmlParser.attributesHistory));
            System.out.println("History finished");

            newTable(statement, "PostLinks", XmlParser.attributesPostLinks);
            addToDB(statement, "PostLinks", parser.parseXmlAttributes("C:\\Users\\Wingfly\\Desktop\\DB\\PostLinks.xml", XmlParser.attributesPostLinks));
            System.out.println("PostLinks finished");*/

            //  newTable(statement, "Post", XmlParser.attributesPost);
            addToDB(statement, "Post", parser.parseXmlAttributes("C:\\Users\\Wingfly\\Desktop\\DB\\Posts.xml", XmlParser.attributesPost));
            System.out.println("Post finished");

          /*  newTable(statement, "Tags", XmlParser.attributesTags);
            addToDB(statement, "Tags", parser.parseXmlAttributes("C:\\Users\\Wingfly\\Desktop\\DB\\Tags.xml", XmlParser.attributesTags));
            System.out.println("Tags finished");

            newTable(statement, "Votes", XmlParser.attributesVotes);
            addToDB(statement, "Votes", parser.parseXmlAttributes("C:\\Users\\Wingfly\\Desktop\\DB\\Votes.xml", XmlParser.attributesVotes));
            System.out.println("Votes finished");
            System.out.println("parsing finished");*/
        } catch (OutOfMemoryError error)
        {
            check = false;
        }
    }

    private static void addToDB(Statement statement, String tableName, ArrayList<HashMap<String, String>> data) throws SQLException
    {
        try
        {
            for (HashMap<String, String> str : data)
            {
                StringBuilder column = new StringBuilder();
                StringBuilder val = new StringBuilder();
                for (String name : str.keySet())
                {
                    String s = str.get(name);
                    s = s.replace("'", "")
                            .replace("`", "")
                            .replace("\\", "");
                    column.append("`").append(name).append("`");
                    val.append("'").append(s).append("'");
                    // if name equals to last key of map dont append , at the end
                    if (!name.equals(str.keySet().toArray()[str.size() - 1]))
                    {
                        column.append(", ");
                        val.append(", ");
                    }
                }
                String selectTableSQL2 = "INSERT INTO `" + tableName +
                        "` (" + column.toString() + ") VALUES (" +
                        val.toString() + ")";
                statement.executeUpdate(selectTableSQL2);
                System.out.println(selectTableSQL2);
            }
        } catch (Exception e)
        {
            check = false;
            e.printStackTrace();
        }
    }

    private static void newTable(Statement statement, String tableName, String[] array) throws SQLException
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE table `dbbig`.`")
                .append(tableName)
                .append("`( `idUsers` INT NOT NULL AUTO_INCREMENT,");
        for (int i = 0; i < array.length; i++)
        {
            builder.append("`" + array[i] + "` TEXT(1000000),");
        }
        builder.append(" PRIMARY KEY (`idUsers`));");
        statement.executeUpdate("DROP TABLE  if exists `dbbig`.`" + tableName + "`");
        statement.executeUpdate(builder.toString());
    }

    static class HeapSizeChecker extends Thread
    {
        @Override
        public void run()
        {
            while (check)
            {
                try
                {
                    sleep(2000);
                    System.out.println(Runtime.getRuntime().freeMemory() / 1000000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}