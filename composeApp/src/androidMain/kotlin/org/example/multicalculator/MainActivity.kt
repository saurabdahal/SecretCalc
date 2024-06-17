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
import androidx.compose.runtime.remember
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
    val leftNumber = rememberSaveable { mutableStateOf(0) }
    val rightNumber = rememberSaveable { mutableStateOf(0) }
    val operation = rememberSaveable { mutableStateOf("") }
    val complete = rememberSaveable { mutableStateOf(false) }

    if (complete.value && operation.value.isNotEmpty()) {
        var answer = 0
        when (operation.value) {
            "+" -> answer = leftNumber.value + rightNumber.value
            "-" -> answer = leftNumber.value - rightNumber.value
            "*" -> answer = leftNumber.value * rightNumber.value
            "/" -> answer = leftNumber.value / rightNumber.value
        }
        displayText.value = answer.toString()
        complete.value = false
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
                    CalcRow(display = displayText, startNum = i, numButtons = 3)
                }
                Row {
                    CalcNumericButton(number = 0, display = displayText)
                    CalcEqualsButton(display = displayText, leftNumber, rightNumber, operation, complete)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                CalcOperationButton(operation = "+", display = displayText, leftNumber, rightNumber, operation, complete)
                CalcOperationButton(operation = "-", display = displayText, leftNumber, rightNumber, operation, complete)
                CalcOperationButton(operation = "*", display = displayText, leftNumber, rightNumber, operation, complete)
                CalcOperationButton(operation = "/", display = displayText, leftNumber, rightNumber, operation, complete)
            }
        }
    }
}



/**
Composable to format the structure of the buttons
 * @param display: MutableState<String> : display parameter which is the main component to
 *  handle display on the app
 * @param startNum: Int : this is the starting number for the numeric button
 * @param numButtons: Int : this determines how many buttons we need in a row
 */

@Composable
fun CalcRow(
    display: MutableState<String>, startNum: Int, numButtons: Int
) {
    val endNum = startNum + numButtons

    Row(modifier = Modifier.padding(0.dp)) { // Row with 0dp padding
        for (i in startNum until endNum) {
            CalcNumericButton(number = i, display = display)
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
 * @param display: MutableState<String> : the value that is displayed on the display
 */

@Composable
fun CalcNumericButton(number: Int, display: MutableState<String>) {
    Button(
        onClick = { display.value += number },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = number.toString())
    }
}

/**
 * Composable to handle Operation buttons. Any feature on the buttons can be
 * modified from this method
 * @param operation: String : Operation that is displayed on the button
 * @param display: MutableState<String> : the value that is displayed on the display
 */
@Composable
fun CalcOperationButton(operation: String, display: MutableState<String>) {
    Button(
        onClick = {},
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = operation)
    }
}

/**
 * Composable to handle Equals buttons. Any feature on the buttons can be
 * modified from this method
 * @param display: MutableState<String> : the value that is displayed on the display
 */
@Composable
fun CalcEqualsButton(display: MutableState<String>) {
    Button(
        onClick = { display.value = "0" },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "=")
    }
}
