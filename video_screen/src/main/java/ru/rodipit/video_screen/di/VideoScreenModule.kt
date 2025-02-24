package ru.rodipit.video_screen.di

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val videoScreenModule = module {

    factory<Player> {
        ExoPlayer
            .Builder(androidContext())
            .build()
    }
}