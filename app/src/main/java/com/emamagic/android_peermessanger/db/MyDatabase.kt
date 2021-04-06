package com.emamagic.android_peermessanger.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emamagic.android_peermessanger.db.entity.ChatListEntity
import com.emamagic.android_peermessanger.db.entity.MessageListEntity

@Database(entities = [ChatListEntity::class ,MessageListEntity::class] ,version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun getDao(): MyDao
}