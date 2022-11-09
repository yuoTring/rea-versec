package com.versec.versecko.view.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.versec.versecko.data.datasource.local.UserLocalDataSource
import com.versec.versecko.data.datasource.local.UserLocalDataSourceImpl
import com.versec.versecko.data.datasource.remote.*
import com.versec.versecko.data.repository.*
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
    single(named("setting")) { androidApplication().getSharedPreferences("setting",0) }

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

val firebaseModule = module {

    single { Firebase.firestore }
    single { FirebaseAuth.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseMessaging.getInstance() }

    single { Firebase.database.reference }


}

val dataSourceModule = module {

    single<UserLocalDataSource>
    { UserLocalDataSourceImpl(get(), get(named("users")), get(named("filter")), get(named("lounge")), get(named("setting"))) }
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get(), get(), get(), get()) }
    single<RoomDataSource> { RoomDataSourceImpl(get(),get()) }
    single<StoryDataSource> { StoryDataSourceImpl(get(), get(), get())}
}

val repositoryModule = module {


    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<RoomRepository> { RoomRepositoryImpl(get()) }
    single<StoryRepository> { StoryRepositoryImpl(get())  }


}

/**

val getUserUseCaseModule = module {

    factory { GetUserUseCase(get()) }
}**/

val viewModelModule = module {

    viewModel { SignInViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProfileModifyViewModel(get()) }
    viewModel { ImageModifyViewModel(get()) }
    viewModel { FillUserInfoViewModel(get()) }
    viewModel { FillUserImageViewModel(get(),get()) }
    viewModel { MatchingViewModel(get()) }
    viewModel { DiscoveryViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { FilterViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { SettingViewModel(get()) }

    viewModel { RoomListViewModel(get(),get()) }
    viewModel { DetailProfileViewModel(get(),get()) }
    viewModel { MessageViewModel(get()) }

    viewModel { StoryUploadViewModel(get()) }
    viewModel { StoryFeedViewModel(get()) }
    viewModel { StoryDetailViewModel(get()) }

}





