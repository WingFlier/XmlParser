import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionController
{
    //TODO add port textfield too
    @FXML
    TextField tfUser = new TextField();
    @FXML
    TextField tfPassword = new TextField();
    @FXML
    TextField tfDbName = new TextField();

    @FXML
    public void connect(ActionEvent actionEvent)
    {
        try
        {
            java.sql.Connection connection = null;
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/" + tfDbName.getText(), tfUser.getText(), tfPassword.getText());
            FIleParser.setStatement(connection.createStatement());
            Error.newInfo("connected successfully");
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.close();
        } catch (SQLException e)
        {
            Error.newError("connection not established");
            e.printStackTrace();
        } finally
        {
           /* Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.close();*/
        }
    }
}
