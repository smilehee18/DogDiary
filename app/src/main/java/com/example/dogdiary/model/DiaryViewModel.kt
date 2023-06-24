package com.example.dogdiary.model

import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.fragment.navArgs
import com.example.dogdiary.data.Diary
import com.example.dogdiary.data.DiaryDao
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModelProvider

class DiaryViewModel(private val diaryDao: DiaryDao, private var dogViewModel: DogViewModel) : ViewModel() {
    init {
        this.dogViewModel = dogViewModel
    }

    class DiaryViewModelFactory(private val diaryDao: DiaryDao, private val dogViewModel: DogViewModel) : ViewModelProvider.Factory {
        //객체 인스턴스 생성 위한 팩토리 클래스
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DiaryViewModel(diaryDao, dogViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    val _total = dogViewModel.total
    val _walkhour = dogViewModel.walkhour
    val _walkminute = dogViewModel.walkminute
    val _bowelscount = dogViewModel.bowelscount
    val _memo = dogViewModel.memo


    val allDiaries: LiveData<List<Diary>> = diaryDao.getDiaries().asLiveData()
    private val TAG ="diarymodel"


    fun updateDiary(id:Int, total:Double, walkhour:Int, walkminute:Int, bowlscount:Int, memo:String)
    {
        val updatedDiary = getUpdatedDiaryEntry(id, total, walkhour, walkminute, bowlscount, memo)
        updateDiary(updatedDiary)
    }

    private fun updateDiary(diary: Diary) {
        viewModelScope.launch {
            diaryDao.update(diary)
            Log.v(TAG, diary.memo)
        }
    }

    fun addNewDiary() {
        val newDiary = getNewDiaryEntry()
        insertDiary(newDiary)
    }

    private fun insertDiary(diary: Diary) {
        viewModelScope.launch {
            diaryDao.insert(diary)
        }
    }

    fun deleteDiary(diary: Diary) {
        viewModelScope.launch {
            diaryDao.delete(diary)
        }
    }

    fun retrieveDiary(id: Int): LiveData<Diary> {
        return diaryDao.getDiary(id).asLiveData()
    }

    private fun getNewDiaryEntry(): Diary {
        return Diary(
            feedTotal = _total.value!!,
            walkHour = _walkhour.value!!,
            walkMinute = _walkminute.value!!,
            bowelsCount = _bowelscount.value!!,
            memo = _memo.value!!
        )
    }

    fun getUpdatedDiaryEntry(id:Int, total:Double, walkhour:Int, walkminute: Int, bowlscount: Int, memo: String):Diary
    {
        //여기서 id를 지정해주어야만 정상적으로 update가 되었음
        return Diary(
            id = id,
            feedTotal = total,
            walkHour = walkhour,
            walkMinute = walkminute,
            bowelsCount =  bowlscount,
            memo = memo
        )
    }
}
