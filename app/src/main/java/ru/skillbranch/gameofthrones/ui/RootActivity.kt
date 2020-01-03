package ru.skillbranch.gameofthrones.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.splash.SplashFragmentDirections

class RootActivity : AppCompatActivity() {

    private lateinit var viewModel: RootViewModel
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        initViewModel()
        savedInstanceState ?: prepareDate()
        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
    }

    private fun prepareDate() {
        viewModel.syncDataIfNeed().observe(this, Observer {
            Log.d("RootActivity", "prepareDate: $it")
            when (it) {
                LoadResult.Loading -> navController.navigate(R.id.nav_splash)
                LoadResult.Success -> {
                    val action = SplashFragmentDirections.actionNavSplashToNavHouses()
                    navController.navigate(action)
                }
                is LoadResult.Error -> {
                    Snackbar.make(
                        root_container,
                        it.message,
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(RootViewModel::class.java)
    }
}
