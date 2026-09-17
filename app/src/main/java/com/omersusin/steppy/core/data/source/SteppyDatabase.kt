package com.omersusin.steppy.core.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omersusin.steppy.core.data.source.util.Converters
import com.omersusin.steppy.core.domain.model.Day

@Database(entities = [Day::class], version = 1)
@TypeConverters(Converters::class)
abstract class SteppyDatabase : RoomDatabase() {

    abstract val dayDao: DayDao

    companion object {
        const val DATABASE_NAME = "steppy_database"
    }
}
