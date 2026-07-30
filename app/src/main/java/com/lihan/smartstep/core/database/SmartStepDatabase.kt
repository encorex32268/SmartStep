package com.lihan.smartstep.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        DailyStepEntity::class
    ],
    version = 1
)
abstract class SmartStepDatabase: RoomDatabase(){
    abstract fun dailyStepDao(): DailyStepsDao
}