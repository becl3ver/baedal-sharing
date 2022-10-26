package com.fourfifths.android.baedalsharing.view.board

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.fourfifths.android.baedalsharing.databinding.ItemBoardBinding
import com.fourfifths.android.baedalsharing.databinding.ItemLoadingBinding
import kotlin.collections.ArrayList

class CommunityRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Board?>()
    private val VIEW_TYPE_BOARD = 1
    private val VIEW_TYPE_LOADING = 2

    inner class BoardButtonViewHolder(private val binding: ItemBoardBinding) :
        BoardViewHolder(binding) {
        override fun bind(board: Board) {
            super.bind(board)

            binding.clContainer.setOnClickListener {
                val intent = Intent(context, BoardViewActivity::class.java)
                intent.putExtra("board", board)
                context.startActivity(intent)
            }
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
                BoardButtonViewHolder(binding)
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
        if (holder is BoardButtonViewHolder) {
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