package com.dareewss.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dareewss.myapplication.ui.components.FitCard
import com.dareewss.myapplication.ui.components.FitField
import com.dareewss.myapplication.ui.components.FitPrimaryButton
import com.dareewss.myapplication.ui.components.MonoLabel
import com.dareewss.myapplication.ui.theme.FitCarbs
import com.dareewss.myapplication.ui.theme.FitFat
import com.dareewss.myapplication.ui.theme.FitMuted
import com.dareewss.myapplication.ui.theme.FitProtein
import com.dareewss.myapplication.ui.theme.FitSurface
import com.dareewss.myapplication.ui.theme.FitText
import com.dareewss.myapplication.ui.theme.Inter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogMealSheet(onDismiss: () -> Unit, onLog: (Float, Float, Float) -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FitSurface
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Log a meal",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = FitText
            )
            Text(
                "Add protein, carbs and fat from this sitting.",
                color = FitMuted,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MacroInput("Protein", protein, FitProtein, { protein = it }, Modifier.weight(1f))
                MacroInput("Carbs", carbs, FitCarbs, { carbs = it }, Modifier.weight(1f))
                MacroInput("Fat", fat, FitFat, { fat = it }, Modifier.weight(1f))
            }
            Spacer(Modifier.height(24.dp))
            FitPrimaryButton("+ Log a meal") {
                onLog(protein.toFloatOrZero(), carbs.toFloatOrZero(), fat.toFloatOrZero())
                onDismiss()
            }
        }
    }
}

@Composable
private fun MacroInput(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    onChange: (String) -> Unit,
    modifier: Modifier
) {
    FitCard(modifier) {
        MonoLabel(label, color)
        Spacer(Modifier.height(10.dp))
        FitField(value, onChange, "0", KeyboardType.Decimal)
    }
}

private fun String.toFloatOrZero(): Float = toFloatOrNull() ?: 0f
