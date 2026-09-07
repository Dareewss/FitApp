package com.dareewss.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dareewss.myapplication.ui.screens.DashboardScreen
import com.dareewss.myapplication.ui.screens.LogMealSheet
import com.dareewss.myapplication.ui.screens.OnboardingScreen
import com.dareewss.myapplication.ui.screens.ProfileScreen
import com.dareewss.myapplication.ui.screens.StatsScreen
import com.dareewss.myapplication.ui.screens.WorkoutsScreen
import com.dareewss.myapplication.ui.theme.FitBackground
import com.dareewss.myapplication.ui.theme.FitMuted
import com.dareewss.myapplication.ui.theme.FitPrimary
import com.dareewss.myapplication.ui.theme.FitSurface
import com.dareewss.myapplication.ui.theme.Inter
import com.dareewss.myapplication.ui.theme.JetBrainsMono

private enum class FitTab { Dashboard, Workouts, Stats, Profile }

@Composable
fun FitApp(viewModel: FitViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableStateOf(FitTab.Stats) }
    var showLog by rememberSaveable { mutableStateOf(false) }
    var editing by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(FitBackground)
            .statusBarsPadding()
    ) {
        val profile = state.profile
        val metrics = state.metrics
        if (profile == null || metrics == null || editing) {
            OnboardingScreen(existing = profile) { saved ->
                viewModel.saveProfile(saved)
                editing = false
                tab = FitTab.Stats
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f)) {
                    when (tab) {
                        FitTab.Dashboard -> DashboardScreen(
                            sleepHours = profile.sleepHours,
                            metrics = metrics,
                            log = state.log,
                            completedWorkoutId = state.completedWorkoutId,
                            onLogMeal = { showLog = true },
                            onOpenWorkouts = { tab = FitTab.Workouts }
                        )
                        FitTab.Workouts -> WorkoutsScreen(state.completedWorkoutId) {
                            viewModel.completeWorkout(it)
                        }
                        FitTab.Stats -> StatsScreen(profile, metrics, state.log) { showLog = true }
                        FitTab.Profile -> ProfileScreen(profile, metrics) { editing = true }
                    }
                }
                FitBottomBar(tab) { tab = it }
            }
        }
    }

    if (showLog) {
        LogMealSheet(onDismiss = { showLog = false }) { p, c, f ->
            viewModel.logMeal(p, c, f)
        }
    }
}

@Composable
private fun FitBottomBar(current: FitTab, onSelect: (FitTab) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(FitSurface)
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 10.dp)
    ) {
        FitTab.entries.forEach { item ->
            val selected = item == current
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelect(item) }
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .width(18.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (selected) FitPrimary else FitSurface)
                )
                Spacer(Modifier.height(6.dp))
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (selected) FitPrimary else FitMuted,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.label,
                    fontFamily = if (selected) JetBrainsMono else Inter,
                    fontSize = 11.sp,
                    color = if (selected) FitPrimary else FitMuted
                )
            }
        }
    }
}

private val FitTab.icon: ImageVector
    get() = when (this) {
        FitTab.Dashboard -> Icons.Outlined.GridView
        FitTab.Workouts -> Icons.Outlined.FitnessCenter
        FitTab.Stats -> Icons.AutoMirrored.Outlined.ShowChart
        FitTab.Profile -> Icons.Outlined.Person
    }

private val FitTab.label: String
    get() = when (this) {
        FitTab.Dashboard -> "Dashboard"
        FitTab.Workouts -> "Workouts"
        FitTab.Stats -> "Stats"
        FitTab.Profile -> "Profile"
    }
