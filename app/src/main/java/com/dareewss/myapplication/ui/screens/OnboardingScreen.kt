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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dareewss.myapplication.data.ActivityLevel
import com.dareewss.myapplication.data.UserProfile
import com.dareewss.myapplication.ui.components.FitCard
import com.dareewss.myapplication.ui.components.FitField
import com.dareewss.myapplication.ui.components.FitPrimaryButton
import com.dareewss.myapplication.ui.components.MonoLabel
import com.dareewss.myapplication.ui.components.ScreenHeader
import com.dareewss.myapplication.ui.components.SelectableChip
import com.dareewss.myapplication.ui.theme.FitMuted
import com.dareewss.myapplication.ui.theme.FitPrimary
import com.dareewss.myapplication.ui.theme.FitText
import com.dareewss.myapplication.ui.theme.Inter

@Composable
fun OnboardingScreen(existing: UserProfile?, onComplete: (UserProfile) -> Unit) {
    var male by remember { mutableStateOf(existing?.isMale ?: true) }
    var genderSet by remember { mutableStateOf(existing != null) }
    var age by remember { mutableStateOf(existing?.age?.toString().orEmpty()) }
    var weight by remember { mutableStateOf(existing?.weightKg?.takeIf { it > 0 }?.toString().orEmpty()) }
    var height by remember { mutableStateOf(existing?.heightCm?.takeIf { it > 0 }?.toString().orEmpty()) }
    var sleep by remember { mutableIntStateOf(existing?.sleepHours ?: 8) }
    var activity by remember { mutableStateOf(existing?.activity) }
    var bodyFat by remember { mutableStateOf(existing?.bodyFatPercent?.toString().orEmpty()) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 28.dp, bottom = 40.dp)
    ) {
        ScreenHeader(
            if (existing != null) "Edit profile" else "Let's get started",
            "Tell us a bit about yourself"
        )
        Spacer(Modifier.height(24.dp))

        FitCard {
            MonoLabel("Gender")
            Text("Used for accurate BMR when body fat is unknown", color = FitMuted, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, bottom = 14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SelectableChip(selected = genderSet && male, modifier = Modifier.weight(1f), onClick = { genderSet = true; male = true }) {
                    Text("Male", color = FitText, fontWeight = FontWeight.SemiBold)
                }
                SelectableChip(selected = genderSet && !male, modifier = Modifier.weight(1f), onClick = { genderSet = true; male = false }) {
                    Text("Female", color = FitText, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        FitCard {
            MonoLabel("Age")
            Spacer(Modifier.height(12.dp))
            FitField(age, { age = it.filter(Char::isDigit) }, "e.g. 25", KeyboardType.Number)
        }
        Spacer(Modifier.height(12.dp))
        FitCard {
            MonoLabel("Weight (kg)")
            Spacer(Modifier.height(12.dp))
            FitField(weight, { weight = it }, "e.g. 82")
        }
        Spacer(Modifier.height(12.dp))
        FitCard {
            MonoLabel("Height (cm)")
            Spacer(Modifier.height(12.dp))
            FitField(height, { height = it }, "e.g. 185")
        }
        Spacer(Modifier.height(12.dp))
        FitCard {
            MonoLabel("Body fat %  ·  optional")
            Text("Unlocks Katch-McArdle BMR", color = FitMuted, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))
            FitField(bodyFat, { bodyFat = it }, "e.g. 15")
        }
        Spacer(Modifier.height(12.dp))
        FitCard {
            MonoLabel("Sleep goal")
            Text("How many hours do you want to sleep per night?", color = FitMuted, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
            Slider(
                value = sleep.toFloat(),
                onValueChange = { sleep = it.toInt() },
                valueRange = 4f..12f,
                steps = 7,
                colors = SliderDefaults.colors(
                    thumbColor = FitPrimary,
                    activeTrackColor = FitPrimary,
                    inactiveTrackColor = FitPrimary.copy(alpha = 0.2f)
                )
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("4h", color = FitMuted, fontSize = 12.sp)
                Text("$sleep hours", color = FitPrimary, fontFamily = Inter, fontWeight = FontWeight.SemiBold)
                Text("12h", color = FitMuted, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(12.dp))
        FitCard {
            MonoLabel("Activity level")
            Text("Used to calculate daily calorie needs", color = FitMuted, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ActivityLevel.entries.forEach { level ->
                    SelectableChip(
                        selected = activity == level,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { activity = level }
                    ) {
                        Column {
                            Text(level.title, color = if (activity == level) FitPrimary else FitText, fontWeight = FontWeight.SemiBold)
                            Text(level.detail, color = FitMuted, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        error?.let {
            Text(it, color = com.dareewss.myapplication.ui.theme.FitDanger, modifier = Modifier.padding(top = 16.dp))
        }

        Spacer(Modifier.height(24.dp))
        FitPrimaryButton("Continue") {
            val w = weight.toFloatOrNull()
            val h = height.toFloatOrNull()
            val a = age.toIntOrNull()
            val fat = bodyFat.toFloatOrNull()
            when {
                !genderSet -> error = "Select a gender"
                a == null || a !in 13..90 -> error = "Enter a valid age"
                w == null || w !in 30f..300f -> error = "Enter a valid weight"
                h == null || h !in 120f..230f -> error = "Enter a valid height"
                activity == null -> error = "Select your activity level"
                fat != null && fat !in 3f..60f -> error = "Body fat should be between 3 and 60"
                else -> onComplete(
                    UserProfile(
                        isMale = male,
                        age = a,
                        weightKg = w,
                        heightCm = h,
                        sleepHours = sleep,
                        activity = activity!!,
                        bodyFatPercent = fat
                    )
                )
            }
        }
    }
}
