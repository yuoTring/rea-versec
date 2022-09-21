package com.versec.versecko.view.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.versec.versecko.data.datasource.local.UserLocalDataSource
import com.versec.versecko.data.datasource.local.UserLocalDataSourceImpl
import com.versec.versecko.data.datasource.remote.ChatDataSource
import com.versec.versecko.data.datasource.remote.ChatDataSourceImpl
import com.versec.versecko.data.datasource.remote.UserRemoteDataSource
import com.versec.versecko.data.datasource.remote.UserRemoteDataSourceImpl
import com.versec.versecko.data.repository.ChatRepository
import com.versec.versecko.data.repository.ChatRepositoryImpl
import com.versec.versecko.data.repository.UserRepository
import com.versec.versecko.data.repository.UserRepositoryImpl
import com.versec.versecko.data.room.AppDatabase
import com.versec.versecko.data.room.UserEntityDAO
import com.versec.versecko.viewmodel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharedPreferenceModule = module {

    single { androidApplication().getSharedPreferences("duplicateUsers", 0) }

}


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
    single { FirebaseStorage.getInstance() }

}

val chatDataSourceModule = module {

    single { Firebase.database.reference }
}

val repositoryModule = module {

    single<UserLocalDataSource> { UserLocalDataSourceImpl(get(), get()) }
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get(), get(), get()) }
    single<ChatDataSource> { ChatDataSourceImpl(get(),get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }


}

/**

val getUserUseCaseModule = module {

    factory { GetUserUseCase(get()) }
}**/

val userViewModelModule = module {

    viewModel { SignInViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ProfileModifyViewModel(get()) }
    viewModel { ImageModifyViewModel(get()) }
    viewModel { FillUserInfoViewModel(get()) }
    viewModel { FillUserImageViewModel(get()) }
    viewModel { MatchingViewModel(get()) }
    viewModel { MainViewModel(get()) }

    viewModel { ChatViewModel(get(),get()) }
    viewModel { DetailProfileViewModel(get()) }
    viewModel { RoomViewModel(get()) }

}





