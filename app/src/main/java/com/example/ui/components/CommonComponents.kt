package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectDomain
import com.example.ui.theme.BiologyTeal
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BotanyGreen
import com.example.ui.theme.ChemistryGreen
import com.example.ui.theme.FocusPurple
import com.example.ui.theme.MathsAmber
import com.example.ui.theme.PhysicsCyan
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ZoologyPurple

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderColor: Color = BorderSubtle,
    backgroundColor: Color = SurfaceCard,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val clickModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape)
            .then(clickModifier)
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun StatChip(
    iconText: String,
    title: String,
    value: String,
    accentColor: Color = FocusPurple,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceElevated)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = iconText, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title.uppercase(),
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = value,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SubjectBadge(
    subject: SubjectDomain,
    modifier: Modifier = Modifier
) {
    val (color, name) = when (subject) {
        SubjectDomain.PHYSICS -> PhysicsCyan to "Physics"
        SubjectDomain.CHEMISTRY -> ChemistryGreen to "Chemistry"
        SubjectDomain.MATHEMATICS -> MathsAmber to "Maths"
        SubjectDomain.BOTANY -> BotanyGreen to "Botany"
        SubjectDomain.ZOOLOGY -> ZoologyPurple to "Zoology"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = name,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ConsistencyHeatmap(
    studyHoursList: List<Float>, // Recent days
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "STUDY CONSISTENCY GRID",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Last 28 Days",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 4 rows x 7 days grid
        val rows = 4
        val cols = 7
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (r in 0 until rows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (c in 0 until cols) {
                        val index = (r * cols) + c
                        val hours = studyHoursList.getOrElse(index) { 0f }
                        val cubeColor = when {
                            hours >= 6f -> Color(0xFF00F0FF) // Level 5
                            hours >= 4f -> Color(0xFFA855F7) // Level 4
                            hours >= 2f -> Color(0xFF8B5CF6) // Level 3
                            hours >= 1f -> Color(0xFF6D28D9) // Level 2
                            hours > 0f -> Color(0xFF4C1D95)  // Level 1
                            else -> Color(0xFF1E2230)        // Level 0
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(16.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(cubeColor)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Legend
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Less", color = TextMuted, fontSize = 9.sp)
            Spacer(modifier = Modifier.width(4.dp))
            listOf(
                Color(0xFF1E2230),
                Color(0xFF4C1D95),
                Color(0xFF6D28D9),
                Color(0xFF8B5CF6),
                Color(0xFFA855F7),
                Color(0xFF00F0FF)
            ).forEach { color ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                )
                Spacer(modifier = Modifier.width(2.dp))
            }
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "6h+", color = TextMuted, fontSize = 9.sp)
        }
    }
}
