package android.weather.compose.presentation

import android.weather.compose.R
import android.weather.compose.ui.theme.DarkText
import android.weather.compose.ui.theme.splashColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun SplashUi(onNavigate: () -> Unit) {
    val ui = rememberSystemUiController()
    ui.setStatusBarColor(splashColor())
    val delay by remember {
        mutableLongStateOf(1000L)
    }
    SplashContent(modifier = Modifier, delay = delay, onNavigate = onNavigate)
}

@Composable
fun SplashContent(modifier: Modifier, delay: Long, onNavigate: () -> Unit) {
    ConstraintLayout(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(color = splashColor())
    ) {
        val topGuideline = createGuidelineFromTop(0.6f)
        val (icon, animation, text, secAnimation) = createRefs()
        Image(
            painterResource(id = R.drawable.splash_icon),
            contentDescription = "navigation",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(topGuideline)
                    start.linkTo(parent.start)
                    width = Dimension.wrapContent
                    height = Dimension.fillToConstraints
                },
        )
        val millAnimation =
            rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.wind_mill)
            )
        val windAnimation =
            rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.splash_animation)
            )
        LottieAnimation(
            composition = millAnimation.value,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.constrainAs(animation) {
                bottom.linkTo(topGuideline)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.wrapContent
                height = Dimension.value(200.dp)
            }
        )
        DarkText(
            text = stringResource(id = R.string.weather),
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier
                .constrainAs(text) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(animation.bottom)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
        )
        LottieAnimation(
            composition = windAnimation.value,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.constrainAs(secAnimation) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(text.bottom)
                bottom.linkTo(parent.bottom)
                width = Dimension.wrapContent
                height = Dimension.value(200.dp)
            }
        )
        LaunchedEffect(null) {
            delay(delay)
            onNavigate()
        }
    }
}