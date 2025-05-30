package com.CoolPeppers.android.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.CoolPeppers.android.R
import com.CoolPeppers.android.data.model.Clinic
//import com.CoolPeppers.android.ui.theme.LightBgSecondary
//import com.CoolPeppers.android.ui.theme.LightTextPrimary
import com.CoolPeppers.android.ui.theme.Montserrat
import com.CoolPeppers.android.util.PriceConversion
import com.CoolPeppers.android.util.RatingStars

@Composable
fun ClinicCard(
    clinic: Clinic,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
//            containerColor = LightBgSecondary
        ),
        modifier = Modifier
            .size(width = 290.dp, height = 152.dp)
            .clickable(onClick = onClick)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = clinic.logoUrl,
                        contentDescription = "Clinic Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = clinic.name,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
//                                color = LightTextPrimary
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = clinic.address,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
//                                color = LightTextPrimary
                            ),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.metro),
                                contentDescription = "Metro",
                                modifier = Modifier.size(17.dp),
                                tint = Color.Red
                            )
                            Spacer(Modifier.width(2.dp))
                            clinic.metro?.let {
                                Text(
                                    text = it,
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontFamily = Montserrat,
                                        fontWeight = FontWeight.Light,
                                        fontSize = 12.sp,
//                                        color = LightTextPrimary
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                    .padding(start = 5.dp)
                ) {
                    clinic.price?.let {
                        PriceConversion(it.toInt())
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    RatingStars(clinic.rating.toInt())
                }
            }

            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
