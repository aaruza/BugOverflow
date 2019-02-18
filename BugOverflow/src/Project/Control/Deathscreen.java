package Project.Control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Deathscreen {

    @FXML
    AnchorPane mainAnchor;

    public Deathscreen() {

    }

    @FXML
    public void initialize () {

    }

    @FXML
    private void replay (ActionEvent e) throws IOException {
        Parent root = new FXMLLoader().load(getClass().getResource("../FXML/GameLayout/GameLayout.fxml"));
        Scene scene = new Scene(root, mainAnchor.getWidth(), mainAnchor.getHeight());

        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

}
