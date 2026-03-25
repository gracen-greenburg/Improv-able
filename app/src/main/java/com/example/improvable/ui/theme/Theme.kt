package com.example.improvable.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Platinum,
    secondary = DarkGray,
    tertiary = GunMetal,

    background = DarkGray,
    surface = DarkGray,

    // trying for contrasting accessible colors
    //    onPrimary = DarkGray,
    //    onSecondary = Platinum,
    //    onTertiary = Platinum,
    //    onBackground = Platinum,
    //    onSurface = Platinum,
)

private val LightColorScheme = lightColorScheme( // --> see if works for light mode as we progress
    primary = DarkGray,
    secondary = Platinum,
    tertiary = GunMetal,

    background = White,
    surface = White,

    // trying for contrasting accessible colors later
    //    onPrimary = Color.White,
    //    onSecondary = Color.White,
    //    onTertiary = Color.White,
    //    onBackground = Color(0xFF1C1B1F),
    //    onSurface = Color(0xFF1C1B1F),

)

// theme composable
@Composable
// yoinked concepts from Hoskins -- foundations found in CSCE546 examples
fun ImprovableTheme(
    // CHECKS IF THE DEVICE IS RUNNING DARK MODE, THEN SWITCHES
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
