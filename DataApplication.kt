package com.example.project_appmovie

import android.app.Application
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DataApplication: Application()
//{
//
//    companion object {
//        lateinit var myDatabase: MyDatabase
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        myDatabase = Room.databaseBuilder(
//            applicationContext,
//            MyDatabase::class.java,
//            MyDatabase.DATABASE_NAME
//        )
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//}