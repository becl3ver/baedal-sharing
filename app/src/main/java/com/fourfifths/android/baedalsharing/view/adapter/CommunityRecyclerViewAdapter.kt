package com.fourfifths.android.baedalsharing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.fourfifths.android.baedalsharing.databinding.ItemBoardBinding

class CommunityRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Board>()
    private val VIEW_TYPE_BOARD = 1
    private val VIEW_TYPE_LOADING = 2

    inner class BoardViewHolder(private val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        i
    }

    inner class LoadingViewHolder(private val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_BOARD -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBoardBinding.inflate(layoutInflater, parent, false)
                BoardViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBoardBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is BoardViewHolder) {

        } else if(holder is LoadingViewHolder) {

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addBoards(boards: ArrayList<Board>) {
        items.addAll(boards)
    }

    fun deleteProgressBar() {
        items.removeAt(items.size - 1)
    }
}