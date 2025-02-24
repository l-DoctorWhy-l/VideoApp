package ru.rodipit.videoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import ru.rodipit.core.navigation.Screen
import ru.rodipit.core.snackbar.SnackbarController
import ru.rodipit.design.theme.AppTheme
import ru.rodipit.main_screen.api.MainScreenWrapper
import ru.rodipit.utils.compose.ObserveAsEvents
import ru.rodipit.video_screen.api.VideoScreenWrapper

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val snackbarHostState = remember {
                    SnackbarHostState()
                }
                val scope = rememberCoroutineScope()

                ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = event.name,
                            duration = SnackbarDuration.Short,
                        )
                    }
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                ) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Main,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(8.dp),
                    ) {
                        composable<Screen.Main> {
                            MainScreenWrapper(
                                navController = navController,
                            )
                        }

                        composable<Screen.Video> {
                            val args = it.toRoute<Screen.Video>()
                            VideoScreenWrapper(
                                videoId = args.id,
                                navController = navController,
                            )
                        }
                    }

                }
            }
        }
    }
}
