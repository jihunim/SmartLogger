package smartmonitor.google.com.smartlogger.writer;

import android.util.Log;

import smartmonitor.google.com.smartlogger.Data.AccessibilityData;

/**
 * Created by jihun.im on 2017-04-25.
 */

public class LogWriter implements DataWritingInterface {
    private static LogWriter logWriter = null;
    private final String TAG = "AccessTest";

    private LogWriter() {

    }

    @Override
    public void write(AccessibilityData data) {
        Log.d(TAG, data.getContents());
    }

    public static DataWritingInterface getInstance() {
        if (logWriter == null) {
            synchronized (LogWriter.class) {
                if (logWriter == null) {
                    logWriter = new LogWriter();
                }
            }
        }
        return logWriter;
    }
}
