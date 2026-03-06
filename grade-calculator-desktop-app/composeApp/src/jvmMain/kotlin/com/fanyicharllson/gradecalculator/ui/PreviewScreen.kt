package com.fanyicharllson.gradecalculator.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanyicharllson.gradecalculator.model.Student
import com.fanyicharllson.gradecalculator.ui.theme.*
import com.fanyicharllson.gradecalculator.viewmodel.AppViewModel
import java.io.File

/**
 * ## PreviewScreen
 * Shows imported students and asks user to confirm before calculating.
 */
@Composable
fun PreviewScreen(
    file: File,
    students: List<Student>,
    viewModel: AppViewModel
) {
    var showDialog by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize().background(DarkBg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ) {

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Import Preview",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = OffWhite
                    )
                    Text(
                        "${students.size} students from '${file.name}'",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayMid
                    )
                }
                TextButton(onClick = { viewModel.onCancelPreview() }) {
                    Text("Cancel", color = GrayMid)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Column headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlueLight, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    "#",
                    modifier = Modifier.width(40.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
                Text(
                    "Name",
                    modifier = Modifier.weight(1f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
                Text(
                    "Scores",
                    modifier = Modifier.weight(2f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
                Text(
                    "Count",
                    modifier = Modifier.width(60.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
            }

            // Student list
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(students) { index, student ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        StudentPreviewRow(index + 1, student)
                    }
                    if (index < students.lastIndex)
                        HorizontalDivider(color = GrayLight.copy(alpha = 0.4f), thickness = 0.5.dp)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Confirm button
            Button(
                onClick = {
                    showDialog = true
                    viewModel.onConfirmCalculation(file, students)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text(
                    "Looks good — Calculate Grades",
                    fontWeight = FontWeight.Bold,
                    color = DarkBg,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun StudentPreviewRow(index: Int, student: Student) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (index % 2 == 0) DarkSurface else DarkElevated)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$index",
            modifier = Modifier.width(40.dp),
            fontSize = 13.sp,
            color = GrayMid
        )
        Text(
            student.name,
            modifier = Modifier.weight(1f),
            fontSize = 13.sp,
            color = OffWhite,
            fontWeight = FontWeight.Medium
        )
        Text(
            student.scores.joinToString("  |  ") { "%.0f".format(it) },
            modifier = Modifier.weight(2f),
            fontSize = 12.sp,
            color = GrayMid
        )
        Text(
            "${student.scores.size}",
            modifier = Modifier.width(60.dp),
            fontSize = 13.sp,
            color = BluePrimary,
            fontWeight = FontWeight.Bold
        )
    }
}