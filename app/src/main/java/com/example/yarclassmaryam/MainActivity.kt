package com.example.yarclassmaryam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

private val Burgundy = Color(0xFF7A1538)
private val DarkBurgundy = Color(0xFF4A0920)
private val Gold = Color(0xFFD4AF37)
private val Cream = Color(0xFFFFF8E7)
private val LightPink = Color(0xFFFFE5EC)
private val LightGreen = Color(0xFFE3F5E8)
private val LightBlue = Color(0xFFE2F0FF)
private val LightYellow = Color(0xFFFFF5C7)

data class Student(
    val id: Long,
    val name: String,
    val code: String
)

data class Attendance(
    val studentId: Long,
    val date: String,
    val status: String
)

data class Evaluation(
    val studentId: Long,
    val lesson: String,
    val level: String,
    val note: String
)

data class ScheduleItem(
    val day: String,
    val period: Int,
    val lesson: String
)

data class ClassRecord(
    val date: String,
    val lesson: String,
    val activity: String,
    val homework: String
)

data class Homework(
    val title: String,
    val lesson: String,
    val level: String,
    val text: String
)

data class ExamQuestion(
    val question: String,
    val options: List<String>,
    val answer: Int
)

data class Exam(
    val title: String,
    val grade: String,
    val lesson: String,
    val questions: List<ExamQuestion>
)

class AppStorage(context: Context) {

    private val prefs =
        context.getSharedPreferences("yar_class_storage", Context.MODE_PRIVATE)

    private fun saveArray(key: String, array: JSONArray) {
        prefs.edit().putString(key, array.toString()).apply()
    }

    private fun readArray(key: String): JSONArray {
        return try {
            JSONArray(prefs.getString(key, "[]") ?: "[]")
        } catch (_: Exception) {
            JSONArray()
        }
    }

