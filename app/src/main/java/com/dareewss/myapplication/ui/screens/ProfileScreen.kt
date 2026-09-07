package com.dareewss.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dareewss.myapplication.data.BodyMetrics
import com.dareewss.myapplication.data.UserProfile
import com.dareewss.myapplication.ui.components.FitCard
import com.dareewss.myapplication.ui.components.FitPrimaryButton
import com.dareewss.myapplication.ui.components.MonoLabel
import com.dareewss.myapplication.ui.components.ScreenHeader
import com.dareewss.myapplication.ui.theme.FitMuted
import com.dareewss.myapplication.ui.theme.FitText
import com.dareewss.myapplication.ui.theme.Inter
import java.util.Locale

@Composable
fun ProfileScreen(
    profile: UserProfile,
    metrics: BodyMetrics,
    onEdit: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 20.dp, bottom = 28.dp)
    ) {
        ScreenHeader("Profile", "Your baseline. Update it as you change.")
        Spacer(Modifier.height(22.dp))
        FitCard {
            MonoLabel("Athlete")
            Text(
                if (profile.isMale) "Male" else "Female",
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = FitText,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text("${profile.age} years · ${profile.activity.title}", color = FitMuted, modifier = Modifier.padding(top = 4.dp))
        }
        Spacer(Modifier.height(12.dp))
        FitCard {
            ProfileRow("Weight", String.format(Locale.US, "%.1f kg", profile.weightKg))
            ProfileRow("Height", String.format(Locale.US, "%.1f cm", profile.heightCm))
            ProfileRow("Sleep goal", "${profile.sleepHours} hours")
            ProfileRow("Body fat", profile.bodyFatPercent?.let { String.format(Locale.US, "%.1f%%", it) } ?: "Not set")
            ProfileRow("BMR method", metrics.method.label)
            ProfileRow("Maintain", "${metrics.tdee} kcal")
        }
        Spacer(Modifier.height(20.dp))
        FitPrimaryButton("Edit profile", onClick = onEdit)
        Text(
            "Editing takes you back through setup. Meal logs for today are kept.",
            color = FitMuted,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        MonoLabel(label, FitMuted)
        Spacer(Modifier.weight(1f))
        Text(value, color = FitText, fontWeight = FontWeight.Medium)
    }
}
