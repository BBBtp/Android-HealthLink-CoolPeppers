import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.api.local.TokenManager
import com.CoolPeppers.android.data.model.User
import com.CoolPeppers.android.presentation.components.HealthLinkTextField
import com.CoolPeppers.android.presentation.components.Option
import com.CoolPeppers.android.presentation.profile.ProfileViewModel
import com.CoolPeppers.android.ui.theme.ShimmerColorShades
import com.CoolPeppers.android.ui.theme.secondaryDark
import com.CoolPeppers.android.ui.theme.secondaryLight
import com.CoolPeppers.android.util.createFileFromUri
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier.padding(12.dp),
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {

    val user by viewModel.userState.collectAsState()
    val loading by viewModel.loadingState
    val error by viewModel.errorState

    val context = LocalContext.current

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val photoFile = createFileFromUri("avatar", uri, context = context)
                viewModel.updateUser(
                    user.firstName, user.lastName, user.age, user.bloodType, photoFile
                )
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


    Column(
        verticalArrangement = Arrangement.spacedBy(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {

        ProfileInfo(
            profile = user,
            onAvatarClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            loadingState = loading, errorState = error
        )
        OptionsList(navController = navController)

    }


}

@Composable
fun OptionsList(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController

) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
//            .padding(start = 16.dp, end = 8.dp)
    ) {
        Option(icon = Icons.Default.Face,
            text = stringResource(R.string.edit_profile),
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = { navController.navigate("profile edit") })
        Option(icon = Icons.Default.Build,
            text = stringResource(R.string.settings),
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = { navController.navigate("settings") })
        Option(icon = Icons.Default.Refresh,
            text = stringResource(R.string.history),
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {})
        Option(icon = Icons.Default.Info,
            text = stringResource(R.string.about),
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = {})
        Option(icon = Icons.AutoMirrored.Filled.ExitToApp,
            text = stringResource(R.string.log_out),
            buttonIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            onClick = { showLogoutDialog = true })
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Подтверждение выхода") },
            text = { Text("Вы уверены, что хотите выйти из аккаунта?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logOut()
                        navController.navigate("auth") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                ) {
                    Text("Выйти")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}


@Composable
fun ProfileInfo(
    modifier: Modifier = Modifier,
    profile: User,
    onAvatarClick: () -> Unit,
    loadingState: Boolean,
    errorState: String?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Avatar(
            onClick = onAvatarClick,
            avatarUrl = profile.photoUrl,
            loadingState = loadingState,
            errorState = errorState
        )
        Text(
            text = (profile.firstName ?: "") + " " + (profile.lastName ?: " "),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = profile.email, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun About(modifier: Modifier = Modifier) {
    Text(
        fontSize = 20.sp, fontWeight = FontWeight.Medium, text = stringResource(R.string.about_text), color = MaterialTheme.colorScheme.secondary
    )
}

@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    Avatar(
        onClick = {},
        avatarUrl = "https://www.meme-arsenal.com/memes/5bfd716225affd016f78d5b2630c67e0.jpg",
        loadingState = false,
        errorState = null
    )
}

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    avatarUrl: String?,
    loadingState: Boolean,
    errorState: String?,
) {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing), RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = ShimmerColorShades,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )
    // это я украл из components/shimmereffect. надо вопрос решить
    Box(modifier = modifier) {

        val customModifier: Modifier = Modifier
            .size(122.dp)
            .clip(CircleShape)


        if (loadingState) {
            ShimmerItem(
                brush = brush,
                height = 122.dp,
                cornerRadius = 1.dp,
                modifier = customModifier
            )
        } else if (errorState != null) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = stringResource(R.string.avatar),
                modifier = customModifier
            )
        } else {
            AsyncImage(
                model = avatarUrl,
                contentDescription = stringResource(R.string.avatar),
                modifier = customModifier,
                contentScale = ContentScale.Crop
            )
        }
        Surface(
            shape = CircleShape, modifier = Modifier
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

@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController,
) {
    val profile by viewModel.userState.collectAsState()
/*
    Log.d("edit profile", "accuired profile:"
            + "\nfirst name = " + profile.firstName
            + "\nlast name = " + profile.lastName
            + "\nemail = " + profile.email
            + "\nage = " + profile.age
            + "\nblood = " + profile.bloodType
            + "\nusername = " + profile.username)
    */
    var firstName by remember(profile) { mutableStateOf(profile.firstName ?: "") }
    var lastName by remember(profile) { mutableStateOf(profile.lastName ?: "") }
    var email by remember(profile) { mutableStateOf(profile.email) }
    var age by remember(profile) { mutableStateOf(profile.age?.toString() ?: "") }
    var bloodType by remember(profile) { mutableStateOf(profile.bloodType ?: "") }
    var username by remember(profile) { mutableStateOf(profile.username) }
/*
    Log.d("edit profile", "working profile:"
            + "\nfirst name = " + firstName
            + "\nlast name = " + lastName
            + "\nemail = " + email
            + "\nage = " + age
            + "\nblood = " + bloodType
            + "\nusername = " + username)
    */
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        HealthLinkTextField(
            label = { Text(stringResource(R.string.first_name)) },
            value = firstName,
            onValueChange = { firstName = it },
            modifier = Modifier.fillMaxWidth()
        )
        HealthLinkTextField(
            label = { Text(stringResource(R.string.last_name)) },
            value = lastName,
            onValueChange = { lastName = it },
            modifier = Modifier.fillMaxWidth()
        )
        HealthLinkTextField(
            label = { Text(stringResource(R.string.email)) },
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth()
        )
        HealthLinkTextField(
            label = { Text(stringResource(R.string.username)) },
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth()
        )
        HealthLinkTextField(
            label = { Text(stringResource(R.string.age)) },
            value = age,
            onValueChange = { age = it },
            modifier = Modifier.fillMaxWidth()
        )
        HealthLinkTextField(
            label = { Text(stringResource(R.string.blood_type)) },
            value = bloodType,
            onValueChange = { bloodType = it },
            modifier = Modifier.fillMaxWidth()
        )


//        PasswordField(
//            value = "",
//            onValueChange = {}
//        )

        Button(
            onClick = {
                viewModel.updateUser(
                    firstName, lastName, age.toIntOrNull(), bloodType, photo = null
                )
                navController.navigate("profile")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_changes))
        }


    }
}

//@Preview(showBackground = true)
//@Composable
//fun EditProfilePreview() {
//    EditProfile(
//        viewModel = hiltViewModel(), modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//    )
//}

@Composable
fun ProfileTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholder: Int
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(stringResource(placeholder)) },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileTextFieldPreview() {
    ProfileTextField(
        label = "textfield",
        value = "placeholder",
        onValueChange = {},
        placeholder = R.string.avatar
    )
}
