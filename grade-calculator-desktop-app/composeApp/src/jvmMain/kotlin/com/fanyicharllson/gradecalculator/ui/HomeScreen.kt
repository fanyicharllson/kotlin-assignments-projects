package com.fanyicharllson.gradecalculator.ui


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanyicharllson.gradecalculator.ui.theme.*
import com.fanyicharllson.gradecalculator.viewmodel.AppViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

/**
 * ## HomeScreen
 * Entry point — lets user pick an Excel file via file dialog.
 * Shows format hint card at the bottom.
 */
@Composable
fun HomeScreen(viewModel: AppViewModel) {

    // Pulse animation on the icon box
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.widthIn(max = 520.dp)
        ) {

            // Animated logo box
            Box(
                modifier = Modifier
                    .size((90 * pulse).dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(BlueLight),
                contentAlignment = Alignment.Center
            ) {
                Text("GC", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
            }

            Spacer(Modifier.height(28.dp))

            Text(
                "Grade Calculator",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = OffWhite
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Import an Excel file with student scores\nto calculate and export grades.",
                style = MaterialTheme.typography.bodyLarge,
                color = GrayMid,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            // Import button
            Button(
                onClick = {
                    val file = pickExcelFile()
                    file?.let { viewModel.onFileSelected(it) }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text(
                    "Import Excel File",
                    fontWeight = FontWeight.Bold,
                    color = DarkBg,
                    fontSize = 15.sp
                )
            }

            Spacer(Modifier.height(32.dp))

            // Format hint card
            FormatHintCard()
        }
    }
}

/** Opens a native file dialog filtered to .xlsx files */
private fun pickExcelFile(): File? {
    val dialog = FileDialog(null as Frame?, "Select Excel File", FileDialog.LOAD).apply {
        file = "*.xlsx"
        isVisible = true
    }
    val dir = dialog.directory ?: return null
    val name = dialog.file ?: return null
    return File(dir, name).takeIf { it.exists() }
}

@Composable
private fun FormatHintCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Expected Excel Format",
                    fontWeight = FontWeight.SemiBold,
                    color = BluePrimary,
                    fontSize = 15.sp
                )
                Spacer(Modifier.width(8.dp))
                Surface(shape = RoundedCornerShape(6.dp), color = BlueLight) {
                    Text(
                        ".xlsx",
                        fontSize = 11.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkElevated)
            ) {
                HintTableRow(listOf("Name", "Score 1", "Score 2", "Score 3"), isHeader = true)
                HorizontalDivider(color = GrayLight, thickness = 0.5.dp)
                HintTableRow(listOf("Alice", "85", "90", "78"))
                HorizontalDivider(color = GrayLight.copy(alpha = 0.4f), thickness = 0.5.dp)
                HintTableRow(listOf("Bob", "60", "55", "70"))
                HorizontalDivider(color = GrayLight.copy(alpha = 0.4f), thickness = 0.5.dp)
                HintTableRow(listOf("Carol", "92", "88", "95"))
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "• Row 1 = header   • Column A = name   • Columns B+ = scores",
                style = MaterialTheme.typography.bodySmall,
                color = GrayMid
            )
        }
    }
}

@Composable
private fun HintTableRow(cells: List<String>, isHeader: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isHeader) BluePrimary.copy(alpha = 0.12f) else DarkElevated)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        cells.forEach { cell ->
            Text(
                text = cell,
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
                color = if (isHeader) BluePrimary else OffWhite.copy(alpha = 0.8f)
            )
        }
    }
}