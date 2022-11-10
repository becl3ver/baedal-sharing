package com.fourfifths.android.baedalsharing.view.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.data.firebase.model.community.Comment
import com.fourfifths.android.baedalsharing.data.firebase.model.community.Post
import com.fourfifths.android.baedalsharing.databinding.ItemCommentBinding
import com.fourfifths.android.baedalsharing.databinding.ItemPostBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostRecyclerViewAdapter(private val post: Post) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Comment>()

    private val VIEW_TYPE_BOARD = 0
    private val VIEW_TYPE_COMMENT = 1

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(comment: Comment) {
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
        } else if(holder is PostViewHolder) {
            holder.bind(post)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_BOARD -> {
                val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
                PostViewHolder(binding)
            } else -> {
                val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
                CommentViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_BOARD else VIEW_TYPE_COMMENT
    }

    fun addComments(comments: MutableList<Comment>) {
        items.clear()
        items.add(Comment("","", "", Timestamp.now(), ""))
        items.addAll(comments)
    }
}