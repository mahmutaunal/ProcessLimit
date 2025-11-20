package com.mahmutalperenunal.processlimit

import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import androidx.tv.material3.darkColorScheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ProcessLimitScreen(
                        onApplyClick = {
                            applyMaxCachedProcessesZero()
                        },
                        getCurrentValue = {
                            getCurrentActivityManagerConstants()
                        }
                    )
                }
            }
        }
    }

    private fun applyMaxCachedProcessesZero() {
        try {
            Settings.Global.putString(
                contentResolver,
                "activity_manager_constants",
                "max_cached_processes=0"
            )

            Toast.makeText(
                this,
                getString(R.string.toast_applied),
                Toast.LENGTH_SHORT
            ).show()
        } catch (_: SecurityException) {
            Toast.makeText(
                this,
                getString(R.string.toast_permission_denied),
                Toast.LENGTH_LONG
            ).show()
        } catch (t: Throwable) {
            Toast.makeText(
                this,
                getString(R.string.toast_error, t.message),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getCurrentActivityManagerConstants(): String? {
        return Settings.Global.getString(
            contentResolver,
            "activity_manager_constants"
        )
    }
}

@Composable
fun ProcessLimitScreen(
    onApplyClick: () -> Unit,
    getCurrentValue: () -> String?
) {
    var currentValue by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        currentValue = getCurrentValue()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.title_process_limit),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(id = R.string.label_current_value),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = currentValue ?: stringResource(id = R.string.label_empty_or_default),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            Button(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .focusable()
                    .width(320.dp)
                    .height(72.dp),
                onClick = {
                    onApplyClick()
                    currentValue = getCurrentValue()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.btn_apply_zero),
                    fontSize = 18.sp
                )
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}