package com.omersusin.steppy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : androidx.appcompat.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.pager)
        val tabs = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabs)
        pager.adapter = TabsAdapter(this)
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.setText(TABS[position].first)
        }.attach()

        requestStepPermissionIfNeeded()
        startStepCounterService()
    }

    private fun requestStepPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permission = Manifest.permission.ACTIVITY_RECOGNITION
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), REQUEST_ACTIVITY_RECOGNITION)
            }
        }
    }

    private fun startStepCounterService() {
        val intent = Intent(this, com.omersusin.steppy.core.service.StepCounterService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private class TabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = TABS.size
        override fun createFragment(position: Int) = TABS[position].second.getConstructor().newInstance()
    }

    private companion object {
        val TABS = listOf(
            R.string.tab_summary to com.omersusin.steppy.summary.SummaryFragment::class.java,
            R.string.tab_map to MapFragment::class.java,
            R.string.tab_stats to StatsFragment::class.java,
            R.string.tab_settings to com.omersusin.steppy.settings.SettingsFragment::class.java,
        )
        const val REQUEST_ACTIVITY_RECOGNITION = 1
    }
}

class MapFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(inflater.context).apply {
            setText(R.string.map_placeholder)
            gravity = Gravity.CENTER
        }
    }
}

class StatsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(inflater.context).apply {
            setText(R.string.stats_placeholder)
            gravity = Gravity.CENTER
        }
    }
}
