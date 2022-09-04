package com.versec.versecko

import android.app.Application
import com.versec.versecko.view.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

class AppContext : Application() {

    companion object{
        lateinit var appContext : Application

        var latitude by Delegates.notNull<Double>()
        var longitude by Delegates.notNull<Double>()


        fun getInstance() : Application {

            return AppContext.appContext
        }


    }

    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidLogger()
            androidContext(this@AppContext)
            modules(roomDatabaseModule, userRemoteDataSourceModule, repositoryModule, userViewModelModule, signInModule)
        }
    }
}