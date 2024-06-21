package com.example.reminder.di

import android.content.Context
import androidx.room.Room
import com.example.reminder.data.database.AppDatabase
import com.example.reminder.data.database.dao.AppDao
import com.example.reminder.permissions.PermissionsApi
import com.example.reminder.permissions.PermissionsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PermissionsModule {
    @Provides
    fun providePermissionsApi(@ApplicationContext appContext: Context): PermissionsApi {

        return PermissionsImpl(appContext)
    }
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "location_db"
        ).build()
    }

    @Provides
    fun provideAppDao(database: AppDatabase): AppDao {
        return database.appDao()
    }

}
