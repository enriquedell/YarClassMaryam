package com.example.yarclassmaryam

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
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
import kotlin.random.Random


// =========================
// رنگ‌های برنامه
// =========================

private val Burgundy = Color(0xFF861638)
private val DarkBurgundy = Color(0xFF64102A)
private val Gold = Color(0xFFD4B24C)
private val Cream = Color(0xFFFFF8EE)
private val LightPink = Color(0xFFFFE1E9)
private val LightGreen = Color(0xFFE8F7D5)
private val LightBlue = Color(0xFFE3F1FF)
private val LightYellow = Color(0xFFFFF2B8)


// =========================
// مدل‌های اطلاعاتی
// =========================

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


// =========================
// ذخیره‌سازی اطلاعات
// =========================

class AppStorage(context: Context) {

    private val pref =
        context.getSharedPreferences(
            "yar_class_storage",
            Context.MODE_PRIVATE
        )

    // ---------- دانش‌آموزان ----------

    fun getStudents(): MutableList<Student> {

        val result = mutableListOf<Student>()

        val array = JSONArray(
            pref.getString("students", "[]")
        )

        for (i in 0 until array.length()) {

            val o = array.getJSONObject(i)

            result.add(
                Student(
                    id = o.getLong("id"),
                    name = o.getString("name"),
                    code = o.getString("code")
                )
            )
        }

        return result
    }

    fun saveStudents(list: List<Student>) {

        val array = JSONArray()

        list.forEach {

            val o = JSONObject()

            o.put("id", it.id)
            o.put("name", it.name)
            o.put("code", it.code)

            array.put(o)
        }

        pref.edit()
            .putString("students", array.toString())
            .apply()
    }


    // ---------- حضور و غیاب ----------

    fun getAttendance(): MutableList<Attendance> {

        val result = mutableListOf<Attendance>()

        val array = JSONArray(
            pref.getString("attendance", "[]")
        )

        for (i in 0 until array.length()) {

            val o = array.getJSONObject(i)

            result.add(
                Attendance(
                    studentId = o.getLong("studentId"),
                    date = o.getString("date"),
                    status = o.getString("status")
                )
            )
        }

        return result
    }

    fun saveAttendance(list: List<Attendance>) {

        val array = JSONArray()

        list.forEach {

            val o = JSONObject()

            o.put("studentId", it.studentId)
            o.put("date", it.date)
            o.put("status", it.status)

            array.put(o)
        }

        pref.edit()
            .putString("attendance", array.toString())
            .apply()
    }


    // ---------- ارزشیابی ----------

    fun getEvaluations(): MutableList<Evaluation> {

        val result = mutableListOf<Evaluation>()

        val array = JSONArray(
            pref.getString("evaluations", "[]")
        )

        for (i in 0 until array.length()) {

            val o = array.getJSONObject(i)

            result.add(
                Evaluation(
                    studentId = o.getLong("studentId"),
                    lesson = o.getString("lesson"),
                    level = o.getString("level"),
                    note = o.getString("note")
                )
            )
        }

        return result
    }

    fun saveEvaluations(list: List<Evaluation>) {

        val array = JSONArray()

        list.forEach {

            val o = JSONObject()

            o.put("studentId", it.studentId)
            o.put("lesson", it.lesson)
            o.put("level", it.level)
            o.put("note", it.note)

            array.put(o)
        }

        pref.edit()
            .putString("evaluations", array.toString())
            .apply()
    }


    // ---------- برنامه هفتگی ----------

    fun getSchedule(): MutableList<ScheduleItem> {

        val result = mutableListOf<ScheduleItem>()

        val array = JSONArray(
            pref.getString("schedule", "[]")
        )

        for (i in 0 until array.length()) {

            val o = array.getJSONObject(i)

            result.add(
                ScheduleItem(
                    day = o.getString("day"),
                    period = o.getInt("period"),
                    lesson = o.getString("lesson")
                )
            )
        }

        return result
    }

    fun saveSchedule(list: List<ScheduleItem>) {

        val array = JSONArray()

        list.forEach {

            val o = JSONObject()

            o.put("day", it.day)
            o.put("period", it.period)
            o.put("lesson", it.lesson)

            array.put(o)
        }

        pref.edit()
            .putString("schedule", array.toString())
            .apply()
    }


    // ---------- دفتر کلاس ----------

