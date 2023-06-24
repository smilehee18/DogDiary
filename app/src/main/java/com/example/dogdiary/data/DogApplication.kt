package com.example.dogdiary.data

import android.app.Application
import com.example.dogdiary.data.DiaryDatabase

class DogApplication : Application() {
    val database: DiaryDatabase by lazy { //처음 접근할 때 초기화하겠다.
        DiaryDatabase.getDatabase(this)
    }
}