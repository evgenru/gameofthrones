package ru.skillbranch.gameofthrones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
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

        viewModel
    }
}
