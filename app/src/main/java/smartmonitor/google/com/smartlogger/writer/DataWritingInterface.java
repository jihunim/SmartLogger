package smartmonitor.google.com.smartlogger.writer;

import smartmonitor.google.com.smartlogger.Data.AccessibilityData;

/**
 * Created by jihun.im on 2017-04-25.
 */

public interface DataWritingInterface {
    void write(AccessibilityData data);
}
