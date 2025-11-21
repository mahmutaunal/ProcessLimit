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
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 64.dp, vertical = 48.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 720.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.title_process_limit),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.label_current_value),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = currentValue ?: stringResource(id = R.string.label_empty_or_default),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .focusable()
                    .width(360.dp)
                    .height(72.dp),
                onClick = {
                    onApplyClick()
                    currentValue = getCurrentValue()
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.btn_apply_zero),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}