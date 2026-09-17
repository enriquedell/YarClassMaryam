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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
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

private fun today(): String {
    return SimpleDateFormat("yyyy/MM/dd", Locale.US).format(Date())
}

private fun newId(): Long = System.currentTimeMillis()

class AppStorage(context: Context) {

    private val prefs =
        context.getSharedPreferences("yar_class_storage", Context.MODE_PRIVATE)

    fun loadStudents(): MutableList<Student> {
        val result = mutableListOf<Student>()
        val array = JSONArray(prefs.getString("students", "[]") ?: "[]")

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

        prefs.edit().putString("students", array.toString()).apply()
    }

    fun loadAttendance(): MutableList<AttendanceRecord> {
        val result = mutableListOf<AttendanceRecord>()
        val array = JSONArray(prefs.getString("attendance", "[]") ?: "[]")

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
        return result
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

        prefs.edit().putString("attendance", array.toString()).apply()
    }

    fun loadEvaluations(): MutableList<Evaluation> {
        val result = mutableListOf<Evaluation>()
        val array = JSONArray(prefs.getString("evaluations", "[]") ?: "[]")

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
        return result
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

        prefs.edit().putString("evaluations", array.toString()).apply()
    }

    fun loadSchedule(): MutableList<ScheduleItem> {
        val result = mutableListOf<ScheduleItem>()
        val array = JSONArray(prefs.getString("schedule", "[]") ?: "[]")

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
        return result
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

        prefs.edit().putString("schedule", array.toString()).apply()
    }

    fun loadHomework(): MutableList<Homework> {
        val result = mutableListOf<Homework>()
        val array = JSONArray(prefs.getString("homework", "[]") ?: "[]")

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

        prefs.edit().putString("homework", array.toString()).apply()
    }

    fun loadExams(): MutableList<Exam> {
        val result = mutableListOf<Exam>()
        val array = JSONArray(prefs.getString("exams", "[]") ?: "[]")

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

        prefs.edit().putString("exams", array.toString()).apply()
    }

    fun loadQuestions(): MutableList<ExamQuestion> {
        val result = mutableListOf<ExamQuestion>()
        val array = JSONArray(prefs.getString("questions", "[]") ?: "[]")

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

        prefs.edit().putString("questions", array.toString()).apply()
    }

    fun exportBackup(): String {
        return JSONObject().apply {
            put("students", JSONArray(prefs.getString("students", "[]")))
            put("attendance", JSONArray(prefs.getString("attendance", "[]")))
            put("evaluations", JSONArray(prefs.getString("evaluations", "[]")))
            put("schedule", JSONArray(prefs.getString("schedule", "[]")))
            put("homework", JSONArray(prefs.getString("homework", "[]")))
            put("exams", JSONArray(prefs.getString("exams", "[]")))
            put("questions", JSONArray(prefs.getString("questions", "[]")))
        }.toString()
    }

    fun restoreBackup(text: String): Boolean {
        return try {
            val o = JSONObject(text)

            prefs.edit()
                .putString("students", o.getJSONArray("students").toString())
                .putString("attendance", o.getJSONArray("attendance").toString())
                .putString("evaluations", o.getJSONArray("evaluations").toString())
                .putString("schedule", o.getJSONArray("schedule").toString())
                .putString("homework", o.getJSONArray("homework").toString())
                .putString("exams", o.getJSONArray("exams").toString())
                .putString("questions", o.getJSONArray("questions").toString())
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
                androidx.compose.ui.platform.LocalLayoutDirection provides LayoutDirection.Rtl
            ) {
                YarClassApp()
            }
        }
    }
}

@Composable
fun YarClassApp() {

    var loggedIn by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Burgundy,
            secondary = Gold,
            background = Cream,
            surface = Color.White
        )
    ) {

        if (!loggedIn) {

            LoginScreen(
                password = password,
                onPasswordChange = { password = it },
                onLogin = {
                    if (password == "1234") {
                        loggedIn = true
                    }
                }
            )

        } else {

            HomeScreen(
                onLogout = {
                    loggedIn = false
                    password = ""
                }
            )
        }
    }
}

