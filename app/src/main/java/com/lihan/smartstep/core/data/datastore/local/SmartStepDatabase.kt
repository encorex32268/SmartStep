package com.lihan.smartstep.core.data.datastore.local

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