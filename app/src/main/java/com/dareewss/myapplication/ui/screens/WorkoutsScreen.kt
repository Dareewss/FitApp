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
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dareewss.myapplication.data.WorkoutLibrary
import com.dareewss.myapplication.data.WorkoutPlan
import com.dareewss.myapplication.ui.components.FitCard
import com.dareewss.myapplication.ui.components.FitPrimaryButton
import com.dareewss.myapplication.ui.components.IconBadge
import com.dareewss.myapplication.ui.components.MonoLabel
import com.dareewss.myapplication.ui.components.ScreenHeader
import com.dareewss.myapplication.ui.components.StatusPill
import com.dareewss.myapplication.ui.theme.FitMuted
import com.dareewss.myapplication.ui.theme.FitPrimary
import com.dareewss.myapplication.ui.theme.FitText
import com.dareewss.myapplication.ui.theme.Inter

@Composable
fun WorkoutsScreen(completedId: String?, onComplete: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 20.dp, bottom = 28.dp)
    ) {
        ScreenHeader("Workouts", "Four sessions. Pick one and finish it.")
        Spacer(Modifier.height(22.dp))
        WorkoutLibrary.plans.forEach { plan ->
            WorkoutCard(plan, completedId == plan.id) { onComplete(plan.id) }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun WorkoutCard(plan: WorkoutPlan, done: Boolean, onComplete: () -> Unit) {
    FitCard {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconBadge(Icons.Outlined.FitnessCenter)
            Spacer(Modifier.padding(start = 10.dp))
            Column(Modifier.weight(1f)) {
                MonoLabel(plan.focus)
                Text(
                    plan.title,
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = FitText,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            if (done) StatusPill("DONE") else MonoLabel(plan.duration)
        }
        Text("${plan.level} · ${plan.exercises.size} movements", color = FitMuted, modifier = Modifier.padding(top = 8.dp, bottom = 14.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            plan.exercises.forEach { move ->
                Text("·  $move", color = FitText, fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
        if (!done) {
            Spacer(Modifier.height(16.dp))
            FitPrimaryButton("Mark complete", onClick = onComplete)
        }
    }
}