    fun saveStudents(list: List<Student>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("name", it.name)
                    put("code", it.code)
                }
            )
        }

        saveArray("students", array)
    }

    fun getStudents(): List<Student> {
        val result = mutableListOf<Student>()
        val array = readArray("students")

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)

            result.add(
                Student(
                    o.optLong("id"),
                    o.optString("name"),
                    o.optString("code")
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
                    put("studentId", it.studentId)
                    put("date", it.date)
                    put("status", it.status)
                }
            )
        }

        saveArray("attendance", array)
    }

    fun getAttendance(): List<Attendance> {
        val result = mutableListOf<Attendance>()
        val array = readArray("attendance")

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)

            result.add(
                Attendance(
                    o.optLong("studentId"),
                    o.optString("date"),
                    o.optString("status")
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
                    put("studentId", it.studentId)
                    put("lesson", it.lesson)
                    put("level", it.level)
                    put("note", it.note)
                }
            )
        }

        saveArray("evaluations", array)
    }

    fun getEvaluations(): List<Evaluation> {
        val result = mutableListOf<Evaluation>()
        val array = readArray("evaluations")

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)

            result.add(
                Evaluation(
                    o.optLong("studentId"),
                    o.optString("lesson"),
                    o.optString("level"),
                    o.optString("note")
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
                    put("day", it.day)
                    put("period", it.period)
                    put("lesson", it.lesson)
                }
            )
        }

        saveArray("schedule", array)
    }

    fun getSchedule(): List<ScheduleItem> {
        val result = mutableListOf<ScheduleItem>()
        val array = readArray("schedule")

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)

            result.add(
                ScheduleItem(
                    o.optString("day"),
                    o.optInt("period"),
                    o.optString("lesson")
                )
            )
        }

        return result
    }

    fun saveRecords(list: List<ClassRecord>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("date", it.date)
                    put("lesson", it.lesson)
                    put("activity", it.activity)
                    put("homework", it.homework)
                }
            )
        }

        saveArray("records", array)
    }

    fun getRecords(): List<ClassRecord> {
        val result = mutableListOf<ClassRecord>()
        val array = readArray("records")

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)

            result.add(
                ClassRecord(
                    o.optString("date"),
                    o.optString("lesson"),
                    o.optString("activity"),
                    o.optString("homework")
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
                    put("title", it.title)
                    put("lesson", it.lesson)
                    put("level", it.level)
                    put("text", it.text)
                }
            )
        }

        saveArray("homework", array)
    }

    fun getHomework(): List<Homework> {
        val result = mutableListOf<Homework>()
        val array = readArray("homework")

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)

            result.add(
                Homework(
                    o.optString("title"),
                    o.optString("lesson"),
                    o.optString("level"),
                    o.optString("text")
                )
            )
        }

        return result
    }

    fun createBackup(): String {

        val root = JSONObject()

        root.put("students", JSONArray().apply {
            getStudents().forEach {
                put(
                    JSONObject().apply {
                        put("id", it.id)
                        put("name", it.name)
                        put("code", it.code)
                    }
                )
            }
        })

        root.put("attendance", JSONArray().apply {
            getAttendance().forEach {
                put(
                    JSONObject().apply {
                        put("studentId", it.studentId)
                        put("date", it.date)
                        put("status", it.status)
                    }
                )
            }
        })

        root.put("evaluations", JSONArray().apply {
            getEvaluations().forEach {
                put(
                    JSONObject().apply {
                        put("studentId", it.studentId)
                        put("lesson", it.lesson)
                        put("level", it.level)
                        put("note", it.note)
                    }
                )
            }
        })

        root.put("schedule", JSONArray().apply {
            getSchedule().forEach {
                put(
                    JSONObject().apply {
                        put("day", it.day)
                        put("period", it.period)
                        put("lesson", it.lesson)
                    }
                )
            }
        })

        root.put("records", JSONArray().apply {
            getRecords().forEach {
                put(
                    JSONObject().apply {
                        put("date", it.date)
                        put("lesson", it.lesson)
                        put("activity", it.activity)
                        put("homework", it.homework)
                    }
                )
            }
        })

        root.put("homework", JSONArray().apply {
            getHomework().forEach {
                put(
                    JSONObject().apply {
                        put("title", it.title)
                        put("lesson", it.lesson)
                        put("level", it.level)
                        put("text", it.text)
                    }
                )
            }
        })

        return root.toString(2)
    }

    fun restoreBackup(json: String): Boolean {

        return try {

            val root = JSONObject(json)

            val students = mutableListOf<Student>()
            val sArray = root.optJSONArray("students") ?: JSONArray()

            for (i in 0 until sArray.length()) {
                val o = sArray.getJSONObject(i)

                students.add(
                    Student(
                        o.optLong("id"),
                        o.optString("name"),
                        o.optString("code")
                    )
                )
            }

            saveStudents(students)

            val attendance = mutableListOf<Attendance>()
            val aArray = root.optJSONArray("attendance") ?: JSONArray()

            for (i in 0 until aArray.length()) {
                val o = aArray.getJSONObject(i)

                attendance.add(
                    Attendance(
                        o.optLong("studentId"),
                        o.optString("date"),
                        o.optString("status")
                    )
                )
            }

            saveAttendance(attendance)

            val evaluations = mutableListOf<Evaluation>()
            val eArray = root.optJSONArray("evaluations") ?: JSONArray()

            for (i in 0 until eArray.length()) {
                val o = eArray.getJSONObject(i)

                evaluations.add(
                    Evaluation(
                        o.optLong("studentId"),
                        o.optString("lesson"),
                        o.optString("level"),
                        o.optString("note")
                    )
                )
            }

            saveEvaluations(evaluations)

            val schedule = mutableListOf<ScheduleItem>()
            val schArray = root.optJSONArray("schedule") ?: JSONArray()

            for (i in 0 until schArray.length()) {
                val o = schArray.getJSONObject(i)

                schedule.add(
                    ScheduleItem(
                        o.optString("day"),
                        o.optInt("period"),
                        o.optString("lesson")
                    )
                )
            }

            saveSchedule(schedule)

            val records = mutableListOf<ClassRecord>()
            val rArray = root.optJSONArray("records") ?: JSONArray()

            for (i in 0 until rArray.length()) {
                val o = rArray.getJSONObject(i)

                records.add(
                    ClassRecord(
                        o.optString("date"),
                        o.optString("lesson"),
                        o.optString("activity"),
                        o.optString("homework")
                    )
                )
            }

            saveRecords(records)

            val homework = mutableListOf<Homework>()
            val hArray = root.optJSONArray("homework") ?: JSONArray()

            for (i in 0 until hArray.length()) {
                val o = hArray.getJSONObject(i)

                homework.add(
                    Homework(
                        o.optString("title"),
                        o.optString("lesson"),
                        o.optString("level"),
                        o.optString("text")
                    )
                )
            }

            saveHomework(homework)

            true

        } catch (_: Exception) {
            false
        }
    }
}

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Cream
            ) {
                YarClassApp(AppStorage(this))
            }
        }
    }
}

