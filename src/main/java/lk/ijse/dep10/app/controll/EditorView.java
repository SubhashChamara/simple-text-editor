package lk.ijse.dep10.app.controll;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dep10.app.AppInitializer;
import lk.ijse.dep10.app.controll.util.SearchResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.beans.binding.Bindings.select;

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
    static int pos;
    private ArrayList<SearchResult> searchResults=new ArrayList<>();
    static int flag = 0;

    public void initialize(){

        AppInitializer.stage.setOnCloseRequest(windowEvent -> {
            if(isEdited) windowEvent.consume();
            try {
                mnCloseOnAction(new ActionEvent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


        txtFind.textProperty().addListener((observableValue, s, current) -> {
            findResultCount();


        });
        txtEditor.textProperty().addListener((observableValue, s, current) -> {
            findResultCount();

        });
    }


    @FXML
    void btnDownOnAction(ActionEvent event) {
        pos++;
        if (pos == searchResults.size()) {
            pos=-1;
            return;
        }
        select();
    }
    private void select() {
        if(searchResults.isEmpty())return;
        SearchResult searchResult = searchResults.get(pos);
        txtEditor.selectRange(searchResult.getStart(),searchResult.getEnd());
        lblResult.setText(String.format("%d / %d Results", pos+1,searchResults.size()));
    }

    @FXML
    void btnReplaceAllOnAction(ActionEvent event) {
        if(txtReplace.getText().isEmpty()||searchResults.size()==0)return;
        txtEditor.deselect();
        findResultCount();
        String newString = txtReplace.getText();
        String oldText = txtEditor.getSelectedText();

        txtEditor.setText(txtEditor.getText().replaceAll(txtEditor.getSelectedText(),newString));
        if (flag == 2) {
            while (searchResults.size()!=0) {
                findResultCount();
                txtEditor.setText(txtEditor.getText().replaceAll(txtEditor.getSelectedText(),newString));
            }

        }
        findResultCount();
        txtReplace.clear();
        txtEditor.deselect();

    }

    @FXML
    void btnReplaceOnAction(ActionEvent event) {
        findResultCount();
        if(txtReplace.getText().isEmpty()||searchResults.size()==0)return;

        String newString = txtReplace.getText();
        txtEditor.replaceSelection(newString);
        findResultCount();
        if (searchResults.size() == 0) {
            txtEditor.deselect();
            txtReplace.clear();
        }

    }

    private void findResultCount() {
        String query = txtFind.getText();
        searchResults.clear();
        pos=0;
        txtEditor.deselect();

        Pattern pattern;
        try {
            pattern = Pattern.compile(query,flag);
        } catch (Exception e) {
            return;
        }
        Matcher matcher = pattern.matcher(txtEditor.getText());

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            SearchResult searchResult = new SearchResult(start, end);
            searchResults.add(searchResult);
        }
        lblResult.setText(String.format("%d Results", searchResults.size()));
        select();
    }

    @FXML
    void btnUpOnAction(ActionEvent event) {
        pos--;
        if (pos < 0) {
            pos=searchResults.size();
            return;
        }
        select();
    }

    @FXML
    void chkMatchCaseOnAction(ActionEvent event) {
        flag = flag == 0 ? 2 : 0;
        findResultCount();
    }

    @FXML
    void mnAboutOnAction(ActionEvent event) throws IOException {
        URL file = getClass().getResource("/view/AboutUs.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(file);
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("About TextME");
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void mnCloseOnAction(ActionEvent event) throws IOException {
        if (isEdited) {

            ButtonType buttonSaveClose = new ButtonType("Save and Close");
            ButtonType buttonClose = new ButtonType("Close without Save");

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, String.format("Save changers to the Document %s before Closing", AppInitializer.observableTitle)
                    ,ButtonType.CANCEL,buttonSaveClose,buttonClose);
            Optional<ButtonType> button =confirm.showAndWait();
            if(button.isEmpty() || button.get() == ButtonType.CANCEL) return;
            if (button.get() == buttonSaveClose) {
                System.out.println("save close");
                mnSaveOnAction(event);

                if(isEdited) return;
            }
        }
        System.out.println("platform exit");
        Platform.exit();
    }
    @FXML
    void mnNewOnAction(ActionEvent event) throws IOException {

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
    void mnSaveAsOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a text File");

        File file =fileChooser.showSaveDialog(txtEditor.getScene().getWindow());
        System.out.println(file);
        if(file == null)return;
        fileAddress =file;
        FileOutputStream fos = new FileOutputStream(file,false);

        String text = txtEditor.getText();
        byte[] bytes = text.getBytes();
        fos.write(bytes);
        fos.close();
        String fileName = String.valueOf(file);
        AppInitializer.observableTitle.set(fileName.substring(fileName.lastIndexOf('/')+1));
        isEdited = false;
    }

    @FXML
    void mnSaveOnAction(ActionEvent event) throws IOException {
        if(!isEdited) return;
        if (AppInitializer.observableTitle.getValue().equals("untitled")) {
            Alert inform = new Alert(Alert.AlertType.INFORMATION, "There is no text to save",ButtonType.CLOSE);
            inform.show();
            return;
        }
        if (AppInitializer.observableTitle.getValue().equals("*untitled")) {
            mnSaveAsOnAction(event);


        } else {
            FileOutputStream fos = new FileOutputStream(fileAddress, false);

            String text = txtEditor.getText();
            byte[] bytes = text.getBytes();
            fos.write(bytes);
            fos.close();
            isEdited = false;
            AppInitializer.observableTitle.set(AppInitializer.observableTitle.get().substring(1));
        }

    }

    private void txtEditorOnKeyReleased() {
        isEdited = true;
        if (AppInitializer.observableTitle.get().charAt(0) == '*')return;
        AppInitializer.observableTitle.set("*".concat(AppInitializer.observableTitle.get()));

    }

    @FXML
    void rootOnDragDropped(DragEvent event) {

    }

    @FXML
    void rootOnDragOver(DragEvent event) {

    }

    @FXML
    void txtEditorOnMouseClicked(MouseEvent event) {
        txtEditorOnKeyReleased();
    }

}
