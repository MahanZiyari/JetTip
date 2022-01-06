package com.mahan.jettipapp

import android.os.Bundle
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
import com.mahan.jettipapp.utils.calculateTotalPerPerson
import com.mahan.jettipapp.utils.calculateTotalTip

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                var totalBillState by remember {
                    mutableStateOf("")
                }
                var numberOfContributors by remember {
                    mutableStateOf(1)
                }
                var tipAmount by remember {
                    mutableStateOf(0.0f)
                }
                val totalPerPerson = remember(
                    key1 = totalBillState,
                    key2 = numberOfContributors,
                    key3 = tipAmount
                ) {
                    calculateTotalPerPerson(
                        billValue = totalBillState,
                        tip = tipAmount,
                        splitBy = numberOfContributors
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    TopHeader(
                        totalPerPerson = totalPerPerson.toDouble()
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    MainContent(
                        totalBillState = totalBillState,
                        onBillAmountChanged = { newBillValue ->
                            totalBillState = newBillValue
                        },
                        numberOfContributors = numberOfContributors,
                        onNumberOfContributorsChanged = {
                            numberOfContributors = it
                        },
                        tipAmount = tipAmount,
                        onSliderValueChanged = {
                            // Calculating the amount of tip
                            tipAmount = calculateTotalTip(
                                totalBillState.toInt(),
                                it.times(100).toInt()
                            )
                        }
                    )
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
@Composable
fun MainContent(
    totalBillState: String,
    onBillAmountChanged: (String) -> Unit,
    numberOfContributors: Int,
    onNumberOfContributorsChanged: (Int) -> Unit,
    tipAmount: Float,
    onSliderValueChanged: (Float) -> Unit
) {
    BillForm(
        totalBillState = totalBillState,
        onBillAmountChanged = {
            onBillAmountChanged(it)
        },
        numberOfContributors = numberOfContributors,
        onNumberOfContributorsChanged = {
            onNumberOfContributorsChanged(it)
        },
        totalTipAmount = tipAmount,
        onSliderValueChanged = {
            onSliderValueChanged(it)
        }
    )
}

@ExperimentalComposeUiApi
@Composable
private fun BillForm(
    modifier: Modifier = Modifier,
    totalBillState: String,
    onBillAmountChanged: (String) -> Unit,
    numberOfContributors: Int,
    onNumberOfContributorsChanged: (Int) -> Unit,
    totalTipAmount: Float,
    onSliderValueChanged: (Float) -> Unit,
) {

    // States
    val validState = remember(key1 = totalBillState) {
        totalBillState.trim().isNotEmpty()
    }
    var sliderPositionValue by remember {
        mutableStateOf(0f)
    }


    // Non-states Variables
    val keyBoardController = LocalSoftwareKeyboardController.current
    val splitRange = IntRange(start = 1, endInclusive = 100)

    // The start of UI Design
    Surface(
        modifier = modifier
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
                    // onValueChanged(totalBillState.value)
                    onBillAmountChanged(totalBillState)

                    keyBoardController!!.hide()

                },
                onValueChanged = {
                    onBillAmountChanged(it)
                }
            )

            if (validState) {
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
                                if (numberOfContributors > splitRange.first) {
                                    onNumberOfContributorsChanged(
                                        numberOfContributors - 1
                                    )
                                }
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
                                if (numberOfContributors < splitRange.last) {
                                    onNumberOfContributorsChanged(
                                        numberOfContributors + 1
                                    )
                                }
                            }
                        )
                    }

                } // End of First Row

                // Second Row
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
                        text = "$totalTipAmount $",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                } // End of Second Row

                // Tip Percentage and Slider
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
                            onSliderValueChanged(it)
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