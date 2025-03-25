/*
package com.CoolPeppers.android.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.ui.theme.LightBgSecondary
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat

@Composable
fun RecordCard(
    appointment: Appointment,
    doctor: Doctor

) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = LightBgSecondary
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(appointment.doctor.image),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = appointment.doctor.fullName,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = colorResource(id = R.color.light_text_primary)
                            )
                        )
                        Text(
                            text = appointment.doctor.specialty,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.light_text_primary)
                            )
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = appointment.date, style = androidx.compose.ui.text.TextStyle(
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.light_text_primary)
                        )
                    )
                    Text(
                        text = appointment.timeRange, style = androidx.compose.ui.text.TextStyle(
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.light_text_primary)
                        )
                    )
                }
            }
        }
    }*/
