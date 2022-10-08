package com.fourfifths.android.baedalsharing.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.fourfifths.android.baedalsharing.App
import com.fourfifths.android.baedalsharing.databinding.FragmentChattingBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import io.getstream.chat.android.ui.message.MessageListActivity

class ChattingFragment : Fragment() {
    private var _binding: FragmentChattingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChattingBinding.inflate(inflater, container, false)

        val id = FirebaseAuthUtils.getUid()
        val name = App.prefs.getChatName()
        val user = User(
            id = id!!,
            name = name!!
        )

        val token = App.client.devToken(id)
        App.client.connectUser(
            user = user,
            token = token
        ).enqueue {
            if (it.isSuccess) {
                App.client.createChannel(
                    channelType = "messaging",
                    channelId = "channel",
                    memberIds = listOf(user.id),
                    extraData = mapOf()
                ).enqueue()
            }
        }

        val viewModelFactory = ChannelListViewModelFactory()
        val viewModel: ChannelListViewModel by viewModels { viewModelFactory }
        viewModel.bindView(binding.channelListView, this)

        binding.channelListView.setChannelItemClickListener { channel ->
            startActivity(
                Intent(context, MessageListActivity::class.java)
                    .putExtra("key:cid", channel.cid)
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}