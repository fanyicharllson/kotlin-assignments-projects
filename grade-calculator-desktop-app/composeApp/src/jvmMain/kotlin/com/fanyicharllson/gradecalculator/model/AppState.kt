package com.fanyicharllson.gradecalculator.model


import java.io.File

/**
 * ## AppState — Sealed Class
 * Represents every possible screen/state the app can be in.
 * Used by AppViewModel to drive navigation cleanly.
 */
sealed class AppState {
    /** Initial screen — waiting for user to import a file */
    object Home : AppState()

    /**
     * File imported — showing preview, awaiting user confirmation.
     * @param file     The imported .xlsx file
     * @param students Parsed students ready to preview
     */
    data class Preview(
        val file     : File,
        val students : List<Student>
    ) : AppState()

    /**
     * Loading state — used both during file import and grade calculation.
     * @param message What to show the user on the loading screen.
     */
    data class Calculating(val message: String) : AppState()

    /**
     * Calculation done — showing results with export options.
     * @param file     Original file (needed for Excel re-export)
     * @param results  Pairs of Student → GradeResult
     */
    data class Results(
        val file    : File,
        val results : List<Pair<Student, GradeResult>>
    ) : AppState()

    /**
     * Something went wrong — show error with message.
     * @param message  Human-readable error description
     */
    data class Error(val message: String) : AppState()
}