@Composable
fun YarClassApp(storage: AppStorage) {

    var loggedIn by remember { mutableStateOf(false) }
    var screen by remember { mutableStateOf("home") }
    var refreshKey by remember { mutableStateOf(0) }

    if (!loggedIn) {

        LoginScreen(
            onLogin = {
                loggedIn = true
                screen = "home"
            }
        )

        return
    }

    when (screen) {

        "home" -> HomeScreen(
            onNavigate = {
                screen = it
            },
            onLogout = {
                loggedIn = false
                screen = "home"
            }
        )

        "students" -> StudentsScreen(
            storage = storage,
            onBack = {
                screen = "home"
            }
        )

        "attendance" -> AttendanceScreen(
            storage = storage,
            onBack = {
                screen = "home"
            }
        )

        "evaluation" -> EvaluationScreen(
            storage = storage,
            onBack = {
                screen = "home"
            }
        )

        "schedule" -> ScheduleScreen(
            storage = storage,
            onBack = {
                screen = "home"
            }
        )

        "homework" -> HomeworkScreen(
            storage = storage,
            onBack = {
                screen = "home"
            }
        )

        "classbook" -> ClassBookScreen(
            storage = storage,
            onBack = {
                screen = "home"
            }
        )

        "reports" -> ReportsScreen(
            storage = storage,
            onBack = {
                screen = "home"
            }
        )

        "exam" -> ExamScreen(
            storage = storage,
            onBack = {
                screen = "home"
            }
        )

        "backup" -> BackupScreen(
            storage = storage,
            onBack = {
                refreshKey++
                screen = "home"
            }
        )

        "handwriting" -> InfoScreen(
            title = "خط تحریری",
            text = "در این بخش می‌توان تمرین‌های خط تحریری دانش‌آموزان را مدیریت کرد.",
            onBack = {
                screen = "home"
            }
        )
    }

    // جلوگیری از حذف refreshKey توسط کامپایلر
    refreshKey.hashCode()
}

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {

    val context = LocalContext.current

    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    fun biometricLogin() {

        val activity = context as? FragmentActivity

        if (activity == null) {
            error = "خطا در شناسایی دستگاه"
            return
        }

        val manager = BiometricManager.from(activity)

        val result = manager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        )

        if (result != BiometricManager.BIOMETRIC_SUCCESS) {
            error = "اثر انگشت یا قابلیت بیومتریک روی دستگاه فعال نیست."
            return
        }

        val executor = activity.mainExecutor

        val prompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    onLogin()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    error = errString.toString()
                }

                override fun onAuthenticationFailed() {
                    error = "اثر انگشت شناسایی نشد."
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("ورود به یار کلاس مریم")
            .setSubtitle("برای ورود، اثر انگشت خود را تأیید کنید")
            .setNegativeButtonText("ورود با رمز")
            .build()

        prompt.authenticate(promptInfo)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Burgundy,
                    modifier = Modifier.height(70.dp)
                )

                Text(
                    text = "یار کلاس مریم",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "سامانه مدیریت کلاس",
                    color = DarkBurgundy
                )

                Spacer(Modifier.height(30.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = {
                        pin = it
                        error = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("رمز ورود")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {

                        if (pin == "1234") {
                            onLogin()
                        } else {
                            error = "رمز ورود اشتباه است."
                        }

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ورود با رمز")
                }

                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        biometricLogin()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(
                        Icons.Default.Fingerprint,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text("ورود با اثر انگشت")
                }

                if (error.isNotEmpty()) {

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = error,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "رمز پیش‌فرض: 1234",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "یار کلاس مریم",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Burgundy,
                    titleContentColor = Color.White
                ),
                actions = {

                    IconButton(
                        onClick = onLogout
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "خروج",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {

                Text(
                    text = "پنل مدیریت کلاس",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "همه اطلاعات کلاس شما در یک مکان",
                    color = Color.DarkGray
                )
            }

            item {
                HomeButton(
                    "دانش‌آموزان",
                    Icons.Default.Person,
                    LightPink
                ) {
                    onNavigate("students")
                }
            }

            item {
                HomeButton(
                    "حضور و غیاب",
                    Icons.Default.CheckCircle,
                    LightGreen
                ) {
                    onNavigate("attendance")
                }
            }

            item {
                HomeButton(
                    "ارزشیابی",
                    Icons.Default.Assessment,
                    LightBlue
                ) {
                    onNavigate("evaluation")
                }
            }

            item {
                HomeButton(
                    "برنامه هفتگی",
                    Icons.Default.Schedule,
                    LightYellow
                ) {
                    onNavigate("schedule")
                }
            }

            item {
                HomeButton(
                    "تکلیف‌ساز",
                    Icons.Default.MenuBook,
                    LightPink
                ) {
                    onNavigate("homework")
                }
            }

            item {
                HomeButton(
                    "دفتر کلاسی",
                    Icons.Default.CalendarMonth,
                    LightGreen
                ) {
                    onNavigate("classbook")
                }
            }

            item {
                HomeButton(
                    "آزمون‌ساز",
                    Icons.Default.Quiz,
                    LightBlue
                ) {
                    onNavigate("exam")
                }
            }

            item {
                HomeButton(
                    "گزارش‌ها",
                    Icons.Default.Assessment,
                    LightYellow
                ) {
                    onNavigate("reports")
                }
            }

            item {
                HomeButton(
                    "خط تحریری",
                    Icons.Default.Edit,
                    LightPink
                ) {
                    onNavigate("handwriting")
                }
            }

            item {
                HomeButton(
                    "پشتیبان‌گیری و بازیابی",
                    Icons.Default.Backup,
                    LightGreen
                ) {
                    onNavigate("backup")
                }
            }

            item {

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text("خروج از حساب")
                }
            }
        }
    }
}

@Composable
fun HomeButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    background: Color,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                icon,
                contentDescription = null,
                tint = Burgundy
            )

            Spacer(Modifier.width(16.dp))

            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBurgundy
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text(title)
                },
                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            Icons.Default.Home,
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun StudentsScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var students by remember {
        mutableStateOf(storage.getStudents())
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var name by remember {
        mutableStateOf("")
    }

    var code by remember {
        mutableStateOf("")
    }

    PageScaffold(
        title = "دانش‌آموزان",
        onBack = onBack
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Button(
                onClick = {
                    name = ""
                    code = ""
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    Icons.Default.PersonAdd,
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text("افزودن دانش‌آموز")
            }

            Spacer(Modifier.height(12.dp))

            if (students.isEmpty()) {

                Text(
                    "هنوز دانش‌آموزی ثبت نشده است.",
                    color = Color.Gray
                )

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        students,
                        key = { it.id }
                    ) { student ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Burgundy
                                )

                                Spacer(Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        student.name,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        "کد: ${student.code}",
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }

                                IconButton(
                                    onClick = {

                                        val updated =
                                            students.filter {
                                                it.id != student.id
                                            }

                                        storage.saveStudents(updated)
                                        students = updated
                                    }
                                ) {

                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "حذف",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
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
                            Text("نام و نام خانوادگی")
                        }
                    )

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = code,
                        onValueChange = {
                            code = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("کد دانش‌آموزی")
                        }
                    )
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        if (name.trim().isNotEmpty()) {

                            val student = Student(
                                id = System.currentTimeMillis(),
                                name = name.trim(),
                                code = code.trim()
                            )

                            val updated =
                                students + student

                            storage.saveStudents(updated)
                            students = updated

                            showDialog = false
                        }
                    }
                ) {
                    Text("ذخیره")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("انصراف")
                }
            }
        )
    }
}

