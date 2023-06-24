package com.example.dogdiary.ui_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogdiary.R
import com.example.dogdiary.adapter.DiaryListAdapter
import com.example.dogdiary.model.DogViewModel
import com.example.dogdiary.model.DiaryViewModel.DiaryViewModelFactory
import com.example.dogdiary.data.DogApplication
import com.example.dogdiary.model.DiaryViewModel
import com.example.dogdiary.databinding.FragmentDogDiaryListBinding

class DogDiaryListFragment: Fragment() {

    private val sharedViewModel: DogViewModel by activityViewModels()
    //diary view model 객체
    private val viewModel: DiaryViewModel by activityViewModels {
        DiaryViewModelFactory(
            (activity?.application as DogApplication).database.diaryDao(), sharedViewModel
        )
    }

    private var _binding: FragmentDogDiaryListBinding? = null
    //lateinit var diary: Diary //Diary db에서 가져옴
    private val binding get() = _binding!!
//    private val sharedViewModel: DogViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDogDiaryListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DiaryListAdapter{
            val action =
                DogDiaryListFragmentDirections.actionDogDiaryListFragmentToDiaryDetailFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.allDiaries.observe(this.viewLifecycleOwner) { diaries ->
            diaries.let {
                adapter.submitList(it)
            }
        }
    }

    fun cancelOrder(){
        findNavController().navigate(R.id.action_dogDiaryListFragment_to_startMenuFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}