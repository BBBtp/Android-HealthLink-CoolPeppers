package com.CoolPeppers.android.presentation.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.CoolPeppers.android.R
import com.CoolPeppers.android.ui.theme.LightTextPrimary

@Composable
fun PaymentSuccessScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp),
                contentAlignment = Alignment.Center
            ) {

                    Image(
                        painter = painterResource(id = R.drawable.success),
                        contentDescription = "Success",
                        modifier = Modifier.size(100.dp)
                    )
                }


            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Оплата прошла успешно!\nСпасибо, что вы выбираете нас",
                color = LightTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                modifier = Modifier.width(343.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))


            Button(
                onClick = {navController.navigate("home")},
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEAF4F4),
                    contentColor = LightTextPrimary
                )
            ) {
                Text(
                    text = "На главную",
                    fontSize = 16.sp
                )
            }
        }
    }
}