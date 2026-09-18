package com.example.yarclassmaryam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.Date

/* =========================================================
   COLORS
   ========================================================= */

private val Burgundy = Color(0xFF7A1538)
private val DarkBurgundy = Color(0xFF4A0920)
private val Cream = Color(0xFFFFF8E7)
private val LightPink = Color(0xFFFFE5EC)
private val LightGreen = Color(0xFFE3F5E8)
private val LightBlue = Color(0xFFE2F0FF)
private val LightYellow = Color(0xFFFFF5C7)

/* =========================================================
   DATA
   ========================================================= */

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
    val id: Long,
    val studentId: Long,
    val lesson: String,
    val level: String,
    val note: String
)

data class ScheduleItem(
    val id: Long,
    val day: String,
    val period: Int,
    val lesson: String
)

data class ClassRecord(
    val id: Long,
    val date: String,
    val lesson: String,
    val activity: String,
    val homework: String
)

data class Homework(
    val id: Long,
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
    val id: Long,
    val title: String,
    val grade: String,
    val lesson: String,
    val questions: List<ExamQuestion>
)

/* =========================================================
   JALALI (PERSIAN) DATE HELPERS
   ========================================================= */

/**
 * Converts a Gregorian date to the Jalali (Solar Hijri / Persian) calendar.
 * Standard public-domain conversion algorithm.
 */
private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): Triple<Int, Int, Int> {
    val gDaysInMonth = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)

    val gy2 = if (gm > 2) gy + 1 else gy

    var days = 355666 +
        (365 * gy) +
        ((gy2 + 3) / 4) -
        ((gy2 + 99) / 100) +
        ((gy2 + 399) / 400) +
        gd +
        gDaysInMonth[gm - 1]

    var jy = -1595 + (33 * (days / 12053))
    days %= 12053

    jy += 4 * (days / 1461)
    days %= 1461

    if (days > 365) {
        jy += (days - 1) / 365
        days = (days - 1) % 365
    }

    val jm: Int
    val jd: Int

    if (days < 186) {
        jm = 1 + (days / 31)
        jd = 1 + (days % 31)
    } else {
        jm = 7 + ((days - 186) / 30)
        jd = 1 + ((days - 186) % 30)
    }

    return Triple(jy, jm, jd)
}

/** Returns today's date formatted as a Jalali (Persian) date string, e.g. "1404/06/27". */
private fun todayJalaliString(): String {
    val cal = Calendar.getInstance()
    cal.time = Date()

    val gy = cal.get(Calendar.YEAR)
    val gm = cal.get(Calendar.MONTH) + 1
    val gd = cal.get(Calendar.DAY_OF_MONTH)

    val (jy, jm, jd) = gregorianToJalali(gy, gm, gd)

    return "%04d/%02d/%02d".format(jy, jm, jd)
}

/* =========================================================
   STORAGE
   ========================================================= */

class AppStorage(context: Context) {

    private val prefs = context.getSharedPreferences(
        "yar_class_storage",
        Context.MODE_PRIVATE
    )

