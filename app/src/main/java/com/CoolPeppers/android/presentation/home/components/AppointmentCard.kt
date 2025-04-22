package com.CoolPeppers.android.presentation.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.SlotResponse
import com.CoolPeppers.android.ui.theme.LightBgSecondary
import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat
import com.CoolPeppers.android.util.ParseSLotTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordCard(
    appointment: Appointment,
    doctor: Doctor,
    slot: SlotResponse
) {
    val (date, time) = ParseSLotTime(slot.slotTime)

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
                    AsyncImage(
                        model = doctor.photoUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = doctor.firstName + " " + doctor.lastName,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = LightTextPrimary
                            )
                        )
                        Text(
                            text = doctor.specialization,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                color = LightTextPrimary
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = date,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = LightTextPrimary
                            )
                        )
                        Text(
                            text = time,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                color = LightTextPrimary
                            )
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = "Info",
                            tint = LightTextPrimary
                        )
                    }
                }

            }
        }
    }
}