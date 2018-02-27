package com.chesire.malime

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.activity_main_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            val selectedFragment: Fragment =
                    if (item.itemId == R.id.menu_main_navigation_anime) {
                        AnimeFragment.newInstance()
                    } else {
                        MangaFragment.newInstance()
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
}
