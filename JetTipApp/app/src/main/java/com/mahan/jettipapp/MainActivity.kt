package com.mahan.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahan.jettipapp.ui.components.InputField
import com.mahan.jettipapp.ui.components.RoundButton
import com.mahan.jettipapp.ui.components.TopHeader
import com.mahan.jettipapp.ui.theme.JetTipAppTheme

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    TopHeader()
                    Spacer(modifier = Modifier.height(4.dp))
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent() {
    BillForm() {
        Log.d("value", "MainContent: $it")
    }
}

@ExperimentalComposeUiApi
@Composable
private fun BillForm(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(key1 = totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    var numberOfContributors by remember {
        mutableStateOf(1)
    }
    var sliderPositionValue by remember {
        mutableStateOf(0f)
    }
    val keyBoardController = LocalSoftwareKeyboardController.current
    val splitRange = IntRange(start = 1, endInclusive = 100)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.LightGray
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                modifier = Modifier.fillMaxWidth(),
                valueState = totalBillState,
                label = "Bill Amount",
                leadingIcon = Icons.Rounded.AttachMoney,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions

                    //onValueChanged
                    onValueChanged(totalBillState.value)

                    keyBoardController!!.hide()

                }
            )

            if (true) {
                // First Row under InputField
                Row(
                    modifier = Modifier
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier
                            .align(
                                Alignment.CenterVertically
                            )
                            .padding(start = 6.dp)
                    )
                    Spacer(modifier = Modifier.width(170.dp))
                    // Plus and Minus Buttons Row
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RoundButton(
                            icon = Icons.Default.Remove,
                            onClick = {
                                if (numberOfContributors > splitRange.first)
                                    numberOfContributors--
                            }
                        )
                        Text(
                            text = numberOfContributors.toString(),
                            modifier = Modifier
                                .align(
                                    Alignment.CenterVertically
                                )
                                .padding(horizontal = 9.dp)
                        )
                        RoundButton(
                            icon = Icons.Default.Add,
                            onClick = {
                                if (numberOfContributors < splitRange.last)
                                    numberOfContributors++
                            }
                        )
                    }

                } // End of First Row
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(230.dp))
                    Text(
                        text = "$33",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                } // End of Second Row
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(text = "${sliderPositionValue.times(100).toInt()}%")
                    Slider(
                        value = sliderPositionValue,
                        onValueChange = {
                            // it refers to new Value
                            sliderPositionValue = it
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        steps = 5
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        TopHeader()
    }
}