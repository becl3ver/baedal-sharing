package com.fourfifths.android.baedalsharing.view.board

import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.data.remote.model.board.Board
import com.fourfifths.android.baedalsharing.databinding.ItemBoardBinding

open class BoardViewHolder(private val binding: ItemBoardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val CATEGORY_NAME = arrayOf("구함", "질문", "자유", "추천")

    open fun bind(board: Board) {
        binding.tvCategory.text = CATEGORY_NAME[board.category.toInt()]
        binding.tvTitle.text = board.title
        binding.tvContent.text = board.content
        binding.tvNickname.text = board.nickname
        binding.tvDate.text = board.date
    }
}