package ru.skillbranch.gameofthrones

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.skillbranch.gameofthrones.di.dataModule

class MainApplication: Application() {

    companion object {
        lateinit var instance: MainApplication
    }


    override fun onCreate() {
        super.onCreate()

        instance = this

        // DI
        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@MainApplication)

            // load properties from assets/koin.properties file
//            androidFileProperties()

            // module list
            modules(dataModule)
        }

    }
}