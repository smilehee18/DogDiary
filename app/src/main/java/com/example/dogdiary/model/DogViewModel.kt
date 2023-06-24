package com.example.dogdiary.model

import androidx.lifecycle.*
//강아지의 사료 g수, 산책 시간, 배변 횟수, 영양제 X알 Livedata정보들을 viewModel로 감싼다
//특히 사료는 g수와 횟수를 모두 사용자료부터 입력받아서 viewHolder가 처리해서 UI에 바로바로 업뎃해준다.
//따라서 화면에 g수와 횟수를 곱한 값이 바로 표시됨
//산책 시간은 2시간 30분 이런식으로 시간과 분을 입력받고
//배변 횟수는 X회 입력받는다.

class DogViewModel : ViewModel() {

    private var previousFeedGram = 0.0
    private var previousFeedCount = 0
    private var previousTotalValue = 0.0


    private var _feedgram = MutableLiveData<Double>(0.0) //사료 질량
    val feedgram:LiveData<Double?> = _feedgram

    private var _feedcount = MutableLiveData<Int>(0) //사료 준 횟수
    val feedcount: LiveData<Int?> = _feedcount

    private var _walkhour = MutableLiveData<Int>(0) //산책 시간
    val walkhour: LiveData<Int?> = _walkhour

    private var _walkminute = MutableLiveData<Int>(0) //산책 분
    val walkminute: LiveData<Int?> = _walkminute

    private var _bowelscount = MutableLiveData<Int>(0) //배변 횟수
    val bowelscount: LiveData<Int?> = _bowelscount

    private var _memo = MutableLiveData<String>("") //일기
    val memo: LiveData<String?> = _memo

    val inputGram = MutableLiveData<String>()  //사용자로부터 입력받은 사료 질량
    val inputGramCount = MutableLiveData<String>()  //입력받은 사료 횟수

    val inputHour = MutableLiveData<String>("")
    val inputMinute = MutableLiveData<String>("")

    val inputBowelsCount = MutableLiveData<String>("")

    val inputMemo = MutableLiveData<String>("")

    private val _total = MutableLiveData<Double>(0.0) //사료 질량 X 횟수 곱한 값 : 합계
    val total: LiveData<Double?> = _total


    //계산하기 버튼을 누르면 getFeedGram()함수가 호출된다
    fun getFeedGram() {
        _feedgram.value = 0.0
        var value: Double = 0.0
        if (inputGram.value == "" || inputGram.value == null)
            return
        else
            value = inputGram.value!!.toDouble() //질량 값을 받아서 double형으로 반환할게요
        _feedgram.value =
            (_feedgram.value)?.plus(value) //처음값에 앞서 계산한 값을 더해서 그 값을 feedgram livedata에 실시간으로 저장
        getFeedCount() //getFeedCount() 함수를 호출
    }

    fun getFeedCount() {
        _feedcount.value = 0
        var value:Int = 0
        if (inputGramCount.value == "" || inputGramCount.value == null)
            return
        else
            value = inputGramCount.value!!.toInt() //위와 동일한 로직
        _feedcount.value = (_feedcount.value)?.plus(value)
        setTotalValue()
    }


    //사료 합계 계산
    //Log통해서 값 변화하는거 보기
    fun setTotalValue() {
        _total.value = 0.0
        previousTotalValue = 0.0
        previousFeedGram = 0.0
        previousFeedCount = 0
        if (_feedgram.value != 0.0 && _feedcount.value != 0) //feedcount를 아무것도 입력받지 않았을 때는 자동으로 1개로 계산하기 위해
        {
            previousFeedGram = _feedgram.value!!
            previousFeedCount = _feedcount.value!!
        }
        if (_feedgram.value != 0.0 && _feedcount.value == null)//분기를 2개로 나누어서 계산함
        {
            previousFeedCount = 1
            previousFeedGram = _feedgram.value!!
        }
        if (_total.value != null) {
            _total.value = previousFeedGram * previousFeedCount
        }
    }


    //계산 초기화
    fun resetOrder() {
        previousFeedGram = 0.0
        previousFeedCount = 0
        inputGram.value = ""
        inputGramCount.value = ""
        inputBowelsCount.value = ""
        _total.value = 0.0
        _feedgram.value = 0.0
        _feedcount.value = 0
        _walkminute.value = 0
        _walkhour.value = 0
        _bowelscount.value = 0
        _memo.value = ""
        inputHour.value = ""
        inputMinute.value =""
        inputMemo.value = ""
    }

    fun setTotalAgain(total:Double)
    {
        _total.value = total
        inputGram.value = ""
        inputGramCount.value = ""
    }


    //입력 내용 저장
    fun save() {
        _walkhour.value = 0
        _walkminute.value = 0
        _bowelscount.value = 0
        _memo.value = ""

        //null일 때 혹은 아무 문자열도 없을때를 고려하여 분기문을 나눔
        if (inputHour.value == null || inputHour.value == "")
            _walkhour.value = 0
        else
           _walkhour.value = (_walkhour.value)?.plus(inputHour.value!!.toInt())

        if (inputMinute.value == null || inputMinute.value == "")
            _walkminute.value = 0
        else
           _walkminute.value = (_walkminute.value)?.plus(inputMinute.value!!.toInt())

        if (inputBowelsCount.value == null || inputBowelsCount.value == "")
            _bowelscount.value = 0
        else
            _bowelscount.value = (_bowelscount.value)?.plus(inputBowelsCount.value!!.toInt())

        if (inputMemo.value == null || inputMemo.value == "")
            _memo.value = ""
        else
            _memo.value = inputMemo.value

    }
}