    fun getRecords(): MutableList<ClassRecord> {

        val result = mutableListOf<ClassRecord>()

        val array = JSONArray(
            pref.getString("records", "[]")
        )

        for (i in 0 until array.length()) {

            val o = array.getJSONObject(i)

            result.add(
                ClassRecord(
                    date = o.getString("date"),
                    lesson = o.getString("lesson"),
                    activity = o.getString("activity"),
                    homework = o.getString("homework")
                )
            )
        }

        return result
    }

    fun saveRecords(list: List<ClassRecord>) {

        val array = JSONArray()

        list.forEach {

            val o = JSONObject()

            o.put("date", it.date)
            o.put("lesson", it.lesson)
            o.put("activity", it.activity)
            o.put("homework", it.homework)

            array.put(o)
        }

        pref.edit()
            .putString("records", array.toString())
            .apply()
    }


    // ---------- تکالیف ----------

    fun getHomework(): MutableList<Homework> {

        val result = mutableListOf<Homework>()

        val array = JSONArray(
            pref.getString("homework", "[]")
        )

        for (i in 0 until array.length()) {

            val o = array.getJSONObject(i)

            result.add(
                Homework(
                    title = o.getString("title"),
                    lesson = o.getString("lesson"),
                    level = o.getString("level"),
                    text = o.getString("text")
                )
            )
        }

        return result
    }

    fun saveHomework(list: List<Homework>) {

        val array = JSONArray()

        list.forEach {

            val o = JSONObject()

            o.put("title", it.title)
            o.put("lesson", it.lesson)
            o.put("level", it.level)
            o.put("text", it.text)

            array.put(o)
        }

        pref.edit()
            .putString("homework", array.toString())
            .apply()
    }


    // ---------- پشتیبان ----------

    fun makeBackup(): String {

        val root = JSONObject()

        root.put(
            "students",
            pref.getString("students", "[]")
        )

        root.put(
            "attendance",
            pref.getString("attendance", "[]")
        )

        root.put(
            "evaluations",
            pref.getString("evaluations", "[]")
        )

        root.put(
            "schedule",
            pref.getString("schedule", "[]")
        )

        root.put(
            "records",
            pref.getString("records", "[]")
        )

        root.put(
            "homework",
            pref.getString("homework", "[]")
        )

        return root.toString(2)
    }


    fun restoreBackup(text: String) {

        val root = JSONObject(text)

        pref.edit()
            .putString(
                "students",
                root.optString("students", "[]")
            )
            .putString(
                "attendance",
                root.optString("attendance", "[]")
            )
            .putString(
                "evaluations",
                root.optString("evaluations", "[]")
            )
            .putString(
                "schedule",
                root.optString("schedule", "[]")
            )
            .putString(
                "records",
                root.optString("records", "[]")
            )
            .putString(
                "homework",
                root.optString("homework", "[]")
            )
            .apply()
    }
}


// =========================
// MainActivity
// =========================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val storage = AppStorage(this)

        setContent {

            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl
            ) {

                YarClassApp(storage)
            }
        }
    }
}


// =========================
// برنامه اصلی
// =========================

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
        }

        return
    }

    when (screen) {

        "home" -> {

            HomeScreen(
                onOpen = {
                    screen = it
                }
            )
        }

        "students" -> {

            StudentsScreen(
                storage = storage,
                onBack = {
                    screen = "home"
                }
            )
        }

        "attendance" -> {

            AttendanceScreen(
                storage = storage,
                onBack = {
                    screen = "home"
                }
            )
        }

        "evaluation" -> {

            EvaluationScreen(
                storage = storage,
                onBack = {
                    screen = "home"
                }
            )
        }

        "schedule" -> {

            ScheduleScreen(
                storage = storage,
                onBack = {
                    screen = "home"
                }
            )
        }

        "homework" -> {

            HomeworkScreen(
                storage = storage,
                onBack = {
                    screen = "home"
                }
            )
        }

        "classbook" -> {

            ClassBookScreen(
                storage = storage,
                onBack = {
                    screen = "home"
                }
            )
        }

        "exam" -> {

            SimpleInfoScreen(
                title = "آزمون‌ساز",
                text = "در این بخش می‌توانید آزمون‌های کلاسی طراحی و مدیریت کنید.",
                icon = Icons.Default.Quiz,
                onBack = {
                    screen = "home"
                }
            )
        }

        "reports" -> {

            ReportsScreen(
                storage = storage,
                onBack = {
                    screen = "home"
                }
            )
        }

        "handwriting" -> {

            SimpleInfoScreen(
                title = "خط تحریری",
                text = "نمونه‌ها و تمرین‌های خط تحریری را از این بخش مدیریت کنید.",
                icon = Icons.Default.Create,
                onBack = {
                    screen = "home"
                }
            )
        }

        "backup" -> {

            BackupScreen(
                storage = storage,
                onBack = {
                    screen = "home"
                }
            )
        }
    }
}


