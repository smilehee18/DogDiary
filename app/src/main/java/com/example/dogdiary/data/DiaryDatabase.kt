package com.example.dogdiary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Diary::class], version = 1, exportSchema = false)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao() : DiaryDao

    companion object {
        @Volatile //캐시에 있는 것들을 메모리에 옮겨서 처리해서 동기화를 수행한다.
        private var INSTANCE: DiaryDatabase? = null

        fun getDatabase(context: Context): DiaryDatabase {
            return INSTANCE ?: synchronized(this){ //instance가 null이 아니면 바로 리턴하고 null이면 코드가 수행된다.
                //여러 쓰레드가 이 부분을 동시에 접근할 수 있어서 충돌방지 -> synchronized에 의해 방지함
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}