package com.copcatan.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.copcatan.app.ui.auth.AuthScreen
import com.copcatan.app.ui.chat.ChatScreen
import com.copcatan.app.ui.discovery.DiscoveryScreen
import com.copcatan.app.ui.matches.MatchesScreen
import com.copcatan.app.ui.onboarding.OnboardingScreen

private const val ROUTE_AUTH = "auth"
private const val ROUTE_ONBOARDING = "onboarding"
private const val ROUTE_DISCOVERY = "discovery"
private const val ROUTE_MATCHES = "matches"
private const val ROUTE_CHAT = "chat/{matchId}"

@Composable
fun CopCatanApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROUTE_AUTH) {
        composable(ROUTE_AUTH) {
            AuthScreen(
                onAuthenticated = {
                    navController.navigate(ROUTE_ONBOARDING) {
                        popUpTo(ROUTE_AUTH) { inclusive = true }
                    }
                }
            )
        }
        composable(ROUTE_ONBOARDING) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(ROUTE_DISCOVERY) {
                        popUpTo(ROUTE_ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(ROUTE_DISCOVERY) {
            MainScaffold(navController, currentRoute = ROUTE_DISCOVERY) {
                DiscoveryScreen()
            }
        }
        composable(ROUTE_MATCHES) {
            MainScaffold(navController, currentRoute = ROUTE_MATCHES) {
                MatchesScreen(onOpenChat = { matchId -> navController.navigate("chat/$matchId") })
            }
        }
        composable(ROUTE_CHAT) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId") ?: ""
            ChatScreen(matchId = matchId, onBack = { navController.popBackStack() })
        }
    }
}

@Composable
private fun MainScaffold(
    navController: NavHostController,
    currentRoute: String,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == ROUTE_DISCOVERY,
                    onClick = {
                        if (currentRoute != ROUTE_DISCOVERY) {
                            navController.navigate(ROUTE_DISCOVERY) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Keşfet") },
                    label = { Text("Keşfet") }
                )
                NavigationBarItem(
                    selected = currentRoute == ROUTE_MATCHES,
                    onClick = {
                        if (currentRoute != ROUTE_MATCHES) {
                            navController.navigate(ROUTE_MATCHES) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Eşleşmeler") },
                    label = { Text("Eşleşmeler") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}
