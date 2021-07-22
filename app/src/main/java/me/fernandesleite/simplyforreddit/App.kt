package me.fernandesleite.simplyforreddit

import android.app.Application
import android.util.Log
import me.fernandesleite.simplyforreddit.di.adaptersModule
import me.fernandesleite.simplyforreddit.di.mainModule
import me.fernandesleite.simplyforreddit.util.MalformedJsonInterceptor
import net.dean.jraw.RedditClient
import net.dean.jraw.android.*
import net.dean.jraw.http.LogAdapter
import net.dean.jraw.http.SimpleHttpLogger
import net.dean.jraw.oauth.AccountHelper
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*

class App : Application() {


    companion object {
        lateinit var accountHelper: AccountHelper
            private set
        lateinit var tokenStore: SharedPreferencesTokenStore
            private set
    }

    override fun onCreate() {
        super.onCreate()
        initTokenStore()
        initAccountHelper()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(mainModule, adaptersModule)
        }
    }

    private fun initTokenStore() {
        tokenStore = SharedPreferencesTokenStore(applicationContext)
        tokenStore.load()
        tokenStore.autoPersist = true
    }

    private fun initAccountHelper() {
        val provider: AppInfoProvider = ManifestAppInfoProvider(applicationContext)
        val deviceUUID = UUID.randomUUID()

        // quick Interceptor fix to remove icon_url NPE from searchSubredditsByName Results
        val httpClient: OkHttpClient =
            OkHttpClient().newBuilder().addInterceptor(MalformedJsonInterceptor()).build()

        accountHelper = AndroidHelper.accountHelper(provider, deviceUUID, tokenStore, httpClient)
        accountHelper.onSwitch { redditClient: RedditClient ->
            val logAdapter: LogAdapter = SimpleAndroidLogAdapter(Log.INFO)
            redditClient.logger = SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter)
        }
    }
}