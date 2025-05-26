package com.CoolPeppers.android.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.CoolPeppers.android.data.model.User
//import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat

@Composable
fun UserInfoBlock(
    user: User,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.username, style = androidx.compose.ui.text.TextStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
//                    color = LightTextPrimary
                )
            )

            Row() {
                Text(
                    text = user.firstName + " " + user.lastName + ",", style = androidx.compose.ui.text.TextStyle(
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
//                        color = LightTextPrimary
                    )
                )
                Text(text = " ", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = user.age.toString() + " лет", style = androidx.compose.ui.text.TextStyle(
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
//                        color = LightTextPrimary
                    )
                )
            }
        }
        AsyncImage(
            model = user.photoUrl,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable {
                    navController.navigate("profile")
                },
            contentScale = ContentScale.Crop,
        )
    }
}