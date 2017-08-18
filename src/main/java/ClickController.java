import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;


public class ClickController
{
    private static Stage stage;
    @FXML
    private TextField element = new TextField();
    @FXML
    public static Checkbox checkTableDelete = new Checkbox();


    public static void init(Stage pStage)
    {
        stage = pStage;
    }

    @FXML
    public void chooseFIle(ActionEvent actionEvent)
    {
        if (FIleParser.getStatement() == null)
        {
            Error.newError("connection must be established, restart application");
        }
        String element = this.element.getText();
        if (element.equals(""))
        {
            Error.newError("row cannot be empty");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("xml",
                "*.xml");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Open Resource File");
        FIleParser fIleParser = FIleParser.newInstance(fileChooser.showOpenDialog(stage), element);
        if (fIleParser == null)
            Error.newError("choose a file");
    }


}