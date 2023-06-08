package android.weather.compose.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.weather.compose.utils.poppins
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun WeatherAppComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun textColor() = if (isSystemInDarkTheme()) Color.White else Color.Black

@Composable
fun BackgroundContainer(
    modifier: Modifier = Modifier,
    columnScope: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = containerColor()),
        modifier = modifier
            .padding(horizontal = 11.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(6.7.dp)
    ) {
        columnScope(this)
    }
}

@Composable
fun ContainerRow(text: String, onClick: () -> Unit = {}) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(start = 9.3.dp, top = 13.dp, end = 12.dp, bottom = 13.dp)
        ) {
            Text(text = text)
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "ic_details",
                tint = textColor()
            )
        }
        Divider(
            color = dividerColor(), modifier = Modifier
                .fillMaxWidth()
                .height(0.7.dp)
        )
    }
}

@Composable
fun DarkText(
    modifier: Modifier = Modifier,
    text: String,
    family: androidx.compose.ui.text.font.FontFamily = poppins,
    color: Color = textColor(),
    fontSize: TextUnit = 14.sp,
    textAlign: TextAlign = TextAlign.Justify,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        fontFamily = family,
        color = color,
        fontSize = fontSize,
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun LightText(
    modifier: Modifier = Modifier,
    text: String,
    family: androidx.compose.ui.text.font.FontFamily = poppins,
    color: Color = LightText,
    fontSize: TextUnit = 14.sp,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        fontFamily = family,
        fontSize = fontSize,
        color = color,
        modifier = modifier,
        overflow = overflow
    )
}

@SuppressLint("ComposableNaming")
@Composable
fun text(
    text: String,
    family: androidx.compose.ui.text.font.FontFamily = poppins,
    color: Color = textColor(),
) = Text(text = text, fontFamily = family, color = color)


val shape = RoundedCornerShape(6.7.dp)