package com.example

import android.app.Application
import com.example.blocker.AndroidBlockingService
import com.example.blocker.BlockingService
import com.example.data.local.FocusForgeDatabase
import com.example.data.repository.FocusForgeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FocusForgeApplication : Application() {

    lateinit var database: FocusForgeDatabase
        private set

    lateinit var repository: FocusForgeRepository
        private set

    lateinit var blockingService: BlockingService
        private set

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = FocusForgeDatabase.getDatabase(this)
        repository = FocusForgeRepository(database)
        blockingService = AndroidBlockingService(this)

        applicationScope.launch {
            repository.seedDefaultsIfEmpty()
        }
    }

    companion object {
        var instance: FocusForgeApplication? = null
            private set
    }
}
