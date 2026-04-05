package com.example.xpensate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.xpensate.features.add_expenses.AddExpense
import com.example.xpensate.features.home.HomeScreen
import com.example.xpensate.features.onboarding.OnboardingScreen
import com.example.xpensate.features.splash.SplashScreen
import com.example.xpensate.features.stats.StatsScreen
import com.example.xpensate.ui.theme.Zinc

@Composable

fun NavHostScreen() {
    val navController = rememberNavController()
    var bottomBarVisibility by remember { mutableStateOf(true) }

    Scaffold(bottomBar = {
        AnimatedVisibility(visible = bottomBarVisibility) {
            NavigationBottomBar(
                navController = navController,
                items = listOf(
                    NavItem("home_screen", R.drawable.ic_home),
                    NavItem("stats_screen", R.drawable.ic_stats)
                )
            )
        }
    }) {
        NavHost(navController= navController, startDestination = "splash",modifier = Modifier.padding(it)) {
            // 🔹 Splash
            composable("splash") {
                bottomBarVisibility = false
                SplashScreen {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }

            // 🔹 Onboarding
            composable("onboarding") {
                bottomBarVisibility = false
                OnboardingScreen {
                    navController.navigate("home_screen") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            }

            composable("home_screen") {
                bottomBarVisibility = true
                HomeScreen(navController)
            }
            composable("add_expense_screen") {
                bottomBarVisibility = false
                AddExpense(navController)
            }
            composable("stats_screen") {
                bottomBarVisibility = true
                StatsScreen(navController)
            }
        }
    }
}

data class NavItem(
    val route: String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items : List<NavItem>
) {
    //bottom navigation bar implementation
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar{
        items.forEach{
            NavigationBarItem(
                icon = {
                    Icon(painterResource(id = it.icon), contentDescription = null)
                },
                selected = currentRoute == it.route,
                onClick = {
                    navController.navigate(it.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Zinc,
                    selectedIconColor = Zinc,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray,
                )
            )
        }
    }

}