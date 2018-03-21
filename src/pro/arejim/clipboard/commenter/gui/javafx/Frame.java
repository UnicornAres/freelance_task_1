package pro.arejim.clipboard.commenter.gui.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pro.arejim.clipboard.commenter.gui.swing.TrayController;
import pro.arejim.clipboard.commenter.main.Settings;

import javax.swing.*;
import java.awt.*;

import static javafx.scene.paint.Color.TRANSPARENT;
import static pro.arejim.clipboard.commenter.utils.Utils.getWorkArea;

public class Frame extends Application {

    public static Stage stage;

    // Просто старт графического интерфейса, для запуска из других классов
    public static void start(String[] args) {
        launch(args);
    }

    /**
     * Скрывает окно
     */
    public static void hideStage() {
        if (stage != null) {
            if (stage.isShowing()) {
                stage.hide();
            }
        }
    }

    /**
     * Показывает окно
     */
    public static void showStage() {
        if (stage != null) {
            if (stage.isIconified()) {
                stage.setIconified(false);
            }
            if (!stage.isShowing()) {
                stage.show();
                Platform.runLater(() -> stage.toFront()); // Выводит окно поверх других
            }
        }
    }

    @Override
    public void start(final Stage stage) {
        // Создаёт ссылку на этот стэйдж за пределами метода
        Frame.stage = stage;

        // Не выходить из приложения при закрытии окна
        Platform.setImplicitExit(false);

        // Создать иконку в трее (с помощью awt)
        new Thread(() -> SwingUtilities.invokeLater(TrayController::addAppToTray)).start();

        // Убрать стандартную рамку интерфейса
        stage.initStyle(StageStyle.TRANSPARENT);

        // Загрузка шаблона окна из файла fxml
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(
                    ClassLoader.getSystemResource("pro/arejim/clipboard/commenter/assets/fxmls/notes.fxml")), TRANSPARENT);
            System.out.println("notes.fxml loaded.");
        } catch (Exception e) {
            System.err.println("Can`t load frame.");
            // Создание окна ошибки при загрузке файла fxml
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // Событие при закрытии окна ошибки
            alert.setOnCloseRequest(event -> System.exit(0));
            // Текст заглавления окна
            alert.setHeaderText(Settings.PROJECT_NAME + " starting error!");
            // Текст ошибки
            alert.setContentText(e.getMessage());
            scene = alert.getDialogPane().getScene();
        }

        // Определение размера рабочего пространства для прикрепления окна к правому нижнему углу
        Rectangle rectangle = getWorkArea();
        stage.setY(0);
        stage.setX(0);
        stage.setWidth(rectangle.width);
        stage.setHeight(rectangle.height);

        // Устанавливает иконку приложения
        Image ico = new Image("pro/arejim/clipboard/commenter/assets/img/favicon.png");
        stage.getIcons().add(ico);
        // Присваивает название приложению
        stage.setTitle(Settings.PROJECT_NAME);
        // Отключение "растяжимости" окна
        stage.setResizable(false);

        stage.setScene(scene);
    }
}
