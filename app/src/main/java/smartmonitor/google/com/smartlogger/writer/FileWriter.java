package smartmonitor.google.com.smartlogger.writer;

import android.util.Log;

import smartmonitor.google.com.smartlogger.Data.AccessibilityData;

/**
 * Created by jihun.im on 2017-04-25.
 */

public class FileWriter implements DataWritingInterface {
    private static FileWriter fileWriter = null;
    private final String TAG = "AccessTest";

    private FileWriter() {

    }

    @Override
    public void write(AccessibilityData data) {
        Log.d(TAG, "writed: " + data.getContents());

    }

    public static DataWritingInterface getInstance() {
        if (fileWriter == null) {
            synchronized (FileWriter.class) {
                if (fileWriter == null) {
                    fileWriter = new FileWriter();
                }
            }
        }
        return fileWriter;
    }
}
