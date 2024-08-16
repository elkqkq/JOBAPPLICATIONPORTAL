package com.example.jobapplicationportal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jobapplicationportal.screens.LoginScreen
import com.example.jobapplicationportal.ui.theme.JOBAPPLICATIONPORTALTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            JOBAPPLICATIONPORTALTheme {
                LoginScreen()


                }
            }
        }
    }




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JOBAPPLICATIONPORTALTheme {
        LoginScreen()

    }
}

