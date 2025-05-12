package com.example.factsnumberstask.main.sl

import android.content.Context
import com.example.factsnumberstask.numbers.data.cache.CacheModule
import com.example.factsnumberstask.numbers.data.cloud.CloudModule
import com.example.factsnumberstask.numbers.presentation.DispatchersList
import com.example.factsnumberstask.numbers.presentation.ManageResources

interface Core : CloudModule, CacheModule, ManageResources {

    fun provideDispatchers(): DispatchersList

    class Base(
        context: Context,
        private val isRelease: Boolean
    ) : Core {

        private val manageResources: ManageResources = ManageResources.Base(context)

        private val dispatchersList by lazy {
            DispatchersList.Base()
        }
        private val cloudModule by lazy {
            if (isRelease)
                CloudModule.Release()
            else
                CloudModule.Debug()
        }

        private val cacheModule by lazy {
            if (isRelease)
                CacheModule.Base(context)
            else
                CacheModule.Mock(context)//inMemoryDatabaseBuilder (dont save when kill proc)
        }

        override fun <T> service(clasz: Class<T>): T = cloudModule.service(clasz)

        override fun provideDataBase() = cacheModule.provideDataBase()

        override fun string(id: Int) = manageResources.string(id)

        override fun provideDispatchers() = dispatchersList

    }
}