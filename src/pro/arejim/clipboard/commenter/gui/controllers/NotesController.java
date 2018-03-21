package pro.arejim.clipboard.commenter.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import pro.arejim.clipboard.commenter.gui.javafx.Frame;
import pro.arejim.clipboard.commenter.main.Settings;
import pro.arejim.clipboard.commenter.utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class NotesController implements Initializable {


    private static volatile boolean shift = false, enter = false;
    @FXML   // текстовое поле для текста из буфера обмена
    public TextArea clipboardArea;
    @FXML   // текстовое поле для заметок
    public TextArea commentArea;
    @FXML   // Строка с названием проекта
    private Label titleLbl;
    @FXML   // кнопка "Сохранить"
    private Button saveBtn;
    @FXML   // кнопка "Отмена"
    private Button cancelBtn;
    @FXML
    private Button iconfieBtn;

    // Обновление текста в текстовых полях
    private void updateAreas() {
        commentArea.setText(""); // Удаление предыдущей заметки, если она была
        Clipboard clipboard = Clipboard.getSystemClipboard();   // Ситемный буфер обмена
        // Встака текста из буфера обмена в текстовое поле
        Object tmpContent = clipboard.getContent(DataFormat.PLAIN_TEXT);
        if (tmpContent != null) {
            String text = clipboard.getContent(DataFormat.PLAIN_TEXT).toString();
            if (!text.isEmpty()) {
                clipboardArea.setText(text);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Установка названия из Settings
        titleLbl.setText(Settings.PROJECT_NAME);
        // Событие при нажатии на кнопку "Отменить" (приложение в трей)
        cancelBtn.setOnAction(e -> Platform.runLater(Frame::hideStage));
        // Событие при нажатии на кнопку "Сохранить"
        saveBtn.setOnAction(e -> {
            // Запись текста из полей в файл
            Utils.appendFile(clipboardArea.getText(), commentArea.getText());
            // Приложение в трей
            Platform.runLater(Frame::hideStage);
        });
        iconfieBtn.setOnAction(e -> {
            if (!Frame.stage.isIconified()) {
                Frame.stage.setIconified(true);
            }
        });
        commentArea.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SHIFT) {
                shift = true;
            } else if (e.getCode() == KeyCode.ENTER) {
                enter = true;
            }
            if (shift && enter) {
                // Запись текста из полей в файл
                Utils.appendFile(clipboardArea.getText(), commentArea.getText());
                // Приложение в трей
                Platform.runLater(Frame::hideStage);
            }
        });
        commentArea.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SHIFT) {
                shift = false;
            } else if (e.getCode() == KeyCode.ENTER) {
                enter = false;
            }
        });

        // После загрузки интерфейса
        Frame.stage.setOnShown(e -> {
            updateAreas(); // Обновление текстовых полей
            Platform.runLater(() -> commentArea.requestFocus()); // Выбрать поле заметок для ввода
        });
    }
}
