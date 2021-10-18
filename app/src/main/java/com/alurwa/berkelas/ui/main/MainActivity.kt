package com.alurwa.berkelas.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alurwa.berkelas.R
import com.alurwa.berkelas.databinding.ActivityMainBinding
import com.alurwa.berkelas.ui.login.LoginActivity
import com.alurwa.berkelas.util.setupToolbar
import com.alurwa.common.model.onSuccess
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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

    private var titleJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!viewModel.isLogged) {
            navigateToLogin()
            finish()
        }

        setContentView(binding.root)

        binding.toolbar.setupToolbar(this, "")
        setupBottomNavigation()
        observeRoom()
        observeUser()
        observeToolbarTitle()
    }

    private fun setupBottomNavigation() {
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.homeFragment, R.id.accountFragment),
        )

        binding.bottomNavMain.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
//            val title = when (destination.id) {
//                R.id.homeFragment -> viewModel.room.value.roomName
//                R.id.accountFragment -> "Account"
//                else -> ""
//            }
//
//            supportActionBar?.title = title
            viewModel.setDestinationId(destination.id)
        }
    }

    private fun observeRoom() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeRoom.collectLatest { result ->
                    result.onSuccess {
                        val roomData = it

                        if (roomData != null) {
                            viewModel.setRoom(it)
                        }
                    }
                }
            }
        }
    }

    private fun observeUser() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUser.collectLatest { result ->
                    result.onSuccess {
                        val data = it
                        if (data != null) {
                            viewModel.setUser(
                                data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun observeToolbarTitle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.destinationId.collectLatest { collect ->
                    when (collect) {
                        R.id.homeFragment -> {
                            titleJob?.cancel()
                            titleJob = null
                            titleJob = launch {
                                viewModel.room.collectLatest {
                                    supportActionBar?.title = it.roomName
                                    Timber.d("Iki nested seng title")
                                }
                            }
                            titleJob?.invokeOnCompletion {
                                Timber.d("title canntre")
                            }

                        }
                        R.id.accountFragment -> {
                            supportActionBar?.title = "Account"
                        }
                    }
                }
            }
        }
    }

    private fun isLoggedListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoggedListener.collectLatest { result ->
                    result.onSuccess {
                        if (!it) {
                            finish()
                        }
                    }
                }
            }
        }
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
            .setTitle(getString(R.string.title_signout))
            .setMessage(R.string.message_signout)
            .setPositiveButton(R.string.btn_sign_out) { _: DialogInterface, _: Int ->
                signOut()
                navigateToLogin()
                finish()
            }
            .setNegativeButton(R.string.btn_cancel) { d, _ ->
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
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}