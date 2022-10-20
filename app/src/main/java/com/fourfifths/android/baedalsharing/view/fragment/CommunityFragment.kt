package com.fourfifths.android.baedalsharing.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.CategoryDialog
import com.fourfifths.android.baedalsharing.CategoryDialogInterface
import com.fourfifths.android.baedalsharing.databinding.FragmentCommunityBinding
import com.fourfifths.android.baedalsharing.view.activity.NewBoardActivity
import com.fourfifths.android.baedalsharing.view.adapter.CommunityRecyclerViewAdapter
import com.fourfifths.android.baedalsharing.viewmodel.CommunityViewModel

class CommunityFragment : Fragment(), CategoryDialogInterface {
    private lateinit var adapter: CommunityRecyclerViewAdapter
    private val viewModel: CommunityViewModel by activityViewModels()

    private val TAG = CommunityFragment::class.java.simpleName

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private var isEnd = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        initRecyclerView()

        viewModel.initBoards(1)

        viewModel.boards.observe(viewLifecycleOwner, Observer {
            val position = adapter.itemCount
            adapter.addBoards(it)
            adapter.notifyItemRangeInserted(position, adapter.itemCount - position)
        })

        binding.rvCommunity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                if (!binding.rvCommunity.canScrollVertically(1) && position == recyclerView.adapter!!.itemCount - 1 && !isEnd) {
                    adapter.deleteProgressBar()

                    if (!viewModel.loadMoreBoards(1)) {
                        isEnd = true
                        Toast.makeText(context, "마지막 글입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        binding.btnNewBoard.setOnClickListener {
            val dialog = CategoryDialog(this)
            dialog.show(activity?.supportFragmentManager!!, "CategoryDialog")
        }

        return binding.root
    }

    override fun onCategoryButtonOnClick(category: Int) {
        val intent = Intent(context, NewBoardActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        binding.rvCommunity.layoutManager = LinearLayoutManager(context)
        adapter = CommunityRecyclerViewAdapter()
        binding.rvCommunity.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}