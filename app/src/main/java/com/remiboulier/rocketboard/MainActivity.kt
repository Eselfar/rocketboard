package com.remiboulier.rocketboard

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.remiboulier.rocketboard.extension.displayErrorDialog
import com.remiboulier.rocketboard.screen.home.HomeFragment
import com.remiboulier.rocketboard.util.DialogContainer
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), MainActivityCallback {

    private val container: DialogContainer = DialogContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fix an issue with TLS 1.2 on pre-Lollipop versions
        // see: https://medium.com/tech-quizlet/working-with-tls-1-2-on-android-4-4-and-lower-f4f5205629a
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            installTls12()
        }

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

    private fun Context.installTls12() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            // Prompt the user to install/update/enable Google Play services.
            GoogleApiAvailability.getInstance()
                    .showErrorNotification(this, e.connectionStatusCode)
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates a non-recoverable error: let the user know.
            container.displayErrorDialog(
                    this,
                    R.string.error_google_play_services,
                    MaterialDialog.SingleButtonCallback { _, _ -> this@MainActivity.finish() })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        container.dismissDialog()
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
