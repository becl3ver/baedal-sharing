package com.fourfifths.android.baedalsharing.view.board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourfifths.android.baedalsharing.databinding.FragmentCommunityBinding
import com.fourfifths.android.baedalsharing.viewmodel.CommunityViewModel

class CommunityFragment : Fragment(), CategoryDialogInterface {
    private val TAG = CommunityFragment::class.simpleName

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CommunityRecyclerViewAdapter
    private val viewModel: CommunityViewModel by activityViewModels()

    private var category = 0
    private var isLoading = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        initRecyclerView()
        initObserver()
        initScrollListener()

        viewModel.initBoards(category)

        binding.btnNewBoard.setOnClickListener {
            val dialog = CategoryDialog(this)
            dialog.show(activity?.supportFragmentManager!!, "CategoryDialog")
        }

        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvCommunity.layoutManager = LinearLayoutManager(context)
        adapter = CommunityRecyclerViewAdapter(requireContext())
        binding.rvCommunity.adapter = adapter
    }

    private fun initObserver() {
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.addBoards(it)
            isLoading = false
        })

        viewModel.isLastItemVisible.observe(viewLifecycleOwner, Observer {
            if(it) {
                adapter.removeBoard()
                Toast.makeText(context, "마지막 글입니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initScrollListener() {
        binding.rvCommunity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(isLoading) {
                    return
                }

                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                if (!binding.rvCommunity.canScrollVertically(1) && position == recyclerView.adapter!!.itemCount - 1) {
                    isLoading = true
                    viewModel.loadMoreBoards(category)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCategoryButtonClick(category: Long) {
        val intent = Intent(context, NewPostActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }
}