@Composable
fun LoginScreen(
    password: String,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🌙",
                fontSize = 60.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ماه زیبا، سلام",
                color = Burgundy,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "به یارِ کلاس مریم خوش آمدید",
                color = Brown,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            Card(
                modifier = Modifier.size(150.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Burgundy
                )
            ) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "م",
                        color = Color.White,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("رمز ورود") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "رمز"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                )
            ) {
                Text(
                    "ورود به برنامه",
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "رمز اولیه: 1234",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

data class MenuItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {

    var selected by remember { mutableStateOf<String?>(null) }

    val menuItems = listOf(
        MenuItem("دانش‌آموزان", Icons.Default.Person),
        MenuItem("حضور و غیاب", Icons.Default.CheckCircle),
        MenuItem("ارزشیابی توصیفی", Icons.Default.Star),
        MenuItem("برنامه هفتگی", Icons.Default.DateRange),
        MenuItem("آزمون‌ساز", Icons.Default.Edit),
        MenuItem("تکالیف و یادداشت‌ها", Icons.Default.Edit),
        MenuItem("گزارش‌ها", Icons.Default.CheckCircle),
        MenuItem("خط تحریری", Icons.Default.Edit),
        MenuItem("پشتیبان‌گیری", Icons.Default.CheckCircle)
    )

    if (selected != null) {

        when (selected) {

            "دانش‌آموزان" ->
                StudentsScreen { selected = null }

            "حضور و غیاب" ->
                AttendanceScreen { selected = null }

            "ارزشیابی توصیفی" ->
                EvaluationScreen { selected = null }

            "برنامه هفتگی" ->
                ScheduleScreen { selected = null }

            "آزمون‌ساز" ->
                ExamScreen { selected = null }

            "تکالیف و یادداشت‌ها" ->
                HomeworkScreen { selected = null }

            "گزارش‌ها" ->
                ReportsScreen { selected = null }

            "خط تحریری" ->
                HandwritingScreen { selected = null }

            "پشتیبان‌گیری" ->
                BackupScreen { selected = null }
        }

        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "یارِ کلاس مریم",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text(
                            "خروج",
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Card(
                        modifier = Modifier.size(90.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Burgundy
                        )
                    ) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                "م",
                                color = Color.White,
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {

                        Text(
                            "مریم شجاعی",
                            color = Burgundy,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "دستیار هوشمند معلم پایه ششم",
                            color = Brown,
                            fontSize = 14.sp
                        )

                        Text(
                            "مدیریت کلاس • دانش‌آموزان • ارزیابی",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                "ابزارهای کلاس",
                color = Burgundy,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                items(menuItems) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clickable {
                                selected = item.title
                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                item.icon,
                                contentDescription = item.title,
                                tint = Gold,
                                modifier = Modifier.size(38.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                item.title,
                                color = Brown,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- دانش‌آموزان ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

    var students by remember {
        mutableStateOf(storage.loadStudents())
    }

    var search by remember { mutableStateOf("") }
    var showAdd by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var family by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    val filtered = students.filter {
        "${it.name} ${it.family} ${it.code}".contains(search)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("دانش‌آموزان") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("جستجوی دانش‌آموز") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    name = ""
                    family = ""
                    code = ""
                    showAdd = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                )
            ) {
                Text("＋ افزودن دانش‌آموز")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "تعداد دانش‌آموزان: ${students.size}",
                color = Burgundy,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                items(filtered, key = { it.id }) { student ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
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
                                    "${student.name} ${student.family}",
                                    color = Brown,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    "کد دانش‌آموزی: ${student.code}",
                                    color = Color.Gray
                                )
                            }

                            TextButton(
                                onClick = {
                                    students =
                                        students.filter { it.id != student.id }
                                            .toMutableList()
                                    storage.saveStudents(students)
                                }
                            ) {
                                Text(
                                    "حذف",
                                    color = Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {

        AlertDialog(
            onDismissRequest = {
                showAdd = false
            },
            title = {
                Text("افزودن دانش‌آموز")
            },
            text = {

                Column {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("نام") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = family,
                        onValueChange = { family = it },
                        label = { Text("نام خانوادگی") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("کد دانش‌آموزی") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        if (name.isNotBlank() && family.isNotBlank()) {

                            val newStudent = Student(
                                newId(),
                                name,
                                family,
                                code
                            )

                            students =
                                (students + newStudent).toMutableList()

                            storage.saveStudents(students)

                            showAdd = false
                        }
                    }
                ) {
                    Text("ذخیره")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showAdd = false
                    }
                ) {
                    Text("انصراف")
                }
            }
        )
    }
}

/* ---------------- حضور و غیاب ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

    var students by remember {
        mutableStateOf(storage.loadStudents())
    }

    var records by remember {
        mutableStateOf(storage.loadAttendance())
    }

    var date by remember { mutableStateOf(today()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("حضور و غیاب") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("تاریخ") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (students.isEmpty()) {

                EmptyMessage("ابتدا از بخش دانش‌آموزان، دانش‌آموز اضافه کنید.")

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(students, key = { it.id }) { student ->

                        val current =
                            records.find {
                                it.studentId == student.id &&
                                        it.date == date
                            }?.status

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(
                                    "${student.name} ${student.family}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Brown
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(6.dp)
                                ) {

                                    AttendanceButton(
                                        "حاضر",
                                        current == "حاضر",
                                        Green
                                    ) {
                                        records = setAttendance(
                                            records,
                                            student.id,
                                            date,
                                            "حاضر"
                                        )
                                        storage.saveAttendance(records)
                                    }

                                    AttendanceButton(
                                        "غایب",
                                        current == "غایب",
                                        Red
                                    ) {
                                        records = setAttendance(
                                            records,
                                            student.id,
                                            date,
                                            "غایب"
                                        )
                                        storage.saveAttendance(records)
                                    }

                                    AttendanceButton(
                                        "تأخیر",
                                        current == "تأخیر",
                                        Gold
                                    ) {
                                        records = setAttendance(
                                            records,
                                            student.id,
                                            date,
                                            "تأخیر"
                                        )
                                        storage.saveAttendance(records)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun setAttendance(
    records: List<AttendanceRecord>,
    studentId: Long,
    date: String,
    status: String
): MutableList<AttendanceRecord> {

    val result =
        records.filterNot {
            it.studentId == studentId && it.date == date
        }.toMutableList()

    result.add(
        AttendanceRecord(
            studentId,
            date,
            status
        )
    )

    return result
}

@Composable
fun AttendanceButton(
    title: String,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (selected) color else Color.LightGray
        )
    ) {
        Text(
            title,
            color =
                if (selected) Color.White else Brown
        )
    }
}

/* ---------------- ارزشیابی ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

    var students by remember {
        mutableStateOf(storage.loadStudents())
    }

    var evaluations by remember {
        mutableStateOf(storage.loadEvaluations())
    }

    var selectedStudent by remember { mutableStateOf<Long?>(null) }
    var lesson by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("خوب") }
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ارزشیابی توصیفی") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                "انتخاب دانش‌آموز",
                color = Burgundy,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (students.isEmpty()) {

                EmptyMessage("هنوز دانش‌آموزی ثبت نشده است.")

            } else {

                LazyColumn(
                    modifier = Modifier.height(190.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    items(students, key = { it.id }) { student ->

                        OutlinedButton(
                            onClick = {
                                selectedStudent = student.id
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                if (selectedStudent == student.id)
                                    "✓ ${student.name} ${student.family}"
                                else
                                    "${student.name} ${student.family}"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = lesson,
                    onValueChange = { lesson = it },
                    label = { Text("درس") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "سطح عملکرد",
                    color = Brown,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(5.dp)
                ) {

                    listOf(
                        "عالی",
                        "خیلی خوب",
                        "خوب",
                        "نیازمند تلاش"
                    ).forEach { item ->

                        OutlinedButton(
                            onClick = {
                                level = item
                            }
                        ) {
                            Text(
                                if (level == item)
                                    "✓ $item"
                                else item,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("توضیحات") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {

                        if (
                            selectedStudent != null &&
                            lesson.isNotBlank()
                        ) {

                            evaluations =
                                (
                                    evaluations + Evaluation(
                                        newId(),
                                        selectedStudent!!,
                                        lesson,
                                        level,
                                        note,
                                        today()
                                    )
                                ).toMutableList()

                            storage.saveEvaluations(evaluations)

                            lesson = ""
                            note = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Burgundy
                    )
                ) {
                    Text("ثبت ارزشیابی")
                }
            }
        }
    }
}

/* ---------------- برنامه هفتگی ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

    var list by remember {
        mutableStateOf(storage.loadSchedule())
    }

    var showAdd by remember { mutableStateOf(false) }
    var day by remember { mutableStateOf("شنبه") }
    var time by remember { mutableStateOf("") }
    var lesson by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("برنامه هفتگی") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            Button(
                onClick = {
                    showAdd = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                )
            ) {
                Text("＋ افزودن برنامه")
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(list, key = { it.id }) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    item.day,
                                    color = Burgundy,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    "${item.time} — ${item.lesson}",
                                    color = Brown
                                )
                            }

                            TextButton(
                                onClick = {

                                    list =
                                        list.filterNot {
                                            it.id == item.id
                                        }.toMutableList()

                                    storage.saveSchedule(list)
                                }
                            ) {
                                Text(
                                    "حذف",
                                    color = Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {

        AlertDialog(
            onDismissRequest = {
                showAdd = false
            },
            title = {
                Text("برنامه جدید")
            },
            text = {

                Column {

                    OutlinedTextField(
                        value = day,
                        onValueChange = { day = it },
                        label = { Text("روز") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("ساعت") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = lesson,
                        onValueChange = { lesson = it },
                        label = { Text("درس") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        if (lesson.isNotBlank()) {

                            list =
                                (
                                    list + ScheduleItem(
                                        newId(),
                                        day,
                                        time,
                                        lesson
                                    )
                                ).toMutableList()

                            storage.saveSchedule(list)

                            showAdd = false
                            lesson = ""
                            time = ""
                        }
                    }
                ) {
                    Text("ذخیره")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAdd = false
                    }
                ) {
                    Text("انصراف")
                }
            }
        )
    }
}

/* ---------------- تکالیف ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeworkScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

    var list by remember {
        mutableStateOf(storage.loadHomework())
    }

    var showAdd by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(today()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("تکالیف و یادداشت‌ها") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            Button(
                onClick = {
                    showAdd = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                )
            ) {
                Text("＋ تکلیف جدید")
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(list, key = { it.id }) { homework ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
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
                                checked = homework.done,
                                onCheckedChange = {

                                    list =
                                        list.map {
                                            if (it.id == homework.id)
                                                it.copy(done = !it.done)
                                            else it
                                        }.toMutableList()

                                    storage.saveHomework(list)
                                }
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    homework.title,
                                    color = Brown,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    homework.description,
                                    color = Color.Gray
                                )

                                Text(
                                    homework.date,
                                    color = Gold,
                                    fontSize = 12.sp
                                )
                            }

                            TextButton(
                                onClick = {

                                    list =
                                        list.filterNot {
                                            it.id == homework.id
                                        }.toMutableList()

                                    storage.saveHomework(list)
                                }
                            ) {
                                Text(
                                    "حذف",
                                    color = Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {

        AlertDialog(
            onDismissRequest = {
                showAdd = false
            },
            title = {
                Text("تکلیف جدید")
            },
            text = {

                Column {

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("عنوان") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("توضیحات") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("تاریخ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        if (title.isNotBlank()) {

                            list =
                                (
                                    list + Homework(
                                        newId(),
                                        title,
                                        description,
                                        date,
                                        false
                                    )
                                ).toMutableList()

                            storage.saveHomework(list)

                            title = ""
                            description = ""
                            showAdd = false
                        }
                    }
                ) {
                    Text("ذخیره")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showAdd = false
                    }
                ) {
                    Text("انصراف")
                }
            }
        )
    }
}

/* ---------------- آزمون ساز ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

    var exams by remember {
        mutableStateOf(storage.loadExams())
    }

    var questions by remember {
        mutableStateOf(storage.loadQuestions())
    }

    var selectedExam by remember { mutableStateOf<Exam?>(null) }

    var showAddExam by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

    if (selectedExam != null) {

        ExamQuestionsScreen(
            exam = selectedExam!!,
            questions = questions,
            onQuestionsChanged = {
                questions = it
                storage.saveQuestions(it)
            },
            onBack = {
                selectedExam = null
            }
        )

        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("آزمون‌ساز") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            Button(
                onClick = {
                    title = ""
                    subject = ""
                    showAddExam = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                )
            ) {
                Text("＋ ساخت آزمون")
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(exams, key = { it.id }) { exam ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedExam = exam
                            },
                        shape = RoundedCornerShape(16.dp),
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
                                    exam.title,
                                    color = Burgundy,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    "درس: ${exam.subject}",
                                    color = Brown
                                )

                                Text(
                                    "برای افزودن سؤال، روی آزمون بزنید.",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

                            TextButton(
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
                                Text(
                                    "حذف",
                                    color = Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddExam) {

        AlertDialog(
            onDismissRequest = {
                showAddExam = false
            },
            title = {
                Text("ساخت آزمون")
            },
            text = {

                Column {

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("عنوان آزمون") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("درس") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        if (title.isNotBlank()) {

                            exams =
                                (
                                    exams + Exam(
                                        newId(),
                                        title,
                                        subject
                                    )
                                ).toMutableList()

                            storage.saveExams(exams)
                            showAddExam = false
                        }
                    }
                ) {
                    Text("ساخت")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showAddExam = false
                    }
                ) {
                    Text("انصراف")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamQuestionsScreen(
    exam: Exam,
    questions: List<ExamQuestion>,
    onQuestionsChanged: (MutableList<ExamQuestion>) -> Unit,
    onBack: () -> Unit
) {

    var showAdd by remember { mutableStateOf(false) }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }

    val list =
        questions.filter { it.examId == exam.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(exam.title) },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                "تعداد سؤال‌ها: ${list.size}",
                color = Burgundy,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    question = ""
                    answer = ""
                    showAdd = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                )
            ) {
                Text("＋ افزودن سؤال")
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(list, key = { it.id }) { q ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    q.question,
                                    color = Brown,
                                    fontWeight = FontWeight.Bold
                                )

                                if (q.answer.isNotBlank()) {
                                    Text(
                                        "پاسخ: ${q.answer}",
                                        color = Green
                                    )
                                }
                            }

                            TextButton(
                                onClick = {

                                    onQuestionsChanged(
                                        questions.filterNot {
                                            it.id == q.id
                                        }.toMutableList()
                                    )
                                }
                            ) {
                                Text(
                                    "حذف",
                                    color = Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {

        AlertDialog(
            onDismissRequest = {
                showAdd = false
            },
            title = {
                Text("سؤال جدید")
            },
            text = {

                Column {

                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("متن سؤال") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text("پاسخ صحیح") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        if (question.isNotBlank()) {

                            onQuestionsChanged(
                                (
                                    questions + ExamQuestion(
                                        newId(),
                                        exam.id,
                                        question,
                                        answer
                                    )
                                ).toMutableList()
                            )

                            showAdd = false
                        }
                    }
                ) {
                    Text("ذخیره")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showAdd = false
                    }
                ) {
                    Text("انصراف")
                }
            }
        )
    }
}

/* ---------------- گزارش‌ها ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

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

    val todayRecords =
        attendance.filter { it.date == today() }

    val present =
        todayRecords.count { it.status == "حاضر" }

    val absent =
        todayRecords.count { it.status == "غایب" }

    val late =
        todayRecords.count { it.status == "تأخیر" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("گزارش‌ها") },
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
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {

                ReportCard(
                    "تعداد دانش‌آموزان",
                    students.size.toString()
                )
            }

            item {

                ReportCard(
                    "حاضر امروز",
                    present.toString()
                )
            }

            item {

                ReportCard(
                    "غایب امروز",
                    absent.toString()
                )
            }

            item {

                ReportCard(
                    "تأخیر امروز",
                    late.toString()
                )
            }

            item {

                ReportCard(
                    "تعداد ارزشیابی‌ها",
                    evaluations.size.toString()
                )
            }

            item {

                ReportCard(
                    "کل تکالیف",
                    homework.size.toString()
                )
            }

            item {

                ReportCard(
                    "تکالیف انجام‌شده",
                    homework.count { it.done }.toString()
                )
            }

            item {

                Text(
                    "وضعیت دانش‌آموزان",
                    color = Burgundy,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(students, key = { it.id }) { student ->

                val studentRecords =
                    todayRecords.filter {
                        it.studentId == student.id
                    }

                val status =
                    studentRecords.firstOrNull()?.status
                        ?: "ثبت نشده"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            "${student.name} ${student.family}",
                            color = Brown,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            status,
                            color = when (status) {
                                "حاضر" -> Green
                                "غایب" -> Red
                                "تأخیر" -> Gold
                                else -> Color.Gray
                            }
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                title,
                color = Brown,
                fontSize = 16.sp
            )

            Text(
                value,
                color = Burgundy,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/* ---------------- خط تحریری ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandwritingScreen(
    onBack: () -> Unit
) {

    var practice by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("خط تحریری") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "تمرین خط تحریری",
                color = Burgundy,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        "متن پیشنهادی:",
                        color = Gold,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "به نام خدا\n\nدانش، چراغ راه زندگی است.\n\nمن با تلاش و پشتکار می‌آموزم.",
                        color = Brown,
                        fontSize = 21.sp,
                        lineHeight = 38.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = practice,
                onValueChange = {
                    practice = it
                },
                label = {
                    Text("اینجا تمرین خود را بنویسید")
                },
                minLines = 6,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "تمرین روزانه حتی چند دقیقه‌ای، به بهتر شدن دست‌خط کمک می‌کند. ✍️",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

/* ---------------- پشتیبان‌گیری ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val storage = remember { AppStorage(context) }

    var backupText by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("پشتیبان‌گیری") },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(18.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        "ذخیره پشتیبان",
                        color = Burgundy,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "اطلاعات دانش‌آموزان، حضور و غیاب، ارزشیابی، برنامه، تکالیف و آزمون‌ها در پشتیبان قرار می‌گیرد.",
                        color = Brown
                    )

                    Spacer(modifier = Modifier.height(12.dp))

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
                        Text("💾 ایجاد و ارسال پشتیبان")
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        "بازیابی اطلاعات",
                        color = Burgundy,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "متن پشتیبان JSON را در کادر زیر قرار دهید و بازیابی را بزنید.",
                        color = Brown
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = backupText,
                        onValueChange = {
                            backupText = it
                        },
                        label = {
                            Text("متن پشتیبان")
                        },
                        minLines = 5,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {

                            val success =
                                storage.restoreBackup(
                                    backupText
                                )

                            message =
                                if (success)
                                    "بازیابی با موفقیت انجام شد."
                                else
                                    "متن پشتیبان معتبر نیست."

                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Burgundy
                        )
                    ) {
                        Text("بازیابی اطلاعات")
                    }

                    if (message.isNotBlank()) {

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            message,
                            color =
                                if (
                                    message.contains("موفقیت")
                                )
                                    Green
                                else Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/* ---------------- ابزارهای عمومی ---------------- */

@Composable
fun EmptyMessage(
    text: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(18.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text,
                color = Brown,
                textAlign = TextAlign.Center
            )
        }
    }
}
