package com.yzq.compose_tutor

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.test.core.app.ApplicationProvider
import com.yzq.compose_tutor.ui.theme.ComposetutorTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposetutorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar { /* Top app bar content */ }
                        },
                        floatingActionButton = {
                            FloatBtn()
                        },
                        floatingActionButtonPosition = FabPosition.Center
                    ) {
                        Greeting("Android")  // Screen content
                    }

                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatBtn() {
    FloatingActionButton(onClick = {
        // 1. ??????????????????(????????????channelId)
        val context: Context = ApplicationProvider.getApplicationContext()
        val channelId = "ChannelId" // ????????????

        val notification: Notification = Notification.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_delete)
            .setContentTitle("????????????")
            .setContentText("????????????")
            .build()
        // 2. ??????????????????????????????(????????????channelId)
        val notificationManager = context
            .getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            "?????????????????????",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        // 3. ????????????(Notification???NotificationManager???channelId????????????)
        notificationManager.notify(11, notification)
        /* ... */
    }) {
        Icon(
            Icons.Outlined.PlayArrow,
            contentDescription = "Favorite",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )  /* FAB content */
    }
}
@Composable
fun Greeting(name: String) {
    Card {
        ArtistCard()
    }

}

@Composable
fun ArtistCard() {
    Column {
        Icon(
            Icons.Filled.Favorite,
            contentDescription = "Favorite",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposetutorTheme {
        Greeting("Android")
    }
}