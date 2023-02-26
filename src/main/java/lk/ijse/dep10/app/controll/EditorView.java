package lk.ijse.dep10.app.controll;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import lk.ijse.dep10.app.AppInitializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class EditorView {

    @FXML
    private Button btnDown;

    @FXML
    private Button btnReplace;

    @FXML
    private Button btnReplaceAll;

    @FXML
    private Button btnUp;

    @FXML
    private CheckBox chkMatchCase;

    @FXML
    private Label lblResult;

    @FXML
    private MenuItem mnAbout;

    @FXML
    private MenuItem mnClose;

    @FXML
    private MenuItem mnNew;

    @FXML
    private MenuItem mnOpen;

    @FXML
    private MenuItem mnPrint;

    @FXML
    private MenuItem mnSave;

    @FXML
    private MenuItem mnSaveAS;

    @FXML
    private TextArea txtEditor;

    @FXML
    private TextField txtFind;

    @FXML
    private TextField txtReplace;

    private static boolean isEdited = false;
    private static File fileAddress;

    @FXML
    void btnDownOnAction(ActionEvent event) {

    }

    @FXML
    void btnReplaceAllOnAction(ActionEvent event) {

    }

    @FXML
    void btnReplaceOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpOnAction(ActionEvent event) {

    }

    @FXML
    void chkMatchCaseOnAction(ActionEvent event) {

    }

    @FXML
    void mnAboutOnAction(ActionEvent event) {

    }

    @FXML
    void mnCloseOnAction(ActionEvent event) {

    }

    @FXML
    void mnNewOnAction(ActionEvent event) {

        if(isEdited) {
            ButtonType buttonNo = new ButtonType("No");
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save the existing file before create new Text file",buttonNo,ButtonType.YES);
            Optional<ButtonType> button = confirm.showAndWait();
            System.out.println(button.get());
            System.out.println(button.get());
            if (button.isEmpty() || button.get() == ButtonType.NO) {

                System.out.println("no select");
                return;
            }
            if (button.get() == ButtonType.YES) {
                mnSaveOnAction(event);
            }
        }
        AppInitializer.observableTitle.set("untitled");
        txtEditor.setText("");
        isEdited = false;

    }

    @FXML
    void mnOpenOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open a text file");

        File file = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());

        if(file == null) return;

        fileAddress = file;
        String fileName = String.valueOf(file);
        AppInitializer.observableTitle.set(fileName.substring(fileName.lastIndexOf('/')+1));
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        String content = new String(bytes);
        txtEditor.setText(new String(bytes));
    }

    @FXML
    void mnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void mnSaveAsOnAction(ActionEvent event) {

    }

    @FXML
    void mnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void rootOnDragDropped(DragEvent event) {

    }

    @FXML
    void rootOnDragOver(DragEvent event) {

    }

    @FXML
    void txtEditorOnMouseClicked(MouseEvent event) {

    }

}
