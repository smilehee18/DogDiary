package com.example.dogdiary.ui_fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dogdiary.R
import com.example.dogdiary.databinding.FragmentStartMenuBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [StartMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartMenuFragment : Fragment() {
    private var _binding: FragmentStartMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var callback: OnBackPressedCallback
    var mBackWait:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStartMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //일기 쓰기 버튼 클릭시
        binding.dogDiary.setOnClickListener{
            //findNavController().navigate(R.id.action_startMenuFragment_to_dogDiaryFragment)
            //처음 일기 쓸 때는 ID값을 0으로 일부러 전달 -> 수정하는 단계가 아니라는 정보 전달
            val action = StartMenuFragmentDirections.actionStartMenuFragmentToDogDiaryFragment(diaryId = 0)
            this.findNavController().navigate(action)
        }
        //강아지 종류 검색 버튼 클릭시
        binding.dogSearch.setOnClickListener{
            findNavController().navigate(R.id.action_startMenuFragment_to_dogListFragment)
        }

        //지난 일기 보기 버튼 클릭시
        binding.dogRecord.setOnClickListener{
            findNavController().navigate(R.id.action_startMenuFragment_to_dogDiaryListFragment)
        }
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 뒤로가기 기능 구현
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if(System.currentTimeMillis() - mBackWait >=2000) {
                    mBackWait = System.currentTimeMillis()
                    Snackbar.make(binding.root, R.string.exit ,Snackbar.LENGTH_LONG).show()
                } else
                    requireActivity().finish()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}