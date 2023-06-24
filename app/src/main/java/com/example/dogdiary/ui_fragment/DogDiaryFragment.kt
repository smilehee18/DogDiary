package com.example.dogdiary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dogdiary.data.Diary
import com.example.dogdiary.data.DogApplication
import com.example.dogdiary.databinding.FragmentDogDiaryBinding
import com.example.dogdiary.model.DiaryViewModel
import com.example.dogdiary.model.DogViewModel
import com.example.dogdiary.ui_fragment.DiaryDetailFragmentArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * A simple [Fragment] subclass.
 * Use the [DogDiaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class DogDiaryFragment: Fragment() {

    private var _binding: FragmentDogDiaryBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: DogViewModel by activityViewModels()
    val navigationArgs :DiaryDetailFragmentArgs by navArgs()
    //private val TAG ="dogdiary"
    //Diary 항목 불러오기
    lateinit var diary: Diary
    //val navigationArgs: DiaryDetailFragmentArgs by navArgs()


    //viewModel 업데이트 -> 여기가 다이어리 모델
    val diaryViewModel: DiaryViewModel by activityViewModels{
        DiaryViewModel.DiaryViewModelFactory(
            (activity?.application as DogApplication).database.diaryDao(), sharedViewModel
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDogDiaryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    //onViewCreated 메소드 -> 디테일에서 일기쓰기로 넘어갈 때 무조건 불리는 함수
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //전달인자를 받습니다
        //id는 일기를 처음쓸때/ 그리고 디테일에서 -> 일기로 돌아올 때 2가지 경우 존재, 따라서 0보다 크거나 아예 없거나
        val id = navigationArgs.diaryId
        //다시 수정하러 돌아올때의 경우
        if (id  > 0) {
            diaryViewModel.retrieveDiary(id).observe(this.viewLifecycleOwner) { selectedItem ->
                diary = selectedItem
                bind(diary) //diary 값을 뽑아내서 bind함수를 호출
            }
        } //처음 일기 쓰러 왔을 때의 경우
        else {
            binding.saveButton.setOnClickListener {saveDiary()}
            sharedViewModel.resetOrder()
        }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel //공유모델은 DogViewModel
            dogDiaryFragment = this@DogDiaryFragment
        }
    }

    //bind함수 원래 값을 복원해주는 역할
    private fun bind(diary: Diary)
    {
        binding.apply {
            total.text = diary.feedTotal.toString()
            walkHourName.setText(diary.walkHour.toString(), TextView.BufferType.SPANNABLE)
            walkMinuteName.setText(diary.walkMinute.toString(),TextView.BufferType.SPANNABLE)
            bowelsCountName.setText(diary.bowelsCount.toString(),TextView.BufferType.SPANNABLE)
            diaryName.setText(diary.memo,TextView.BufferType.SPANNABLE)
            sharedViewModel.save()
            sharedViewModel.setTotalAgain(diary.feedTotal)
        }
    }

//    //로그 찍어서 ID 제대로 전달되는지 확인했음
//    fun logging(){
//        val navigationArgs: DiaryDetailFragmentArgs by navArgs()
//        //Log.v(TAG, navigationArgs.diaryId.toString())
//    }

    fun cancelOrder() {
        //sharedViewModel.save()
        findNavController().navigate(R.id.action_dogDiaryFragment_to_startMenuFragment)
    }

    fun saveDiary() {
        //내 아이디가 0보다 크다면 -> 즉 수정하는 단계라면 updateDiary를 호출해줘
        if(navigationArgs.diaryId > 0)
        {
            updateDiary()
            return //그리고 함수를 끝낸다.
        }
        sharedViewModel.save()
        //아무것도 입력받지 않았을 때
        if(sharedViewModel.walkminute.value == 0 && sharedViewModel.walkhour.value == 0 && sharedViewModel.feedgram.value == 0.0
            && sharedViewModel.feedcount.value == 0 && sharedViewModel.memo.value == "")
        {
            showConfirmationDialog()
            return //함수 끝냄.
        }
        else //제대로 입력받으면
        {
            showToastbar(1) //토스트바 띄우는 함수
        }
        diaryViewModel.addNewDiary() //다이어리 모델
        sharedViewModel.resetOrder()
        findNavController().navigate(R.id.action_dogDiaryFragment_to_dogDiaryListFragment)
    }

    fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.Iamnull))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->  }
            .show()
    }

    fun showToastbar(type:Int)
    {
        if(type == 1)
        {
            Toast.makeText(
                context, R.string.saving, Toast.LENGTH_SHORT).show()
        }
        else if(type == 2)
        {
            Toast.makeText(
                context, R.string.editing, Toast.LENGTH_SHORT).show()
        }
    }


    //일기 수정시에만 호출되는 함수
    fun updateDiary() {
        //입력된 값을 viewModel에 저장하고
        sharedViewModel.save()
        //값들을 아무것도 입력받지 않았을 때.
        if(sharedViewModel.walkminute.value == 0 && sharedViewModel.walkhour.value == 0 && sharedViewModel.feedgram.value == 0.0
            && sharedViewModel.feedcount.value == 0 && sharedViewModel.memo.value == "")
        {
            showConfirmationDialog()
            return //함수 끝냄.
        }
        //5개 값들을 diary 뷰모델의 updateDiay를 호출하여 넘겨준다
        diaryViewModel.updateDiary(navigationArgs.diaryId, sharedViewModel.total.value!!, sharedViewModel.walkhour.value!!, sharedViewModel.walkminute.value!!,
        sharedViewModel.bowelscount.value!!, sharedViewModel.memo.value!!)
        //logging()
        //수정되었습니다 메시지 띄우는 함수 호출
        showToastbar(2)
        //이 부분!! 넘어갈 때 id를 넘겨주어야 무슨 항목을 넘겨주는지 DiaryListFragment에서 파악이 가능함
        val action = DogDiaryFragmentDirections.actionDogDiaryFragmentToDogDiaryListFragment(diaryId = navigationArgs.diaryId)
        findNavController().navigate(action)
    }

    fun recordDiary()
    {
        findNavController().navigate(R.id.action_dogDiaryFragment_to_dogDiaryListFragment)
    }
}

