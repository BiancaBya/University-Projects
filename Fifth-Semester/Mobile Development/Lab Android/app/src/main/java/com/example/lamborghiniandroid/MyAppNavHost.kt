package com.example.lamborghiniandroid

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lamborghiniandroid.auth.LoginScreen
import com.example.lamborghiniandroid.core.data.UserPreferences
import com.example.lamborghiniandroid.core.data.remote.Api
import com.example.lamborghiniandroid.core.ui.UserPreferencesViewModel
import com.example.lamborghiniandroid.todo.ui.car.CarScreen
import com.example.lamborghiniandroid.todo.ui.cars.CarsScreen

import com.example.lamborghiniandroid.location.MyLocationScreen
import com.example.lamborghiniandroid.camera.MyPhotosScreen
import com.example.lamborghiniandroid.sensor.ProximitySensorScreen
import com.example.lamborghiniandroid.animations.AnimationsHomeScreen

const val carsRoute = "cars"
const val authRoute = "auth"

const val labLocationRoute = "lab/location"
const val labCameraRoute = "lab/camera"
const val labSensorsRoute = "lab/sensors"
const val labAnimationsRoute = "lab/animations"

@Composable
fun MyAppNavHost() {
    val navController = rememberNavController()

    val prefsViewModel: UserPreferencesViewModel =
        viewModel(factory = UserPreferencesViewModel.Factory)
    val prefs by prefsViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )

    val appViewModel: MyAppViewModel = viewModel(factory = MyAppViewModel.Factory)

    val onCloseCar: () -> Unit = {
        navController.popBackStack()
    }

    NavHost(
        navController = navController,
        startDestination = authRoute
    ) {
        composable(authRoute) {
            LoginScreen(
                onClose = {
                    Log.d("MyAppNavHost", "Login success -> navigate to cars")
                    navController.navigate(carsRoute) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(carsRoute) {
            CarsScreen(
                onCarClick = { id ->
                    navController.navigate("$carsRoute/$id")
                },
                onAddCar = {
                    navController.navigate("$carsRoute-new")
                },
                onLogout = {
                    appViewModel.logout()
                    Api.tokenInterceptor.token = null
                    navController.navigate(authRoute) {
                        popUpTo(0)
                    }
                },

                onOpenLocation = { navController.navigate(labLocationRoute) },
                onOpenCamera = { navController.navigate(labCameraRoute) },
                onOpenSensors = { navController.navigate(labSensorsRoute) },
                onOpenAnimations = { navController.navigate(labAnimationsRoute) }
            )
        }

        composable(
            route = "$carsRoute/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")?.toString()
            CarScreen(
                carId = id,
                onClose = onCloseCar
            )
        }

        composable("$carsRoute-new") {
            CarScreen(
                carId = null,
                onClose = onCloseCar
            )
        }

        composable(labLocationRoute) {
            MyLocationScreen()
        }

        composable(labCameraRoute) {
            MyPhotosScreen()
        }

        composable(labSensorsRoute) {
            // poți schimba cu DeviceSensorsScreen dacă vrei lista de senzori
            ProximitySensorScreen()
        }

        composable(labAnimationsRoute) {
            AnimationsHomeScreen()
        }
    }

    LaunchedEffect(prefs.token) {
        if (prefs.token.isNotEmpty()) {
            Log.d("MyAppNavHost", "Token found -> navigate to cars")
            Api.tokenInterceptor.token = prefs.token
            appViewModel.setToken(prefs.token)
            navController.navigate(carsRoute) {
                popUpTo(0)
            }
        }
    }
}
