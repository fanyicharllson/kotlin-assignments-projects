package com.fanyicharllson.gradecalculator.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanyicharllson.gradecalculator.model.GradeResult
import com.fanyicharllson.gradecalculator.model.Student
import com.fanyicharllson.gradecalculator.ui.theme.*
import com.fanyicharllson.gradecalculator.viewmodel.AppViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

/**
 * ## ResultScreen
 * Shows calculated grades and export options (Excel, PDF, HTML, XML).
 */
@Composable
fun ResultScreen(
    sourceFile: File,
    results: List<Pair<Student, GradeResult>>,
    viewModel: AppViewModel
) {
    val exportMessage by viewModel.exportMessage.collectAsState()
    val passCount = results.count { it.second is GradeResult.Pass }
    val failCount = results.count { it.second is GradeResult.Fail }

    // Convert results to Student list for Excel export
    val studentsWithGrades = results.map { (student, result) ->
        when (result) {
            is GradeResult.Pass -> student.copy(average = result.average, grade = result.grade, passed = true)
            is GradeResult.Fail -> student.copy(average = result.average, grade = result.grade, passed = false)
            is GradeResult.NoScores -> student.copy(average = 0.0, grade = "-", passed = false)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(DarkBg).padding(32.dp)
    ) {

        // Header row
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Results", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OffWhite)
                Text(
                    "Calculation complete — ${results.size} students",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayMid
                )
            }
            TextButton(onClick = { viewModel.onReset() }) {
                Text("Start Over", color = GrayMid)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Summary strip
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(BlueLight, RoundedCornerShape(14.dp))
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryChip("Total", "${results.size}", OffWhite)
            SummaryChip("Pass", "$passCount", AccentGreen)
            SummaryChip("Fail", "$failCount", AccentRed)
        }

        Spacer(Modifier.height(16.dp))

        // Column headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkElevated, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                "Name",
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            Text(
                "Average",
                modifier = Modifier.width(90.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            Text(
                "Grade",
                modifier = Modifier.width(80.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            Text(
                "Status",
                modifier = Modifier.width(90.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
        }

        // Results list
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(results) { index, (student, result) ->
                ResultRow(index, student, result)
                if (index < results.lastIndex)
                    HorizontalDivider(color = GrayLight.copy(alpha = 0.3f), thickness = 0.5.dp)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Export message snackbar
        AnimatedVisibility(visible = exportMessage != null, enter = fadeIn(), exit = fadeOut()) {
            exportMessage?.let { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (msg.startsWith("Saved"))
                            AccentGreen.copy(alpha = 0.15f)
                        else
                            AccentRed.copy(alpha = 0.15f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            msg,
                            modifier = Modifier.weight(1f),
                            color = if (msg.startsWith("Saved")) AccentGreen else AccentRed,
                            fontSize = 13.sp
                        )
                        TextButton(onClick = { viewModel.clearExportMessage() }) {
                            Text("Dismiss", color = GrayMid, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Export buttons row
        Text("Export As", fontSize = 13.sp, color = GrayMid, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Lambda — each button picks a file and calls the right export
            ExportButton("Excel (.xlsx)", BluePrimary, Modifier.weight(1f)) {
                pickSaveFile("GradeResults", "xlsx")?.let { f ->
                    viewModel.exportExcel(sourceFile, studentsWithGrades, f)
                }
            }
            ExportButton("PDF", AccentRed.copy(alpha = 0.8f), Modifier.weight(1f)) {
                pickSaveFile("GradeResults", "pdf")?.let { f ->
                    viewModel.exportPdf(results, f)
                }
            }
            ExportButton("HTML", AccentGreen.copy(alpha = 0.8f), Modifier.weight(1f)) {
                pickSaveFile("GradeResults", "html")?.let { f ->
                    viewModel.exportHtml(results, f)
                }
            }
            ExportButton("XML", GradeC.copy(alpha = 0.9f), Modifier.weight(1f)) {
                pickSaveFile("GradeResults", "xml")?.let { f ->
                    viewModel.exportXml(results, f)
                }
            }
        }
    }
}

// ── Helpers ────────────────────────────────────────────────────────────────

@Composable
private fun SummaryChip(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 13.sp, color = GrayMid)
    }
}

@Composable
private fun ExportButton(label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color.copy(alpha = 0.2f))
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun ResultRow(index: Int, student: Student, result: GradeResult) {
    val (avg, grade, status) = when (result) {
        is GradeResult.Pass -> Triple("%.1f".format(result.average), result.grade, "PASS")
        is GradeResult.Fail -> Triple("%.1f".format(result.average), result.grade, "FAIL")
        is GradeResult.NoScores -> Triple("N/A", "-", "NO SCORES")
    }
    val gradeColor = when (grade) {
        "A" -> GradeA; "B+" -> GradeB; "B" -> GradeB
        "C+" -> GradeC; "C" -> GradeC
        "D+" -> GradeD; "D" -> GradeD
        else -> GradeF
    }
    val statusColor = if (status == "PASS") AccentGreen else AccentRed

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (index % 2 == 0) DarkSurface else DarkElevated)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(student.name, modifier = Modifier.weight(1f), fontSize = 13.sp, color = OffWhite)
        Text(avg, modifier = Modifier.width(90.dp), fontSize = 13.sp, color = GrayMid)

        // Grade badge
        Box(
            modifier = Modifier.width(80.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = gradeColor.copy(alpha = 0.15f)
            ) {
                Text(
                    grade,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = gradeColor
                )
            }
        }

        // Status badge
        Box(modifier = Modifier.width(90.dp), contentAlignment = Alignment.CenterStart) {
            Surface(
                shape = RoundedCornerShape(50),
                color = statusColor.copy(alpha = 0.15f)
            ) {
                Text(
                    status,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
        }
    }
}

/** Opens native save dialog */
private fun pickSaveFile(defaultName: String, extension: String): File? {
    val dialog = FileDialog(null as Frame?, "Save $extension File", FileDialog.SAVE).apply {
        file = "$defaultName.$extension"
        isVisible = true
    }
    val dir = dialog.directory ?: return null
    val name = dialog.file ?: return null
    return File(dir, name)
}