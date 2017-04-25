package smartmonitor.google.com.smartlogger.writer;

import android.os.Build;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import smartmonitor.google.com.smartlogger.Data.AccessibilityData;

/**
 * Created by jihun.im on 2017-04-25.
 */

public class FirebaseDBWriter implements DataWritingInterface {
    private static FirebaseDBWriter firebaseDBWriter = null;
    private final String TAG = "AccessTest";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String databaseName;

    private FirebaseDBWriter() {
        databaseName = Build.ID + " " + Build.MODEL;
    }

    @Override
    public void write(AccessibilityData data) {
        Log.d(TAG, data.getContents());
        databaseReference.child(databaseName).push().setValue(data);
    }

    public static FirebaseDBWriter getInstance() {
        if (firebaseDBWriter == null) {
            synchronized (LogWriter.class) {
                if (firebaseDBWriter == null) {
                    firebaseDBWriter = new FirebaseDBWriter();
                }
            }
        }
        return firebaseDBWriter;
    }
}
