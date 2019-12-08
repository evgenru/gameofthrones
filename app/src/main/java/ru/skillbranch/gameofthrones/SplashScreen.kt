package ru.skillbranch.gameofthrones

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash_screen.*
import ru.skillbranch.gameofthrones.viewmodels.Done
import ru.skillbranch.gameofthrones.viewmodels.Error
import ru.skillbranch.gameofthrones.viewmodels.Progress
import ru.skillbranch.gameofthrones.viewmodels.SplashViewModel

class SplashScreen : AppCompatActivity() {

    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()

        viewModel.data.observe(this, Observer {
            when (it) {
                is Progress -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Error -> {
                    progressBar.visibility = View.GONE
                    Snackbar.make(
                        progressBar,
                        R.string.loading_error,
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
                is Done -> {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = 100
                    startActivity(
                        Intent(this, CharactersListScreen::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        }
                    )
                    finish()
                }
            }
        })
    }
}
