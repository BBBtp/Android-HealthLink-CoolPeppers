import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R
import com.CoolPeppers.android.ui.theme.ShimmerColorShades


@Preview(widthDp = 400, showBackground = true)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        ProfileInfo()
        OptionsList()
    }
}

@Composable
fun OptionsList(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
//            .padding(start = 16.dp, end = 8.dp)
    ) {
        Option(
            icon = Icons.Default.Face,
            text = "Редактирование профиля",
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {})
        Option(
            icon = Icons.Default.Build,
            text = "Настройки",
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {})
        Option(
            icon = Icons.Default.Refresh,
            text = "История записей",
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {})
        Option(
            icon = Icons.Default.Info,
            text = "О приложении",
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {})
        Option(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            text = "Выход",
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun Settings(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, bottom = 4.dp)
    ) {
        Option(
            icon = Icons.Default.Face,
            text = "Язык",
            buttonIcon = Icons.Default.KeyboardArrowDown,
            onClick = {})
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Option(
                icon = Icons.Default.Build,
                text = "Темная тема",
                modifier = Modifier.weight(1f),
                onClick = {})
            Switch(
                checked = false,
                onCheckedChange = {},
                modifier = Modifier
                    .size(40.dp, 20.dp)
                    .scale(0.7f)
            )
        }
    }
}


@Composable
fun ProfileInfo(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Avatar()
        Text(
            text = stringResource(R.string.app_name),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = stringResource(R.string.email),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OptionPreview() {
    Option(onClick = {}, text = "random", icon = Icons.Default.AccountCircle)
}


@Composable
fun Option(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: (() -> Unit),
    buttonIcon: ImageVector? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != {}) Modifier.clickable { onClick() } else Modifier)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        if (buttonIcon != null) {
            Icon(
                imageVector = buttonIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun About(modifier: Modifier = Modifier) {
    Text(
        fontSize = 20.sp, fontWeight = FontWeight.Medium,
        text = stringResource(R.string.about)
    )
}

@Preview(showBackground = true)
@Composable
fun Avatar(modifier: Modifier = Modifier) {
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
    // это я украл из components/shimmereffect. надо вопрос решить
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Аватар пользователя",
            modifier = Modifier
                .size(122.dp)
                .clip(CircleShape)
                .background(brush)
        )
        Surface(
            shape = CircleShape,
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.BottomEnd)
        ) {

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun EditProfile(modifier: Modifier = Modifier) {

    var username by remember { mutableStateOf("") }

    OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") }
    )
}

