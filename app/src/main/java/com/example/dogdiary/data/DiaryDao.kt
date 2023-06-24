package com.example.dogdiary.data

import android.content.ClipData
import kotlinx.coroutines.flow.flow
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {

    @Query("SELECT * from diary")
    fun getDiaries(): Flow<List<Diary>>

    @Query("SELECT * from diary WHERE id = :id")
    fun getDiary(id: Int): Flow<Diary>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(diary: Diary)

    @Update
    suspend fun update(diary: Diary)

    @Delete
    suspend fun delete(diary: Diary)

//    @Delete
//    suspend fun clear()
}
