package com.fourfifths.android.baedalsharing.view.board

import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.data.firebase.model.community.Post
import com.fourfifths.android.baedalsharing.databinding.ItemPostBinding

open class PostViewHolder(private val binding: ItemPostBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val CATEGORY_NAME = arrayOf("구함", "질문", "자유", "추천")

    open fun bind(post: Post) {
        binding.tvCategory.text = CATEGORY_NAME[post.category.toInt()]
        binding.tvTitle.text = post.title
        binding.tvContent.text = post.content
        binding.tvNickname.text = post.nickname
        binding.tvDate.text = post.date
    }
}