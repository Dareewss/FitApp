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
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dareewss.myapplication.data.BodyMetrics
import com.dareewss.myapplication.data.MacroLog
import com.dareewss.myapplication.data.UserProfile
import com.dareewss.myapplication.ui.components.DataValue
import com.dareewss.myapplication.ui.components.FitCard
import com.dareewss.myapplication.ui.components.FitPrimaryButton
import com.dareewss.myapplication.ui.components.IconBadge
import com.dareewss.myapplication.ui.components.MonoLabel
import com.dareewss.myapplication.ui.components.ScreenHeader
import com.dareewss.myapplication.ui.components.StatusPill
import com.dareewss.myapplication.ui.components.VerticalMacroBar
import com.dareewss.myapplication.ui.theme.FitCarbs
import com.dareewss.myapplication.ui.theme.FitFat
import com.dareewss.myapplication.ui.theme.FitMuted
import com.dareewss.myapplication.ui.theme.FitPrimary
import com.dareewss.myapplication.ui.theme.FitProtein
import com.dareewss.myapplication.ui.theme.FitSecondary
import com.dareewss.myapplication.ui.theme.FitSubtle
import com.dareewss.myapplication.ui.theme.FitText
import com.dareewss.myapplication.ui.theme.Inter
import com.dareewss.myapplication.ui.theme.JetBrainsMono
import java.util.Locale

@Composable
fun StatsScreen(
    profile: UserProfile,
    metrics: BodyMetrics,
    log: MacroLog,
    onLogMeal: () -> Unit
) {
    var showBmiInfo by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 20.dp, bottom = 28.dp)
    ) {
        ScreenHeader("Your Stats", "Here's what we know about you")
        Spacer(Modifier.height(22.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricTile(
                modifier = Modifier.weight(1f),
                icon = { IconBadge(Icons.Outlined.MonitorWeight) },
                label = "Weight",
                value = String.format(Locale.US, "%.1f kg", profile.weightKg)
            )
            MetricTile(
                modifier = Modifier.weight(1f),
                icon = { IconBadge(Icons.Outlined.Straighten, FitProtein) },
                label = "Height",
                value = String.format(Locale.US, "%.1f cm", profile.heightCm)
            )
        }

        Spacer(Modifier.height(12.dp))

        FitCard {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.Outlined.NightsStay, FitProtein)
                Spacer(Modifier.padding(start = 10.dp))
                MonoLabel("Sleep Goal")
                Spacer(Modifier.weight(1f))
                StatusPill(metrics.sleepStatus)
            }
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                DataValue("${profile.sleepHours}", FitSecondary)
                Text(
                    "  hours per night",
                    color = FitMuted,
                    fontFamily = Inter,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        FitCard {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.AutoMirrored.Outlined.ShowChart)
                Spacer(Modifier.padding(start = 10.dp))
                MonoLabel("BMI")
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { showBmiInfo = true }) {
                    Icon(Icons.Outlined.Info, contentDescription = "BMI info", tint = FitSubtle)
                }
            }
            DataValue(String.format(Locale.US, "%.1f", metrics.bmi), FitSecondary)
            Text(
                metrics.bmiLabel,
                color = FitText,
                fontFamily = Inter,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        FitCard {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.Outlined.LocalFireDepartment, FitFat)
                Spacer(Modifier.padding(start = 10.dp))
                MonoLabel("BMR")
                Spacer(Modifier.weight(1f))
                MonoLabel(metrics.method.label, FitSubtle)
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                DataValue("${metrics.bmr}", FitSecondary)
                Text(
                    "  calories",
                    color = FitMuted,
                    fontFamily = Inter,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Text(
                "You need ${metrics.tdee} calories to maintain your weight with activity.",
                color = FitMuted,
                fontFamily = Inter,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        FitCard {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.Outlined.TrackChanges)
                Spacer(Modifier.padding(start = 10.dp))
                MonoLabel("Daily Macros")
                Spacer(Modifier.weight(1f))
                Text(
                    "${log.kcal} / ${metrics.macros.kcal} kcal",
                    fontFamily = JetBrainsMono,
                    fontSize = 12.sp,
                    color = FitMuted
                )
            }
            Spacer(Modifier.height(20.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                VerticalMacroBar(
                    progress = log.proteinG / metrics.macros.proteinG.coerceAtLeast(1).toFloat(),
                    color = FitProtein,
                    caption = "${log.proteinG.toInt()} / ${metrics.macros.proteinG}g",
                    name = "Protein"
                )
                VerticalMacroBar(
                    progress = log.carbsG / metrics.macros.carbsG.coerceAtLeast(1).toFloat(),
                    color = FitCarbs,
                    caption = "${log.carbsG.toInt()} / ${metrics.macros.carbsG}g",
                    name = "Carbs"
                )
                VerticalMacroBar(
                    progress = log.fatG / metrics.macros.fatG.coerceAtLeast(1).toFloat(),
                    color = FitFat,
                    caption = "${log.fatG.toInt()} / ${metrics.macros.fatG}g",
                    name = "Fat"
                )
            }
            Spacer(Modifier.height(22.dp))
            FitPrimaryButton("+ Log a meal", onClick = onLogMeal)
        }
    }

    if (showBmiInfo) {
        AlertDialog(
            onDismissRequest = { showBmiInfo = false },
            confirmButton = {
                TextButton(onClick = { showBmiInfo = false }) {
                    Text("Got it", color = FitPrimary)
                }
            },
            title = { Text("Body mass index", fontWeight = FontWeight.SemiBold) },
            text = {
                Text("Under 18.5 underweight · 18.5–24.9 normal · 25–29.9 overweight · 30+ obese. BMI is a screening number, not a diagnosis.")
            }
        )
    }
}

@Composable
private fun MetricTile(
    modifier: Modifier,
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    FitCard(modifier) {
        icon()
        Spacer(Modifier.height(14.dp))
        MonoLabel(label)
        Spacer(Modifier.height(8.dp))
        DataValue(value, size = 22)
    }
}
