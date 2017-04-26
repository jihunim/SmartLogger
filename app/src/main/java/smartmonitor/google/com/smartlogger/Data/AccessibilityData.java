package smartmonitor.google.com.smartlogger.Data;

/**
 * Created by jihun.im on 2017-04-25.
 */

public class AccessibilityData {
    private String contents;
    private String date;
    private String apkName;


    public AccessibilityData() {
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }
}
