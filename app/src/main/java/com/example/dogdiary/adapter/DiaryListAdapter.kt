package com.example.dogdiary.adapter

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dogdiary.data.Diary
import com.example.dogdiary.databinding.DiaryListDiaryBinding

class DiaryListAdapter(private val onItemClicked: (Diary) -> Unit) :
    ListAdapter<Diary, DiaryListAdapter.DiaryViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        return DiaryViewHolder(
            DiaryListDiaryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class DiaryViewHolder(private var binding: DiaryListDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(diary: Diary) {
            binding.apply{
                binding.feed.text = diary.feedTotal.toString()
                binding.hour.text = diary.walkHour.toString()
                binding.minute.text = diary.walkMinute.toString()
                binding.bowel.text = diary.bowelsCount.toString()
                binding.memo.text = diary.memo
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Diary>() {
            override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
                //수정
                return oldItem === newItem
            }
            override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
                //수정
                return oldItem.memo == newItem.memo
            }
        }
    }

}