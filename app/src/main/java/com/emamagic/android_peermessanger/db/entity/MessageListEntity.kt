package com.emamagic.android_peermessanger.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_list")
data class MessageListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)