package com.example.dogdiary.ui_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dogdiary.R
import com.example.dogdiary.data.Diary
import com.example.dogdiary.data.DogApplication
import com.example.dogdiary.databinding.FragmentDiaryDetailBinding
import com.example.dogdiary.model.DiaryViewModel
import com.example.dogdiary.model.DogViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DiaryDetailFragment: Fragment() {

    private val navigationArgs: DiaryDetailFragmentArgs by navArgs()
    private val sharedViewModel: DogViewModel by activityViewModels()
    lateinit var diary: Diary //Diary db에서 가져옴

    //diary view 모델임
    private val diaryviewModel: DiaryViewModel by activityViewModels {
        DiaryViewModel.DiaryViewModelFactory(
            (activity?.application as DogApplication).database.diaryDao(), sharedViewModel
        )
    }

    private var _binding: FragmentDiaryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(diary: Diary) {
        binding.apply {
            diaryFeed.text = diary.feedTotal.toString()
            diaryHour.text = diary.walkHour.toString()
            diaryMinute.text = diary.walkMinute.toString()
            diaryBowel.text = diary.bowelsCount.toString()
            diaryMemo.text = diary.memo
            deleteDiary.setOnClickListener { showConfirmationDialog() }
            editDiary.setOnClickListener { editDiary() }
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ -> deleteDiary() }
            .show()
    }

    private fun deleteDiary() {
        diaryviewModel.deleteDiary(diary)
        findNavController().navigateUp()
        showToastbar()
    }

    fun showToastbar()
    {
        Toast.makeText(
            context, R.string.deleting, Toast.LENGTH_SHORT).show()
    }

    //여기서도 마찬가지로 현재 diary 항목의 id 인자 넘겨주기
    private fun editDiary() {
        //id인자를 DogDiart 프래그먼트에 전달
        val action = DiaryDetailFragmentDirections.actionDiaryDetailFragmentToDogDiaryFragment(diaryId = navigationArgs.diaryId)
        this.findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.diaryId

        diaryviewModel.retrieveDiary(id).observe(this.viewLifecycleOwner) { selectedDiary ->
            diary = selectedDiary
            bind(diary)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}