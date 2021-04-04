package com.emamagic.android_peermessanger.util

import com.emamagic.android_peermessanger.BuildConfig

object Constants {

    const val DB_NAME = "MY_DB"
    const val MODE_TOAST_SUCCESS = 1
    const val MODE_TOAST_WARNING = 2
    const val MODE_TOAST_ERROR = 3

    const val INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect"
    const val NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel"
    const val INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID + ".MainActivity"
    const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001

}