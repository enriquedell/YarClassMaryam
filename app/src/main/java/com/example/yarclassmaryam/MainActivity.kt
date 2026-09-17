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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.compositionLocalOf
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

private val Burgundy = Color(0xFF7A1731)
private val Cream = Color(0xFFFFF8EE)
private val Brown = Color(0xFF5D4037)
private val Gold = Color(0xFFD4AF37)
private val Green = Color(0xFF2E7D32)
private val Red = Color(0xFFC62828)

data class Student(
    val id: String,
    val name: String,
    val family: String,
    val code: String
)

data class Attendance(
    val id: String,
    val studentId: String,
    val date: String,
    val status: String
)

data class Evaluation(
    val id: String,
    val studentId: String,
    val date: String,
    val level: String,
    val comment: String
)

data class ScheduleItem(
    val id: String,
    val day: String,
    val lesson: String,
    val time: String
)

data class Homework(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val done: Boolean
)

data class Exam(
    val id: String,
    val title: String,
    val subject: String
)

data class ExamQuestion(
    val id: String,
    val examId: String,
    val question: String,
    val answer: String
)

fun newId(): String = UUID.randomUUID().toString()

fun today(): String =
    SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())

class AppStorage(context: Context) {

    private val prefs =
        context.getSharedPreferences("yar_class_maryam", Context.MODE_PRIVATE)

    private fun put(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    private fun get(key: String): String =
        prefs.getString(key, "[]") ?: "[]"

    fun loadStudents(): MutableList<Student> {
        val result = mutableListOf<Student>()
        val array = JSONArray(get("students"))

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                Student(
                    o.getString("id"),
                    o.getString("name"),
                    o.getString("family"),
                    o.getString("code")
                )
            )
        }
        return result
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

        put("students", array.toString())
    }

    fun loadAttendance(): MutableList<Attendance> {
        val result = mutableListOf<Attendance>()
        val array = JSONArray(get("attendance"))

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                Attendance(
                    o.getString("id"),
                    o.getString("studentId"),
                    o.getString("date"),
                    o.getString("status")
                )
            )
        }
        return result
    }

    fun saveAttendance(list: List<Attendance>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("studentId", it.studentId)
                    put("date", it.date)
                    put("status", it.status)
                }
            )
        }

        put("attendance", array.toString())
    }

    fun loadEvaluations(): MutableList<Evaluation> {
        val result = mutableListOf<Evaluation>()
        val array = JSONArray(get("evaluations"))

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                Evaluation(
                    o.getString("id"),
                    o.getString("studentId"),
                    o.getString("date"),
                    o.getString("level"),
                    o.getString("comment")
                )
            )
        }
        return result
    }

    fun saveEvaluations(list: List<Evaluation>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("studentId", it.studentId)
                    put("date", it.date)
                    put("level", it.level)
                    put("comment", it.comment)
                }
            )
        }

        put("evaluations", array.toString())
    }

    fun loadSchedule(): MutableList<ScheduleItem> {
        val result = mutableListOf<ScheduleItem>()
        val array = JSONArray(get("schedule"))

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                ScheduleItem(
                    o.getString("id"),
                    o.getString("day"),
                    o.getString("lesson"),
                    o.getString("time")
                )
            )
        }
        return result
    }

    fun saveSchedule(list: List<ScheduleItem>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("day", it.day)
                    put("lesson", it.lesson)
                    put("time", it.time)
                }
            )
        }

        put("schedule", array.toString())
    }

    fun loadHomework(): MutableList<Homework> {
        val result = mutableListOf<Homework>()
        val array = JSONArray(get("homework"))

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                Homework(
                    o.getString("id"),
                    o.getString("title"),
                    o.getString("description"),
                    o.getString("date"),
                    o.getBoolean("done")
                )
            )
        }
        return result
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

        put("homework", array.toString())
    }

    fun loadExams(): MutableList<Exam> {
        val result = mutableListOf<Exam>()
        val array = JSONArray(get("exams"))

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                Exam(
                    o.getString("id"),
                    o.getString("title"),
                    o.getString("subject")
                )
            )
        }
        return result
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

        put("exams", array.toString())
    }

    fun loadQuestions(): MutableList<ExamQuestion> {
        val result = mutableListOf<ExamQuestion>()
        val array = JSONArray(get("questions"))

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                ExamQuestion(
                    o.getString("id"),
                    o.getString("examId"),
                    o.getString("question"),
                    o.getString("answer")
                )
            )
        }
        return result
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

        put("questions", array.toString())
    }

    fun exportBackup(): String {
        return JSONObject().apply {
            put("students", JSONArray(get("students")))
            put("attendance", JSONArray(get("attendance")))
            put("evaluations", JSONArray(get("evaluations")))
            put("schedule", JSONArray(get("schedule")))
            put("homework", JSONArray(get("homework")))
            put("exams", JSONArray(get("exams")))
            put("questions", JSONArray(get("questions")))
        }.toString()
    }

    fun restoreBackup(text: String): Boolean {
        return try {
            val o = JSONObject(text)

            put("students", o.optJSONArray("students")?.toString() ?: "[]")
            put("attendance", o.optJSONArray("attendance")?.toString() ?: "[]")
            put("evaluations", o.optJSONArray("evaluations")?.toString() ?: "[]")
            put("schedule", o.optJSONArray("schedule")?.toString() ?: "[]")
            put("homework", o.optJSONArray("homework")?.toString() ?: "[]")
            put("exams", o.optJSONArray("exams")?.toString() ?: "[]")
            put("questions", o.optJSONArray("questions")?.toString() ?: "[]")

            true
        } catch (e: Exception) {
            false
        }
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = AppStorage(this)

        setContent {
            CompositionLocalProvider(
                androidx.compose.ui.platform.LocalLayoutDirection provides
                        LayoutDirection.Rtl
            ) {
                YarClassApp(storage)
            }
        }
    }
}