// =========================
// صفحه ورود
// =========================

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
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(
                    RoundedCornerShape(30.dp)
                )
                .background(Burgundy),

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "م",
                color = Color.White,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "یار کلاس مریم",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Burgundy
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        OutlinedTextField(
            value = password,

            onValueChange = {

                password = it
                error = false
            },

            label = {
                Text("رمز ورود")
            },

            singleLine = true
        )

        if (error) {

            Text(
                text = "رمز صحیح نیست",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {

                if (password == "1234") {

                    onLogin()

                } else {

                    error = true
                }
            }
        ) {

            Text("ورود")
        }
    }
}


// =========================
// آیتم منو
// =========================

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val screen: String
)


// =========================
// صفحه اصلی
// =========================

@Composable
fun HomeScreen(
    onOpen: (String) -> Unit
) {

    val menu = listOf(

        MenuItem(
            "دانش‌آموزان",
            Icons.Default.Person,
            "students"
        ),

        MenuItem(
            "حضور و غیاب",
            Icons.Default.CheckCircle,
            "attendance"
        ),

        MenuItem(
            "ارزشیابی توصیفی",
            Icons.Default.Star,
            "evaluation"
        ),

        MenuItem(
            "برنامه هفتگی",
            Icons.Default.CalendarToday,
            "schedule"
        ),

        MenuItem(
            "آزمون‌ساز",
            Icons.Default.Quiz,
            "exam"
        ),

        MenuItem(
            "تکالیف و یادداشت‌ها",
            Icons.Default.MenuBook,
            "homework"
        ),

        MenuItem(
            "دفتر کلاسی",
            Icons.Default.Today,
            "classbook"
        ),

        MenuItem(
            "گزارش‌ها",
            Icons.Default.Assessment,
            "reports"
        ),

        MenuItem(
            "خط تحریری",
            Icons.Default.Create,
            "handwriting"
        ),

        MenuItem(
            "پشتیبان‌گیری",
            Icons.Default.Backup,
            "backup"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Burgundy)
                .padding(20.dp)
        ) {

            Text(
                text = "یار کلاس مریم",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,

                modifier = Modifier.fillMaxWidth(),

                textAlign = TextAlign.Right
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            items(menu) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp)
                        .clickable {
                            onOpen(item.screen)
                        },

                    shape =
                        RoundedCornerShape(22.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                    elevation =
                        CardDefaults.cardElevation(3.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 18.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text = item.title,

                            modifier =
                                Modifier.weight(1f),

                            textAlign =
                                TextAlign.Right,

                            fontSize = 21.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color = Burgundy
                        )

                        Spacer(
                            modifier =
                                Modifier.width(18.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(55.dp)
                                .clip(
                                    RoundedCornerShape(17.dp)
                                )
                                .background(LightPink),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    item.icon,

                                contentDescription =
                                    item.title,

                                tint = Burgundy,

                                modifier =
                                    Modifier.size(31.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


// =========================
// Scaffold صفحات
// =========================

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

                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,

                            contentDescription =
                                "بازگشت"
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
        ) {

            content()
        }
    }
}


// =========================
// دانش‌آموزان
// =========================

@Composable
fun StudentsScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var students by remember {
        mutableStateOf(
            storage.getStudents()
        )
    }

    var showAdd by remember {
        mutableStateOf(false)
    }

    PageScaffold(
        title = "دانش‌آموزان",
        onBack = onBack
    ) {

        Button(
            onClick = {
                showAdd = true
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Text("＋ افزودن دانش‌آموز")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightYellow)
                .border(1.dp, Gold)
                .padding(10.dp)
        ) {

            Text(
                text = "نام و نام خانوادگی",

                modifier =
                    Modifier.weight(1f),

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = "کد",

                modifier =
                    Modifier.width(75.dp),

                textAlign =
                    TextAlign.Center,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = "ردیف",

                modifier =
                    Modifier.width(55.dp),

                textAlign =
                    TextAlign.Center,

                fontWeight =
                    FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(students) { student ->

                val number =
                    students.indexOf(student) + 1

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .border(
                            0.5.dp,
                            Color.LightGray
                        )
                        .padding(
                            vertical = 15.dp,
                            horizontal = 8.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = student.name,

                        modifier =
                            Modifier.weight(1f),

                        fontSize = 17.sp
                    )

                    Text(
                        text = student.code,

                        modifier =
                            Modifier.width(75.dp),

                        textAlign =
                            TextAlign.Center
                    )

                    Text(
                        text = number.toString(),

                        modifier =
                            Modifier.width(55.dp),

                        textAlign =
                            TextAlign.Center
                    )
                }
            }
        }
    }

    if (showAdd) {

        var name by remember {
            mutableStateOf("")
        }

        var code by remember {
            mutableStateOf("")
        }

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

                        onValueChange = {
                            name = it
                        },

                        label = {
                            Text(
                                "نام و نام خانوادگی"
                            )
                        }
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = code,

                        onValueChange = {
                            code = it
                        },

                        label = {
                            Text("کد دانش‌آموز")
                        }
                    )
                }
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        if (name.isNotBlank()) {

                            students =
                                (
                                    students +
                                            Student(
                                                id =
                                                    Random.nextLong(),

                                                name =
                                                    name,

                                                code =
                                                    code
                                            )
                                ).toMutableList()

                            storage.saveStudents(
                                students
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


// =========================
// حضور و غیاب
// =========================

@Composable
fun AttendanceScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var students by remember {
        mutableStateOf(
            storage.getStudents()
        )
    }

    var records by remember {
        mutableStateOf(
            storage.getAttendance()
        )
    }

    val date =
        SimpleDateFormat(
            "yyyy/MM/dd",
            Locale.getDefault()
        ).format(Date())

    PageScaffold(
        title = "حضور و غیاب",
        onBack = onBack
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightYellow)
                .border(1.dp, Gold)
                .padding(8.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "نام و نام خانوادگی",

                modifier =
                    Modifier.weight(1f),

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = "حاضر",

                modifier =
                    Modifier.width(65.dp),

                textAlign =
                    TextAlign.Center,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = "غایب",

                modifier =
                    Modifier.width(65.dp),

                textAlign =
                    TextAlign.Center,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = "تأخیر",

                modifier =
                    Modifier.width(65.dp),

                textAlign =
                    TextAlign.Center,

                fontWeight =
                    FontWeight.Bold
            )
        }

        LazyColumn {

            items(students) { student ->

                val current =
                    records.firstOrNull {

                        it.studentId ==
                                student.id &&

                                it.date ==
                                date

                    }?.status ?: ""

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .border(
                            0.5.dp,
                            Color.LightGray
                        )
                        .padding(7.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = student.name,

                        modifier =
                            Modifier.weight(1f),

                        fontSize = 16.sp
                    )

                    AttendanceButton(
                        text = "✓",

                        selected =
                            current == "حاضر",

                        onClick = {

                            records.removeAll {

                                it.studentId ==
                                        student.id &&

                                        it.date ==
                                        date
                            }

                            records.add(
                                Attendance(
                                    student.id,
                                    date,
                                    "حاضر"
                                )
                            )

                            storage.saveAttendance(
                                records
                            )

                            records =
                                storage.getAttendance()
                        }
                    )

                    AttendanceButton(
                        text = "×",

                        selected =
                            current == "غایب",

                        onClick = {

                            records.removeAll {

                                it.studentId ==
                                        student.id &&

                                        it.date ==
                                        date
                            }

                            records.add(
                                Attendance(
                                    student.id,
                                    date,
                                    "غایب"
                                )
                            )

                            storage.saveAttendance(
                                records
                            )

                            records =
                                storage.getAttendance()
                        }
                    )

                    AttendanceButton(
                        text = "⏰",

                        selected =
                            current == "تأخیر",

                        onClick = {

                            records.removeAll {

                                it.studentId ==
                                        student.id &&

                                        it.date ==
                                        date
                            }

                            records.add(
                                Attendance(
                                    student.id,
                                    date,
                                    "تأخیر"
                                )
                            )

                            storage.saveAttendance(
                                records
                            )

                            records =
                                storage.getAttendance()
                        }
                    )
                }
            }
        }
    }
}


