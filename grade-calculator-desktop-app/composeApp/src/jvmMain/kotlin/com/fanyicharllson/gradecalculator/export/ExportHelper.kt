package com.fanyicharllson.gradecalculator.export


import com.fanyicharllson.gradecalculator.model.GradeResult
import com.fanyicharllson.gradecalculator.model.Student
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfWriter
import java.awt.Color
import java.io.File
import java.io.FileOutputStream

/**
 * ## ExportHelper
 *
 * Handles PDF, HTML, and XML exports of grade results.
 * Each function takes the results list and a destination [File].
 * Returns true on success, false on failure — consistent with ExcelHelper.
 */
object ExportHelper {

    // ── PDF Export ─────────────────────────────────────────────────────────

    /**
     * Exports results as a formatted PDF using OpenPDF.
     * @param results  Calculated student results
     * @param output   Destination .pdf file
     */
    fun exportPdf(
        results: List<Pair<Student, GradeResult>>,
        output: File
    ): Boolean = runCatching {
        val document = Document(PageSize.A4)
        PdfWriter.getInstance(document, FileOutputStream(output))
        document.open()

        // ── Title ──────────────────────────────────────────────────────────
        val titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f, Color(30, 45, 69))
        val bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11f)
        val boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11f)

        document.add(Paragraph("Student Grade Report", titleFont))
        document.add(Paragraph("ICT University — Grade Calculator", bodyFont))
        document.add(Chunk.NEWLINE)

        // ── Table ──────────────────────────────────────────────────────────
        val table = com.lowagie.text.pdf.PdfPTable(4).apply {
            widthPercentage = 100f
            setWidths(floatArrayOf(3f, 1.5f, 1f, 1f))
        }

        // Header row
        listOf("Name", "Average", "Grade", "Status").forEach { title ->
            table.addCell(
                com.lowagie.text.pdf.PdfPCell(Phrase(title, boldFont)).apply {
                    backgroundColor = Color(30, 45, 69)
                    setPadding(6f)
                }
            )
        }

        // Data rows
        results.forEach { (student, result) ->
            val (avg, grade, status) = when (result) {
                is GradeResult.Pass -> Triple("%.1f".format(result.average), result.grade, "PASS")
                is GradeResult.Fail -> Triple("%.1f".format(result.average), result.grade, "FAIL")
                is GradeResult.NoScores -> Triple("N/A", "-", "NO SCORES")
            }
            val rowColor = when (status) {
                "PASS" -> Color(232, 245, 233)
                "FAIL" -> Color(255, 235, 238)
                else -> Color(245, 245, 245)
            }
            listOf(student.name, avg, grade, status).forEach { value ->
                table.addCell(
                    com.lowagie.text.pdf.PdfPCell(Phrase(value, bodyFont)).apply {
                        backgroundColor = rowColor
                        setPadding(5f)
                    }
                )
            }
        }

        document.add(table)
        document.add(Chunk.NEWLINE)

        // Summary
        val passCount = results.count { it.second is GradeResult.Pass }
        val failCount = results.count { it.second is GradeResult.Fail }
        document.add(
            Paragraph(
                "Total: ${results.size}   |   Pass: $passCount   |   Fail: $failCount",
                boldFont
            )
        )

        document.close()
    }.isSuccess

    // ── HTML Export ────────────────────────────────────────────────────────

    /**
     * Exports results as a self-contained HTML file with inline CSS.
     * No external dependencies — opens in any browser.
     */
    fun exportHtml(
        results: List<Pair<Student, GradeResult>>,
        output: File
    ): Boolean = runCatching {
        val passCount = results.count { it.second is GradeResult.Pass }
        val failCount = results.count { it.second is GradeResult.Fail }

        // Lambda — build each table row
        val rows = results.joinToString("\n") { (student, result) ->
            val (avg, grade, status) = when (result) {
                is GradeResult.Pass -> Triple("%.1f".format(result.average), result.grade, "PASS")
                is GradeResult.Fail -> Triple("%.1f".format(result.average), result.grade, "FAIL")
                is GradeResult.NoScores -> Triple("N/A", "-", "NO SCORES")
            }
            val rowClass = when (status) {
                "PASS" -> "pass"; "FAIL" -> "fail"; else -> ""
            }
            """        <tr class="$rowClass">
              <td>${student.name}</td>
              <td>$avg</td>
              <td>$grade</td>
              <td><span class="badge $rowClass">$status</span></td>
            </tr>"""
        }

        output.writeText(
            """
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Grade Results</title>
  <style>
    body { font-family: 'Segoe UI', sans-serif; background: #0f1117; color: #e8eaed; padding: 40px; }
    h1   { color: #4e9bff; margin-bottom: 4px; }
    p    { color: #8a8fa8; margin-top: 0; }
    table { width: 100%; border-collapse: collapse; margin-top: 24px; }
    th   { background: #1e2d45; color: #4e9bff; padding: 12px 16px; text-align: left; }
    td   { padding: 10px 16px; border-bottom: 1px solid #3a3d4a; }
    tr.pass td { background: #0d2818; }
    tr.fail td { background: #2d0d0d; }
    .badge { padding: 3px 10px; border-radius: 50px; font-size: 12px; font-weight: bold; }
    .badge.pass { background: rgba(0,230,118,0.15); color: #00e676; }
    .badge.fail { background: rgba(255,82,82,0.15);  color: #ff5252; }
    .summary { margin-top: 24px; background: #1a1d27; padding: 16px 20px; border-radius: 12px; }
    .summary span { margin-right: 24px; font-weight: bold; }
  </style>
</head>
<body>
  <h1>Student Grade Report</h1>
  <p>ICT University — Grade Calculator</p>
  <table>
    <thead><tr><th>Name</th><th>Average</th><th>Grade</th><th>Status</th></tr></thead>
    <tbody>
$rows
    </tbody>
  </table>
  <div class="summary">
    <span>Total: ${results.size}</span>
    <span style="color:#00e676">Pass: $passCount</span>
    <span style="color:#ff5252">Fail: $failCount</span>
  </div>
</body>
</html>""".trimIndent()
        )
    }.isSuccess

    // ── XML Export ─────────────────────────────────────────────────────────

    /**
     * Exports results as structured XML.
     * No external library needed — pure string building.
     */
    fun exportXml(
        results: List<Pair<Student, GradeResult>>,
        output: File
    ): Boolean = runCatching {
        val passCount = results.count { it.second is GradeResult.Pass }
        val failCount = results.count { it.second is GradeResult.Fail }

        // Lambda — build each student XML block
        val studentNodes = results.joinToString("\n") { (student, result) ->
            val (avg, grade, status) = when (result) {
                is GradeResult.Pass -> Triple("%.1f".format(result.average), result.grade, "PASS")
                is GradeResult.Fail -> Triple("%.1f".format(result.average), result.grade, "FAIL")
                is GradeResult.NoScores -> Triple("N/A", "-", "NO_SCORES")
            }
            """    <student id="${student.id}">
        <name>${student.name}</name>
        <scores>${student.scores.joinToString(", ")}</scores>
        <average>$avg</average>
        <grade>$grade</grade>
        <status>$status</status>
    </student>"""
        }

        output.writeText(
            """<?xml version="1.0" encoding="UTF-8"?>
<gradeReport>
  <summary>
    <total>${results.size}</total>
    <pass>$passCount</pass>
    <fail>$failCount</fail>
    <scale>ICT University</scale>
  </summary>
  <students>
$studentNodes
  </students>
</gradeReport>"""
        )
    }.isSuccess
}