package com.emamagic.android_peermessanger.bluetooth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat
import com.emamagic.android_peermessanger.R
import com.emamagic.android_peermessanger.util.Constants
import java.io.IOException
import java.util.*

class SerialService : Service(), SerialListener {
    internal inner class SerialBinder : Binder() {
        val service: SerialService
            get() = this@SerialService
    }

    private enum class QueueType {
        Connect, ConnectError, Read, IoError
    }

    private inner class QueueItem(
        var type: QueueType,
        var data: ByteArray?,
        var e: Exception?
    )

    private val mainLooper: Handler = Handler(Looper.getMainLooper())
    private val binder: IBinder
    private val queue1: Queue<QueueItem>
    private val queue2: Queue<QueueItem>
    private var socket: SerialSocket? = null
    private var listener: SerialListener? = null
    private var connected = false

    init {
        binder = SerialBinder()
        queue1 = LinkedList()
        queue2 = LinkedList()
        }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }


    @Throws(IOException::class)
    fun connect(socket: SerialSocket) {
        socket.connect(this)
        this.socket = socket
        connected = true
    }

    fun disconnect() {
        connected = false
        cancelNotification()
        if (socket != null) {
            socket?.disconnect()
            socket = null
        }
    }

    @Throws(IOException::class)
    fun write(data: ByteArray?) {
        if (!connected) throw IOException("not connected")
        socket?.write(data)
    }

    fun attach(listener: SerialListener) {
        // throw exception
        require(!(Looper.getMainLooper().thread !== Thread.currentThread())) { "not in main thread" }
        cancelNotification()
        synchronized(this) { this.listener = listener }
        for (item in queue1) {
            when (item.type) {
                QueueType.Connect -> listener.onSerialConnect()
                QueueType.ConnectError -> listener.onSerialConnectError(item.e)
                QueueType.Read -> listener.onSerialRead(item.data)
                QueueType.IoError -> listener.onSerialIoError(item.e)
            }
        }
        for (item in queue2) {
            when (item.type) {
                QueueType.Connect -> listener.onSerialConnect()
                QueueType.ConnectError -> listener.onSerialConnectError(item.e)
                QueueType.Read -> listener.onSerialRead(item.data)
                QueueType.IoError -> listener.onSerialIoError(item.e)
            }
        }
        queue1.clear()
        queue2.clear()
    }

    fun detach() {
        if (connected) createNotification()
        listener = null
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nc = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL,
                "Background service",
                NotificationManager.IMPORTANCE_LOW
            )
            nc.setShowBadge(false)
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(nc)
        }
        val disconnectIntent = Intent()
            .setAction(Constants.INTENT_ACTION_DISCONNECT)
        val restartIntent = Intent()
            .setClassName(this, Constants.INTENT_CLASS_MAIN_ACTIVITY)
            .setAction(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
        val disconnectPendingIntent =
            PendingIntent.getBroadcast(this, 1, disconnectIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val restartPendingIntent =
            PendingIntent.getActivity(this, 1, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(resources.getColor(R.color.design_default_color_primary))
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText(if (socket != null) "Connected to " + socket?.name else "Background Service")
            .setContentIntent(restartPendingIntent)
            .setOngoing(true)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_clear_white_24dp,
                    "Disconnect",
                    disconnectPendingIntent
                )
            )
        val notification = builder.build()
        startForeground(Constants.NOTIFY_MANAGER_START_FOREGROUND_SERVICE, notification)
    }

    private fun cancelNotification() {
        stopForeground(true)
    }

    override fun onSerialConnect() {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialConnect()
                        } else {
                            queue1.add(QueueItem(QueueType.Connect, null, null))
                        }
                    }
                } else {
                    queue2.add(QueueItem(QueueType.Connect, null, null))
                }
            }
        }
    }

    override fun onSerialConnectError(e: Exception?) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialConnectError(e)
                        } else {
                            queue1.add(QueueItem(QueueType.ConnectError, null, e))
                            cancelNotification()
                            disconnect()
                        }
                    }
                } else {
                    queue2.add(QueueItem(QueueType.ConnectError, null, e))
                    cancelNotification()
                    disconnect()
                }
            }
        }
    }

    override fun onSerialRead(data: ByteArray?) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialRead(data)
                        } else {
                            queue1.add(QueueItem(QueueType.Read, data, null))
                        }
                    }
                } else {
                    queue2.add(QueueItem(QueueType.Read, data, null))
                }
            }
        }
    }

    override fun onSerialIoError(e: Exception?) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialIoError(e)
                        } else {
                            queue1.add(QueueItem(QueueType.IoError, null, e))
                            cancelNotification()
                            disconnect()
                        }
                    }
                } else {
                    queue2.add(QueueItem(QueueType.IoError, null, e))
                    cancelNotification()
                    disconnect()
                }
            }
        }
    }

    override fun onDestroy() {
        cancelNotification()
        disconnect()
        super.onDestroy()
    }
}