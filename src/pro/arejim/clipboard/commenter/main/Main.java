package pro.arejim.clipboard.commenter.main;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import pro.arejim.clipboard.commenter.gui.javafx.Frame;
import pro.arejim.clipboard.commenter.utils.GlobalKeyListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        new Thread(() -> {
            // Проверка приложения на уникальность
            try {
                ServerSocket serverSocket = new ServerSocket(64752);
                serverSocket.accept();
            } catch (IOException e) {
                System.err.println(Settings.PROJECT_NAME + " is already running");
                System.exit(0);
            }
        }, "socket").start();
        // Назначение слушателей на клавиатуру с помощью сторонних библиотек
        // отдельным потоком для ускорения запуска приложения
        new Thread(() -> {
            // Отключение вывода лишних сообщений из сторонних библиотек
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            // регистрация сторонних библиотек
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException ex) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());
                System.exit(1);
            }
            // Добавление слушателей на клавиатуру
            GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        }, "background_processes").start();
        // Старт JavaFX
        Frame.start(args);
    }
}
