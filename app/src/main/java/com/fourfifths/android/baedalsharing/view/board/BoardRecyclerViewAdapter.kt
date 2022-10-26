package com.fourfifths.android.baedalsharing.view.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.fourfifths.android.baedalsharing.data.remote.model.board.CommentDataModel
import com.fourfifths.android.baedalsharing.databinding.ItemBoardBinding
import com.fourfifths.android.baedalsharing.databinding.ItemCommentBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BoardRecyclerViewAdapter(private val board: Board) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<CommentDataModel>()

    private val VIEW_TYPE_BOARD = 0
    private val VIEW_TYPE_COMMENT = 1

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(comment: CommentDataModel) {
                binding.tvContent.text = comment.content
                binding.tvDate.text = SimpleDateFormat(
                    "yyyy/MM/dd HH:mm",
                    Locale.getDefault()
                ).format(comment.timestamp.toDate())
                binding.tvNickname.text = comment.nickname
            }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CommentViewHolder) {
            holder.bind(items[position])
        } else if(holder is BoardViewHolder) {
            holder.bind(board)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_BOARD -> {
                val binding = ItemBoardBinding.inflate(layoutInflater, parent, false)
                BoardViewHolder(binding)
            } else -> {
                val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
                CommentViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_BOARD else VIEW_TYPE_COMMENT
    }

    fun addComments(comments: MutableList<CommentDataModel>) {
        items.clear()
        items.add(CommentDataModel("", "", Timestamp.now(), ""))
        items.addAll(comments)
    }
}