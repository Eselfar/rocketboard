package com.remiboulier.rocketboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.remiboulier.rocketboard.screen.home.HomeFragment
import com.remiboulier.rocketboard.screen.home.TestItem
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainActivityCallback, HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var testItem: TestItem

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingFragmentInjector

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

        testItem.alive()
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
