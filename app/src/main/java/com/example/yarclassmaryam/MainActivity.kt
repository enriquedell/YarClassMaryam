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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🌙",
                fontSize = 60.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "ماه زیبا، سلام",
                color = Burgundy,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
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

            // به جای عکس خارجی، از آیکون استفاده شده
            // تا خطای maryam_photo نداشته باشیم.
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
                        contentDescription = "رمز"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

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
            icon = Icons.Default.Person
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
            icon = Icons.Default.Edit
        ),

        MenuItem(
            title = "گزارش‌ها",
            icon = Icons.Default.CheckCircle
        ),

        MenuItem(
            title = "خط تحریری",
            icon = Icons.Default.Edit
        ),

        MenuItem(
            title = "پشتیبان‌گیری",
            icon = Icons.Default.CheckCircle
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
                        text = "یارِ کلاس مریم",
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
                                text = "م",
                                color = Color.White,
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.width(16.dp)
                    )

                    Column {

                        Text(
                            text = "مریم شجاعی",
                            color = Burgundy,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "دستیار هوشمند معلم پایه ششم",
                            color = Brown,
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "مدیریت کلاس • دانش‌آموزان • ارزیابی",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "ابزارهای کلاس",
                color = Burgundy,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

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
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Gold,
                                modifier = Modifier.size(38.dp)
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = item.title,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    title: String,
    onBack: () -> Unit
) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(text = title)
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = title,
                fontSize = 28.sp,
                color = Burgundy,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "این بخش در نسخه بعدی با امکانات کامل تکمیل می‌شود.",
                color = Brown,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "طراحی اصلی برنامه آماده است 🌷",
                color = Gold,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
