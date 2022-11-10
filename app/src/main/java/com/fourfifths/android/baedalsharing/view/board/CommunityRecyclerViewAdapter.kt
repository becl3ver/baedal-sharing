package com.fourfifths.android.baedalsharing.view.board

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.data.firebase.model.community.Post
import com.fourfifths.android.baedalsharing.databinding.ItemLoadingBinding
import com.fourfifths.android.baedalsharing.databinding.ItemPostBinding
import kotlin.collections.ArrayList

class CommunityRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Post?>()
    private val VIEW_TYPE_BOARD = 1
    private val VIEW_TYPE_LOADING = 2

    inner class BoardButtonViewHolder(private val binding: ItemPostBinding) :
        PostViewHolder(binding) {
        override fun bind(post: Post) {
            super.bind(post)

            binding.clContainer.setOnClickListener {
                val intent = Intent(context, PostActivity::class.java)
                intent.putExtra("board", post)
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
                val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
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

    fun addBoards(posts: MutableList<Post>) {
        if(items.isNotEmpty()) {
            items.removeLast()
        }

        items.addAll(posts)
        items.add(null)
        notifyDataSetChanged()
    }

    fun removeBoard() {
        val position = items.size
        items.removeLast()
        notifyItemRemoved(position)
    }
}