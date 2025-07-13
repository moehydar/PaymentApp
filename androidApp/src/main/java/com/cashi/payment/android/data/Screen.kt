package com.cashi.payment.android.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Payment : Screen("payment", "Send Payment", Icons.Default.Send)
    data object History : Screen("history", "History", Icons.Default.List) //couldn't find a better icon :P
}