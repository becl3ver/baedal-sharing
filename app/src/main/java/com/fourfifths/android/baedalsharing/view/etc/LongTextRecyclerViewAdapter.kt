package com.fourfifths.android.baedalsharing.view.etc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.databinding.ItemLongTextBinding

class LongTextRecyclerViewAdapter(private val items: ArrayList<LongText>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLongTextBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    inner class ItemViewHolder(private val binding: ItemLongTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(longText: LongText) {
            binding.tvTitle.text = longText.title
            binding.tvContent.text = longText.content
            binding.tvSubTitle.text = longText.subtitle
        }
    }
}