package com.emamagic.android_peermessanger.di

import android.content.Context
import androidx.room.Room
import com.emamagic.android_peermessanger.db.MyDatabase
import com.emamagic.android_peermessanger.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideMyDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context ,MyDatabase::class.java ,Constants.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMyDao(myDatabase: MyDatabase) =
        myDatabase.getDao()

}