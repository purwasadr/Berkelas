package com.alurwa.berkelas.util

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.BsdItemImageBinding
import com.alurwa.berkelas.model.BsdItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Class ini digunakan untuk membuat BotttomSheetDialog yang memiliki gambar
 */
class BottomSheetDialogListImage(
    context: Context,
    val bsdList: List<BsdItem>
) : BottomSheetDialog(context) {

    var listView: ListView

    init {

        val view = layoutInflater.inflate(R.layout.bsd_lv, null)

        listView = view.findViewById(R.id.lv)

        val arrayAdapter = BottomSheetListAdapter()

        listView.adapter = arrayAdapter

        setContentView(view)

        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun setOnItemClick(listener: (dialog: DialogInterface, position: Int) -> Unit): BottomSheetDialogListImage {
        listView.setOnItemClickListener { _, _, position, _ ->
            listener.invoke(this, position)
        }

        return this
    }

    inner class BottomSheetListAdapter() : BaseAdapter() {
        override fun getCount(): Int {
            return bsdList.size
        }

        override fun getItem(position: Int): Any {
            return bsdList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return bsdList.get(position).hashCode().toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView

            val binding: BsdItemImageBinding

            if (view == null) {
                binding = BsdItemImageBinding.inflate(LayoutInflater.from(context))
                view = binding.root
            }

            view.findViewById<ImageView>(R.id.img).setImageResource(bsdList[position].drawableRes)
            view.findViewById<TextView>(R.id.txt1).text = bsdList[position].text

            return view
        }

    }
}

