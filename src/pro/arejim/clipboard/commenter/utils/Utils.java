package pro.arejim.clipboard.commenter.utils;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Utils {

    private final static File notesFile = new File("notes.txt");
    private final static DateFormat timeFormat = new SimpleDateFormat("[dd.MM.yyyy] (kk:mm:ss)");

    // Максимальное рабочее пространство интерфейса (учитывая Пуск)
    public static Rectangle getWorkArea() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    }

    // Запись текста в файл
    public static void appendFile(String clipboard, String note) {
        try {
            FileWriter fileWriter = new FileWriter(notesFile, true);
            fileWriter.write("**********************************" + timeFormat.format(System.currentTimeMillis()) + "**********************************\n");
            fileWriter.write(clipboard + '\n');
            if (!note.isEmpty()) { // Если заметка не пуста
                fileWriter.write("----------------------------------------- Заметка -----------------------------------------\n");
                fileWriter.write(note + '\n');
            }
            fileWriter.write("*******************************************************************************************\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
