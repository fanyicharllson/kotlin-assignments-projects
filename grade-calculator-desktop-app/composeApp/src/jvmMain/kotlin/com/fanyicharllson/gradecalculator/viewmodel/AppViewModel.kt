package com.fanyicharllson.gradecalculator.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanyicharllson.gradecalculator.calculator.GradeCalculator
import com.fanyicharllson.gradecalculator.export.ExcelHelper
import com.fanyicharllson.gradecalculator.export.ExportHelper
import com.fanyicharllson.gradecalculator.model.AppState
import com.fanyicharllson.gradecalculator.model.GradeResult
import com.fanyicharllson.gradecalculator.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * ## AppViewModel
 *
 * Single source of truth for all app state.
 * Drives navigation via [AppState] sealed class.
 */
class AppViewModel : ViewModel() {

    private val calculator = GradeCalculator()

    private val _state = MutableStateFlow<AppState>(AppState.Home)
    val state: StateFlow<AppState> = _state.asStateFlow()

    // ── Export feedback ────────────────────────────────────────────────────
    private val _exportMessage = MutableStateFlow<String?>(null)
    val exportMessage: StateFlow<String?> = _exportMessage.asStateFlow()

    // ── Actions ────────────────────────────────────────────────────────────

    /** Called when user picks an Excel file on HomeScreen */
    fun onFileSelected(file: File) {
        viewModelScope.launch {
            // Show loading immediately — user knows something is happening
            _state.value = AppState.Calculating("Extracting data from '${file.name}'...\nPlease wait.")

            val students = withContext(Dispatchers.IO) {
                ExcelHelper.readStudents(file)
            }

            _state.value = if (students.isEmpty())
                AppState.Error("No students found in '${file.name}'.\n\nMake sure:\n• Row 1 is a header row\n• Column A contains student names\n• Columns B+ contain numeric scores")
            else
                AppState.Preview(file = file, students = students)
        }
    }

    /** Called when user confirms preview — triggers calculation */
    fun onConfirmCalculation(file: File, students: List<Student>) {
        viewModelScope.launch {
            _state.value = AppState.Calculating("Calculating grades...\nPlease wait.")
            delay(800)

            val results = withContext(Dispatchers.Default) {
                calculator.calculateAll(students)
            }
            _state.value = AppState.Results(file = file, results = results)
        }
    }

    /** User cancelled on preview — go back home */
    fun onCancelPreview() {
        _state.value = AppState.Home
    }

    /** Start over from results screen */
    fun onReset() {
        _state.value = AppState.Home
    }

    // ── Exports ────────────────────────────────────────────────────────────

    /**
     * Generic export handler — takes a lambda that does the actual writing.
     * This keeps each export call in the UI clean — just pass the action.
     */
    private fun export(
        outputFile: File,
        formatName: String,
        exportAction: (File) -> Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = exportAction(outputFile)
            withContext(Dispatchers.Main) {
                _exportMessage.value = if (success)
                    "Saved as $formatName → ${outputFile.absolutePath}"
                else
                    "Failed to export as $formatName. Please try again."
            }
        }
    }

    fun exportExcel(inputFile: File, students: List<Student>, outputFile: File) =
        export(outputFile, "Excel") {
            ExcelHelper.writeResults(inputFile, outputFile, students)
        }

    fun exportPdf(results: List<Pair<Student, GradeResult>>, outputFile: File) =
        export(outputFile, "PDF") {
            ExportHelper.exportPdf(results, outputFile)
        }

    fun exportHtml(results: List<Pair<Student, GradeResult>>, outputFile: File) =
        export(outputFile, "HTML") {
            ExportHelper.exportHtml(results, outputFile)
        }

    fun exportXml(results: List<Pair<Student, GradeResult>>, outputFile: File) =
        export(outputFile, "XML") {
            ExportHelper.exportXml(results, outputFile)
        }

    fun clearExportMessage() {
        _exportMessage.value = null
    }
}