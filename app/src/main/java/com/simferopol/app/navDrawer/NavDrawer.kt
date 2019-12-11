package com.simferopol.app.navDrawer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.simferopol.app.R
import com.simferopol.app.utils.ui.CustomActivity
import java.io.File

class NavDrawer : CustomActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_map, R.id.nav_routes, R.id.nav_monuments,
                R.id.nav_info_marks, R.id.nav_category, R.id.nav_history,
                R.id.nav_about, R.id.nav_ar, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val downloadManager =
                    p0?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val action = p1?.action
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                    var downloadId = p1?.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0
                    )
                    val query = DownloadManager.Query()
                    query.setFilterById(downloadId)
                    val cursor = downloadManager.query(query)
                    cursor?.let {
                        if (it.moveToFirst()) {
                            val columnIndex = it
                                .getColumnIndex(DownloadManager.COLUMN_STATUS)
                            if (DownloadManager.STATUS_SUCCESSFUL == it
                                    .getInt(columnIndex)
                            ) {
                                val uriString = it
                                    .getString(
                                        it
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                                    )
                                val uri = Uri.parse(uriString)
                                val loaded = File(uri.path)
                                val dest =
                                    File(p0.filesDir.toString() + "/downloads/" + loaded.name)
                                loaded.copyTo(dest, true)
                                loaded.delete()
                            }
                        }
                    }
                }
            }
        }
        registerReceiver(
            receiver, IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
