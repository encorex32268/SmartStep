package com.lihan.smartstep.stepcount.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    exportSchema = false,
    entities = [
        DailyStepEntity::class
               ],
    version = 1
)
abstract class SmartStepDatabase: RoomDatabase(){
    abstract val dailyStepDao: DailyStepDao
}