# Kotlin Assignments & Projects

**Course:** SE 3242 – Android Application Development  
**Institution:** ICT University, Cameroon  
**Student:** Fanyi Charllson  
**Collaborator:** Adrien Tello

---

## Repository Overview

This repository contains a collection of **Kotlin programming exercises and projects** developed as part of the Android Application Development course.

The goal of these exercises is to demonstrate understanding of:

- Kotlin syntax and functional programming
- Higher-order functions and lambdas
- Collection transformations
- Data processing with Kotlin collections
- Desktop application development using Kotlin
- Android development concepts

Each assignment focuses on specific concepts used in modern Kotlin and Android development.

---

# Projects and Exercises

## 1. Grade Calculator Desktop Application

### Overview

A **Kotlin Multiplatform Desktop Application (JVM)** built with modern UI principles to calculate student grades.

The application allows instructors or students to:

- Import student scores from Excel files
- Automatically calculate grades
- Apply the ICT University grading scale
- Export results for reporting

### Key Features

- Excel data import
- Automatic grade calculation
- User-friendly desktop interface
- Exportable results

### Technologies Used

- Kotlin JVM
- Desktop UI architecture
- Data processing logic

### Purpose

This project demonstrates how Kotlin can be used to build **real-world desktop applications** for academic environments.

---

## 2. Kotlin Basics – Exercise One

**Topic:** Lambdas & Higher-Order Functions

### Objective

Implement a higher-order function called `processList` that accepts:

- a list of integers
- a lambda predicate

The function returns only the elements that satisfy the condition.

---

### Concepts Demonstrated

#### Higher-Order Functions

A higher-order function is a function that takes another function as a parameter.
```kotlin
fun processList(list: List<Int>, predicate: (Int) -> Boolean): List<Int>
```

#### Lambda Expressions

Lambda expressions allow functions to be passed as arguments.
```kotlin
processList(nums) { it % 2 == 0 }
```

#### Kotlin `filter`

Internally this logic behaves similarly to Kotlin's built-in `filter` function.

---

### Example

Input list:
```
[1, 2, 3, 4, 5, 6]
```

Predicate:
```
it % 2 == 0
```

Output:
```
[2, 4, 6]
```

---

### Key Takeaway

This exercise demonstrates how **higher-order functions and lambdas enable flexible and reusable code in Kotlin.**

---

## 3. Kotlin Basics – Exercise Two

**Topic:** Collections & Functional Transformations

### Objective

Transform a list of strings into a Map where:

- the **key** is the word
- the **value** is the length of the word

Then print only entries where the length is greater than **4**.

---

### Concepts Demonstrated

#### associateWith()

Converts a list into a map where each element becomes a key.
```kotlin
words.associateWith { it.length }
```

Example result:

| Word    | Length |
| ------- | ------ |
| apple   | 5      |
| cat     | 3      |
| banana  | 6      |

---

#### Map Filtering

Filtering map entries using Kotlin's `filter`.
```kotlin
filter { it.value > 4 }
```

---

### Expected Output
```
apple has length 5
banana has length 6
elephant has length 8
```

---

### Key Takeaway

Kotlin collections provide powerful transformation functions that make data processing concise and expressive.

---

## 4. Kotlin Basics – Exercise Three

**Topic:** Data Classes & Collection Processing

### Objective

Calculate the **average age** of people whose names start with **A or B** from a list of `Person` objects.

---

### Data Model
```kotlin
data class Person(val name: String, val age: Int)
```

---

### Processing Pipeline
```
Start list
   ↓
Filter names starting with A or B
   ↓
Extract ages
   ↓
Compute average
   ↓
Format output
```

---

### Example Data
```
Alice/25
Bob/30
Charlie/35
Anna/22
Ben/28
```

Filtered:
```
Alice, Bob, Anna, Ben
```

Average age:
```
26.3
```

---

### Key Takeaway

This exercise demonstrates how **Kotlin's collection operations (`filter`, `map`, `average`) enable clean and readable data processing pipelines.**

---

## 5. ICTU Exchange – Course Project

### Overview

**ICTU Exchange** is a conceptual Android marketplace designed specifically for students of **The ICT University, Cameroon**.

The platform enables students to:

- Buy and sell academic resources
- Exchange electronics and study materials
- Connect within a trusted campus marketplace

---

### Problem Addressed

Students often struggle with:

- High cost of academic materials
- Limited access to reliable online marketplaces
- Poor internet connectivity

---

### Solution

ICTU Exchange introduces:

- A **student-only digital marketplace**
- **Offline-first application design**
- Integration with device sensors for enhanced functionality

---

### Project Goal

The goal of this project is to explore how Android applications can solve **real campus problems using modern mobile development practices.**

---

# Repository Structure
```
kotlin-assignments-projects
│
├── grade-calculator-desktop-app
├── kotlin-basics-exercise-one
├── kotlin-basics-exercise-two
├── kotlin-basics-exercise-three
└── ictu-exchange
```

Each folder contains the implementation for a specific assignment or project.

---

# Collaboration Workflow

This repository follows a collaborative development workflow using:

- Git branches
- Pull Requests
- Code Reviews

Each feature or assignment is developed in a separate branch and merged through Pull Requests after review.

This workflow simulates **real-world software development practices used in professional teams.**

---

# Authors

**Fanyi Charllson**  
Software Developer | Android & Backend Development

**Adrien Tello**  
Collaborator – Code Review & Project Contributions

---

# License

This repository is intended for **educational purposes as part of coursework at ICT University**.