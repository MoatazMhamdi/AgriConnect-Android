package tn.sim5.agriconnect.ui


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast


import tn.sim5.agriconnect.R
import tn.sim5.agriconnect.ViewModels.EquipmentListActivity
import tn.sim5.agriconnect.utils.SessionManager


class MainScreen : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val logoutButton = navigationView.menu.findItem(R.id.nav_item_six)

        sessionManager = SessionManager(this)

        if (sessionManager.isAuthenticated()) {
            logoutButton.isVisible = true
        } else {
            logoutButton.isVisible = false
        }

        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> {
                val intent = Intent(this, EquipmentListActivity::class.java)
                startActivity(intent)


            }
            R.id.nav_item_two -> {
                val intent = Intent(this, BlogListActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_item_three -> {
                val intent = Intent(this, Profile::class.java)
                startActivity(intent)
            }
            // Handle other menu items
            R.id.nav_item_six -> {
                sessionManager.clearSession()
                if (!sessionManager.isAuthenticated()) {
                    // Session cleared successfully, display a message or log a statement
                    println("Session cleared successfully")
                    Toast.makeText(this, "User Logged Out Succefuly", Toast.LENGTH_SHORT).show()
                    // Redirect to the login page
                    startActivity(Intent(this, login::class.java))
                    finish() // Close the current activity to prevent going back to the profile screen using the back button
                } else {
                    // Session not cleared, display an error message or log an error statement
                    println("Error clearing session")
                }
                startActivity(Intent(this, login::class.java))
                finish()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



}
