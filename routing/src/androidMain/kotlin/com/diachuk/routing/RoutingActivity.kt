package com.diachuk.routing.com.diachuk.routing

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diachuk.routing.DeeplinkParams
import com.diachuk.routing.Routing
import com.diachuk.routing.RoutingHost
import com.diachuk.routing.routes.Route

abstract class RoutingActivity : ComponentActivity() {
    private var onCreateTime = 0L
    abstract val routing: Routing
    abstract val startRoute: Route<Unit>

    open fun beforeOnCreate() {}
    open fun readyToBeDrawn(onCreateTime: Long): Boolean = true
    open fun handleDeepLink(deeplink: DeeplinkParams): Route<Unit>? = null

    @Composable
    open fun OnSetContent() {
    }
    
    private fun handleDeepLink(intent: Intent): Route<Unit>? { // TODO args
        val uri = intent.dataString?.let { Uri.parse(it) } ?: return null
        val scheme = uri.scheme ?: return null
        val host = uri.host ?: return null
        val path = uri.path ?: return null
        val params: Map<String, String> = uri.queryParameterNames
            .map { it to uri.getQueryParameter(it) }
            .filter { it.second != null }
            .associate { it.first to it.second!! }

        return handleDeepLink(DeeplinkParams(scheme, host, path, params))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeOnCreate()
        onCreateTime = System.currentTimeMillis()

        routing.changeCurrent(handleDeepLink(intent) ?: startRoute)
        super.onCreate(savedInstanceState)

        setContent {
            OnSetContent()
            RoutingHost(Modifier.fillMaxSize(), routing)
        }

        // wait for splash screen animation
        val content: View = findViewById(R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return readyToBeDrawn(onCreateTime).also {
                        if (it) content.viewTreeObserver.removeOnPreDrawListener(this)
                    }
                }
            }
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent ?: return
        handleDeepLink(intent)?.let {
            routing.backStack.clear()
            routing.changeCurrent(it)
        }
    }
}