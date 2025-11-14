package com.example.orphanapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    background = md_theme_light_background,
    surface = md_theme_light_surface,
    onBackground = md_theme_light_onBackground,
    onSurface = md_theme_light_onSurface,
)

@Composable
fun OrphanAppTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
