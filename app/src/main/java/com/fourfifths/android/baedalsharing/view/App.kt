package com.fourfifths.android.baedalsharing.view

import android.app.Application
import com.fourfifths.android.baedalsharing.BuildConfig
import com.fourfifths.android.baedalsharing.utils.PreferenceUtils
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory

class App : Application() {
    companion object {
        lateinit var prefs: PreferenceUtils
        lateinit var client: ChatClient
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtils(applicationContext)

        val offlinePluginFactory = StreamOfflinePluginFactory(
            config = io.getstream.chat.android.offline.plugin.configuration.Config(),
            appContext = applicationContext
        )

        client = ChatClient.Builder(BuildConfig.STREAM_CHAT_API_KEY, applicationContext)
            .withPlugin(offlinePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()
    }
}