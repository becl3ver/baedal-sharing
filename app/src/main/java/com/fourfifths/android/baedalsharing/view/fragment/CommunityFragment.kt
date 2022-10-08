package com.fourfifths.android.baedalsharing.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.fourfifths.android.baedalsharing.databinding.FragmentCommunityBinding
import com.fourfifths.android.baedalsharing.view.activity.NewBoardActivity
import com.fourfifths.android.baedalsharing.view.adapter.CommunityRecyclerViewAdapter
import com.fourfifths.android.baedalsharing.viewmodel.MainViewModel

class CommunityFragment : Fragment() {
    private lateinit var adapter: CommunityRecyclerViewAdapter
    private val viewModel: MainViewModel by activityViewModels()

    private var cnt = 1
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        viewModel.initBoards()

        binding.rvCommunity.apply {
            binding.rvCommunity.layoutManager =  LinearLayoutManager(context)
            adapter = CommunityRecyclerViewAdapter()
            binding.rvCommunity.adapter = adapter
        }

        viewModel.boards.observe(viewLifecycleOwner, Observer {
            val position = adapter.itemCount
            adapter.addBoards(it)
            adapter.notifyItemRangeInserted(position, adapter.itemCount - position)
        })

        binding.rvCommunity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val position = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                if(!binding.rvCommunity.canScrollVertically(1) && position == recyclerView.adapter!!.itemCount - 1) {
                    adapter.deleteProgressBar()
                    viewModel.loadBoards()
                }
            }
        })

        binding.btnNewBoard.setOnClickListener {
            // TODO : 커스텀 alertdialog
            startActivity(Intent(context, NewBoardActivity::class.java).putExtra("category", 1))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}