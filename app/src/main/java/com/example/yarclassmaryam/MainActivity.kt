package com.example.yarclassmaryam

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Cream = Color(0xFFFFF8EC)
private val Burgundy = Color(0xFF7A1830)
private val Gold = Color(0xFFC79A35)
private val Brown = Color(0xFF3B251A)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YarClassApp()
        }
    }
}

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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                text = "🌙",
                fontSize = 58.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "ماه زیبا، سلام",
                color = Burgundy,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "به یارِ کلاس مریم خوش آمدید",
                color = Brown,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Card(
                modifier = Modifier.size(130.dp),
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
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = {
                    Text("رمز ورود")
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Button(
                onClick = onLogin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy
                ),
                modifier = Modifier.fillMaxWidth(0.72f)
            ) {

                Text(
                    text = "ورود به برنامه",
                    fontSize = 17.sp
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "رمز اولیه: 1234",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

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
            title = "دانش‌آموزان",
            icon = Icons.Default.People
        ),

        MenuItem(
            title = "حضور و غیاب",
            icon = Icons.Default.CheckCircle
        ),

        MenuItem(
            title = "ارزشیابی توصیفی",
            icon = Icons.Default.Star
        ),

        MenuItem(
            title = "برنامه هفتگی",
            icon = Icons.Default.DateRange
        ),

        MenuItem(
            title = "آزمون‌ساز",
            icon = Icons.Default.Edit
        ),

        MenuItem(
            title = "تکالیف و یادداشت‌ها",
            icon = Icons.Default.Note
        ),

        MenuItem(
            title = "گزارش‌ها",
            icon = Icons.Default.BarChart
        ),

        MenuItem(
            title = "خط تحریری",
            icon = Icons.Default.Create
        ),

        MenuItem(
            title = "پشتیبان‌گیری",
            icon = Icons.Default.Backup
        )
    )

    if (selected != null) {

        DetailScreen(
            title = selected!!,
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
                    Text(
