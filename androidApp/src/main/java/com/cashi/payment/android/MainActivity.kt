package com.cashi.payment.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cashi.payment.android.data.Screen
import com.cashi.payment.android.ui.theme.CashiPaymentTheme
import com.cashi.payment.android.ui.screen.PaymentScreen
import com.cashi.payment.android.ui.screen.TransactionHistoryScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashiPaymentTheme {
                CashiPaymentApp()
            }
        }
    }
}

@Composable
fun CashiPaymentApp() {
    val navController = rememberNavController()
    val screens = listOf(Screen.Payment, Screen.History)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.semantics { contentDescription = screen.title }  // ← ADDED: Test tag
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Payment.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Payment.route) { PaymentScreen() }
            composable(Screen.History.route) { TransactionHistoryScreen() }
        }
    }
}
