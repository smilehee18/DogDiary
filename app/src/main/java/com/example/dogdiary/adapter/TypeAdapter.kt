package com.example.dogdiary.adapter
//강아지들의 종류 별 정보(소형견, 중형견, 대형견)에 따라 목록의 정보를 넘겨준다.
//프래그먼트 간 탐색 및 데이터 이동 (기능1번 구현)

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dogdiary.data.DogTypeList
import com.example.dogdiary.R
import com.example.dogdiary.ui_fragment.TypeListFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.job
import kotlin.coroutines.coroutineContext

class TypeAdapter:RecyclerView.Adapter<TypeAdapter.ItemViewHolder>(){

      //강아지 타입 목록(소형, 중형, 대형, 토종)
      private val typeList = DogTypeList

    class ItemViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val button: Button = view.findViewById(R.id.button_item)
    }

    override fun getItemCount() = typeList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
              val adapterLayout  = LayoutInflater
                  .from(parent.context)
                  .inflate(R.layout.item_view, parent, false)

              //adapterLayout.accessibilityDelegate = SearchAdapter.Accessibility
              return ItemViewHolder(adapterLayout)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int){
             val item = typeList.get(position)
             holder.button.setText(typeList[position])

             holder.button.setOnClickListener{
                 val action = TypeListFragmentDirections.actionDogListFragmentToDogListFragment2(type = item)
                 Snackbar.make(holder.button, R.string.button, Snackbar.LENGTH_LONG).show()
                 holder.view.findNavController().navigate(action)
             }
        }


}