    private fun saveArray(key: String, array: JSONArray) {
        prefs.edit()
            .putString(key, array.toString())
            .apply()
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
            val o = array.optJSONObject(i) ?: continue

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
            val o = array.optJSONObject(i) ?: continue

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
                    put("id", it.id)
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
            val o = array.optJSONObject(i) ?: continue

            result.add(
                Evaluation(
                    if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
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
                    put("id", it.id)
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
            val o = array.optJSONObject(i) ?: continue

            result.add(
                ScheduleItem(
                    if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
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
                    put("id", it.id)
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
            val o = array.optJSONObject(i) ?: continue

            result.add(
                ClassRecord(
                    if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
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
                    put("id", it.id)
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
            val o = array.optJSONObject(i) ?: continue

            result.add(
                Homework(
                    if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
                    o.optString("title"),
                    o.optString("lesson"),
                    o.optString("level"),
                    o.optString("text")
                )
            )
        }

        return result
    }

    fun saveExams(list: List<Exam>) {
        val array = JSONArray()

        list.forEach { exam ->
            val questions = JSONArray()

            exam.questions.forEach { q ->
                questions.put(
                    JSONObject().apply {
                        put("question", q.question)
                        put("options", JSONArray(q.options))
                        put("answer", q.answer)
                    }
                )
            }

            array.put(
                JSONObject().apply {
                    put("id", exam.id)
                    put("title", exam.title)
                    put("grade", exam.grade)
                    put("lesson", exam.lesson)
                    put("questions", questions)
                }
            )
        }

        saveArray("exams", array)
    }

    fun getExams(): List<Exam> {
        val result = mutableListOf<Exam>()
        val array = readArray("exams")

        for (i in 0 until array.length()) {
            val o = array.optJSONObject(i) ?: continue

            val qArray =
                o.optJSONArray("questions") ?: JSONArray()

            val questions = mutableListOf<ExamQuestion>()

            for (j in 0 until qArray.length()) {
                val q = qArray.optJSONObject(j) ?: continue

                val optionArray =
                    q.optJSONArray("options") ?: JSONArray()

                val options = mutableListOf<String>()

                for (k in 0 until optionArray.length()) {
                    options.add(optionArray.optString(k))
                }

                questions.add(
                    ExamQuestion(
                        q.optString("question"),
                        options,
                        q.optInt("answer")
                    )
                )
            }

            result.add(
                Exam(
                    if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
                    o.optString("title"),
                    o.optString("grade"),
                    o.optString("lesson"),
                    questions
                )
            )
        }

        return result
    }

    /**
     * Creates a full JSON backup of all app data.
     */
    fun createBackup(): String {
        val root = JSONObject()

        root.put(
            "students",
            JSONArray().apply {
                getStudents().forEach {
                    put(
                        JSONObject().apply {
                            put("id", it.id)
                            put("name", it.name)
                            put("code", it.code)
                        }
                    )
                }
            }
        )

        root.put(
            "attendance",
            JSONArray().apply {
                getAttendance().forEach {
                    put(
                        JSONObject().apply {
                            put("studentId", it.studentId)
                            put("date", it.date)
                            put("status", it.status)
                        }
                    )
                }
            }
        )

        root.put(
            "evaluations",
            JSONArray().apply {
                getEvaluations().forEach {
                    put(
                        JSONObject().apply {
                            put("id", it.id)
                            put("studentId", it.studentId)
                            put("lesson", it.lesson)
                            put("level", it.level)
                            put("note", it.note)
                        }
                    )
                }
            }
        )

        root.put(
            "schedule",
            JSONArray().apply {
                getSchedule().forEach {
                    put(
                        JSONObject().apply {
                            put("id", it.id)
                            put("day", it.day)
                            put("period", it.period)
                            put("lesson", it.lesson)
                        }
                    )
                }
            }
        )

        root.put(
            "records",
            JSONArray().apply {
                getRecords().forEach {
                    put(
                        JSONObject().apply {
                            put("id", it.id)
                            put("date", it.date)
                            put("lesson", it.lesson)
                            put("activity", it.activity)
                            put("homework", it.homework)
                        }
                    )
                }
            }
        )

        root.put(
            "homework",
            JSONArray().apply {
                getHomework().forEach {
                    put(
                        JSONObject().apply {
                            put("id", it.id)
                            put("title", it.title)
                            put("lesson", it.lesson)
                            put("level", it.level)
                            put("text", it.text)
                        }
                    )
                }
            }
        )

        root.put(
            "exams",
            JSONArray().apply {
                getExams().forEach { exam ->
                    put(
                        JSONObject().apply {
                            put("id", exam.id)
                            put("title", exam.title)
                            put("grade", exam.grade)
                            put("lesson", exam.lesson)

                            put(
                                "questions",
                                JSONArray().apply {
                                    exam.questions.forEach { q ->
                                        put(
                                            JSONObject().apply {
                                                put(
                                                    "question",
                                                    q.question
                                                )
                                                put(
                                                    "options",
                                                    JSONArray(q.options)
                                                )
                                                put(
                                                    "answer",
                                                    q.answer
                                                )
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            }
        )

        return root.toString(2)
    }

    /**
     * Restores app data from a JSON backup string.
     * Validates that the payload is a JSON object containing at least
     * one of the recognized top-level arrays before touching stored data,
     * so garbage or unrelated JSON does not silently wipe existing data.
     */
    fun restoreBackup(json: String): Boolean {
        return try {
            val root = JSONObject(json)

            val knownKeys = listOf(
                "students",
                "attendance",
                "evaluations",
                "schedule",
                "records",
                "homework",
                "exams"
            )

            val hasKnownKey = knownKeys.any { root.has(it) }

            if (!hasKnownKey) {
                // Doesn't look like a backup produced by this app.
                return false
            }

            val students = mutableListOf<Student>()
            val sArray =
                root.optJSONArray("students") ?: JSONArray()

            for (i in 0 until sArray.length()) {
                val o = sArray.optJSONObject(i) ?: continue

                val name = o.optString("name")
                if (name.isBlank()) continue

                students.add(
                    Student(
                        o.optLong("id", System.currentTimeMillis() + i),
                        name,
                        o.optString("code")
                    )
                )
            }

            val attendance = mutableListOf<Attendance>()
            val aArray =
                root.optJSONArray("attendance") ?: JSONArray()

            for (i in 0 until aArray.length()) {
                val o = aArray.optJSONObject(i) ?: continue

                if (o.optString("date").isBlank()) continue

                attendance.add(
                    Attendance(
                        o.optLong("studentId"),
                        o.optString("date"),
                        o.optString("status")
                    )
                )
            }

            val evaluations = mutableListOf<Evaluation>()
            val eArray =
                root.optJSONArray("evaluations") ?: JSONArray()

            for (i in 0 until eArray.length()) {
                val o = eArray.optJSONObject(i) ?: continue

                if (o.optString("lesson").isBlank()) continue

                evaluations.add(
                    Evaluation(
                        if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
                        o.optLong("studentId"),
                        o.optString("lesson"),
                        o.optString("level"),
                        o.optString("note")
                    )
                )
            }

            val schedule = mutableListOf<ScheduleItem>()
            val schArray =
                root.optJSONArray("schedule") ?: JSONArray()

            for (i in 0 until schArray.length()) {
                val o = schArray.optJSONObject(i) ?: continue

                if (o.optString("lesson").isBlank()) continue

                schedule.add(
                    ScheduleItem(
                        if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
                        o.optString("day"),
                        o.optInt("period"),
                        o.optString("lesson")
                    )
                )
            }

            val records = mutableListOf<ClassRecord>()
            val rArray =
                root.optJSONArray("records") ?: JSONArray()

            for (i in 0 until rArray.length()) {
                val o = rArray.optJSONObject(i) ?: continue

                if (o.optString("lesson").isBlank()) continue

                records.add(
                    ClassRecord(
                        if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
                        o.optString("date"),
                        o.optString("lesson"),
                        o.optString("activity"),
                        o.optString("homework")
                    )
                )
            }

            val homework = mutableListOf<Homework>()
            val hArray =
                root.optJSONArray("homework") ?: JSONArray()

            for (i in 0 until hArray.length()) {
                val o = hArray.optJSONObject(i) ?: continue

                if (o.optString("title").isBlank()) continue

                homework.add(
                    Homework(
                        if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
                        o.optString("title"),
                        o.optString("lesson"),
                        o.optString("level"),
                        o.optString("text")
                    )
                )
            }

            val exams = mutableListOf<Exam>()
            val exArray =
                root.optJSONArray("exams") ?: JSONArray()

            for (i in 0 until exArray.length()) {
                val o = exArray.optJSONObject(i) ?: continue

                if (o.optString("title").isBlank()) continue

                val qArray =
                    o.optJSONArray("questions") ?: JSONArray()

                val questions = mutableListOf<ExamQuestion>()

                for (j in 0 until qArray.length()) {
                    val q = qArray.optJSONObject(j) ?: continue

                    val optionArray =
                        q.optJSONArray("options") ?: JSONArray()

                    val options = mutableListOf<String>()

                    for (k in 0 until optionArray.length()) {
                        options.add(optionArray.optString(k))
                    }

                    if (options.isEmpty()) continue

                    questions.add(
                        ExamQuestion(
                            q.optString("question"),
                            options,
                            q.optInt("answer").coerceIn(0, options.size - 1)
                        )
                    )
                }

                exams.add(
                    Exam(
                        if (o.has("id")) o.optLong("id") else System.nanoTime() + i,
                        o.optString("title"),
                        o.optString("grade"),
                        o.optString("lesson"),
                        questions
                    )
                )
            }

            // Only commit once every section has parsed without throwing.
            saveStudents(students)
            saveAttendance(attendance)
            saveEvaluations(evaluations)
            saveSchedule(schedule)
            saveRecords(records)
            saveHomework(homework)
            saveExams(exams)

            true
        } catch (_: Exception) {
            false
        }
    }
}

/* =========================================================
   MAIN ACTIVITY
   ========================================================= */

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Cream
                ) {
                    YarClassApp(AppStorage(this))
                }
            }
        }
    }
}

/* =========================================================
   APP
   ========================================================= */

@Composable
fun YarClassApp(storage: AppStorage) {

    var loggedIn by remember {
        mutableStateOf(false)
    }

    var screen by remember {
        mutableStateOf("home")
    }

    if (!loggedIn) {
        LoginScreen {
            loggedIn = true
            screen = "home"
        }
        return
    }

    when (screen) {
        "home" -> HomeScreen(
            onNavigate = { screen = it },
            onLogout = { loggedIn = false }
        )

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

        "homework" -> HomeworkScreen(
            storage,
            { screen = "home" }
        )

        "classbook" -> ClassBookScreen(
            storage,
            { screen = "home" }
        )

        "exam" -> ExamScreen(
            storage,
            { screen = "home" }
        )

        "reports" -> ReportsScreen(
            storage,
            { screen = "home" }
        )

        "backup" -> BackupScreen(
            storage,
            { screen = "home" }
        )

        "handwriting" -> InfoScreen(
            "خط تحریری",
            "در این بخش می‌توانید تمرین‌های خط تحریری دانش‌آموزان را مدیریت کنید.",
            { screen = "home" }
        )
    }
}

/* =========================================================
   LOGIN
   ========================================================= */

@Composable
fun LoginScreen(onLogin: () -> Unit) {

    val context = LocalContext.current

    var pin by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

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
            error = "قابلیت اثر انگشت روی دستگاه فعال نیست."
            return
        }

        val prompt = BiometricPrompt(
            activity,
            activity.mainExecutor,
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

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("ورود به یار کلاس مریم")
            .setSubtitle("اثر انگشت خود را تأیید کنید")
            .setNegativeButtonText("ورود با رمز")
            .build()

        prompt.authenticate(info)
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
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("🎓", fontSize = 60.sp)

                Text(
                    "یار کلاس مریم",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Text(
                    "سامانه مدیریت کلاس",
                    color = DarkBurgundy
                )

                Spacer(Modifier.height(25.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = {
                        pin = it
                        error = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("رمز ورود") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    visualTransformation =
                        PasswordVisualTransformation()
                )

                Spacer(Modifier.height(12.dp))

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

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { biometricLogin() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🔐 ورود با اثر انگشت")
                }

                if (error.isNotBlank()) {
                    Spacer(Modifier.height(10.dp))

                    Text(
                        error,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    "رمز پیش‌فرض: 1234",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

/* =========================================================
   HOME
   ========================================================= */

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
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                Text(
                    "پنل مدیریت کلاس",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Text(
                    "همه اطلاعات کلاس شما در یک مکان",
                    color = Color.DarkGray
                )
            }

            item {
                HomeButton(
                    "👨‍🎓 دانش‌آموزان",
                    LightPink
                ) {
                    onNavigate("students")
                }
            }

            item {
                HomeButton(
                    "✅ حضور و غیاب",
                    LightGreen
                ) {
                    onNavigate("attendance")
                }
            }

            item {
                HomeButton(
                    "📊 ارزشیابی",
                    LightBlue
                ) {
                    onNavigate("evaluation")
                }
            }

            item {
                HomeButton(
                    "📅 برنامه هفتگی",
                    LightYellow
                ) {
                    onNavigate("schedule")
                }
            }

            item {
                HomeButton(
                    "📝 تکلیف‌ساز",
                    LightPink
                ) {
                    onNavigate("homework")
                }
            }

            item {
                HomeButton(
                    "📚 دفتر کلاسی",
                    LightGreen
                ) {
                    onNavigate("classbook")
                }
            }

            item {
                HomeButton(
                    "🧪 آزمون‌ساز",
                    LightBlue
                ) {
                    onNavigate("exam")
                }
            }

            item {
                HomeButton(
                    "📈 گزارش‌ها",
                    LightYellow
                ) {
                    onNavigate("reports")
                }
            }

            item {
                HomeButton(
                    "✍️ خط تحریری",
                    LightPink
                ) {
                    onNavigate("handwriting")
                }
            }

            item {
                HomeButton(
                    "💾 پشتیبان‌گیری و بازیابی",
                    LightGreen
                ) {
                    onNavigate("backup")
                }
            }
        }
    }
}

@Composable
fun HomeButton(
    title: String,
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
        Text(
            title,
            modifier = Modifier.padding(20.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBurgundy
        )
    }
}

/* =========================================================
   PAGE SCAFFOLD
   ========================================================= */

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
                title = { Text(title) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(
                            "←",
                            fontSize = 26.sp,
                            color = Color.White
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

/**
 * Small reusable delete button used across list items throughout the app.
 */
@Composable
fun DeleteButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            "حذف",
            color = Color.Red
        )
    }
}

/* =========================================================
   STUDENTS
   ========================================================= */

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

    PageScaffold("دانش‌آموزان", onBack) {

        Column(Modifier.fillMaxSize()) {

            Button(
                onClick = {
                    name = ""
                    code = ""
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ افزودن دانش‌آموز")
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
                            Modifier.fillMaxWidth()
                        ) {

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column(
                                    Modifier.weight(1f)
                                ) {

                                    Text(
                                        student.name,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        "کد: ${student.code}",
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                }

                                DeleteButton {
                                    val updated =
                                        students.filter {
                                            it.id != student.id
                                        }

                                    storage.saveStudents(updated)
                                    students = updated
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
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("نام و نام خانوادگی")
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
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
                                System.currentTimeMillis(),
                                name.trim(),
                                code.trim()
                            )

                            val updated = students + student

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

/* =========================================================
   ATTENDANCE
   ========================================================= */

@Composable
fun AttendanceScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val students = remember {
        storage.getStudents()
    }

    var attendance by remember {
        mutableStateOf(storage.getAttendance())
    }

    val date = remember { todayJalaliString() }

    PageScaffold("حضور و غیاب", onBack) {

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
                    Modifier.fillMaxWidth()
                ) {

                    Column(
                        Modifier.padding(12.dp)
                    ) {

                        Text(
                            student.name,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(5.dp)
                        ) {

                            AttendanceButton(
                                "حاضر",
                                current?.status == "حاضر"
                            ) {
                                setAttendance(
                                    storage,
                                    attendance,
                                    student.id,
                                    date,
                                    "حاضر"
                                ) {
                                    attendance = it
                                }
                            }

                            AttendanceButton(
                                "غایب",
                                current?.status == "غایب"
                            ) {
                                setAttendance(
                                    storage,
                                    attendance,
                                    student.id,
                                    date,
                                    "غایب"
                                ) {
                                    attendance = it
                                }
                            }

                            AttendanceButton(
                                "تاخیر",
                                current?.status == "تاخیر"
                            ) {
                                setAttendance(
                                    storage,
                                    attendance,
                                    student.id,
                                    date,
                                    "تاخیر"
                                ) {
                                    attendance = it
                                }
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

fun setAttendance(
    storage: AppStorage,
    old: List<Attendance>,
    studentId: Long,
    date: String,
    status: String,
    update: (List<Attendance>) -> Unit
) {

    val newList =
        old.filterNot {
            it.studentId == studentId &&
                it.date == date
        } + Attendance(
            studentId,
            date,
            status
        )

    storage.saveAttendance(newList)
    update(newList)
}

@Composable
fun AttendanceButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    if (selected) {
        Button(onClick = onClick) {
            Text(text)
        }
    } else {
        OutlinedButton(onClick = onClick) {
            Text(text)
        }
    }
}

/* =========================================================
   EVALUATION
   ========================================================= */

@Composable
fun EvaluationScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val students = remember {
        storage.getStudents()
    }

    var evaluations by remember {
        mutableStateOf(storage.getEvaluations())
    }

    var selectedStudent by remember {
        mutableStateOf<Student?>(null)
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

    PageScaffold("ارزشیابی", onBack) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {

                var expanded by remember {
                    mutableStateOf(false)
                }

                Box {

                    OutlinedButton(
                        onClick = {
                            expanded = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            selectedStudent?.name
                                ?: "انتخاب دانش‌آموز"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        students.forEach { student ->

                            DropdownMenuItem(
                                text = {
                                    Text(student.name)
                                },
                                onClick = {
                                    selectedStudent = student
                                    expanded = false
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

                Spacer(Modifier.height(5.dp))

                listOf(
                    "عالی",
                    "خیلی خوب",
                    "خوب",
                    "نیازمند تلاش"
                ).forEach { value ->

                    if (level == value) {

                        Button(
                            onClick = {
                                level = value
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(value)
                        }

                    } else {

                        OutlinedButton(
                            onClick = {
                                level = value
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(value)
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

                        val student = selectedStudent

                        if (
                            student != null &&
                            lesson.isNotBlank()
                        ) {

                            val updated =
                                evaluations +
                                    Evaluation(
                                        System.currentTimeMillis(),
                                        student.id,
                                        lesson.trim(),
                                        level,
                                        note.trim()
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

                Text(
                    "ارزشیابی‌های ثبت‌شده",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )
            }

            items(
                evaluations,
                key = { it.id }
            ) { item ->

                val student =
                    students.firstOrNull {
                        it.id == item.studentId
                    }

                Card(
                    Modifier.fillMaxWidth()
                ) {

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {

                        Column(
                            Modifier.weight(1f)
                        ) {

                            Text(
                                student?.name
                                    ?: "دانش‌آموز حذف شده",
                                fontWeight = FontWeight.Bold
                            )

                            Text("درس: ${item.lesson}")
                            Text("سطح: ${item.level}")

                            if (item.note.isNotBlank()) {
                                Text("توضیح: ${item.note}")
                            }
                        }

                        DeleteButton {
                            val updated =
                                evaluations.filter {
                                    it.id != item.id
                                }

                            storage.saveEvaluations(updated)
                            evaluations = updated
                        }
                    }
                }
            }
        }
    }
}

/* =========================================================
   SCHEDULE
   ========================================================= */

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

    PageScaffold("برنامه هفتگی", onBack) {

        Column(Modifier.fillMaxSize()) {

            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("روز")
                }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = period,
                onValueChange = { period = it },
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
                onValueChange = { lesson = it },
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
                            schedule +
                                ScheduleItem(
                                    System.currentTimeMillis(),
                                    day.trim(),
                                    period.toIntOrNull() ?: 1,
                                    lesson.trim()
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

                items(
                    schedule,
                    key = { it.id }
                ) { item ->

                    Card(
                        Modifier.fillMaxWidth()
                    ) {

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Column(
                                Modifier.weight(1f)
                            ) {

                                Text(
                                    "${item.day} - زنگ ${item.period}",
                                    fontWeight = FontWeight.Bold
                                )

                                Text(item.lesson)
                            }

                            DeleteButton {
                                val updated =
                                    schedule.filter {
                                        it.id != item.id
                                    }

                                storage.saveSchedule(updated)
                                schedule = updated
                            }
                        }
                    }
                }
            }
        }
    }
}

/* =========================================================
   HOMEWORK
   ========================================================= */

fun createSmartHomework(
    lesson: String,
    level: String,
    count: Int
): String {

    val result = StringBuilder()
    val safeCount = count.coerceIn(1, 20)

    for (i in 1..safeCount) {

        val text = when {

            lesson.contains("ریاضی", true) ->
                when (level) {

                    "آسان" ->
                        "$i. یک سؤال ساده از درس ریاضی حل کنید."

                    "سخت" ->
                        "$i. یک مسئله چالشی ریاضی طراحی و حل کنید."

                    else ->
                        "$i. یک مسئله ریاضی مرتبط با درس حل کنید و مراحل را بنویسید."
                }

            lesson.contains("فارسی", true) ->
                "$i. یک فعالیت مرتبط با درس فارسی انجام دهید."

            lesson.contains("علوم", true) ->
                "$i. یک سؤال مفهومی از درس علوم بنویسید و پاسخ دهید."

            else ->
                "$i. سه نکته مهم از درس $lesson بنویسید."
        }

        result.append(text)
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

    PageScaffold("تکلیف‌ساز", onBack) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {

                OutlinedTextField(
                    value = lesson,
                    onValueChange = { lesson = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("درس / موضوع")
                    }
                )
            }

            item {

                OutlinedTextField(
                    value = level,
                    onValueChange = { level = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("سطح")
                    }
                )
            }

            item {

                OutlinedTextField(
                    value = count,
                    onValueChange = { count = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("تعداد فعالیت")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
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
                        Modifier.fillMaxWidth()
                    ) {

                        Column(
                            Modifier.padding(14.dp)
                        ) {

                            Text(generated)

                            Spacer(Modifier.height(10.dp))

                            Row {

                                Button(
                                    onClick = {

                                        val item =
                                            Homework(
                                                System.currentTimeMillis(),
                                                "تکلیف $lesson",
                                                lesson,
                                                level,
                                                generated
                                            )

                                        val updated =
                                            homework + item

                                        storage.saveHomework(updated)
                                        homework = updated
                                    }
                                ) {
                                    Text("ذخیره")
                                }

                                Spacer(Modifier.width(8.dp))

                                OutlinedButton(
                                    onClick = {
                                        shareText(
                                            context,
                                            generated
                                        )
                                    }
                                ) {
                                    Text("اشتراک")
                                }
                            }
                        }
                    }
                }
            }

            item {

                Text(
                    "تکلیف‌های ذخیره‌شده",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )
            }

            items(
                homework,
                key = { it.id }
            ) { item ->

                Card(
                    Modifier.fillMaxWidth()
                ) {

                    Column(
                        Modifier.padding(14.dp)
                    ) {

                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                item.title,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            DeleteButton {
                                val updated =
                                    homework.filter {
                                        it.id != item.id
                                    }

                                storage.saveHomework(updated)
                                homework = updated
                            }
                        }

                        Spacer(Modifier.height(5.dp))

                        Text(item.text)

                        Spacer(Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                shareText(
                                    context,
                                    "${item.title}\n\n${item.text}"
                                )
                            }
                        ) {
                            Text("ارسال / اشتراک‌گذاری")
                        }
                    }
                }
            }
        }
    }
}

/* =========================================================
   CLASS BOOK
   ========================================================= */

@Composable
fun ClassBookScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var records by remember {
        mutableStateOf(storage.getRecords())
    }

    var lesson by remember {
        mutableStateOf("")
    }

    var activity by remember {
        mutableStateOf("")
    }

    var homework by remember {
        mutableStateOf("")
    }

    val date = remember { todayJalaliString() }

    PageScaffold("دفتر کلاسی", onBack) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                Text(
                    "تاریخ: $date",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )
            }

            item {
                OutlinedTextField(
                    value = lesson,
                    onValueChange = { lesson = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("درس") }
                )
            }

            item {
                OutlinedTextField(
                    value = activity,
                    onValueChange = { activity = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("فعالیت کلاس") },
                    minLines = 3
                )
            }

            item {
                OutlinedTextField(
                    value = homework,
                    onValueChange = { homework = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("تکلیف") },
                    minLines = 2
                )
            }

            item {

                Button(
                    onClick = {

                        if (lesson.isNotBlank()) {

                            val updated =
                                records +
                                    ClassRecord(
                                        System.currentTimeMillis(),
                                        date,
                                        lesson.trim(),
                                        activity.trim(),
                                        homework.trim()
                                    )

                            storage.saveRecords(updated)
                            records = updated

                            lesson = ""
                            activity = ""
                            homework = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ثبت در دفتر کلاس")
                }
            }

            item {
                Text(
                    "سوابق کلاس",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )
            }

            items(
                records,
                key = { it.id }
            ) { record ->

                Card(
                    Modifier.fillMaxWidth()
                ) {

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {

                        Column(
                            Modifier.weight(1f)
                        ) {

                            Text(
                                record.date,
                                fontWeight = FontWeight.Bold
                            )

                            Text("درس: ${record.lesson}")

                            if (record.activity.isNotBlank()) {
                                Text(
                                    "فعالیت: ${record.activity}"
                                )
                            }

                            if (record.homework.isNotBlank()) {
                                Text(
                                    "تکلیف: ${record.homework}"
                                )
                            }
                        }

                        DeleteButton {
                            val updated =
                                records.filter {
                                    it.id != record.id
                                }

                            storage.saveRecords(updated)
                            records = updated
                        }
                    }
                }
            }
        }
    }
}

/* =========================================================
   EXAM
   ========================================================= */

@Composable
fun ExamScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var exams by remember {
        mutableStateOf(storage.getExams())
    }

    var title by remember {
        mutableStateOf("")
    }

    var grade by remember {
        mutableStateOf("")
    }

    var lesson by remember {
        mutableStateOf("")
    }

    var question by remember {
        mutableStateOf("")
    }

    var optionsText by remember {
        mutableStateOf("")
    }

    var answer by remember {
        mutableStateOf("1")
    }

    var questions by remember {
        mutableStateOf(listOf<ExamQuestion>())
    }

    PageScaffold("آزمون‌ساز", onBack) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("عنوان آزمون") }
                )
            }

            item {
                OutlinedTextField(
                    value = grade,
                    onValueChange = { grade = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("پایه") }
                )
            }

            item {
                OutlinedTextField(
                    value = lesson,
                    onValueChange = { lesson = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("درس") }
                )
            }

            item {
                Text(
                    "افزودن سؤال",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )
            }

            item {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("متن سؤال") },
                    minLines = 2
                )
            }

            item {
                OutlinedTextField(
                    value = optionsText,
                    onValueChange = { optionsText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("گزینه‌ها را با | جدا کنید")
                    }
                )
            }

            item {
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("شماره گزینه صحیح")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }

            item {

                OutlinedButton(
                    onClick = {

                        if (
                            question.isNotBlank() &&
                            optionsText.isNotBlank()
                        ) {

                            val options =
                                optionsText
                                    .split("|")
                                    .map { it.trim() }
                                    .filter { it.isNotBlank() }

                            if (options.isNotEmpty()) {

                                val correct =
                                    (answer.toIntOrNull() ?: 1)
                                        .coerceIn(
                                            1,
                                            options.size
                                        ) - 1

                                questions =
                                    questions +
                                        ExamQuestion(
                                            question.trim(),
                                            options,
                                            correct
                                        )

                                question = ""
                                optionsText = ""
                                answer = "1"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("➕ افزودن سؤال")
                }
            }

            item {

                Text(
                    "تعداد سؤال: ${questions.size}",
                    fontWeight = FontWeight.Bold
                )
            }

            itemsIndexed(questions) { index, q ->

                Card(
                    Modifier.fillMaxWidth()
                ) {

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {

                        Column(
                            Modifier.weight(1f)
                        ) {

                            Text(
                                q.question,
                                fontWeight = FontWeight.Bold
                            )

                            q.options.forEachIndexed { optIndex, option ->
                                Text(
                                    "${optIndex + 1}. $option"
                                )
                            }

                            Text(
                                "پاسخ صحیح: ${q.answer + 1}",
                                color = Burgundy
                            )
                        }

                        DeleteButton {
                            questions =
                                questions.filterIndexed { i, _ ->
                                    i != index
                                }
                        }
                    }
                }
            }

            item {

                Button(
                    onClick = {

                        if (
                            title.isNotBlank() &&
                            lesson.isNotBlank() &&
                            questions.isNotEmpty()
                        ) {

                            val exam = Exam(
                                System.currentTimeMillis(),
                                title.trim(),
                                grade.trim(),
                                lesson.trim(),
                                questions
                            )

                            val updated = exams + exam

                            storage.saveExams(updated)
                            exams = updated

                            title = ""
                            grade = ""
                            lesson = ""
                            questions = emptyList()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ذخیره آزمون")
                }
            }

            item {
                Text(
                    "آزمون‌های ذخیره‌شده",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )
            }

            items(
                exams,
                key = { it.id }
            ) { exam ->

                Card(
                    Modifier.fillMaxWidth()
                ) {

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {

                        Column(
                            Modifier.weight(1f)
                        ) {

                            Text(
                                exam.title,
                                fontWeight = FontWeight.Bold
                            )

                            Text("پایه: ${exam.grade}")
                            Text("درس: ${exam.lesson}")
                            Text(
                                "تعداد سؤال: ${exam.questions.size}"
                            )
                        }

                        DeleteButton {
                            val updated =
                                exams.filter {
                                    it.id != exam.id
                                }

                            storage.saveExams(updated)
                            exams = updated
                        }
                    }
                }
            }
        }
    }
}

/* =========================================================
   REPORTS
   ========================================================= */

@Composable
fun ReportsScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val students = remember {
        storage.getStudents()
    }

    val attendance = remember {
        storage.getAttendance()
    }

    val evaluations = remember {
        storage.getEvaluations()
    }

    val records = remember {
        storage.getRecords()
    }

    val homework = remember {
        storage.getHomework()
    }

    val exams = remember {
        storage.getExams()
    }

    PageScaffold("گزارش‌ها", onBack) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                ReportCard(
                    "👨‍🎓 دانش‌آموزان",
                    students.size
                )
            }

            item {
                ReportCard(
                    "✅ رکوردهای حضور و غیاب",
                    attendance.size
                )
            }

            item {
                ReportCard(
                    "📊 ارزشیابی‌ها",
                    evaluations.size
                )
            }

            item {
                ReportCard(
                    "📚 جلسات دفتر کلاسی",
                    records.size
                )
            }

            item {
                ReportCard(
                    "📝 تکالیف",
                    homework.size
                )
            }

            item {
                ReportCard(
                    "🧪 آزمون‌ها",
                    exams.size
                )
            }
        }
    }
}

@Composable
fun ReportCard(
    title: String,
    number: Int
) {

    Card(
        Modifier.fillMaxWidth()
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                title,
                fontWeight = FontWeight.Bold
            )

            Text(
                number.toString(),
                fontSize = 24.sp,
                color = Burgundy,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/* =========================================================
   BACKUP
   ========================================================= */

@Composable
fun BackupScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var message by remember {
        mutableStateOf("")
    }

    var messageIsError by remember {
        mutableStateOf(false)
    }

    val launcher =
        androidx.activity.compose.rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri == null) {
                return@rememberLauncherForActivityResult
            }

            try {

                val json =
                    context.contentResolver
                        .openInputStream(uri)
                        ?.bufferedReader()
                        ?.use { it.readText() }

                if (
                    json != null &&
                    storage.restoreBackup(json)
                ) {
                    message =
                        "بازیابی اطلاعات با موفقیت انجام شد."
                    messageIsError = false
                } else {
                    message =
                        "فایل پشتیبان معتبر نیست."
                    messageIsError = true
                }

            } catch (_: Exception) {
                message =
                    "خطا در خواندن فایل پشتیبان."
                messageIsError = true
            }
        }

    PageScaffold(
        "پشتیبان‌گیری و بازیابی",
        onBack
    ) {

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = {

                    shareText(
                        context,
                        storage.createBackup()
                    )

                    message =
                        "فایل پشتیبان برای اشتراک‌گذاری آماده شد."
                    messageIsError = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("💾 تهیه پشتیبان")
            }

            OutlinedButton(
                onClick = {
                    launcher.launch(
                        arrayOf(
                            "application/json",
                            "text/plain",
                            "*/*"
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📂 بازیابی پشتیبان")
            }

            if (message.isNotBlank()) {

                Card(
                    Modifier.fillMaxWidth()
                ) {

                    Text(
                        message,
                        modifier = Modifier.padding(14.dp),
                        color = if (messageIsError) Color.Red else Burgundy,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                "برای پشتیبان‌گیری، اطلاعات برنامه به صورت متن JSON برای اشتراک‌گذاری آماده می‌شود.",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

/* =========================================================
   INFO SCREEN
   ========================================================= */

@Composable
fun InfoScreen(
    title: String,
    message: String,
    onBack: () -> Unit
) {

    PageScaffold(title, onBack) {

        Card(
            Modifier.fillMaxWidth()
        ) {

            Column(
                Modifier.padding(20.dp)
            ) {

                Text(
                    title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(Modifier.height(12.dp))

                Text(message)
            }
        }
    }
}

/* =========================================================
   SHARE
   ========================================================= */

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
