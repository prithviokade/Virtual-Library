package com.example.virtuallibrary;

/*
Modified from: https://github.com/AgoraIO/Basic-Video-Call
 */

import android.app.Application;
import android.content.res.Resources;

public class RtcTokenGenerator extends Application {
    public static String appId;
    public static String appCertificate;
    public static String channelName;
    public static String userAccount = "";
    public static int uid = 0;
    public static int expirationTimeInSeconds = 3600;
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        res = getResources();
        appId = res.getString(R.string.agora_app_id);
        appCertificate = res.getString(R.string.agora_app_certificate);
    }

    public static String getToken(String name) {
        channelName = name;
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUserAccount(appId, appCertificate,
                channelName, userAccount, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);

        result = token.buildTokenWithUid(appId, appCertificate,
                channelName, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        return result;
    }
}

