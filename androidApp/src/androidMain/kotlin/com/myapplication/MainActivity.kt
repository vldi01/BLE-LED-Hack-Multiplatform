package com.myapplication

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import core.PermissionsManager
import core.PermissionsManagerState
import core.PermissionsManagerState.*
import ui.PermissionsScreen

class MainActivity : AppCompatActivity() {
    private val permissionsManager = PermissionsManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            when(permissionsManager.state.collectAsState().value) {
                Granted -> MainView()
                NotGranted -> PermissionsScreen(permissionsManager)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        permissionsManager.checkForPermissions()
    }
}