@Composable
fun YarClassApp(storage: AppStorage) {

    var loggedIn by remember {
        mutableStateOf(false)
    }

    if (!loggedIn) {
        LoginScreen {
            loggedIn = true
        }
    } else {
        HomeScreen(storage)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Burgundy),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "م",
                color = Color.White,
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "یار کلاس مریم",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Burgundy
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "سامانه مدیریت کلاس",
            color = Brown
        )

        Spacer(Modifier.height(25.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("رمز ورود")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            singleLine = true
        )

        if (error) {

            Spacer(Modifier.height(8.dp))

            Text(
                text = "رمز ورود اشتباه است.",
                color = Red
            )
        }

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {

                if (password == "1234") {
                    onLogin()
                } else {
                    error = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Burgundy
            )
        ) {
            Text("ورود")
        }
    }
}

@Composable
fun HomeScreen(
    storage: AppStorage
) {

    var screen by remember {
        mutableStateOf("home")
    }

    when (screen) {

        "students" -> StudentsScreen(
            storage,
            { screen = "home" }
        )

        "attendance" -> AttendanceScreen(
            storage,
            { screen = "home" }
        )

        "evaluation" -> EvaluationScreen(
            storage,
            { screen = "home" }
        )

        "schedule" -> ScheduleScreen(
            storage,
            { screen = "home" }
        )

        "exam" -> ExamScreen(
            storage,
            { screen = "home" }
        )

        "homework" -> HomeworkScreen(
            storage,
            { screen = "home" }
        )

        "reports" -> ReportsScreen(
            storage,
            { screen = "home" }
        )

        "handwriting" -> HandwritingScreen {
            screen = "home"
        }

        "backup" -> BackupScreen(
            storage,
            { screen = "home" }
        )

        else -> HomeMenu {
            screen = it
        }
    }
}

