package pro.arejim.clipboard.commenter.gui.swing;

import javafx.application.Platform;
import pro.arejim.clipboard.commenter.gui.javafx.Frame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TrayController {

    /**
     * Добавляет иконку приложения в трей
     */
    public static void addAppToTray() {
        try {
            // Проверка системы на поддержку трея
            if (!SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // Установка иконки в трее с подгоном изображения по размеру
            SystemTray tray = SystemTray.getSystemTray();
            Image trayImage = ImageIO.read(ClassLoader.getSystemResource("pro/arejim/clipboard/commenter/assets/img/favicon.png"));
            Dimension trayIconSize = tray.getTrayIconSize();
            TrayIcon trayIcon = new TrayIcon(trayImage.getScaledInstance(trayIconSize.width, trayIconSize.height, Image.SCALE_SMOOTH));

            // Событие при двойном клике ЛКМ по иконке в трее
            trayIcon.addActionListener(event -> Platform.runLater(Frame::showStage));

            // Добавление елементов в меню всплывающее при клике ПКМ по иконке в трее
            // Отрытие окна комментирования
            MenuItem openItem = new MenuItem("Комментировать");
            openItem.addActionListener(event -> Platform.runLater(Frame::showStage));
            // Выход из приложения
            MenuItem exitItem = new MenuItem("Выйти");
            exitItem.addActionListener(event -> {
                tray.remove(trayIcon);
                Platform.exit();
                System.exit(0);
            });

            // Присвоение всплывающего меню к иконке в трее
            final PopupMenu popup = new PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            // Добавление приложения в трей
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Unable to init system tray");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
