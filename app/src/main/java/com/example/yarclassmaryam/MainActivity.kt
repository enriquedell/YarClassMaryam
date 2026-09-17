package com.example.yarclassmaryam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Cream = Color(0xFFFFF8EC)
private val Burgundy = Color(0xFF7A1830)
private val Gold = Color(0xFFC79A35)
private val Brown = Color(0xFF3B251A)
private val Green = Color(0xFF3E7C59)
private val Red = Color(0xFFB23A48)

data class Student(
    val id: Long,
    val name: String,
    val family: String,
    val code: String
)

data class AttendanceRecord(
    val studentId: Long,
    val date: String,
    val status: String
)

data class Evaluation(
    val id: Long,
    val studentId: Long,
    val lesson: String,
    val level: String,
    val note: String,
    val date: String
)

data class ScheduleItem(
    val id: Long,
    val day: String,
    val time: String,
    val lesson: String
)

data class Homework(
    val id: Long,
    val title: String,
    val description: String,
    val date: String,
    val done: Boolean
)

data class Exam(
    val id: Long,
    val title: String,
    val subject: String
)

data class ExamQuestion(
    val id: Long,
    val examId: Long,
    val question: String,
    val answer: String
)

private fun today(): String =
    SimpleDateFormat("yyyy/MM/dd", Locale.US).format(Date())

private fun newId(): Long = System.currentTimeMillis()

class AppStorage(context: Context) {

    private val prefs =
        context.getSharedPreferences("yar_class_storage", Context.MODE_PRIVATE)

    fun loadStudents(): MutableList<Student> {
        val result = mutableListOf<Student>()
        return try {
            val array = JSONArray(
                prefs.getString("students", "[]") ?: "[]"
            )

            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)

                result.add(
                    Student(
                        o.getLong("id"),
                        o.getString("name"),
                        o.getString("family"),
                        o.getString("code")
                    )
                )
            }

