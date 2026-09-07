package com.example.multimodularmodernarchitecturefullapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Light Color Scheme - Modern Vibrant
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
)

// Dark Color Scheme - Modern Vibrant
private val DarkColorScheme = darkColorScheme(
    primary = Primary,                    // Keep vibrant blue
    onPrimary = Color(0xFF000033),       // Very dark blue
    primaryContainer = Color(0xFF003D99),
    onPrimaryContainer = PrimaryContainer,
    secondary = Secondary,                // Keep bright teal
    onSecondary = Color(0xFF003333),
    secondaryContainer = Color(0xFF006666),
    onSecondaryContainer = SecondaryContainer,
    tertiary = Tertiary,                  // Keep bright orange
    onTertiary = Color(0xFF331900),
    tertiaryContainer = Color(0xFF995C00),
    onTertiaryContainer = TertiaryContainer,
    error = Error,                        // Keep bright red
    onError = Color(0xFF660000),
    errorContainer = Color(0xFF8B0000),
    onErrorContainer = ErrorContainer,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = Color(0xFF7C8FA3),
    outlineVariant = Color(0xFF334155),
)



@Composable
fun MultimodularModernArchitectureFullAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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





