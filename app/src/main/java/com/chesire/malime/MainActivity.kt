package com.chesire.malime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    private val sharedPref: SharedPref by lazy { SharedPref(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.activity_main_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            val selectedFragment = when (item.itemId) {
                R.id.menu_main_navigation_anime -> AnimeFragment.newInstance()
                R.id.menu_main_navigation_manga -> MangaFragment.newInstance()
                else -> SearchFragment.newInstance()
            }

            supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_frame, selectedFragment)
                    .commit()

            true
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_frame, AnimeFragment.newInstance())
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_options_view_profile) {
            CustomTabsIntent.Builder()
                    .build()
                    .launchUrl(this, Uri.parse("https://myanimelist.net/profile/${sharedPref.getUsername()}"))
            return true
        } else if (item?.itemId == R.id.menu_options_log_out) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.options_log_out)
                    .setMessage(R.string.log_out_confirmation)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, { _, _ ->
                        sharedPref.clearLoginDetails()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    })
                    .show()

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
