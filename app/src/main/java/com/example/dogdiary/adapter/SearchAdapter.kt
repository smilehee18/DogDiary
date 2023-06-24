package com.example.dogdiary.adapter
//각 강아지들의 특징 및 질병 정보를 리스트화해서 보여준다
//이때 인텐트를 넘겨주는 어댑터입니다.
//탐색 및 프래그먼트 간 데이터 전달시에 사용합니다(9주차 내용)

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dogdiary.data.*
import com.example.dogdiary.R
import com.example.dogdiary.ui_fragment.DogListFragment


/**
 * Adapter for the [RecyclerView] in [MainActivity].
 */
class SearchAdapter(private val dogType:String, context: Context) :
     RecyclerView.Adapter<SearchAdapter.DogViewHolder>() {
    //
    //    //타입 종류에 따라 강아지 리트를 분리하여 나누어줌
    val dogList:List<String> = when(dogType)
    {
        DogTypeList[0] -> smallDogList
        DogTypeList[1] -> middleDogList
        DogTypeList[2] -> BigDogList
        else -> KoreanDogList
    }

    /**
     * Provides a reference for the views needed to display items in your list.
     */
    class DogViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.button_item)
    }

    override fun getItemCount(): Int {
        return dogList.size
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder
    {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        // Setup custom accessibility delegate to set the text read
        layout.accessibilityDelegate = Accessibility
        return DogViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        val item = dogList.get(position) //리스트의 인덱스 값을 뽑아와서
        val context = holder.view.context
        holder.button.text = item //뷰홀더의 텍스를 item값으로 지정한다

        // Assigns a [OnClickListener] to the button contained in the [ViewHolder]
        holder.button.setOnClickListener {
            val queryUrl: Uri = Uri.parse("${DogListFragment.SEARCH_PREFIX}${item}")
            // Create an intent with a destination of DetailActivity
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)// Add the selected letter to the intent as extra data
            // The text of Buttons are [CharSequence], a list of characters,
            // so it must be explicitly converted into a [String].
            //intent.putExtra(DogListFragment.LETTER, holder.button.text.toString())
            // Start an activity using the data and destination from the Intent.
            context.startActivity(intent)
        }
    }

    // Setup custom accessibility delegate to set the text read with
    // an accessibility service
    companion object Accessibility : View.AccessibilityDelegate() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onInitializeAccessibilityNodeInfo(
            host: View,
            info: AccessibilityNodeInfo
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            // With `null` as the second argument to [AccessibilityAction], the
            // accessibility service announces "double tap to activate".
            // If a custom string is provided,
            // it announces "double tap to <custom string>".
            val customString = host.context?.getString(R.string.look_up_dogs)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info.addAction(customClick)
        }
    }
}