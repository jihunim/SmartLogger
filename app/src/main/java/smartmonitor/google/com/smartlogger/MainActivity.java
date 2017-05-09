package smartmonitor.google.com.smartlogger;

/**
 * Created by jihun.im on 2017-04-25.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends Activity {

    /**
     * An intent for launching the system settings.
     */
    private static final Intent sSettingsIntent =
            new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        startActivity(sSettingsIntent);
        finish();

        //TODO File로 저장하게하기, 카카오톡삭제버튼도 기록하기, 삭제버튼 전후내용 인식되게만들기,
    }

}