package pro.arejim.clipboard.commenter.utils;

import javafx.application.Platform;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import pro.arejim.clipboard.commenter.gui.javafx.Frame;

public class GlobalKeyListener implements NativeKeyListener {

    private static volatile boolean ctrl = false, q = false, lock = false;

    public void nativeKeyPressed(NativeKeyEvent e) {
        // Проверка нажатия клавиш
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            ctrl = true;
            if (q) {
                if (!lock) { // Блокирование функции от вызова более 1 раза
                    lock = true;
                    Platform.runLater(Frame::showStage);
                }
            }
        } else if (e.getKeyCode() == NativeKeyEvent.VC_Q) {
            q = true;
            if (ctrl) {
                if (!lock) {// Блокирование функции от вызова более 1 раза
                    lock = true;
                    Platform.runLater(Frame::showStage);
                }
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        // Проверка отжатия клавиш
        // Снятие блокировки при отжатии клавиш
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            ctrl = false;
            lock = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_Q) {
            q = false;
            lock = false;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

}
