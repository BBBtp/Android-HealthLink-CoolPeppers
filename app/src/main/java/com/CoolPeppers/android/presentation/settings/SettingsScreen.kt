package com.CoolPeppers.android.presentation.settings

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.CoolPeppers.android.R
import com.CoolPeppers.android.presentation.components.HealthLinkTextField
import com.CoolPeppers.android.presentation.components.Option
import java.util.Locale

// TODO: сделать больше настроек, выпадающей окно изменения языка

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    modifier: Modifier = Modifier,
) {
    val localeOptions = listOf(
        Pair(R.string.en, "en"),
        Pair(R.string.ru, "ru")
    )
    val context = LocalContext.current
    Log.d("ContextCheck", "SettingsScreen context: $context")

    val sharedPreferences = context.getSharedPreferences("Theme", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {

        val themeOptions = listOf(
            Pair(R.string.light_theme_setting, AppCompatDelegate.MODE_NIGHT_NO),
            Pair(R.string.dark_theme_setting, AppCompatDelegate.MODE_NIGHT_YES),
            Pair(R.string.system_theme_setting, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM))

        var themeExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = themeExpanded,
            onExpandedChange = { themeExpanded = !themeExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            HealthLinkTextField(
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                readOnly = true,
                value = "current theme",
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = themeExpanded) },
            )

            ExposedDropdownMenu(
                expanded = themeExpanded,
                onDismissRequest = { themeExpanded = false }
            ) {
                themeOptions.forEach { (res, mode) ->
                    DropdownMenuItem(
                        text = { Text(stringResource(res)) },
                        onClick = {
                            editor.putInt("mode", mode)
                            editor.apply()
                            val shareMode = sharedPreferences.getInt("mode", AppCompatDelegate.MODE_NIGHT_NO)
                            Log.d("themecheck", "theme mode (settings) = $shareMode")
                            themeExpanded = false
                            (context as? Activity)?.recreate()
                        }
                    )
                }
            }
        }
    }
}
