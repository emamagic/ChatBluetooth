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


    const val MESSAGE_STATE_CHANGED = 0
    const val MESSAGE_READ = 1
    const val MESSAGE_WRITE = 2
    const val MESSAGE_DEVICE_NAME = 3
    const val MESSAGE_TOAST = 4

    const val DEVICE_NAME = "deviceName"
    const val TOAST = "toast"

    const val STATE_NONE = 0
    const val STATE_LISTEN = 1
    const val STATE_CONNECTING = 2
    const val STATE_CONNECTED = 3


}