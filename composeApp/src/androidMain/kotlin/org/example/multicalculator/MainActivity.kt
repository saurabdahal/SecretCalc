package org.example.multicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    /**
     * Main entry point for the calculator application.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalcView()
        }
    }
}

/**
 * The main composable function for the calculator UI.
 * Builds the overall structure of the calculator with a display,
 * numeric buttons, operation buttons, and an equals button.
 * Rows and Columns for the buttons as well as the positioning of the display
 * can be controlled from this composable
 */

@Composable
fun CalcView() {
    val displayText = rememberSaveable { mutableStateOf("0") }
    val leftNumber = rememberSaveable { mutableIntStateOf(0) }
    val rightNumber = rememberSaveable { mutableIntStateOf(0) }
    val operation = rememberSaveable { mutableStateOf("") }
    val complete = rememberSaveable { mutableStateOf(false) }

    if (!complete.value && operation.value.isNotEmpty()) {
        var answer = 0
        when (operation.value) {
            "+" -> answer = leftNumber.intValue + rightNumber.intValue
            "-" -> answer = leftNumber.intValue - rightNumber.intValue
            "*" -> answer = leftNumber.intValue * rightNumber.intValue
            "/" -> answer = leftNumber.intValue / rightNumber.intValue
        }
        displayText.value = answer.toString()
        complete.value = false
    }else if (operation.value.isNotEmpty() && !complete.value) {
        try {
            leftNumber.value = displayText.value.toInt()
            displayText.value = rightNumber.intValue.toString();
        } catch (e: NumberFormatException) {
            println(e.message)
        }
    }else {
        displayText.value = leftNumber.intValue.toString()
    }

    fun numberPress(btnNum: Int) {
        if (complete.value) {
            leftNumber.intValue = 0
            rightNumber.intValue = 0
            operation.value = ""
            complete.value = false
        }

        val currentDisplay = displayText.value
        if (currentDisplay == "0" || operation.value.isNotEmpty()) {
            displayText.value = btnNum.toString()
        } else {
            if (operation.value.isNotEmpty() && !complete.value) {
                rightNumber.intValue = rightNumber.intValue * 10 + btnNum
            } else {
                leftNumber.intValue = leftNumber.intValue * 10 + btnNum
            }
        }
    }


    fun operationPress(op: String) {
        if (!complete.value) {
            operation.value = op
        }
    }

    fun equalsPress() {
        operation.value = ""
        complete.value = true
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        CalcDisplay(display = displayText)

        Row {
            Column(modifier = Modifier.weight(1f)) {
                for (i in 7 downTo 1 step 3) {
                    CalcRow(
                        onPress = { number -> numberPress(number) },
                        startNum = i,
                        numButtons = 3
                    )
                }
                Row {
                    CalcNumericButton(number = 0, onPress = { number -> numberPress(number) })
                    CalcEqualsButton(onPress = {
                        equalsPress()
                    })
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                CalcOperationButton(
                    operation = "+",
                    onPress = { op -> operationPress(op) }
                )
                CalcOperationButton(
                    operation = "-",
                    onPress = { op -> operationPress(op) }
                )
                CalcOperationButton(
                    operation = "*",
                    onPress = { op -> operationPress(op) }
                )
                CalcOperationButton(
                    operation = "/",
                    onPress = { op -> operationPress(op) }
                )
            }
        }
    }
}



/**
Composable to format the structure of the buttons
 *  handle display on the app
 * @param startNum: Int : this is the starting number for the numeric button
 * @param numButtons: Int : this determines how many buttons we need in a row
 */

@Composable
fun CalcRow(
    onPress: (number: Int) -> Unit,
    startNum: Int,
    numButtons: Int
) {
    val endNum = startNum + numButtons

    Row(modifier = Modifier.padding(0.dp)) {
        for (i in startNum until endNum) {
            CalcNumericButton(number = i, onPress = onPress)
        }
    }
}


/**
 * Composable function for displaying the calculator's output.
 * @param display:MutableState<String> : display parameter which is the main component to
 * handle display on the app
 */
@Composable
fun CalcDisplay(display: MutableState<String>) {
    Text(
        text = display.value,
        modifier = Modifier
            .height(50.dp)
            .padding(5.dp)
            .fillMaxWidth()
    )
}

/**
 * Composable to handle numeric buttons. Any feature on the buttons can be
 * modified from this method
 * @param number: Int : Number that is displayed on the button
 */

@Composable
fun CalcNumericButton(number: Int, onPress: (number: Int) -> Unit) {
    Button(
        onClick = { onPress(number) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = number.toString())
    }
}

/**
 * Composable to handle Operation buttons. Any feature on the buttons can be
 * modified from this method
 * @param operation: String : Operation that is displayed on the button
 */
@Composable
fun CalcOperationButton(operation: String, onPress: (operation: String) -> Unit) {
    Button(
        onClick = { onPress(operation) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = operation)
    }
}

/**
 * Composable to handle Equals buttons. Any feature on the buttons can be
 * modified from this method
 */
@Composable
fun CalcEqualsButton(onPress: () -> Unit) {
    Button(
        onClick = onPress,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "=")
    }
}
