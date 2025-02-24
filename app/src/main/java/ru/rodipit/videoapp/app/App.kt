package ru.rodipit.videoapp.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.rodipit.database.di.databaseModule
import ru.rodipit.main_screen.di.mainScreenModule
import ru.rodipit.utils.di.utilsModule
import ru.rodipit.video_screen.di.videoScreenModule
import ru.rodipit.video_service.di.videoServiceModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                mainScreenModule,
                videoScreenModule,
                videoServiceModule,
                databaseModule,
                utilsModule,
            )
        }
    }
}