import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.CoolPeppers.android.R

@Preview(widthDp = 400)
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
            .padding(start = 16.dp, end = 8.dp)
    ) {
        Option(icon = Icons.Default.Face, text = "Редактирование профиля", buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight)
        Option(icon = Icons.Default.Build, text = "Настройки", buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight)
        Option(icon = Icons.Default.Refresh, text = "История записей", buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight)
        Option(icon = Icons.Default.Info, text = "О приложении", buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight)
        Option(icon = Icons.AutoMirrored.Filled.ExitToApp, text = "Выход", buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight)
    }
}

@Preview
@Composable
fun Settings(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, bottom = 4.dp)
    ) {
        Option(icon = Icons.Default.Face, text = "Язык", buttonIcon = Icons.Default.KeyboardArrowDown)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Option(icon = Icons.Default.Build, text = "Темная тема", modifier = Modifier.weight(1f))
            Switch(checked = false, onCheckedChange = {}, modifier = Modifier.size(40.dp, 20.dp).scale(0.7f))
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

@Preview
@Composable
fun Option(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.AccountCircle,
    text: String = "random",
    onClick: (() -> Unit)? = null,
    buttonIcon: ImageVector? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
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

@Preview
@Composable
fun About(modifier: Modifier = Modifier) {
    Text(fontSize = 20.sp, fontWeight = FontWeight.Medium,
        text = "Приложение позволяет быстро и удобно записываться на приём" +
                " к врачам, оплачивать услуги онлайн и взаимодействовать с" +
                " клиниками через встроенный чат.\n" +
                "\uD83D\uDC68\u200D\uD83D\uDCBB Богдан Топорин\n" +
                "\uD83D\uDC68\u200D\uD83D\uDCBB Холматов Темур\n" +
                "\uD83D\uDC68\u200D\uD83D\uDCBB Алексей Утенков\n" +
                "\uD83D\uDC69\u200D\uD83D\uDCBB Анна Гладышева\n" +
                "\uD83D\uDC68\u200D\uD83D\uDCBB Александр Шубин")
}

@Preview
@Composable
fun Avatar(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Аватар пользователя",
            modifier = Modifier
                .size(122.dp)
                .clip(CircleShape)
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

