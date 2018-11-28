package com.remiboulier.rocketboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.remiboulier.rocketboard.screen.home.HomeFragment

class MainActivity : AppCompatActivity(), MainActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Prevent the fragment to be recreated when the state is restored
        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                    .commit()
        }
    }

    override fun goToFragment(fragment: Fragment) {
        val fragmentName = fragment.javaClass.simpleName
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, fragmentName)
                .addToBackStack(fragmentName)
                .commit()
    }
}