@Composable
fun AttendanceScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var students by remember {
        mutableStateOf(storage.getStudents())
    }

    var attendance by remember {
        mutableStateOf(storage.getAttendance())
    }

    val date = SimpleDateFormat(
        "yyyy/MM/dd",
        Locale.getDefault()
    ).format(Date())

    PageScaffold(
        title = "حضور و غیاب",
        onBack = onBack
    ) {

        if (students.isEmpty()) {

            Text(
                "ابتدا از بخش دانش‌آموزان، دانش‌آموز اضافه کنید."
            )

            return@PageScaffold
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {

                Text(
                    "تاریخ: $date",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(Modifier.height(12.dp))
            }

            items(
                students,
                key = { it.id }
            ) { student ->

                val current =
                    attendance.lastOrNull {
                        it.studentId == student.id &&
                                it.date == date
                    }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            student.name,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {

                            AttendanceButton(
                                "حاضر",
                                current?.status == "حاضر"
                            ) {

                                val filtered =
                                    attendance.filterNot {
                                        it.studentId == student.id &&
                                                it.date == date
                                    }

                                val updated =
                                    filtered + Attendance(
                                        student.id,
                                        date,
                                        "حاضر"
                                    )

                                storage.saveAttendance(updated)
                                attendance = updated
                            }

                            AttendanceButton(
                                "غایب",
                                current?.status == "غایب"
                            ) {

                                val filtered =
                                    attendance.filterNot {
                                        it.studentId == student.id &&
                                                it.date == date
                                    }

                                val updated =
                                    filtered + Attendance(
                                        student.id,
                                        date,
                                        "غایب"
                                    )

                                storage.saveAttendance(updated)
                                attendance = updated
                            }

                            AttendanceButton(
                                "تاخیر",
                                current?.status == "تاخیر"
                            ) {

                                val filtered =
                                    attendance.filterNot {
                                        it.studentId == student.id &&
                                                it.date == date
                                    }

                                val updated =
                                    filtered + Attendance(
                                        student.id,
                                        date,
                                        "تاخیر"
                                    )

                                storage.saveAttendance(updated)
                                attendance = updated
                            }
                        }

                        if (current != null) {

                            Text(
                                "وضعیت: ${current.status}",
                                color = Burgundy,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
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
        ) {
            Text(text)
        }

    } else {

        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
        ) {
            Text(text)
        }
    }
}

@Composable
fun EvaluationScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var students by remember {
        mutableStateOf(storage.getStudents())
    }

    var evaluations by remember {
        mutableStateOf(storage.getEvaluations())
    }

    var selectedStudent by remember {
        mutableStateOf<Student?>(null)
    }

    var menuOpen by remember {
        mutableStateOf(false)
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

    PageScaffold(
        title = "ارزشیابی",
        onBack = onBack
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {

                Box {

                    OutlinedButton(
                        onClick = {
                            menuOpen = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            selectedStudent?.name
                                ?: "انتخاب دانش‌آموز"
                        )
                    }

                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = {
                            menuOpen = false
                        }
                    ) {

                        students.forEach { student ->

                            DropdownMenuItem(
                                text = {
                                    Text(student.name)
                                },
                                onClick = {

                                    selectedStudent = student
                                    menuOpen = false
                                }
                            )
                        }
                    }
                }
            }

            item {

                OutlinedTextField(
                    value = lesson,
                    onValueChange = {
                        lesson = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("درس")
                    }
                )
            }

            item {

                Text(
                    "سطح ارزشیابی",
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    listOf(
                        "عالی",
                        "خیلی خوب",
                        "خوب",
                        "نیازمند تلاش"
                    ).forEach { item ->

                        if (level == item) {

                            Button(
                                onClick = {
                                    level = item
                                }
                            ) {
                                Text(item)
                            }

                        } else {

                            OutlinedButton(
                                onClick = {
                                    level = item
                                }
                            ) {
                                Text(item)
                            }
                        }
                    }
                }
            }

            item {

                OutlinedTextField(
                    value = note,
                    onValueChange = {
                        note = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("توضیحات")
                    },
                    minLines = 3
                )
            }

            item {

                Button(
                    onClick = {

                        if (
                            selectedStudent != null &&
                            lesson.isNotBlank()
                        ) {

                            val updated =
                                evaluations + Evaluation(
                                    selectedStudent!!.id,
                                    lesson,
                                    level,
                                    note
                                )

                            storage.saveEvaluations(updated)
                            evaluations = updated

                            lesson = ""
                            note = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ذخیره ارزشیابی")
                }
            }

            item {

                Divider()

                Text(
                    "ارزشیابی‌های ثبت شده",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )
            }

            items(evaluations) { item ->

                val student =
                    students.firstOrNull {
                        it.id == item.studentId
                    }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            student?.name ?: "دانش‌آموز حذف شده",
                            fontWeight = FontWeight.Bold
                        )

                        Text("درس: ${item.lesson}")
                        Text("سطح: ${item.level}")

                        if (item.note.isNotBlank()) {
                            Text("توضیح: ${item.note}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var schedule by remember {
        mutableStateOf(storage.getSchedule())
    }

    var day by remember {
        mutableStateOf("شنبه")
    }

    var period by remember {
        mutableStateOf("1")
    }

    var lesson by remember {
        mutableStateOf("")
    }

    PageScaffold(
        title = "برنامه هفتگی",
        onBack = onBack
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            OutlinedTextField(
                value = day,
                onValueChange = {
                    day = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("روز")
                }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = period,
                onValueChange = {
                    period = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("زنگ")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
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
                }
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {

                    if (lesson.isNotBlank()) {

                        val updated =
                            schedule + ScheduleItem(
                                day,
                                period.toIntOrNull() ?: 1,
                                lesson
                            )

                        storage.saveSchedule(updated)
                        schedule = updated

                        lesson = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ذخیره برنامه")
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(schedule) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier.padding(14.dp)
                        ) {

                            Text(
                                "${item.day} - زنگ ${item.period}",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.width(12.dp))

                            Text(item.lesson)
                        }
                    }
                }
            }
        }
    }
}

fun createSmartHomework(
    lesson: String,
    level: String,
    count: Int
): String {

    val result = StringBuilder()

    val normalized =
        lesson.trim().lowercase(Locale.getDefault())

    for (i in 1..count.coerceIn(1, 20)) {

        val question = when {

            normalized.contains("ریاضی") -> {

                when (level) {

                    "آسان" ->
                        "$i. یک سؤال ساده از مباحث درس ریاضی حل کنید و روش حل را بنویسید."

                    "متوسط" ->
                        "$i. یک مسئله ریاضی مرتبط با درس حل کنید و مراحل حل را کامل بنویسید."

                    else ->
                        "$i. یک مسئله چالشی ریاضی طراحی کنید و پاسخ تشریحی آن را بنویسید."
                }
            }

            normalized.contains("فارسی") -> {

                "$i. یک فعالیت مرتبط با درس فارسی انجام دهید و پاسخ خود را کامل بنویسید."
            }

            normalized.contains("علوم") -> {

                "$i. یک سؤال مفهومی از درس علوم بنویسید و پاسخ دهید."
            }

            else -> {

                "$i. سه نکته مهم از درس $lesson بنویسید."
            }
        }

        result.append(question)
        result.append("\n\n")
    }

    return result.toString()
}

@Composable
fun HomeworkScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var homework by remember {
        mutableStateOf(storage.getHomework())
    }

    var lesson by remember {
        mutableStateOf("")
    }

    var level by remember {
        mutableStateOf("متوسط")
    }

    var count by remember {
        mutableStateOf("5")
    }

    var generated by remember {
        mutableStateOf("")
    }

    PageScaffold(
        title = "تکلیف‌ساز",
        onBack = onBack
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {

                OutlinedTextField(
                    value = lesson,
                    onValueChange = {
                        lesson = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("درس / موضوع")
                    }
                )
            }

            item {

                OutlinedTextField(
                    value = level,
                    onValueChange = {
                        level = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("سطح")
                    }
                )
            }

            item {

                OutlinedTextField(
                    value = count,
                    onValueChange = {
                        count = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("تعداد فعالیت")
                    }
                )
            }

            item {

                Button(
                    onClick = {

                        if (lesson.isNotBlank()) {

                            generated =
                                createSmartHomework(
                                    lesson,
                                    level,
                                    count.toIntOrNull() ?: 5
                                )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ساخت تکلیف")
                }
            }

            if (generated.isNotBlank()) {

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                generated,
                                fontSize = 16.sp
                            )

                            Spacer(Modifier.height(12.dp))

                            Row {

                                Button(
                                    onClick = {

                                        val item =
                                            Homework(
                                                title = "تکلیف $lesson",
                                                lesson = lesson,
                                                level = level,
                                                text = generated
                                            )

                                        val updated =
                                            homework + item

                                        storage.saveHomework(updated)
                                        homework = updated
                                    }
                                ) {

                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null
                                    )

                                    Spacer(Modifier.width(5.dp))

                                    Text("ذخیره")
                                }

                                Spacer(Modifier.width(8.dp))

                                OutlinedButton(
                                    onClick = {

                                        shareText(
                                            context,
                                            "تکلیف درس $lesson\n\n$generated"
                                        )
                                    }
                                ) {

                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = null
                                    )

                                    Spacer(Modifier.width(5.dp))

                                    Text("اشتراک")
                                }
                            }
                        }
                    }
                }
            }

            item {

                Text(
                    "تکلیف‌های ذخیره شده",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )
            }

            items(homework) { item ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {

                        Text(
                            item.title,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            item.text
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {

                                shareText(
                                    context,
                                    "${item.title}\n\n${item.text}"
                                )
                            }
                        ) {

                            Icon(
                                Icons.Default.Send,
                                contentDescription = null
                            )

                            Spacer(Modifier.width(5.dp))

                            Text("ارسال / اشتراک‌گذاری")
                        }
                    }
                }
            }
        }
    }
}

fun shareText(
    context: Context,
    text: String
) {

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            "اشتراک‌گذاری"
        )
    )
}
