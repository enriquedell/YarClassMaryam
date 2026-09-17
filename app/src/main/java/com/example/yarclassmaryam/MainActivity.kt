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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
private val Green = Color(0xFF3E7D4A)
private val Red = Color(0xFFB3261E)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YarClassApp()
        }
    }
}

/* -------------------- DATA -------------------- */

data class Student(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val code: String
)

data class Homework(
    val id: Long,
    val title: String,
    val description: String,
    val date: String
)

data class ScheduleItem(
    val id: Long,
    val day: String,
    val time: String,
    val lesson: String
)

data class Exam(
    val id: Long,
    val title: String,
    val subject: String,
    val questions: Int
)

/* -------------------- STORAGE -------------------- */

object AppStorage {

    private const val PREFS = "yar_class_data"
    private const val STUDENTS = "students"
    private const val HOMEWORKS = "homeworks"
    private const val SCHEDULES = "schedules"
    private const val EXAMS = "exams"
    private const val ATTENDANCE = "attendance"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun loadStudents(context: Context): List<Student> {
        val text = prefs(context).getString(STUDENTS, "[]") ?: "[]"
        val array = JSONArray(text)
        val result = mutableListOf<Student>()

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                Student(
                    id = o.getLong("id"),
                    firstName = o.getString("firstName"),
                    lastName = o.getString("lastName"),
                    code = o.getString("code")
                )
            )
        }

        return result
    }

    fun saveStudents(context: Context, students: List<Student>) {
        val array = JSONArray()

        students.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("firstName", it.firstName)
                    put("lastName", it.lastName)
                    put("code", it.code)
                }
            )
        }

        prefs(context).edit()
            .putString(STUDENTS, array.toString())
            .apply()
    }

    fun loadHomework(context: Context): List<Homework> {
        val text = prefs(context).getString(HOMEWORKS, "[]") ?: "[]"
        val array = JSONArray(text)
        val result = mutableListOf<Homework>()

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                Homework(
                    id = o.getLong("id"),
                    title = o.getString("title"),
                    description = o.getString("description"),
                    date = o.getString("date")
                )
            )
        }

        return result
    }

    fun saveHomework(context: Context, list: List<Homework>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("title", it.title)
                    put("description", it.description)
                    put("date", it.date)
                }
            )
        }

        prefs(context).edit()
            .putString(HOMEWORKS, array.toString())
            .apply()
    }

    fun loadSchedules(context: Context): List<ScheduleItem> {
        val text = prefs(context).getString(SCHEDULES, "[]") ?: "[]"
        val array = JSONArray(text)
        val result = mutableListOf<ScheduleItem>()

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                ScheduleItem(
                    id = o.getLong("id"),
                    day = o.getString("day"),
                    time = o.getString("time"),
                    lesson = o.getString("lesson")
                )
            )
        }

        return result
    }

    fun saveSchedules(context: Context, list: List<ScheduleItem>) {
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

        prefs(context).edit()
            .putString(SCHEDULES, array.toString())
            .apply()
    }

    fun loadExams(context: Context): List<Exam> {
        val text = prefs(context).getString(EXAMS, "[]") ?: "[]"
        val array = JSONArray(text)
        val result = mutableListOf<Exam>()

        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result.add(
                Exam(
                    id = o.getLong("id"),
                    title = o.getString("title"),
                    subject = o.getString("subject"),
                    questions = o.getInt("questions")
                )
            )
        }

        return result
    }

    fun saveExams(context: Context, list: List<Exam>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("title", it.title)
                    put("subject", it.subject)
                    put("questions", it.questions)
                }
            )
        }

        prefs(context).edit()
            .putString(EXAMS, array.toString())
            .apply()
    }

    fun setAttendance(
        context: Context,
        studentId: Long,
        status: String
    ) {
        val date = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).format(Date())

        val key = "$ATTENDANCE-$date-$studentId"

        prefs(context).edit()
            .putString(key, status)
            .apply()
    }

    fun getAttendance(
        context: Context,
        studentId: Long
    ): String {
        val date = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).format(Date())

        val key = "$ATTENDANCE-$date-$studentId"

        return prefs(context).getString(key, "ثبت نشده") ?: "ثبت نشده"
    }

    fun backupText(context: Context): String {
        val root = JSONObject()

        root.put(
            "students",
            JSONArray(
                prefs(context).getString(STUDENTS, "[]")
            )
        )

        root.put(
            "homeworks",
            JSONArray(
                prefs(context).getString(HOMEWORKS, "[]")
            )
        )

        root.put(
            "schedules",
            JSONArray(
                prefs(context).getString(SCHEDULES, "[]")
            )
        )

        root.put(
            "exams",
            JSONArray(
                prefs(context).getString(EXAMS, "[]")
            )
        )

        return root.toString(2)
    }
}

