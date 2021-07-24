package me.fernandesleite.simplyforreddit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.fernandesleite.simplyforreddit.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()
    }

    fun openDrawer() = findViewById<DrawerLayout>(R.id.drawerLayout).open()
    fun closeDrawer() = findViewById<DrawerLayout>(R.id.drawerLayout).close()

    private fun setUpNavigation() {
        val nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(nav, navHostFragment!!.findNavController())

    }


}