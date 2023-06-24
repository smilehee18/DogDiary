package com.example.dogdiary.ui_fragment

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogdiary.R
import com.example.dogdiary.adapter.SearchAdapter
import com.example.dogdiary.databinding.FragmentDogList2Binding


/**
 * A simple [Fragment] subclass.
 * Use the [DogListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DogListFragment : Fragment() {

    companion object {
        val TYPE = "type"
        val SEARCH_PREFIX = "https://www.google.com/search?q=feature+of+"
    }

    private lateinit var dogType: String //소형, 대형, 중형, 토종견의 문자열 정보를 받는다.

    private var _binding: FragmentDogList2Binding? = null
    private val binding get() = _binding!! //null값이 아닌 값만 갖는다.
    private lateinit var recyclerView: RecyclerView
    private var isLinearLayoutManager = false

    /*onCreate에서 메뉴바 모이게 한다*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //argument 전달 받아서 dogType에 할당
        arguments?.let {
            dogType = it.getString(TYPE).toString()
        }
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDogList2Binding.inflate(inflater, container, false)
        return binding.root   //null이 아닌 값만 갖는다.
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        chooseLayout()
    }

    /*레이아웃 설정하는 함수*/
    private fun chooseLayout() {
        when (isLinearLayoutManager) {
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = SearchAdapter(dogType, requireContext()) //여기서 type과 context를 필요로 함
            }
            false -> {
                recyclerView.layoutManager = GridLayoutManager(context, 2)
                recyclerView.adapter = SearchAdapter(dogType, requireContext())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                // Sets isLinearLayoutManager (a Boolean) to the opposite value
                isLinearLayoutManager = !isLinearLayoutManager
                // Sets layout and icon
                chooseLayout()
                setIcon(item)

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}