data class MenuItemData(
    val id: String,
    val title: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenu(
    onSelect: (String) -> Unit
) {

    val items = listOf(
        MenuItemData("students", "دانش‌آموزان"),
        MenuItemData("attendance", "حضور و غیاب"),
        MenuItemData("evaluation", "ارزشیابی توصیفی"),
        MenuItemData("schedule", "برنامه هفتگی"),
        MenuItemData("exam", "آزمون‌ساز"),
        MenuItemData("homework", "تکالیف و یادداشت‌ها"),
        MenuItemData("reports", "گزارش‌ها"),
        MenuItemData("handwriting", "خط تحریری"),
        MenuItemData("backup", "پشتیبان‌گیری")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("یار کلاس مریم")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White
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

            items(items) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable {
                            onSelect(item.id)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = item.title,
                            modifier = Modifier.weight(1f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Burgundy
                        )

                        Text(
                            text = "›",
                            fontSize = 28.sp,
                            color = Gold
                        )
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

    val filtered = students.filter {
        "${it.name} ${it.family} ${it.code}"
            .contains(search, ignoreCase = true)
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
                .padding(10.dp)
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = {
                    search = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("جستجوی دانش‌آموز")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

            StudentTableHeader()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(
                    items = filtered,
                    key = {
                        it.id
                    }
                ) { student ->

                    StudentTableRow(
                        student = student,
                        rowNumber = students.indexOf(student) + 1,
                        onDelete = {

                            students =
                                students.filterNot {
                                    it.id == student.id
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

                val student = Student(
                    id = newId(),
                    name = name,
                    family = family,
                    code = code
                )

                students =
                    (students + student).toMutableList()

                storage.saveStudents(students)

                showAdd = false
            }
        )
    }
}

@Composable
fun StudentTableHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Burgundy)
            .padding(vertical = 12.dp, horizontal = 6.dp),
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
            modifier = Modifier.width(70.dp),
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

@Composable
fun StudentTableRow(
    student: Student,
    rowNumber: Int,
    onDelete: () -> Unit
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 11.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${student.name} ${student.family}",
                modifier = Modifier.weight(1f),
                color = Brown,
                textAlign = TextAlign.Right
            )

            Text(
                text = student.code,
                modifier = Modifier.width(70.dp),
                color = Brown,
                textAlign = TextAlign.Center
            )

            Text(
                text = rowNumber.toString(),
                modifier = Modifier.width(55.dp),
                color = Brown,
                textAlign = TextAlign.Center
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

                Spacer(Modifier.height(8.dp))

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

                Spacer(Modifier.height(8.dp))

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
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
                .padding(10.dp)
        ) {

            OutlinedTextField(
                value = date,
                onValueChange = {
                    date = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("تاریخ")
                },
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

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
                        onStatus = { status ->

                            val old =
                                records.find {
                                    it.studentId == student.id &&
                                            it.date == date
                                }

                            records =
                                if (old == null) {

                                    (
                                            records + Attendance(
                                                id = newId(),
                                                studentId = student.id,
                                                date = date,
                                                status = status
                                            )
                                            ).toMutableList()

                                } else {

                                    records.map {
                                        if (it.id == old.id) {
                                            it.copy(
                                                status = status
                                            )
                                        } else {
                                            it
                                        }
                                    }.toMutableList()
                                }

                            storage.saveAttendance(records)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Burgundy)
            .padding(vertical = 10.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "نام و نام خانوادگی",
            modifier = Modifier.weight(1f),
            color = Color.White,
            fontWeight = FontWeight.Bold
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

@Composable
fun AttendanceRow(
    student: Student,
    status: String,
    onStatus: (String) -> Unit
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${student.name} ${student.family}",
                modifier = Modifier.weight(1f),
                color = Brown,
                textAlign = TextAlign.Right
            )

            AttendanceButton(
                text = "حاضر",
                selected = status == "حاضر",
                onClick = {
                    onStatus("حاضر")
                }
            )

            AttendanceButton(
                text = "غایب",
                selected = status == "غایب",
                onClick = {
                    onStatus("غایب")
                }
            )

            AttendanceButton(
                text = "تأخیر",
                selected = status == "تأخیر",
                onClick = {
                    onStatus("تأخیر")
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
                containerColor = Burgundy
            )
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
            )
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

    val students = remember {
        storage.loadStudents()
    }

    var evaluations by remember {
        mutableStateOf(storage.loadEvaluations())
    }

    var selectedStudent by remember {
        mutableStateOf<Student?>(null)
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
                .padding(10.dp)
        ) {

            items(students) { student ->

                val last =
                    evaluations
                        .filter {
                            it.studentId == student.id
                        }
                        .lastOrNull()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedStudent = student
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {

                        Text(
                            text = "${student.name} ${student.family}",
                            fontWeight = FontWeight.Bold,
                            color = Burgundy
                        )

                        Text(
                            text = if (last == null) {
                                "هنوز ارزشیابی ثبت نشده"
                            } else {
                                "آخرین سطح: ${last.level}"
                            },
                            color = Brown
                        )
                    }
                }
            }
        }
    }

    selectedStudent?.let { student ->

        AddEvaluationDialog(
            student = student,
            onDismiss = {
                selectedStudent = null
            },
            onSave = { level, comment ->

                val evaluation = Evaluation(
                    id = newId(),
                    studentId = student.id,
                    date = today(),
                    level = level,
                    comment = comment
                )

                evaluations =
                    (evaluations + evaluation).toMutableList()

                storage.saveEvaluations(evaluations)

                selectedStudent = null
            }
        )
    }
}

@Composable
fun AddEvaluationDialog(
    student: Student,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    var level by remember {
        mutableStateOf("")
    }

    var comment by remember {
        mutableStateOf("")
    }

    val levels = listOf(
        "خیلی خوب",
        "خوب",
        "قابل قبول",
        "نیاز به تلاش بیشتر"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "ارزشیابی ${student.name} ${student.family}"
            )
        },
        text = {

            Column {

                levels.forEach { item ->

                    OutlinedButton(
                        onClick = {
                            level = item
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (level == item) {
                                "✓ $item"
                            } else {
                                item
                            }
                        )
                    }

                    Spacer(Modifier.height(4.dp))
                }

                OutlinedTextField(
                    value = comment,
                    onValueChange = {
                        comment = it
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

                    if (level.isNotBlank()) {
                        onSave(
                            level,
                            comment.trim()
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
fun ScheduleScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var list by remember {
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

        if (list.isEmpty()) {

            EmptyMessage(
                text = "برنامه‌ای ثبت نشده است.",
                modifier = Modifier.padding(padding)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(10.dp)
            ) {

                items(
                    items = list,
                    key = {
                        it.id
                    }
                ) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
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
                                    text = item.day,
                                    fontWeight = FontWeight.Bold,
                                    color = Burgundy
                                )

                                Text(
                                    text = item.lesson,
                                    color = Brown
                                )

                                Text(
                                    text = item.time,
                                    color = Color.Gray
                                )
                            }

                            IconButton(
                                onClick = {

                                    list =
                                        list.filterNot {
                                            it.id == item.id
                                        }.toMutableList()

                                    storage.saveSchedule(list)
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

    if (showAdd) {

        AddScheduleDialog(
            onDismiss = {
                showAdd = false
            },
            onSave = { day, lesson, time ->

                val item = ScheduleItem(
                    id = newId(),
                    day = day,
                    lesson = lesson,
                    time = time
                )

                list =
                    (list + item).toMutableList()

                storage.saveSchedule(list)

                showAdd = false
            }
        )
    }
}

@Composable
fun AddScheduleDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {

    var day by remember {
        mutableStateOf("")
    }

    var lesson by remember {
        mutableStateOf("")
    }

    var time by remember {
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

                Spacer(Modifier.height(8.dp))

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

                Spacer(Modifier.height(8.dp))

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
            }
        },
        confirmButton = {

            TextButton(
                onClick = {

                    if (
                        day.isNotBlank() &&
                        lesson.isNotBlank()
                    ) {
                        onSave(
                            day.trim(),
                            lesson.trim(),
                            time.trim()
                        )
                    }
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeworkScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var list by remember {
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

        if (list.isEmpty()) {

            EmptyMessage(
                text = "هنوز تکلیف یا یادداشتی ثبت نشده است.",
                modifier = Modifier.padding(padding)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(10.dp)
            ) {

                items(
                    items = list,
                    key = {
                        it.id
                    }
                ) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
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
                                    text = item.title,
                                    fontWeight = FontWeight.Bold,
                                    color = Burgundy
                                )

                                Text(
                                    text = item.description,
                                    color = Brown
                                )

                                Text(
                                    text = item.date,
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )

                                Text(
                                    text = if (item.done) {
                                        "انجام شده ✓"
                                    } else {
                                        "انجام نشده"
                                    },
                                    color = if (item.done) Green else Red
                                )
                            }

                            Column {

                                Button(
                                    onClick = {

                                        list =
                                            list.map {
                                                if (it.id == item.id) {
                                                    it.copy(
                                                        done = !it.done
                                                    )
                                                } else {
                                                    it
                                                }
                                            }.toMutableList()

                                        storage.saveHomework(list)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Burgundy
                                    )
                                ) {
                                    Text("وضعیت")
                                }

                                IconButton(
                                    onClick = {

                                        list =
                                            list.filterNot {
                                                it.id == item.id
                                            }.toMutableList()

                                        storage.saveHomework(list)
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

                list =
                    (list + item).toMutableList()

                storage.saveHomework(list)

                showAdd = false
            }
        )
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
                    }
                )

                Spacer(Modifier.height(8.dp))

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
            TextButton(onClick = onDismiss) {
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

    var selected by remember {
        mutableStateOf<Exam?>(null)
    }

    var showAdd by remember {
        mutableStateOf(false)
    }

    if (selected != null) {

        ExamQuestionsScreen(
            storage = storage,
            exam = selected!!,
            onBack = {
                selected = null
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
                    .padding(10.dp)
            ) {

                items(
                    items = exams,
                    key = {
                        it.id
                    }
                ) { exam ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selected = exam
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
                                    color = Burgundy
                                )

                                Text(
                                    text = "درس: ${exam.subject}",
                                    color = Brown
                                )

                                Text(
                                    text = "برای مشاهده سؤال‌ها لمس کنید.",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

                            IconButton(
                                onClick = {

                                    exams =
                                        exams.filterNot {
                                            it.id == exam.id
                                        }.toMutableList()

                                    storage.saveExams(exams)

                                    val questions =
                                        storage.loadQuestions()
                                            .filterNot {
                                                it.examId == exam.id
                                            }

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

    if (showAdd) {

        AddExamDialog(
            onDismiss = {
                showAdd = false
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

                showAdd = false
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
                    }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = {
                        subject = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("درس")
                    }
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
            TextButton(onClick = onDismiss) {
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
                text = "هنوز سؤالی ثبت نشده است.",
                modifier = Modifier.padding(padding)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(10.dp)
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
                            .padding(vertical = 4.dp),
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

                                        storage.saveQuestions(
                                            storage.loadQuestions()
                                                .filterNot {
                                                    it.id == question.id
                                                }
                                        )
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

                                Spacer(Modifier.height(6.dp))

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

                storage.saveQuestions(
                    storage.loadQuestions() + newQuestion
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

                Spacer(Modifier.height(8.dp))

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
            TextButton(onClick = onDismiss) {
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
                .padding(10.dp)
        ) {

            item {

                ReportCard(
                    "تعداد دانش‌آموزان",
                    students.size.toString()
                )

                ReportCard(
                    "تعداد ثبت‌های حضور و غیاب",
                    attendance.size.toString()
                )

                ReportCard(
                    "تعداد ارزشیابی‌ها",
                    evaluations.size.toString()
                )

                ReportCard(
                    "تعداد تکالیف",
                    homework.size.toString()
                )

                ReportCard(
                    "تکالیف انجام‌شده",
                    homework.count {
                        it.done
                    }.toString()
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    text = "گزارش حضور و غیاب",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(Modifier.height(8.dp))
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

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "حاضر: $present   غایب: $absent   تأخیر: $late",
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
            .padding(vertical = 4.dp),
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
                color = Brown
            )

            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Burgundy
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp)
        ) {

            item {

                Text(
                    text = "تمرین خط تحریری",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))

                HandwritingLine("به نام خدا")
                HandwritingLine("دانش و کوشش")
                HandwritingLine("موفقیت با تلاش")
                HandwritingLine("دوستی و مهربانی")
                HandwritingLine("آینده روشن است")
                HandwritingLine("من می‌توانم")
                HandwritingLine("با تلاش موفق می‌شوم")
            }
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
            .padding(vertical = 8.dp)
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

        Spacer(Modifier.height(10.dp))

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

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {

                    val text =
                        storage.exportBackup()

                    val intent =
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                text
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

                Text("اشتراک‌گذاری نسخه پشتیبان")
            }

            Spacer(Modifier.height(25.dp))

            Text(
                text = "بازیابی نسخه پشتیبان",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Burgundy
            )

            Spacer(Modifier.height(8.dp))

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
                    Text("متن نسخه پشتیبان")
                }
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {

                    if (backupText.isBlank()) {

                        message =
                            "متن نسخه پشتیبان را وارد کنید."

                    } else {

                        message =
                            if (
                                storage.restoreBackup(
                                    backupText
                                )
                            ) {
                                "بازیابی با موفقیت انجام شد."
                            } else {
                                "متن واردشده معتبر نیست."
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

                Spacer(Modifier.height(10.dp))

                Text(
                    text = message,
                    color =
                        if (message.contains("موفقیت"))
                            Green
                        else
                            Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyMessage(
    text: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Brown,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )
    }
}
