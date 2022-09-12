package com.example.crypto_analytics.ui.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.CryptoWorkManager
import com.example.crypto_analytics.data.util.ExitDialog
import com.example.crypto_analytics.data.util.ExitDialog.Companion.EXIT_DIALOG_TAG
import com.example.crypto_analytics.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.sql.Time
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var navController: NavController

    lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolBar()

        navController = findNavController(R.id.nav_host)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.mainDrawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navTooldrawView.setupWithNavController(navController)

        binding.navTooldrawView.setNavigationItemSelectedListener(navigationSideBarListener)

        setWorkManager()
    }


    private fun setWorkManager() {
        val workRequest = PeriodicWorkRequestBuilder<CryptoWorkManager>(
            16, TimeUnit.MINUTES)
//            5, TimeUnit.MINUTES)
            .setConstraints(Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            )
            .build()

       val workManager = WorkManager.getInstance(applicationContext)
       workManager.enqueueUniquePeriodicWork(Constants.WORKMANAGER_UNIQUE_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }

    private fun setToolBar() {
        setSupportActionBar(binding.mainToolbar)

        toggle = ActionBarDrawerToggle(this, binding.mainDrawerLayout, binding.mainToolbar, R.string.open, R.string.close)
        binding.mainDrawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        binding.navTooldrawView.setNavigationItemSelectedListener {
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val navigationSideBarListener = NavigationView.OnNavigationItemSelectedListener { menu ->
        when(menu.itemId) {
            R.id.side_menu_wallet -> {}
            R.id.side_menu_notification -> {}
            else -> {
                val exitDialog = ExitDialog()
                exitDialog.show(supportFragmentManager, EXIT_DIALOG_TAG)
            }
        }
        return@OnNavigationItemSelectedListener true
    }

    override fun onBackPressed() {
        if (appBarConfiguration.openableLayout?.isOpen == true) {
            appBarConfiguration.openableLayout?.close()
        } else {
            super.onBackPressed()
            Log.d("TEEST", navController.currentDestination!!.displayName)
        }
    }

}
