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
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedPreferenceModule = module {

    single(named("users")) { androidApplication().getSharedPreferences("duplicateUsers", 0) }
    single(named("filter")) { androidApplication().getSharedPreferences("filter",0) }

    single(named("lounge")) { androidApplication().getSharedPreferences("lounge",0) }

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

    single<UserLocalDataSource>
    { UserLocalDataSourceImpl(get(), get(named("users")), get(named("filter")), get(named("lounge"))) }
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
    viewModel { FilterViewModel(get()) }

    viewModel { ChatViewModel(get(),get()) }
    viewModel { DetailProfileViewModel(get(),get()) }
    viewModel { RoomViewModel(get()) }

}





