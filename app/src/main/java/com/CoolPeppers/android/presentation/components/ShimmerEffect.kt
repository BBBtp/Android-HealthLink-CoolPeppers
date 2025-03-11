import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.CoolPeppers.android.ui.theme.ShimmerColorShades

@Composable
fun ShimmerAnimation() {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = ShimmerColorShades,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )

    Column(modifier = Modifier.padding(start =  26.dp),
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        ShimmerItemMainScreenNameAndPhoto(brush = brush)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShimmerItemMainScreenRequestAndClinicList(brush = brush)
            ShimmerItemMainScreenRequestAndClinicList(brush = brush)
        }
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShimmerItemMainScreenDoctorType(brush = brush)
            ShimmerItemMainScreenDoctorType(brush = brush)
            ShimmerItemMainScreenDoctorType(brush = brush)
            ShimmerItemMainScreenDoctorType(brush = brush)
        }

        Column(modifier = Modifier.padding(end = 26.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ShimmerItemMainScreenDoctorList(brush = brush)
            ShimmerItemMainScreenDoctorList(brush = brush)
        }

        Row(modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShimmerItemMainScreenRequestAndClinicList(brush = brush)
            ShimmerItemMainScreenRequestAndClinicList(brush = brush)
        }
    }
}

@Composable
fun ShimmerItemMainScreenNameAndPhoto(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        ShimmerItem(
            brush = brush,
            width = 191.dp,
            height = 44.dp,
            cornerRadius = 10.dp
        )

        Spacer(modifier = Modifier.width(120.dp))

        ShimmerItem(
            brush = brush,
            width = 48.dp,
            height = 48.dp,
            shape = CircleShape
        )
    }
}


@Composable
fun ShimmerItemMainScreenRequestAndClinicList(brush: Brush) {
    ShimmerItem(
        brush = brush,
        width = 294.5.dp,
        height = 192.dp,
        cornerRadius = 10.dp
    )
}

@Composable
fun ShimmerItemMainScreenDoctorList(brush: Brush) {
    ShimmerItem(
        brush = brush,
        width = 380.dp,
        height = 70.dp,
        cornerRadius = 10.dp
    )
}

@Composable
fun ShimmerItemMainScreenDoctorType(brush: Brush) {
    ShimmerItem(
        brush = brush,
        width = 100.dp,
        height = 39.dp,
        cornerRadius = 10.dp
    )
}


@Composable
fun ShimmerItem(
    brush: Brush,
    width: Dp? = null,
    height: Dp,
    cornerRadius: Dp = 0.dp,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(cornerRadius)
) {
    Spacer(
        modifier = Modifier
            .then(
                if (width != null) Modifier.size(width, height)
                else Modifier.fillMaxWidth().height(height)
            )
            .clip(shape)
            .background(brush = brush)
    )
}