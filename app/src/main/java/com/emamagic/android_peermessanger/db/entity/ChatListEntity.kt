package com.emamagic.android_peermessanger.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_list")
data class ChatListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)
