package com.versec.versecko.view.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.versec.versecko.data.datasource.local.UserLocalDataSource
import com.versec.versecko.data.datasource.local.UserLocalDataSourceImpl
import com.versec.versecko.data.datasource.remote.UserRemoteDataSource
import com.versec.versecko.data.datasource.remote.UserRemoteDataSourceImpl
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.data.repository.UserRepositoryImpl
import com.versec.versecko.data.room.AppDatabase
import com.versec.versecko.data.room.UserEntityDAO
import com.versec.versecko.viewmodel.SignInViewModel
import com.versec.versecko.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roomDatabaseModule = module {

    single { Room.databaseBuilder(
        androidApplication(),
        AppDatabase::class.java,
        "userDB"
    ).build()

    }

    single<UserEntityDAO> {
        val roomDB = get<AppDatabase>()
        roomDB.userEntityDao()
    }
}

val userRemoteDataSourceModule = module {

    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }

}

val repositoryModule = module {

    single<UserLocalDataSource> { UserLocalDataSourceImpl(get()) }
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }


}

/**

val getUserUseCaseModule = module {

    factory { GetUserUseCase(get()) }
}**/

val userViewModelModule = module {

    viewModel { UserViewModel(get()) }
    viewModel { SignInViewModel(get()) }
}

val signInModule = module {

}




