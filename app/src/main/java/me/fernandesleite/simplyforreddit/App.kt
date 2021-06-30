package me.fernandesleite.simplyforreddit

import android.app.Application
import android.util.Log
import net.dean.jraw.RedditClient
import net.dean.jraw.android.*
import net.dean.jraw.http.LogAdapter
import net.dean.jraw.http.SimpleHttpLogger
import net.dean.jraw.oauth.AccountHelper
import java.util.*

class App : Application(){

    private lateinit var tokenStore: SharedPreferencesTokenStore

    companion object {
        lateinit var accountHelper: AccountHelper
        private set
    }

    override fun onCreate() {
        super.onCreate()
        initTokenStore()
        initAccountHelper()
    }

    private fun initTokenStore() {
        tokenStore = SharedPreferencesTokenStore(applicationContext)
        tokenStore.load()
        tokenStore.autoPersist = true
    }

    private fun initAccountHelper() {
        val provider: AppInfoProvider = ManifestAppInfoProvider(applicationContext)
        val deviceUUID = UUID.randomUUID()
        accountHelper = AndroidHelper.accountHelper(provider, deviceUUID, tokenStore)
        accountHelper.onSwitch { redditClient: RedditClient ->
            val logAdapter: LogAdapter = SimpleAndroidLogAdapter(Log.INFO)
            redditClient.logger = SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter)
        }}
}