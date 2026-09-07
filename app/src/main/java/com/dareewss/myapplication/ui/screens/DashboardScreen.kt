package com.dareewss.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dareewss.myapplication.data.BodyMetrics
import com.dareewss.myapplication.data.MacroLog
import com.dareewss.myapplication.data.WorkoutLibrary
import com.dareewss.myapplication.ui.components.DataValue
import com.dareewss.myapplication.ui.components.FitCard
import com.dareewss.myapplication.ui.components.FitPrimaryButton
import com.dareewss.myapplication.ui.components.IconBadge
import com.dareewss.myapplication.ui.components.MonoLabel
import com.dareewss.myapplication.ui.components.ScreenHeader
import com.dareewss.myapplication.ui.theme.FitCarbs
import com.dareewss.myapplication.ui.theme.FitFat
import com.dareewss.myapplication.ui.theme.FitMuted
import com.dareewss.myapplication.ui.theme.FitPrimary
import com.dareewss.myapplication.ui.theme.FitProtein
import com.dareewss.myapplication.ui.theme.FitSecondary
import com.dareewss.myapplication.ui.theme.FitText
import com.dareewss.myapplication.ui.theme.FitTrack
import com.dareewss.myapplication.ui.theme.Inter
import java.util.Calendar

@Composable
fun DashboardScreen(
    sleepHours: Int,
    metrics: BodyMetrics,
    log: MacroLog,
    completedWorkoutId: String?,
    onLogMeal: () -> Unit,
    onOpenWorkouts: () -> Unit
) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val hello = when (hour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        else -> "Good evening"
    }
    val remaining = (metrics.macros.kcal - log.kcal).coerceAtLeast(0)
    val todayPlan = WorkoutLibrary.plans.first()
    val done = completedWorkoutId != null

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 20.dp, bottom = 28.dp)
    ) {
        ScreenHeader(hello, "Stay consistent. Small inputs, compounding output.")
        Spacer(Modifier.height(22.dp))

        FitCard {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.Outlined.Bolt)
                Spacer(Modifier.padding(start = 10.dp))
                MonoLabel("Energy today")
            }
            Spacer(Modifier.height(14.dp))
            DataValue("$remaining", FitSecondary, 34)
            Text(
                "kcal remaining of ${metrics.macros.kcal}",
                color = FitMuted,
                fontFamily = Inter,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(Modifier.height(16.dp))
            val p = (log.kcal / metrics.macros.kcal.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { p },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = FitPrimary,
                trackColor = FitTrack
            )
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MiniStat("Protein", "${log.proteinG.toInt()}g", FitProtein)
                MiniStat("Carbs", "${log.carbsG.toInt()}g", FitCarbs)
                MiniStat("Fat", "${log.fatG.toInt()}g", FitFat)
            }
            Spacer(Modifier.height(18.dp))
            FitPrimaryButton("+ Log a meal", onClick = onLogMeal)
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FitCard(Modifier.weight(1f)) {
                IconBadge(Icons.Outlined.NightsStay, FitProtein)
                Spacer(Modifier.height(12.dp))
                MonoLabel("Sleep")
                Spacer(Modifier.height(6.dp))
                DataValue("${sleepHours}h", size = 20)
                Text("${metrics.sleepStatus.lowercase().replaceFirstChar { it.titlecase() }} window", color = FitMuted, fontSize = 13.sp)
            }
            FitCard(Modifier.weight(1f)) {
                IconBadge(Icons.Outlined.Restaurant, FitFat)
                Spacer(Modifier.height(12.dp))
                MonoLabel("Logged")
                Spacer(Modifier.height(6.dp))
                DataValue("${log.kcal}", size = 20)
                Text("kcal eaten", color = FitMuted, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(12.dp))
        FitCard(onClick = onOpenWorkouts) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.Outlined.FitnessCenter)
                Spacer(Modifier.padding(start = 10.dp))
                MonoLabel("Today's session")
                Spacer(Modifier.weight(1f))
                Text(
                    if (done) "DONE" else todayPlan.duration,
                    color = if (done) FitPrimary else FitMuted,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(todayPlan.title, color = FitText, fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Text("${todayPlan.focus} · ${todayPlan.level}", color = FitMuted, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column {
        Text(label.uppercase(), color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.8.sp)
        Text(value, color = FitText, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
    }
}