            result
        } catch (_: Exception) {
            result
        }
    }

    fun saveStudents(list: List<Student>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("name", it.name)
                    put("family", it.family)
                    put("code", it.code)
                }
            )
        }

        prefs.edit()
            .putString("students", array.toString())
            .apply()
    }

    fun loadAttendance(): MutableList<AttendanceRecord> {
        val result = mutableListOf<AttendanceRecord>()

        return try {
            val array = JSONArray(
                prefs.getString("attendance", "[]") ?: "[]"
            )

            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)

                result.add(
                    AttendanceRecord(
                        o.getLong("studentId"),
                        o.getString("date"),
                        o.getString("status")
                    )
                )
            }

            result
        } catch (_: Exception) {
            result
        }
    }

    fun saveAttendance(list: List<AttendanceRecord>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("studentId", it.studentId)
                    put("date", it.date)
                    put("status", it.status)
                }
            )
        }

        prefs.edit()
            .putString("attendance", array.toString())
            .apply()
    }

    fun loadEvaluations(): MutableList<Evaluation> {
        val result = mutableListOf<Evaluation>()

        return try {
            val array = JSONArray(
                prefs.getString("evaluations", "[]") ?: "[]"
            )

            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)

                result.add(
                    Evaluation(
                        o.getLong("id"),
                        o.getLong("studentId"),
                        o.getString("lesson"),
                        o.getString("level"),
                        o.getString("note"),
                        o.getString("date")
                    )
                )
            }

            result
        } catch (_: Exception) {
            result
        }
    }

    fun saveEvaluations(list: List<Evaluation>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("studentId", it.studentId)
                    put("lesson", it.lesson)
                    put("level", it.level)
                    put("note", it.note)
                    put("date", it.date)
                }
            )
        }

        prefs.edit()
            .putString("evaluations", array.toString())
            .apply()
    }

    fun loadSchedule(): MutableList<ScheduleItem> {
        val result = mutableListOf<ScheduleItem>()

        return try {
            val array = JSONArray(
                prefs.getString("schedule", "[]") ?: "[]"
            )

            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)

                result.add(
                    ScheduleItem(
                        o.getLong("id"),
                        o.getString("day"),
                        o.getString("time"),
                        o.getString("lesson")
                    )
                )
            }

            result
        } catch (_: Exception) {
            result
        }
    }

    fun saveSchedule(list: List<ScheduleItem>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("day", it.day)
                    put("time", it.time)
                    put("lesson", it.lesson)
                }
            )
        }

        prefs.edit()
            .putString("schedule", array.toString())
            .apply()
    }

    fun loadHomework(): MutableList<Homework> {
        val result = mutableListOf<Homework>()

        return try {
            val array = JSONArray(
                prefs.getString("homework", "[]") ?: "[]"
            )

            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)

                result.add(
                    Homework(
                        o.getLong("id"),
                        o.getString("title"),
                        o.getString("description"),
                        o.getString("date"),
                        o.getBoolean("done")
                    )
                )
            }

            result
        } catch (_: Exception) {
            result
        }
    }

    fun saveHomework(list: List<Homework>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("title", it.title)
                    put("description", it.description)
                    put("date", it.date)
                    put("done", it.done)
                }
            )
        }

        prefs.edit()
            .putString("homework", array.toString())
            .apply()
    }

    fun loadExams(): MutableList<Exam> {
        val result = mutableListOf<Exam>()

        return try {
            val array = JSONArray(
                prefs.getString("exams", "[]") ?: "[]"
            )

            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)

                result.add(
                    Exam(
                        o.getLong("id"),
                        o.getString("title"),
                        o.getString("subject")
                    )
                )
            }

            result
        } catch (_: Exception) {
            result
        }
    }

    fun saveExams(list: List<Exam>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("title", it.title)
                    put("subject", it.subject)
                }
            )
        }

        prefs.edit()
            .putString("exams", array.toString())
            .apply()
    }

    fun loadQuestions(): MutableList<ExamQuestion> {
        val result = mutableListOf<ExamQuestion>()

        return try {
            val array = JSONArray(
                prefs.getString("questions", "[]") ?: "[]"
            )

            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)

                result.add(
                    ExamQuestion(
                        o.getLong("id"),
                        o.getLong("examId"),
                        o.getString("question"),
                        o.getString("answer")
                    )
                )
            }

            result
        } catch (_: Exception) {
            result
        }
    }

    fun saveQuestions(list: List<ExamQuestion>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("examId", it.examId)
                    put("question", it.question)
                    put("answer", it.answer)
                }
            )
        }

        prefs.edit()
            .putString("questions", array.toString())
            .apply()
    }

    fun exportBackup(): String {
        return JSONObject().apply {
            put(
                "students",
                JSONArray(
                    prefs.getString("students", "[]") ?: "[]"
                )
            )

            put(
                "attendance",
                JSONArray(
                    prefs.getString("attendance", "[]") ?: "[]"
                )
            )

            put(
                "evaluations",
                JSONArray(
                    prefs.getString("evaluations", "[]") ?: "[]"
                )
            )

            put(
                "schedule",
                JSONArray(
                    prefs.getString("schedule", "[]") ?: "[]"
                )
            )

            put(
                "homework",
                JSONArray(
                    prefs.getString("homework", "[]") ?: "[]"
                )
            )

            put(
                "exams",
                JSONArray(
                    prefs.getString("exams", "[]") ?: "[]"
                )
            )

            put(
                "questions",
                JSONArray(
                    prefs.getString("questions", "[]") ?: "[]"
                )
            )
        }.toString(2)
    }

    fun restoreBackup(text: String): Boolean {
        return try {
            val root = JSONObject(text)

            prefs.edit()
                .putString(
                    "students",
                    root.optJSONArray("students")?.toString() ?: "[]"
                )
                .putString(
                    "attendance",
                    root.optJSONArray("attendance")?.toString() ?: "[]"
                )
                .putString(
                    "evaluations",
                    root.optJSONArray("evaluations")?.toString() ?: "[]"
                )
                .putString(
                    "schedule",
                    root.optJSONArray("schedule")?.toString() ?: "[]"
                )
                .putString(
                    "homework",
                    root.optJSONArray("homework")?.toString() ?: "[]"
                )
                .putString(
                    "exams",
                    root.optJSONArray("exams")?.toString() ?: "[]"
                )
                .putString(
                    "questions",
                    root.optJSONArray("questions")?.toString() ?: "[]"
                )
                .apply()

            true
        } catch (_: Exception) {
            false
        }
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                androidx.compose.ui.platform.LocalLayoutDirection provides
                    LayoutDirection.Rtl
            ) {
                MaterialTheme(
                    colorScheme = lightColorScheme(
                        primary = Burgundy,
                        secondary = Gold,
                        background = Cream,
                        surface = Color.White
                    )
                ) {
                    YarClassApp()
                }
            }
        }
    }
}