// =========================
// دکمه حضور و غیاب
// =========================

@Composable
fun AttendanceButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    OutlinedButton(

        onClick = onClick,

        modifier = Modifier
            .width(65.dp)
            .padding(2.dp)
    ) {

        Text(
            text = text,

            color =
                if (selected)
                    Burgundy
                else
                    Color.DarkGray,

            fontWeight =
                if (selected)
                    FontWeight.Bold
                else
                    FontWeight.Normal
        )
    }
}


// =========================
// ارزشیابی
// =========================

@Composable
fun EvaluationScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val students = remember {
        storage.getStudents()
    }

    var selected by remember {
        mutableStateOf<Student?>(null)
    }

    var lesson by remember {
        mutableStateOf("")
    }

    var level by remember {
        mutableStateOf("خوب")
    }

    var note by remember {
        mutableStateOf("")
    }

    PageScaffold(
        title = "ارزشیابی توصیفی",
        onBack = onBack
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            item {

                Text(
                    text =
                        "دانش‌آموز را انتخاب کنید",

                    fontWeight =
                        FontWeight.Bold,

                    color = Burgundy
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                students.forEach {

                    OutlinedButton(

                        onClick = {
                            selected = it
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(it.name)
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = lesson,

                    onValueChange = {
                        lesson = it
                    },

                    label = {
                        Text("نام درس")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    text = "سطح ارزشیابی",
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceEvenly
                ) {

                    listOf(
                        "عالی",
                        "خوب",
                        "قابل قبول",
                        "نیازمند تلاش"
                    ).forEach {

                        OutlinedButton(
                            onClick = {
                                level = it
                            }
                        ) {

                            Text(it)
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedTextField(

                    value = note,

                    onValueChange = {
                        note = it
                    },

                    label = {
                        Text("توضیحات")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Button(

                    onClick = {

                        selected?.let { student ->

                            val list =
                                storage.getEvaluations()

                            list.add(
                                Evaluation(
                                    student.id,
                                    lesson,
                                    level,
                                    note
                                )
                            )

                            storage.saveEvaluations(
                                list
                            )

                            lesson = ""
                            note = ""
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("ذخیره ارزشیابی")
                }
            }
        }
    }
}


// =========================
// برنامه هفتگی
// =========================

@Composable
fun ScheduleScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var schedule by remember {
        mutableStateOf(
            storage.getSchedule()
        )
    }

    var selectedDay by remember {
        mutableStateOf("شنبه")
    }

    var selectedPeriod by remember {
        mutableStateOf(1)
    }

    var lessonText by remember {
        mutableStateOf("")
    }

    val days = listOf(
        "شنبه",
        "یکشنبه",
        "دوشنبه",
        "سه‌شنبه",
        "چهارشنبه"
    )

    val lessons = listOf(
        "زنگ اول",
        "زنگ دوم",
        "زنگ سوم",
        "زنگ چهارم",
        "زنگ پنجم"
    )

    PageScaffold(
        title = "برنامه هفتگی",
        onBack = onBack
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            Text(
                text = "برنامه کلاس",

                modifier =
                    Modifier.fillMaxWidth(),

                textAlign =
                    TextAlign.Center,

                fontSize = 23.sp,

                fontWeight =
                    FontWeight.Bold,

                color = Burgundy
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = "روز را انتخاب کنید:",
                fontWeight =
                    FontWeight.Bold
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceEvenly
            ) {

                days.forEach { day ->

                    OutlinedButton(
                        onClick = {
                            selectedDay = day
                        }
                    ) {

                        Text(day)
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = "زنگ را انتخاب کنید:",
                fontWeight =
                    FontWeight.Bold
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceEvenly
            ) {

                (1..5).forEach { p ->

                    OutlinedButton(

                        onClick = {
                            selectedPeriod = p
                        }
                    ) {

                        Text(p.toString())
                    }
                }
            }

            OutlinedTextField(

                value = lessonText,

                onValueChange = {
                    lessonText = it
                },

                label = {

                    Text(
                        "${lessons[selectedPeriod - 1]} - $selectedDay"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth()
            )

            Button(

                onClick = {

                    if (lessonText.isNotBlank()) {

                        schedule.removeAll {

                            it.day ==
                                    selectedDay &&

                                    it.period ==
                                    selectedPeriod
                        }

                        schedule.add(

                            ScheduleItem(
                                selectedDay,
                                selectedPeriod,
                                lessonText
                            )
                        )

                        storage.saveSchedule(
                            schedule
                        )

                        schedule =
                            storage.getSchedule()

                        lessonText = ""
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                Text("ثبت در برنامه")
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = "برنامه کلاس",

                fontWeight =
                    FontWeight.Bold,

                fontSize = 20.sp,

                color = Burgundy
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightYellow)
                    .border(1.dp, Gold)
            ) {

                Text(
                    "روز",

                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),

                    fontWeight =
                        FontWeight.Bold,

                    textAlign =
                        TextAlign.Center
                )

                (1..5).forEach { period ->

                    Text(
                        "زنگ $period",

                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),

                        fontWeight =
                            FontWeight.Bold,

                        textAlign =
                            TextAlign.Center
                    )
                }
            }

            LazyColumn {

                items(days) { day ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(Color.White)
                            .border(
                                0.5.dp,
                                Color.Gray
                            )
                    ) {

                        Text(
                            text = day,

                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp),

                            textAlign =
                                TextAlign.Center,

                            fontWeight =
                                FontWeight.Bold
                        )

                        (1..5).forEach { period ->

                            Text(

                                text =
                                    schedule.firstOrNull {

                                        it.day == day &&
                                                it.period == period

                                    }?.lesson ?: "",

                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp),

                                textAlign =
                                    TextAlign.Center,

                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


// =========================
// تکلیف‌ساز
// =========================

@Composable
fun HomeworkScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var lesson by remember {
        mutableStateOf("")
    }

    var topic by remember {
        mutableStateOf("")
    }

    var level by remember {
        mutableStateOf("ساده")
    }

    var count by remember {
        mutableStateOf("5")
    }

    var homework by remember {
        mutableStateOf<Homework?>(null)
    }

    PageScaffold(
        title = "تکلیف‌ساز هوشمند",
        onBack = onBack
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            item {

                Text(
                    text = "🤖 ساخت تکلیف هوشمند",

                    fontSize = 25.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color = Burgundy
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    "درس و موضوع را وارد کنید تا یک تکلیف متناسب با سطح دانش‌آموزان ساخته شود."
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                OutlinedTextField(

                    value = lesson,

                    onValueChange = {
                        lesson = it
                    },

                    label = {
                        Text("درس")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = topic,

                    onValueChange = {
                        topic = it
                    },

                    label = {
                        Text("موضوع درس")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    text = "سطح تکلیف",

                    fontWeight =
                        FontWeight.Bold,

                    color = Burgundy
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceEvenly
                ) {

                    OutlinedButton(
                        onClick = {
                            level = "ساده"
                        }
                    ) {

                        Text("🟢 ساده")
                    }

                    OutlinedButton(
                        onClick = {
                            level = "متوسط"
                        }
                    ) {

                        Text("🟡 متوسط")
                    }
                }

                OutlinedTextField(

                    value = count,

                    onValueChange = {
                        count = it
                    },

                    label = {
                        Text("تعداد فعالیت")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                Button(

                    onClick = {

                        homework =
                            createSmartHomework(

                                lesson =
                                    lesson.ifBlank {
                                        "درس"
                                    },

                                topic =
                                    topic.ifBlank {
                                        "موضوع امروز"
                                    },

                                level = level,

                                count =
                                    count.toIntOrNull()
                                        ?: 5
                            )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("✨ تولید تکلیف")
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                homework?.let { hw ->

                    HomeworkPoster(hw)

                    Spacer(
                        modifier =
                            Modifier.height(15.dp)
                    )

                    Button(

                        onClick = {

                            val list =
                                storage.getHomework()

                            list.add(hw)

                            storage.saveHomework(
                                list
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("💾 ذخیره تکلیف")
                    }

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Button(

                        onClick = {
                            shareHomework(hw)
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            "📤 اشتراک‌گذاری متن تکلیف"
                        )
                    }
                }
            }
        }
    }
}


// =========================
// ساخت تکلیف هوشمند
// =========================

fun createSmartHomework(
    lesson: String,
    topic: String,
    level: String,
    count: Int
): Homework {

    val safeCount =
        count.coerceIn(3, 10)

    val text =

        if (lesson.contains("ریاضی")) {

            if (level == "ساده") {

                buildString {

                    append(
                        "۱. سه تمرین ساده از مبحث «$topic» حل کن.\n\n"
                    )

                    append(
                        "۲. یک مثال از کتاب پیدا کن و با خط زیبا بنویس.\n\n"
                    )

                    append(
                        "۳. دو تمرین مشابه برای خودت بساز.\n\n"
                    )

                    append(
                        "۴. جواب‌ها را با دقت بررسی کن.\n\n"
                    )

                    append(
                        "۵. در پایان بنویس کدام قسمت برایت آسان‌تر بود."
                    )
                }

            } else {

                buildString {

                    append(
                        "۱. $safeCount تمرین از مبحث «$topic» حل کن.\n\n"
                    )

                    append(
                        "۲. برای دو تمرین، روش حل را مرحله‌به‌مرحله توضیح بده.\n\n"
                    )

                    append(
                        "۳. یک سؤال چالشی از همین مبحث طراحی کن.\n\n"
                    )

                    append(
                        "۴. پاسخ سؤال خودت را نیز بنویس.\n\n"
                    )

                    append(
                        "۵. یک نکته مهمی که امروز یاد گرفتی بنویس."
                    )
                }
            }

        } else if (lesson.contains("فارسی")) {

            if (level == "ساده") {

                """
                ۱. پنج کلمه از موضوع «$topic» پیدا کن.

                ۲. با سه کلمه جمله بساز.

                ۳. یک جمله زیبا درباره درس امروز بنویس.

                ۴. کلمات جدید را یک بار با خط زیبا بنویس.

                ۵. یک نقاشی کوچک مرتبط با موضوع بکش.
                """.trimIndent()

            } else {

                """
                ۱. هفت واژه مرتبط با «$topic» پیدا کن.

                ۲. با چهار واژه جمله کامل بساز.

                ۳. یک پاراگراف کوتاه درباره موضوع بنویس.

                ۴. دو واژه هم‌معنی یا متضاد پیدا کن.

                ۵. یک سؤال خلاقانه درباره درس طراحی کن.
                """.trimIndent()
            }

        } else {

            if (level == "ساده") {

                """
                ۱. درس «$lesson» و موضوع «$topic» را مرور کن.

                ۲. سه نکته مهم درس را بنویس.

                ۳. دو سؤال ساده از درس طرح کن و جواب بده.

                ۴. یک نقاشی یا مثال مرتبط با موضوع آماده کن.

                ۵. در پایان بنویس امروز چه چیزی یاد گرفتی.
                """.trimIndent()

            } else {

                """
                ۱. موضوع «$topic» را با دقت مطالعه کن.

                ۲. پنج نکته مهم درس «$lesson» را بنویس.

                ۳. سه سؤال از موضوع طراحی کن و پاسخ بده.

                ۴. یک مثال واقعی از زندگی روزمره برای موضوع پیدا کن.

                ۵. یک فعالیت خلاقانه درباره درس انجام بده.
                """.trimIndent()
            }
        }

    return Homework(
        title = "تکلیف امروز",
        lesson = lesson,
        level = level,
        text = text
    )
}


// =========================
// پوستر تکلیف
// =========================

@Composable
fun HomeworkPoster(
    homework: Homework
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(25.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = LightPink
            )
    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {

            Text(

                text =
                    "🌸 ${homework.title}",

                modifier =
                    Modifier.fillMaxWidth(),

                textAlign =
                    TextAlign.Center,

                fontSize = 27.sp,

                fontWeight =
                    FontWeight.Bold,

                color = Burgundy
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(

                text =
                    "📚 ${homework.lesson}",

                modifier =
                    Modifier.fillMaxWidth(),

                textAlign =
                    TextAlign.Center,

                fontSize = 21.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(

                text =
                    "سطح: ${homework.level}",

                modifier =
                    Modifier.fillMaxWidth(),

                textAlign =
                    TextAlign.Center,

                color = DarkBurgundy
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(18.dp)
                    )
                    .background(Color.White)
                    .padding(18.dp)
            ) {

                Text(

                    text =
                        homework.text,

                    modifier =
                        Modifier.fillMaxWidth(),

                    fontSize = 17.sp,

                    lineHeight = 29.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            Text(

                text =
                    "🌟 با دقت و حوصله انجام بده 🌟",

                modifier =
                    Modifier.fillMaxWidth(),

                textAlign =
                    TextAlign.Center,

                fontSize = 17.sp,

                fontWeight =
                    FontWeight.Bold,

                color = Burgundy
            )
        }
    }
}


// =========================
// اشتراک‌گذاری
// =========================

fun shareHomework(
    homework: Homework
) {
    // در این نسخه غیرفعال است.
    // برای جلوگیری از خطای Context
    // فعلاً هیچ عملی انجام نمی‌دهد.
}


// =========================
// دفتر کلاسی
// =========================

@Composable
fun ClassBookScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var records by remember {
        mutableStateOf(
            storage.getRecords()
        )
    }

    var date by remember {

        mutableStateOf(

            SimpleDateFormat(
                "yyyy/MM/dd",
                Locale.getDefault()
            ).format(Date())
        )
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

    PageScaffold(
        title = "دفتر کلاسی",
        onBack = onBack
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            item {

                Text(
                    text = "📖 دفتر کلاس",

                    modifier =
                        Modifier.fillMaxWidth(),

                    textAlign =
                        TextAlign.Center,

                    fontSize = 25.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color = Burgundy
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                OutlinedTextField(

                    value = date,

                    onValueChange = {
                        date = it
                    },

                    label = {
                        Text("تاریخ")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedTextField(

                    value = lesson,

                    onValueChange = {
                        lesson = it
                    },

                    label = {
                        Text("نام درس")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedTextField(

                    value = activity,

                    onValueChange = {
                        activity = it
                    },

                    label = {
                        Text(
                            "فعالیت و توضیحات درس"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedTextField(

                    value = homework,

                    onValueChange = {
                        homework = it
                    },

                    label = {
                        Text("تکلیف")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Button(

                    onClick = {

                        if (
                            lesson.isNotBlank() ||
                            activity.isNotBlank() ||
                            homework.isNotBlank()
                        ) {

                            records.add(

                                ClassRecord(
                                    date,
                                    lesson,
                                    activity,
                                    homework
                                )
                            )

                            storage.saveRecords(
                                records
                            )

                            records =
                                storage.getRecords()

                            lesson = ""
                            activity = ""
                            homework = ""
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("ثبت در دفتر کلاس")
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(
                    text = "سوابق دفتر کلاس",

                    fontSize = 20.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color = Burgundy
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )
            }

            items(records.reversed()) { record ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(14.dp)
                    ) {

                        Text(
                            text =
                                "📅 ${record.date}",

                            fontWeight =
                                FontWeight.Bold,

                            color = Burgundy
                        )

                        Text(
                            "📚 درس: ${record.lesson}"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(5.dp)
                        )

                        Text(
                            "📝 فعالیت: ${record.activity}"
                        )

                        Spacer(
                            modifier =
                                Modifier.height(5.dp)
                        )

                        Text(
                            "✏️ تکلیف: ${record.homework}"
                        )
                    }
                }
            }
        }
    }
}


// =========================
// گزارش‌ها
// =========================

@Composable
fun ReportsScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    val students =
        storage.getStudents()

    val attendance =
        storage.getAttendance()

    val evaluations =
        storage.getEvaluations()

    val homework =
        storage.getHomework()

    PageScaffold(
        title = "گزارش‌ها",
        onBack = onBack
    ) {

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            item {

                ReportCard(
                    title =
                        "👩‍🎓 تعداد دانش‌آموزان",

                    value =
                        students.size.toString()
                )
            }

            item {

                ReportCard(
                    title =
                        "📋 ثبت‌های حضور و غیاب",

                    value =
                        attendance.size.toString()
                )
            }

            item {

                ReportCard(
                    title =
                        "⭐ ارزشیابی‌های ثبت‌شده",

                    value =
                        evaluations.size.toString()
                )
            }

            item {

                ReportCard(
                    title =
                        "📝 تکالیف ذخیره‌شده",

                    value =
                        homework.size.toString()
                )
            }
        }
    }
}


// =========================
// کارت گزارش
// =========================

@Composable
fun ReportCard(
    title: String,
    value: String
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(

                text = title,

                fontSize = 18.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(

                text = value,

                fontSize = 28.sp,

                color = Burgundy,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// =========================
// صفحات ساده
// =========================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleInfoScreen(
    title: String,
    text: String,
    icon: ImageVector,
    onBack: () -> Unit
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

                            imageVector =
                                Icons.Default.ArrowBack,

                            contentDescription =
                                "بازگشت"
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(25.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(

                imageVector = icon,

                contentDescription =
                    title,

                tint = Burgundy,

                modifier =
                    Modifier.size(80.dp)
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Text(

                text = title,

                fontSize = 27.sp,

                fontWeight =
                    FontWeight.Bold,

                color = Burgundy
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            Text(

                text = text,

                fontSize = 18.sp,

                textAlign =
                    TextAlign.Center
            )
        }
    }
}


// =========================
// پشتیبان‌گیری
// =========================

@Composable
fun BackupScreen(
    storage: AppStorage,
    onBack: () -> Unit
) {

    var backupText by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    PageScaffold(
        title = "پشتیبان‌گیری",
        onBack = onBack
    ) {

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Button(

                onClick = {

                    backupText =
                        storage.makeBackup()

                    message =
                        "نسخه پشتیبان ساخته شد."
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "ساخت نسخه پشتیبان"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedTextField(

                value = backupText,

                onValueChange = {
                    backupText = it
                },

                label = {
                    Text("متن پشتیبان")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Button(

                onClick = {

                    try {

                        storage.restoreBackup(
                            backupText
                        )

                        message =
                            "اطلاعات با موفقیت بازیابی شد."

                    } catch (e: Exception) {

                        message =
                            "متن پشتیبان صحیح نیست."
                    }
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "بازیابی اطلاعات"
                )
            }

            if (message.isNotBlank()) {

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(

                    text = message,

                    modifier =
                        Modifier.fillMaxWidth(),

                    textAlign =
                        TextAlign.Center,

                    color = Burgundy,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}