/* -------------------- APP -------------------- */

@Composable
fun YarClassApp() {

    var loggedIn by remember {
        mutableStateOf(false)
    }

    var password by remember {
        mutableStateOf("")
    }

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
                onPasswordChange = {
                    password = it
                },
                onLogin = {
                    if (password == "1234") {
                        loggedIn = true
                    }
                }
            )

        } else {

            HomeScreen()
        }
    }
}

/* -------------------- LOGIN -------------------- */

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

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "ماه زیبا، سلام",
                color = Burgundy,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "به یارِ کلاس مریم خوش آمدید",
                color = Brown,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

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
                label = {
                    Text("رمز ورود")
                },
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
                    text = "ورود به برنامه",
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "رمز اولیه: 1234",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

/* -------------------- MENU -------------------- */

data class MenuItem(
    val title: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    var selected by remember {
        mutableStateOf<String?>(null)
    }

    val menuItems = listOf(

        MenuItem(
            "دانش‌آموزان",
            Icons.Default.Person
        ),

        MenuItem(
            "حضور و غیاب",
            Icons.Default.CheckCircle
        ),

        MenuItem(
            "ارزشیابی توصیفی",
            Icons.Default.Star
        ),

        MenuItem(
            "برنامه هفتگی",
            Icons.Default.CalendarMonth
        ),

        MenuItem(
            "آزمون‌ساز",
            Icons.Default.Assignment
        ),

        MenuItem(
            "تکالیف و یادداشت‌ها",
            Icons.Default.Edit
        ),

        MenuItem(
            "گزارش‌ها",
            Icons.Default.ThumbUp
        ),

        MenuItem(
            "خط تحریری",
            Icons.Default.Edit
        ),

        MenuItem(
            "پشتیبان‌گیری",
            Icons.Default.Backup
        )
    )

    if (selected != null) {

        when (selected) {

            "دانش‌آموزان" -> StudentsScreen {
                selected = null
            }

            "حضور و غیاب" -> AttendanceScreen {
                selected = null
            }

            "ارزشیابی توصیفی" -> EvaluationScreen {
                selected = null
            }

            "برنامه هفتگی" -> ScheduleScreen {
                selected = null
            }

            "آزمون‌ساز" -> ExamScreen {
                selected = null
            }

            "تکالیف و یادداشت‌ها" -> HomeworkScreen {
                selected = null
            }

            "گزارش‌ها" -> ReportsScreen {
                selected = null
            }

            "خط تحریری" -> HandwritingScreen {
                selected = null
            }

            "پشتیبان‌گیری" -> BackupScreen {
                selected = null
            }
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
                shape = RoundedCornerShape(24.dp)
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

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            "دستیار هوشمند معلم پایه ششم",
                            color = Brown,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

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
                        shape = RoundedCornerShape(20.dp)
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

/* -------------------- STUDENTS -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var students by remember {
        mutableStateOf(AppStorage.loadStudents(context))
    }

    var search by remember {
        mutableStateOf("")
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var editStudent by remember {
        mutableStateOf<Student?>(null)
    }

    val filtered = students.filter {
        "${it.firstName} ${it.lastName} ${it.code}"
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
                            editStudent = null
                            showDialog = true
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
                onValueChange = {
                    search = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("جست‌وجوی دانش‌آموز")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "جست‌وجو"
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                "تعداد دانش‌آموزان: ${students.size}",
                color = Burgundy,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (filtered.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        "هنوز دانش‌آموزی ثبت نشده است.\nاز دکمه + استفاده کنید.",
                        textAlign = TextAlign.Center,
                        color = Brown
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(
                        filtered,
                        key = { it.id }
                    ) { student ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Card(
                                    modifier = Modifier.size(52.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Burgundy
                                    )
                                ) {

                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Text(
                                            student.firstName
                                                .take(1),
                                            color = Color.White,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        "${student.firstName} ${student.lastName}",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Brown
                                    )

                                    Text(
                                        "کد: ${student.code}",
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        editStudent = student
                                        showDialog = true
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "ویرایش",
                                        tint = Burgundy
                                    )
                                }

                                IconButton(
                                    onClick = {

                                        students =
                                            students.filter {
                                                it.id != student.id
                                            }

                                        AppStorage.saveStudents(
                                            context,
                                            students
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
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {

        StudentDialog(
            student = editStudent,
            onDismiss = {
                showDialog = false
            },
            onSave = { firstName, lastName, code ->

                if (editStudent == null) {

                    val newStudent = Student(
                        id = System.currentTimeMillis(),
                        firstName = firstName,
                        lastName = lastName,
                        code = code
                    )

                    students = students + newStudent

                } else {

                    students = students.map {

                        if (it.id == editStudent!!.id) {
                            it.copy(
                                firstName = firstName,
                                lastName = lastName,
                                code = code
                            )
                        } else {
                            it
                        }
                    }
                }

                AppStorage.saveStudents(
                    context,
                    students
                )

                showDialog = false
            }
        )
    }
}

@Composable
fun StudentDialog(
    student: Student?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {

    var firstName by remember {
        mutableStateOf(student?.firstName ?: "")
    }

    var lastName by remember {
        mutableStateOf(student?.lastName ?: "")
    }

    var code by remember {
        mutableStateOf(student?.code ?: "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (student == null)
                    "افزودن دانش‌آموز"
                else
                    "ویرایش دانش‌آموز"
            )
        },
        text = {

            Column {

                OutlinedTextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                    },
                    label = {
                        Text("نام")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                    },
                    label = {
                        Text("نام خانوادگی")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        code = it
                    },
                    label = {
                        Text("کد دانش‌آموزی")
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {

            TextButton(
                onClick = {
                    if (
                        firstName.isNotBlank() &&
                        lastName.isNotBlank()
                    ) {
                        onSave(
                            firstName.trim(),
                            lastName.trim(),
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

/* -------------------- ATTENDANCE -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val students = remember {
        mutableStateOf(AppStorage.loadStudents(context))
    }

    var refresh by remember {
        mutableStateOf(0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("حضور و غیاب امروز")
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            if (students.value.isEmpty()) {

                Text(
                    "ابتدا از بخش «دانش‌آموزان» دانش‌آموز اضافه کنید.",
                    color = Brown
                )

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(
                        students.value,
                        key = { it.id }
                    ) { student ->

                        val status =
                            AppStorage.getAttendance(
                                context,
                                student.id
                            )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {

                                Text(
                                    "${student.firstName} ${student.lastName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Row(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(6.dp)
                                ) {

                                    AttendanceButton(
                                        text = "حاضر",
                                        selected = status == "حاضر"
                                    ) {
                                        AppStorage.setAttendance(
                                            context,
                                            student.id,
                                            "حاضر"
                                        )
                                        refresh++
                                    }

                                    AttendanceButton(
                                        text = "غایب",
                                        selected = status == "غایب"
                                    ) {
                                        AppStorage.setAttendance(
                                            context,
                                            student.id,
                                            "غایب"
                                        )
                                        refresh++
                                    }

                                    AttendanceButton(
                                        text = "تأخیر",
                                        selected = status == "تأخیر"
                                    ) {
                                        AppStorage.setAttendance(
                                            context,
                                            student.id,
                                            "تأخیر"
                                        )
                                        refresh++
                                    }
                                }

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                Text(
                                    "وضعیت: $status",
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
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Text(text)
        }

    } else {

        OutlinedButton(
            onClick = onClick,
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Text(text)
        }
    }
}

/* -------------------- EVALUATION -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val students = remember {
        AppStorage.loadStudents(context)
    }

    var selectedStudent by remember {
        mutableStateOf<Student?>(null)
    }

    var lesson by remember {
        mutableStateOf("")
    }

    var level by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
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
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {

                Text(
                    "دانش‌آموز",
                    fontWeight = FontWeight.Bold,
                    color = Burgundy
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (students.isEmpty()) {

                    Text(
                        "ابتدا دانش‌آموز اضافه کنید.",
                        color = Brown
                    )

                } else {

                    students.forEach { student ->

                        OutlinedButton(
                            onClick = {
                                selectedStudent = student
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (selectedStudent?.id == student.id)
                                    "✓ ${student.firstName} ${student.lastName}"
                                else
                                    "${student.firstName} ${student.lastName}"
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
                    label = {
                        Text("درس")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {

                Text(
                    "سطح عملکرد",
                    color = Burgundy,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    listOf(
                        "خیلی خوب",
                        "خوب",
                        "قابل قبول",
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
                                else
                                    item
                            )
                        }
                    }
                }
            }

            item {

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("توضیحات معلم")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }

            item {

                Button(
                    onClick = {
                        lesson = ""
                        level = ""
                        description = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ثبت ارزیابی")
                }
            }
        }
    }
}

/* -------------------- SCHEDULE -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var list by remember {
        mutableStateOf(
            AppStorage.loadSchedules(context)
        )
    }

    var showDialog by remember {
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
                            showDialog = true
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {

            if (list.isEmpty()) {

                Text(
                    "برای افزودن برنامه، روی + بزنید.",
                    color = Brown
                )

            } else {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    items(list, key = { it.id }) { item ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        item.lesson,
                                        fontWeight = FontWeight.Bold,
                                        color = Burgundy
                                    )

                                    Text(
                                        "${item.day} - ${item.time}",
                                        color = Brown
                                    )
                                }

                                IconButton(
                                    onClick = {

                                        list =
                                            list.filter {
                                                it.id != item.id
                                            }

                                        AppStorage.saveSchedules(
                                            context,
                                            list
                                        )
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "حذف"
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

        ScheduleDialog(
            onDismiss = {
                showDialog = false
            },
            onSave = { day, time, lesson ->

                val item = ScheduleItem(
                    System.currentTimeMillis(),
                    day,
                    time,
                    lesson
                )

                list = list + item

                AppStorage.saveSchedules(
                    context,
                    list
                )

                showDialog = false
            }
        )
    }
}

@Composable
fun ScheduleDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {

    var day by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var lesson by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("برنامه جدید")
        },
        text = {

            Column {

                OutlinedTextField(
                    value = day,
                    onValueChange = { day = it },
                    label = { Text("روز") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("ساعت") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = lesson,
                    onValueChange = { lesson = it },
                    label = { Text("درس") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (
                        day.isNotBlank() &&
                        time.isNotBlank() &&
                        lesson.isNotBlank()
                    ) {
                        onSave(day, time, lesson)
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

/* -------------------- HOMEWORK -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeworkScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var list by remember {
        mutableStateOf(
            AppStorage.loadHomework(context)
        )
    }

    var showDialog by remember {
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
                            showDialog = true
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
        }
    ) { padding ->

        if (list.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Cream)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "هنوز تکلیفی ثبت نشده است.",
                    color = Brown
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Cream)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                items(list, key = { it.id }) { homework ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier.padding(14.dp)
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    homework.title,
                                    color = Burgundy,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    homework.description,
                                    color = Brown
                                )

                                Text(
                                    "تاریخ: ${homework.date}",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

                            IconButton(
                                onClick = {

                                    list =
                                        list.filter {
                                            it.id != homework.id
                                        }

                                    AppStorage.saveHomework(
                                        context,
                                        list
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "حذف"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {

        HomeworkDialog(
            onDismiss = {
                showDialog = false
            },
            onSave = { title, description, date ->

                val homework = Homework(
                    System.currentTimeMillis(),
                    title,
                    description,
                    date
                )

                list = list + homework

                AppStorage.saveHomework(
                    context,
                    list
                )

                showDialog = false
            }
        )
    }
}

@Composable
fun HomeworkDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("تکلیف جدید")
        },
        text = {

            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان تکلیف") }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = { Text("توضیحات") }
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("تاریخ تحویل") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            title,
                            description,
                            date
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

/* -------------------- EXAM -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var list by remember {
        mutableStateOf(
            AppStorage.loadExams(context)
        )
    }

    var showDialog by remember {
        mutableStateOf(false)
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
                            showDialog = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "آزمون جدید"
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
        }
    ) { padding ->

        if (list.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Cream)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    "هنوز آزمونی ساخته نشده است.\nروی + بزنید.",
                    textAlign = TextAlign.Center,
                    color = Brown
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Cream)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                items(list, key = { it.id }) { exam ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    exam.title,
                                    color = Burgundy,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    "درس: ${exam.subject}",
                                    color = Brown
                                )

                                Text(
                                    "تعداد سؤال: ${exam.questions}",
                                    color = Color.Gray
                                )
                            }

                            IconButton(
                                onClick = {

                                    list =
                                        list.filter {
                                            it.id != exam.id
                                        }

                                    AppStorage.saveExams(
                                        context,
                                        list
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "حذف"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {

        ExamDialog(
            onDismiss = {
                showDialog = false
            },
            onSave = { title, subject, questions ->

                val exam = Exam(
                    System.currentTimeMillis(),
                    title,
                    subject,
                    questions
                )

                list = list + exam

                AppStorage.saveExams(
                    context,
                    list
                )

                showDialog = false
            }
        )
    }
}

@Composable
fun ExamDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Int) -> Unit
) {

    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf("10") }

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
                    label = {
                        Text("عنوان آزمون")
                    }
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = {
                        subject = it
                    },
                    label = {
                        Text("درس")
                    }
                )

                OutlinedTextField(
                    value = questions,
                    onValueChange = {
                        questions = it
                    },
                    label = {
                        Text("تعداد سؤال")
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {

                    val count =
                        questions.toIntOrNull() ?: 10

                    if (title.isNotBlank()) {
                        onSave(
                            title,
                            subject,
                            count
                        )
                    }
                }
            ) {
                Text("ساخت آزمون")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

/* -------------------- REPORTS -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val students =
        remember {
            AppStorage.loadStudents(context)
        }

    val homeworks =
        remember {
            AppStorage.loadHomework(context)
        }

    val schedules =
        remember {
            AppStorage.loadSchedules(context)
        }

    val exams =
        remember {
            AppStorage.loadExams(context)
        }

    var present = 0
    var absent = 0
    var late = 0

    students.forEach {

        when (
            AppStorage.getAttendance(
                context,
                it.id
            )
        ) {

            "حاضر" -> present++
            "غایب" -> absent++
            "تأخیر" -> late++
        }
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
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            item {

                ReportCard(
                    "👩‍🎓",
                    "دانش‌آموزان",
                    students.size.toString()
                )
            }

            item {

                ReportCard(
                    "✅",
                    "حاضر امروز",
                    present.toString()
                )
            }

            item {

                ReportCard(
                    "❌",
                    "غایب امروز",
                    absent.toString()
                )
            }

            item {

                ReportCard(
                    "⏰",
                    "تأخیر امروز",
                    late.toString()
                )
            }

            item {

                ReportCard(
                    "📚",
                    "تکالیف ثبت‌شده",
                    homeworks.size.toString()
                )
            }

            item {

                ReportCard(
                    "📅",
                    "برنامه‌های هفتگی",
                    schedules.size.toString()
                )
            }

            item {

                ReportCard(
                    "📝",
                    "آزمون‌ها",
                    exams.size.toString()
                )
            }
        }
    }
}

@Composable
fun ReportCard(
    emoji: String,
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                emoji,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                title,
                modifier = Modifier.weight(1f),
                color = Brown,
                fontSize = 17.sp
            )

            Text(
                value,
                color = Burgundy,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/* -------------------- HANDWRITING -------------------- */

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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                "تمرین خط تحریری",
                color = Burgundy,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        "تمرین پیشنهادی:",
                        fontWeight = FontWeight.Bold,
                        color = Burgundy
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        "بهار آمد و باغ از عطر گل‌ها پر شد.",
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Text(
                        "برای تمرین، جمله بالا را چند بار با خط خوانا بنویسید.",
                        color = Brown,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/* -------------------- BACKUP -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var backup by remember {
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(20.dp)
        ) {

            Text(
                "پشتیبان‌گیری اطلاعات",
                color = Burgundy,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "از اطلاعات دانش‌آموزان، تکالیف، برنامه هفتگی و آزمون‌ها یک نسخه متنی تهیه می‌شود.",
                color = Brown
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    backup =
                        AppStorage.backupText(context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    Icons.Default.Backup,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("تهیه نسخه پشتیبان")
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (backup.isNotEmpty()) {

                Button(
                    onClick = {

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
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(
                        Icons.Default.Send,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text("ارسال / اشتراک‌گذاری Backup")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "نسخه پشتیبان آماده است.",
                    color = Green,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
