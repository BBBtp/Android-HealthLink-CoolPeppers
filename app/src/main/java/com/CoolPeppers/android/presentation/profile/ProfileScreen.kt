import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.MockProfileController
import com.CoolPeppers.android.data.model.Profile
import com.CoolPeppers.android.data.model.ProfileController
import com.CoolPeppers.android.presentation.profile.ProfileViewModel
import com.CoolPeppers.android.presentation.profile.ProfileViewModelFactory
import com.CoolPeppers.android.ui.theme.ShimmerColorShades


@Preview(widthDp = 400, showBackground = true)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            MockProfileController()
        )
    ),
) {
    val profile by viewModel.profileState
    val loading by viewModel.loadingState

    Column(
        verticalArrangement = Arrangement.spacedBy(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {

        // TODO: сделать нормальное обновление аватарки

        ProfileInfo(profile = profile, onAvatarClick = { viewModel.updateAvatar("newurl") })
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

// TODO: сделать больше настроек, выпадающей окно изменения языка

@Preview(showBackground = true)
@Composable
fun Settings(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
//            .padding(start = 16.dp, end = 8.dp, bottom = 4.dp)
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
fun ProfileInfo(modifier: Modifier = Modifier, profile: Profile, onAvatarClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Avatar(onClick = onAvatarClick, avatarUrl = profile.avatarUrl)
        Text(
            text = profile.first_name + profile.last_name,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = profile.email,
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
fun AvatarPreview() {
    Avatar(
        onClick = {},
        avatarUrl = "https://www.meme-arsenal.com/memes/5bfd716225affd016f78d5b2630c67e0.jpg"
    )
}

// TODO: сделать шиммер при загрузке

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    avatarUrl: String
) {
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
        AsyncImage(
            model = avatarUrl,
            contentDescription = "Аватар",
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
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(onClick = onClick)
            )
        }
    }
}

// TODO: сделать навигацию из ProfileScreen в EditProfile

@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {

    val profile by viewModel.profileState

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProfileTextField(
            label = "Имя",
            value = profile.first_name,
            onValueChange = viewModel::updateFirstName
        )

        ProfileTextField(
            label = "Фамилия",
            value = profile.last_name,
            onValueChange = viewModel::updateLastName
        )

        ProfileTextField(
            label = "Email",
            value = profile.email,
            onValueChange = viewModel::updateEmail
        )

        ProfileTextField(
            label = "Телефон",
            value = profile.phone,
            onValueChange = viewModel::updatePhone
        )

//        PasswordField(
//            value = "",
//            onValueChange = {}
//        )

        Button(
            onClick = viewModel::saveChanges,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить изменения")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    EditProfile(
        viewModel = viewModel(
            factory = ProfileViewModelFactory(
                MockProfileController()
            )
        ),
    )
}

@Composable
fun ProfileTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileTextFieldPreview() {
    ProfileTextField(label = "textfield", value = "placeholder", onValueChange = {})
}

