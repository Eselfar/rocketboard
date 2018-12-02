package com.remiboulier.rocketboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.remiboulier.rocketboard.screen.home.HomeFragment
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), MainActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Prevent the fragment to be recreated when the state is restored
        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                            0, 0,
                            R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                    .commit()
        }
    }

    override fun goToFragment(fragment: Fragment) {
        val fragmentName = fragment.javaClass.simpleName
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container, fragment, fragmentName)
                .addToBackStack(fragmentName)
                .commit()
    }

    override fun updateToolbarTitle(newTitle: String) {
        title = newTitle
        val enabled = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Respond to the action bar's Up/Home button
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