@Composable
fun YarClassApp() {

    val context = LocalContext.current

    val storage = remember {
        AppStorage(context)
    }

    var loggedIn by remember {
        mutableStateOf(false)
    }

    var currentScreen by remember {
        mutableStateOf("home")
    }

    if (!loggedIn) {

        LoginScreen {

            loggedIn = true
            currentScreen = "home"

        }

        return
    }

    when (currentScreen) {

        "home" -> HomeScreen(
            onOpen = {
                currentScreen = it
            },
            onLogout = {
                loggedIn = false
                currentScreen = "home"
            }
        )

        "students" -> StudentsScreen(
            storage = storage,
            onBack = {
                currentScreen = "home"
            }
        )

        "attendance" -> AttendanceScreen(
            storage = storage,
            onBack = {
                currentScreen = "home"
            }
        )

        "evaluation" -> EvaluationScreen(
            storage = storage,
            onBack = {
                currentScreen = "home"
            }
        )

        "schedule" -> ScheduleScreen(
            storage = storage,
            onBack = {
                currentScreen = "home"
            }
        )

        "homework" -> HomeworkScreen(
            storage = storage,
            onBack = {
                currentScreen = "home"
            }
        )

        "exam" -> ExamScreen(
            storage = storage,
            onBack = {
                currentScreen = "home"
            }
        )

        "reports" -> ReportsScreen(
            storage = storage,
            onBack = {
                currentScreen = "home"
            }
        )

        "handwriting" -> HandwritingScreen {
            currentScreen = "home"
        }

        "backup" -> BackupScreen(
            storage = storage,
            onBack = {
                currentScreen = "home"
            }
        )
    }
}

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {

    var password by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            Burgundy,
                            RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "م",
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "یار کلاس مریم",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "ورود به برنامه",
                    color = Brown,
                    fontSize = 16.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        error = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("رمز عبور")
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    singleLine = true
                )

                if (error) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "رمز عبور اشتباه است",
                        color = Red
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {

                        if (password == "1234") {
                            onLogin()
                        } else {
                            error = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Burgundy
                    )
                ) {

                    Text(
                        text = "ورود",
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

data class MenuItem(
    val title: String,
    val key: String,
    val icon: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpen: (String) -> Unit,
    onLogout: () -> Unit
) {

    val menu = listOf(
        MenuItem("دانش‌آموزان", "students", "👩‍🎓"),
        MenuItem("حضور و غیاب", "attendance", "📋"),
        MenuItem("ارزشیابی توصیفی", "evaluation", "⭐"),
        MenuItem("برنامه هفتگی", "schedule", "🗓️"),
        MenuItem("آزمون‌ساز", "exam", "📝"),
        MenuItem("تکالیف و یادداشت‌ها", "homework", "📚"),
        MenuItem("گزارش‌ها", "reports", "📊"),
        MenuItem("خط تحریری", "handwriting", "✍️"),
        MenuItem("پشتیبان‌گیری", "backup", "💾")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "یار کلاس مریم",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {

                    TextButton(
                        onClick = onLogout
                    ) {
                        Text("خروج")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "به برنامه کلاس خوش آمدید 🌷",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Burgundy
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(menu) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                            .clickable {
                                onOpen(item.key)
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = item.icon,
                                fontSize = 32.sp
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = item.title,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = Brown
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var students by remember {
        mutableStateOf(storage.loadStudents())
    }

    var search by remember {
        mutableStateOf("")
    }

    var showAdd by remember {
        mutableStateOf(false)
    }

    var selectedStudent by remember {
        mutableStateOf<Student?>(null)
    }

    val filtered = students.filter {
        val fullName = "${it.name} ${it.family}"
        fullName.contains(search, ignoreCase = true) ||
                it.code.contains(search, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("دانش‌آموزان")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showAdd = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "افزودن"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = {
                    search = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text("جستجوی دانش‌آموز")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            StudentTableHeader()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(filtered) { student ->

                    StudentTableRow(
                        student = student,
                        index = students.indexOf(student) + 1,
                        onClick = {
                            selectedStudent = student
                        },
                        onDelete = {

                            students = students.filter {
                                it.id != student.id
                            }.toMutableList()

                            storage.saveStudents(students)
                        }
                    )
                }
            }
        }
    }

    if (showAdd) {

        AddStudentDialog(
            onDismiss = {
                showAdd = false
            },
            onSave = { name, family, code ->

                val newStudent = Student(
                    id = newId(),
                    name = name,
                    family = family,
                    code = code
                )

                students = (students + newStudent).toMutableList()

                storage.saveStudents(students)

                showAdd = false
            }
        )
    }

    selectedStudent?.let { student ->

        StudentDetailsDialog(
            student = student,
            onDismiss = {
                selectedStudent = null
            }
        )
    }
}

@Composable
fun StudentTableHeader() {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Burgundy,
                    RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp
                    )
                )
                .padding(
                    vertical = 12.dp,
                    horizontal = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "نام و نام خانوادگی",
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right
            )

            Text(
                text = "کد",
                modifier = Modifier.width(75.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "ردیف",
                modifier = Modifier.width(55.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StudentTableRow(
    student: Student,
    index: Int,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 12.dp,
                    horizontal = 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${student.name} ${student.family}",
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                color = Brown,
                textAlign = TextAlign.Right
            )

            Text(
                text = student.code,
                modifier = Modifier.width(75.dp),
                textAlign = TextAlign.Center,
                color = Brown
            )

            Text(
                text = index.toString(),
                modifier = Modifier.width(55.dp),
                textAlign = TextAlign.Center,
                color = Burgundy,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = Red
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gold)
        )
    }
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var family by remember {
        mutableStateOf("")
    }

    var code by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("افزودن دانش‌آموز")
        },
        text = {

            Column {

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("نام")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = family,
                    onValueChange = {
                        family = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("نام خانوادگی")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        code = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("کد دانش‌آموز")
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {

            TextButton(
                onClick = {

                    if (
                        name.isNotBlank() &&
                        family.isNotBlank()
                    ) {
                        onSave(
                            name.trim(),
                            family.trim(),
                            code.trim()
                        )
                    }
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("انصراف")
            }
        }
    )
}

@Composable
fun StudentDetailsDialog(
    student: Student,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("مشخصات دانش‌آموز")
        },
        text = {

            Column {

                Text(
                    text = "نام: ${student.name}",
                    fontSize = 17.sp
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "نام خانوادگی: ${student.family}",
                    fontSize = 17.sp
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "کد دانش‌آموز: ${student.code.ifBlank { "ثبت نشده" }}",
                    fontSize = 17.sp
                )
            }
        },
        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("بستن")
            }
        }
    )
}@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {
    var students by remember {
        mutableStateOf(storage.loadStudents())
    }

    var records by remember {
        mutableStateOf(storage.loadAttendance())
    }

    var date by remember {
        mutableStateOf(today())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("حضور و غیاب")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("تاریخ")
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                OutlinedButton(
                    onClick = {
                        date = today()
                    }
                ) {
                    Text("امروز")
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (students.isEmpty()) {

                EmptyMessage(
                    text = "هنوز دانش‌آموزی ثبت نشده است."
                )

            } else {

                AttendanceHeader()

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(
                        items = students,
                        key = {
                            it.id
                        }
                    ) { student ->

                        val current =
                            records.find {
                                it.studentId == student.id &&
                                        it.date == date
                            }?.status ?: ""

                        AttendanceRow(
                            student = student,
                            status = current,
                            onStatusChange = { newStatus ->

                                val updated =
                                    records.filterNot {
                                        it.studentId == student.id &&
                                                it.date == date
                                    }.toMutableList()

                                updated.add(
                                    AttendanceRecord(
                                        studentId = student.id,
                                        date = date,
                                        status = newStatus
                                    )
                                )

                                records = updated
                                storage.saveAttendance(records)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceHeader() {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Burgundy)
                .padding(
                    vertical = 11.dp,
                    horizontal = 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "نام و نام خانوادگی",
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right
            )

            Text(
                text = "حاضر",
                modifier = Modifier.width(65.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "غایب",
                modifier = Modifier.width(65.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "تأخیر",
                modifier = Modifier.width(65.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AttendanceRow(
    student: Student,
    status: String,
    onStatusChange: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                    horizontal = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${student.name} ${student.family}",
                modifier = Modifier.weight(1f),
                fontSize = 15.sp,
                color = Brown,
                textAlign = TextAlign.Right
            )

            AttendanceButton(
                text = "حاضر",
                selected = status == "حاضر",
                selectedColor = Green,
                onClick = {
                    onStatusChange("حاضر")
                }
            )

            AttendanceButton(
                text = "غایب",
                selected = status == "غایب",
                selectedColor = Red,
                onClick = {
                    onStatusChange("غایب")
                }
            )

            AttendanceButton(
                text = "تأخیر",
                selected = status == "تأخیر",
                selectedColor = Gold,
                onClick = {
                    onStatusChange("تأخیر")
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gold)
        )
    }
}

@Composable
fun AttendanceButton(
    text: String,
    selected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {

    if (selected) {

        Button(
            onClick = onClick,
            modifier = Modifier
                .width(65.dp)
                .padding(horizontal = 2.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 2.dp,
                vertical = 4.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = selectedColor
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = text,
                fontSize = 11.sp
            )
        }

    } else {

        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .width(65.dp)
                .padding(horizontal = 2.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 2.dp,
                vertical = 4.dp
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = text,
                fontSize = 11.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var students by remember {
        mutableStateOf(storage.loadStudents())
    }

    var evaluations by remember {
        mutableStateOf(storage.loadEvaluations())
    }

    var selectedStudent by remember {
        mutableStateOf<Student?>(null)
    }

    var showAdd by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("ارزشیابی توصیفی")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                actions = {

                    IconButton(
                        onClick = {
                            showAdd = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "افزودن"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {

            if (evaluations.isEmpty()) {

                EmptyMessage(
                    text = "هنوز ارزشیابی ثبت نشده است."
                )

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(
                        items = evaluations,
                        key = {
                            it.id
                        }
                    ) { evaluation ->

                        val student =
                            students.find {
                                it.id == evaluation.studentId
                            }

                        EvaluationRow(
                            evaluation = evaluation,
                            studentName =
                                if (student != null)
                                    "${student.name} ${student.family}"
                                else
                                    "دانش‌آموز حذف شده",
                            onDelete = {

                                evaluations =
                                    evaluations.filterNot {
                                        it.id == evaluation.id
                                    }.toMutableList()

                                storage.saveEvaluations(evaluations)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAdd) {

        AddEvaluationDialog(
            students = students,
            onDismiss = {
                showAdd = false
            },
            onSave = { studentId, lesson, level, note ->

                val item = Evaluation(
                    id = newId(),
                    studentId = studentId,
                    lesson = lesson,
                    level = level,
                    note = note,
                    date = today()
                )

                evaluations =
                    (evaluations + item).toMutableList()

                storage.saveEvaluations(evaluations)

                showAdd = false
            }
        )
    }
}

@Composable
fun EvaluationRow(
    evaluation: Evaluation,
    studentName: String,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = studentName,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "درس: ${evaluation.lesson}",
                    color = Brown
                )

                Text(
                    text = "سطح: ${evaluation.level}",
                    color = Brown
                )

                if (evaluation.note.isNotBlank()) {

                    Text(
                        text = "توضیح: ${evaluation.note}",
                        color = Brown
                    )
                }

                Text(
                    text = evaluation.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = Red
                )
            }
        }
    }
}

@Composable
fun AddEvaluationDialog(
    students: List<Student>,
    onDismiss: () -> Unit,
    onSave: (Long, String, String, String) -> Unit
) {

    var selectedStudentId by remember {
        mutableStateOf(
            students.firstOrNull()?.id ?: 0L
        )
    }

    var lesson by remember {
        mutableStateOf("")
    }

    var level by remember {
        mutableStateOf("خیلی خوب")
    }

    var note by remember {
        mutableStateOf("")
    }

    var showStudents by remember {
        mutableStateOf(false)
    }

    val selectedStudent =
        students.find {
            it.id == selectedStudentId
        }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("ثبت ارزشیابی")
        },
        text = {

            Column {

                if (students.isEmpty()) {

                    Text(
                        "ابتدا دانش‌آموز ثبت کنید.",
                        color = Red
                    )

                } else {

                    OutlinedButton(
                        onClick = {
                            showStudents = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            selectedStudent?.let {
                                "${it.name} ${it.family}"
                            } ?: "انتخاب دانش‌آموز"
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    OutlinedTextField(
                        value = lesson,
                        onValueChange = {
                            lesson = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("درس")
                        },
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "سطح عملکرد",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    val levels = listOf(
                        "خیلی خوب",
                        "خوب",
                        "قابل قبول",
                        "نیازمند تلاش"
                    )

                    levels.forEach { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    level = item
                                }
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = level == item,
                                onCheckedChange = {
                                    level = item
                                }
                            )

                            Text(item)
                        }
                    }

                    OutlinedTextField(
                        value = note,
                        onValueChange = {
                            note = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("توضیحات")
                        }
                    )
                }
            }
        },
        confirmButton = {

            TextButton(
                onClick = {

                    if (
                        selectedStudentId != 0L &&
                        lesson.isNotBlank()
                    ) {
                        onSave(
                            selectedStudentId,
                            lesson.trim(),
                            level,
                            note.trim()
                        )
                    }
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("انصراف")
            }
        }
    )

    if (showStudents) {

        AlertDialog(
            onDismissRequest = {
                showStudents = false
            },
            title = {
                Text("انتخاب دانش‌آموز")
            },
            text = {

                LazyColumn {

                    items(students) { student ->

                        Text(
                            text = "${student.name} ${student.family}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    selectedStudentId =
                                        student.id

                                    showStudents = false
                                }
                                .padding(12.dp),
                            fontSize = 17.sp
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Gold)
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var schedule by remember {
        mutableStateOf(storage.loadSchedule())
    }

    var showAdd by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("برنامه هفتگی")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                actions = {

                    IconButton(
                        onClick = {
                            showAdd = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "افزودن"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        if (schedule.isEmpty()) {

            EmptyMessage(
                text = "برنامه‌ای ثبت نشده است.",
                modifier = Modifier.padding(padding)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp)
            ) {

                items(
                    items = schedule,
                    key = {
                        it.id
                    }
                ) { item ->

                    ScheduleRow(
                        item = item,
                        onDelete = {

                            schedule =
                                schedule.filterNot {
                                    it.id == item.id
                                }.toMutableList()

                            storage.saveSchedule(schedule)
                        }
                    )
                }
            }
        }
    }

    if (showAdd) {

        AddScheduleDialog(
            onDismiss = {
                showAdd = false
            },
            onSave = { day, time, lesson ->

                schedule =
                    (
                        schedule + ScheduleItem(
                            id = newId(),
                            day = day,
                            time = time,
                            lesson = lesson
                        )
                    ).toMutableList()

                storage.saveSchedule(schedule)

                showAdd = false
            }
        )
    }
}

@Composable
fun ScheduleRow(
    item: ScheduleItem,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.day,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Text(
                    text = "${item.time} - ${item.lesson}",
                    color = Brown
                )
            }

            IconButton(
                onClick = onDelete
            ) {

                Icon(
                    Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = Red
                )
            }
        }
    }
}

@Composable
fun AddScheduleDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {

    var day by remember {
        mutableStateOf("شنبه")
    }

    var time by remember {
        mutableStateOf("")
    }

    var lesson by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("افزودن برنامه")
        },
        text = {

            Column {

                OutlinedTextField(
                    value = day,
                    onValueChange = {
                        day = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("روز")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = {
                        time = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("ساعت")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = lesson,
                    onValueChange = {
                        lesson = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("درس")
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {

            TextButton(
                onClick = {

                    if (lesson.isNotBlank()) {
                        onSave(
                            day.trim(),
                            time.trim(),
                            lesson.trim()
                        )
                    }
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("انصراف")
            }
        }
    )
}

@Composable
fun EmptyMessage(
    text: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Brown,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )
    }@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeworkScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var homework by remember {
        mutableStateOf(storage.loadHomework())
    }

    var showAdd by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("تکالیف و یادداشت‌ها")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showAdd = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "افزودن"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        if (homework.isEmpty()) {

            EmptyMessage(
                text = "هنوز تکلیف یا یادداشتی ثبت نشده است.",
                modifier = Modifier.padding(padding)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp)
            ) {

                items(
                    items = homework,
                    key = {
                        it.id
                    }
                ) { item ->

                    HomeworkRow(
                        item = item,
                        onToggle = {

                            homework =
                                homework.map {
                                    if (it.id == item.id) {
                                        it.copy(done = !it.done)
                                    } else {
                                        it
                                    }
                                }.toMutableList()

                            storage.saveHomework(homework)
                        },
                        onDelete = {

                            homework =
                                homework.filterNot {
                                    it.id == item.id
                                }.toMutableList()

                            storage.saveHomework(homework)
                        }
                    )
                }
            }
        }
    }

    if (showAdd) {

        AddHomeworkDialog(
            onDismiss = {
                showAdd = false
            },
            onSave = { title, description ->

                val item = Homework(
                    id = newId(),
                    title = title,
                    description = description,
                    date = today(),
                    done = false
                )

                homework =
                    (homework + item).toMutableList()

                storage.saveHomework(homework)

                showAdd = false
            }
        )
    }
}

@Composable
fun HomeworkRow(
    item: Homework,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = item.done,
                onCheckedChange = {
                    onToggle()
                }
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                if (item.description.isNotBlank()) {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = item.description,
                        color = Brown
                    )
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = item.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = onDelete
            ) {

                Icon(
                    Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = Red
                )
            }
        }
    }
}

@Composable
fun AddHomeworkDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("ثبت تکلیف یا یادداشت")
        },
        text = {

            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("عنوان")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("توضیحات")
                    }
                )
            }
        },
        confirmButton = {

            TextButton(
                onClick = {

                    if (title.isNotBlank()) {
                        onSave(
                            title.trim(),
                            description.trim()
                        )
                    }
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("انصراف")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var exams by remember {
        mutableStateOf(storage.loadExams())
    }

    var questions by remember {
        mutableStateOf(storage.loadQuestions())
    }

    var showAddExam by remember {
        mutableStateOf(false)
    }

    var selectedExam by remember {
        mutableStateOf<Exam?>(null)
    }

    if (selectedExam != null) {

        ExamQuestionsScreen(
            storage = storage,
            exam = selectedExam!!,
            onBack = {
                selectedExam = null
                exams = storage.loadExams()
                questions = storage.loadQuestions()
            }
        )

        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("آزمون‌ساز")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                actions = {

                    IconButton(
                        onClick = {
                            showAddExam = true
                        }
                    ) {

                        Icon(
                            Icons.Default.Add,
                            contentDescription = "افزودن آزمون"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        if (exams.isEmpty()) {

            EmptyMessage(
                text = "هنوز آزمونی ساخته نشده است.",
                modifier = Modifier.padding(padding)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp)
            ) {

                items(
                    items = exams,
                    key = {
                        it.id
                    }
                ) { exam ->

                    val count =
                        questions.count {
                            it.examId == exam.id
                        }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clickable {
                                selectedExam = exam
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = exam.title,
                                    fontWeight = FontWeight.Bold,
                                    color = Burgundy,
                                    fontSize = 17.sp
                                )

                                Text(
                                    text = "درس: ${exam.subject}",
                                    color = Brown
                                )

                                Text(
                                    text = "تعداد سؤال: $count",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                            }

                            IconButton(
                                onClick = {

                                    exams =
                                        exams.filterNot {
                                            it.id == exam.id
                                        }.toMutableList()

                                    questions =
                                        questions.filterNot {
                                            it.examId == exam.id
                                        }.toMutableList()

                                    storage.saveExams(exams)
                                    storage.saveQuestions(questions)
                                }
                            ) {

                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "حذف",
                                    tint = Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddExam) {

        AddExamDialog(
            onDismiss = {
                showAddExam = false
            },
            onSave = { title, subject ->

                val exam = Exam(
                    id = newId(),
                    title = title,
                    subject = subject
                )

                exams =
                    (exams + exam).toMutableList()

                storage.saveExams(exams)

                showAddExam = false
            }
        )
    }
}

@Composable
fun AddExamDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var subject by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("ساخت آزمون")
        },
        text = {

            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("عنوان آزمون")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = {
                        subject = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("درس")
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {

            TextButton(
                onClick = {

                    if (
                        title.isNotBlank() &&
                        subject.isNotBlank()
                    ) {

                        onSave(
                            title.trim(),
                            subject.trim()
                        )
                    }
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("انصراف")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamQuestionsScreen(
    storage: AppStorage,
    exam: Exam,
    onBack: () -> Unit
) {

    var questions by remember {
        mutableStateOf(
            storage.loadQuestions()
                .filter {
                    it.examId == exam.id
                }
                .toMutableList()
        )
    }

    var showAdd by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(exam.title)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                actions = {

                    IconButton(
                        onClick = {
                            showAdd = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "افزودن سؤال"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        if (questions.isEmpty()) {

            EmptyMessage(
                text = "هنوز سؤالی برای این آزمون ثبت نشده است.",
                modifier = Modifier.padding(padding)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp)
            ) {

                items(
                    items = questions,
                    key = {
                        it.id
                    }
                ) { question ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = question.question,
                                    modifier = Modifier.weight(1f),
                                    fontWeight = FontWeight.Bold,
                                    color = Burgundy
                                )

                                IconButton(
                                    onClick = {

                                        questions =
                                            questions.filterNot {
                                                it.id == question.id
                                            }.toMutableList()

                                        val all =
                                            storage.loadQuestions()
                                                .filterNot {
                                                    it.id == question.id
                                                }

                                        storage.saveQuestions(all)
                                    }
                                ) {

                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "حذف",
                                        tint = Red
                                    )
                                }
                            }

                            if (question.answer.isNotBlank()) {

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text = "پاسخ: ${question.answer}",
                                    color = Brown
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {

        AddQuestionDialog(
            onDismiss = {
                showAdd = false
            },
            onSave = { question, answer ->

                val newQuestion = ExamQuestion(
                    id = newId(),
                    examId = exam.id,
                    question = question,
                    answer = answer
                )

                questions =
                    (questions + newQuestion).toMutableList()

                val all =
                    storage.loadQuestions()

                storage.saveQuestions(
                    all + newQuestion
                )

                showAdd = false
            }
        )
    }
}

@Composable
fun AddQuestionDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    var question by remember {
        mutableStateOf("")
    }

    var answer by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("افزودن سؤال")
        },
        text = {

            Column {

                OutlinedTextField(
                    value = question,
                    onValueChange = {
                        question = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("سؤال")
                    }
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = answer,
                    onValueChange = {
                        answer = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("پاسخ")
                    }
                )
            }
        },
        confirmButton = {

            TextButton(
                onClick = {

                    if (question.isNotBlank()) {
                        onSave(
                            question.trim(),
                            answer.trim()
                        )
                    }
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("انصراف")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val students = remember {
        storage.loadStudents()
    }

    val attendance = remember {
        storage.loadAttendance()
    }

    val evaluations = remember {
        storage.loadEvaluations()
    }

    val homework = remember {
        storage.loadHomework()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("گزارش‌ها")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {

            item {

                ReportCard(
                    title = "تعداد دانش‌آموزان",
                    value = students.size.toString()
                )

                ReportCard(
                    title = "تعداد ثبت‌های حضور و غیاب",
                    value = attendance.size.toString()
                )

                ReportCard(
                    title = "تعداد ارزشیابی‌ها",
                    value = evaluations.size.toString()
                )

                ReportCard(
                    title = "تعداد تکالیف و یادداشت‌ها",
                    value = homework.size.toString()
                )

                ReportCard(
                    title = "تکالیف انجام‌شده",
                    value = homework.count {
                        it.done
                    }.toString()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "گزارش حضور و غیاب دانش‌آموزان",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            items(students) { student ->

                val records =
                    attendance.filter {
                        it.studentId == student.id
                    }

                val present =
                    records.count {
                        it.status == "حاضر"
                    }

                val absent =
                    records.count {
                        it.status == "غایب"
                    }

                val late =
                    records.count {
                        it.status == "تأخیر"
                    }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            text = "${student.name} ${student.family}",
                            fontWeight = FontWeight.Bold,
                            color = Burgundy
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text = "حاضر: $present    غایب: $absent    تأخیر: $late",
                            color = Brown
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReportCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = Brown,
                fontSize = 16.sp
            )

            Text(
                text = value,
                color = Burgundy,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandwritingScreen(
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("خط تحریری")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "تمرین خط تحریری",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Burgundy
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HandwritingLine(
                text = "به نام خدا"
            )

            HandwritingLine(
                text = "دانش و کوشش"
            )

            HandwritingLine(
                text = "موفقیت با تلاش"
            )

            HandwritingLine(
                text = "دوستی و مهربانی"
            )

            HandwritingLine(
                text = "آینده روشن است"
            )
        }
    }
}

@Composable
fun HandwritingLine(
    text: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {

        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 25.sp,
            color = Brown,
            textAlign = TextAlign.Right
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gold)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gold)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var backupText by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("پشتیبان‌گیری")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Cream
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "تهیه نسخه پشتیبان",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Burgundy
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                onClick = {

                    val backup =
                        storage.exportBackup()

                    val intent =
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                backup
                            )
                        }

                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            "ارسال نسخه پشتیبان"
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                )
            ) {

                Text(
                    "اشتراک‌گذاری نسخه پشتیبان"
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "بازیابی نسخه پشتیبان",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Burgundy
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedTextField(
                value = backupText,
                onValueChange = {
                    backupText = it
                    message = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                label = {
                    Text(
                        "متن نسخه پشتیبان را اینجا وارد کنید"
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                onClick = {

                    if (backupText.isBlank()) {

                        message =
                            "ابتدا متن نسخه پشتیبان را وارد کنید."

                    } else {

                        val success =
                            storage.restoreBackup(
                                backupText
                            )

                        message =
                            if (success) {
                                "نسخه پشتیبان با موفقیت بازیابی شد."
                            } else {
                                "نسخه پشتیبان معتبر نیست."
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                )
            ) {

                Text("بازیابی اطلاعات")
            }

            if (message.isNotBlank()) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = message,
                    color = if (
                        message.contains("موفقیت")
                    ) {
                        Green
                    } else {
                        Red
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
}
