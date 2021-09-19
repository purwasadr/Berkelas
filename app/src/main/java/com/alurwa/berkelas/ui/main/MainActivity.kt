package com.alurwa.berkelas.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityMainBinding
import com.alurwa.berkelas.ui.login.LoginActivity
import com.alurwa.berkelas.util.setupToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val navController by lazy(LazyThreadSafetyMode.NONE) {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment

        navHostFragment.navController
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!viewModel.isLogged) {
            navigateToLogin()
            finish()
        }

        setContentView(binding.root)

        binding.toolbar.setupToolbar(this, "")

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.homeFragment, R.id.accountFragment),
        )

        binding.bottomNavMain.setupWithNavController(navController)
    }

    private fun navigateToLogin() {
        Intent(this, LoginActivity::class.java)
            .also {
                startActivity(it)
            }
    }

    private fun signOut() {
        viewModel.signOut()
    }

    private fun doSignOut() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Sign Out")
            .setMessage("Yakin ingin sign out?")
            .setPositiveButton("Sign Out"){ d: DialogInterface, i: Int ->
                signOut()
                d.dismiss()
            }
            .setNegativeButton("Batal") { d, i ->
                d.dismiss()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out -> {
                doSignOut()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}