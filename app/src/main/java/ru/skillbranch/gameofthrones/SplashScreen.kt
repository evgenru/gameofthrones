package ru.skillbranch.gameofthrones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
            when (it){
                is Progress -> progressBar.progress = it.progress
                is Error -> TODO()
                is Done -> TODO()
            }
        })
    }
}
