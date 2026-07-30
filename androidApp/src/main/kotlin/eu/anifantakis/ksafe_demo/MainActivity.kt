package eu.anifantakis.ksafe_demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

// For Biometric support use AppCompatActivity instead of ComponentActivity
// and update themes.xml and manifest as in this app
class MainActivity : AppCompatActivity() {
    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        enableEdgeToEdge()

        setContent {
            App(
                onPlatformSplashReadyToDismiss = {
                    keepSplashOnScreen = false
                },
            )
        }
    }
}
