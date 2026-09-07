package com.dareewss.myapplication.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dareewss.myapplication.ui.theme.FitBorder
import com.dareewss.myapplication.ui.theme.FitMuted
import com.dareewss.myapplication.ui.theme.FitOnPrimary
import com.dareewss.myapplication.ui.theme.FitPrimary
import com.dareewss.myapplication.ui.theme.FitSurface
import com.dareewss.myapplication.ui.theme.FitText
import com.dareewss.myapplication.ui.theme.FitTrack
import com.dareewss.myapplication.ui.theme.Inter
import com.dareewss.myapplication.ui.theme.JetBrainsMono

val CardShape = RoundedCornerShape(16.dp)

@Composable
fun FitCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(FitSurface)
            .border(1.dp, FitBorder, CardShape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(18.dp),
        content = content
    )
}

@Composable
fun MonoLabel(text: String, color: Color = FitMuted) {
    Text(
        text = text.uppercase(),
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 1.4.sp,
        color = color
    )
}

@Composable
fun DataValue(text: String, color: Color = FitText, size: Int = 28) {
    Text(
        text = text,
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = size.sp,
        color = color
    )
}

@Composable
fun FitPrimaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FitPrimary,
            contentColor = FitOnPrimary
        )
    ) {
        Text(
            text = text.uppercase(),
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            letterSpacing = 0.6.sp
        )
    }
}

@Composable
fun FitField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Decimal
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text(hint, color = FitMuted.copy(alpha = 0.6f)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FitPrimary,
            unfocusedBorderColor = FitBorder,
            focusedContainerColor = FitTrack,
            unfocusedContainerColor = FitTrack,
            focusedTextColor = FitText,
            unfocusedTextColor = FitText,
            cursorColor = FitPrimary
        )
    )
}

@Composable
fun IconBadge(icon: ImageVector, tint: Color = FitPrimary) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(tint.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun StatusPill(text: String, color: Color = FitPrimary) {
    Text(
        text = text,
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        letterSpacing = 0.8.sp,
        color = FitOnPrimary,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun VerticalMacroBar(
    progress: Float,
    color: Color,
    caption: String,
    name: String,
    modifier: Modifier = Modifier,
    height: Dp = 132.dp
) {
    val animated by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f), label = "macro")
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .height(height)
                .width(64.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(FitTrack)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(animated.coerceAtLeast(0.02f))
                    .align(Alignment.BottomCenter)
                    .background(color)
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = caption,
            fontFamily = JetBrainsMono,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = FitText
        )
        Text(
            text = name.uppercase(),
            fontFamily = JetBrainsMono,
            fontSize = 10.sp,
            letterSpacing = 0.8.sp,
            color = color,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun SelectableChip(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) FitPrimary.copy(alpha = 0.16f) else FitTrack)
            .border(1.dp, if (selected) FitPrimary else FitBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        content = content
    )
}

@Composable
fun ScreenHeader(title: String, subtitle: String) {
    Column {
        Text(
            text = title,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = FitText
        )
        Text(
            text = subtitle,
            fontFamily = Inter,
            fontSize = 15.sp,
            color = FitMuted,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
fun Dot(color: Color) {
    Box(
        Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}
