package com.fourfifths.android.baedalsharing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.fourfifths.android.baedalsharing.databinding.ItemBoardBinding
import com.fourfifths.android.baedalsharing.databinding.ItemLoadingBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommunityRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Board?>()
    private val VIEW_TYPE_BOARD = 1
    private val VIEW_TYPE_LOADING = 2
    private val CATEGORY_NAME = arrayOf("구해요", "질문", "자유", "추천")

    inner class BoardViewHolder(private val binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Board) {
            binding.tvCategory.text = CATEGORY_NAME[board.category.toInt()]
            binding.tvTitle.text = board.title
            binding.tvContent.text = board.content
            binding.tvNickname.text = board.nickname
            binding.tvDate.text = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(board.timestamp.toDate())
        }
    }

    inner class LoadingViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_BOARD -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBoardBinding.inflate(layoutInflater, parent, false)
                BoardViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] != null) VIEW_TYPE_BOARD else VIEW_TYPE_LOADING
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BoardViewHolder) {
            holder.bind(items[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addBoards(boards: MutableList<Board>) {
        items.addAll(boards)
        items.add(null)
    }

    fun deleteProgressBar() {
        items.removeAt(items.size - 1)
    }
}