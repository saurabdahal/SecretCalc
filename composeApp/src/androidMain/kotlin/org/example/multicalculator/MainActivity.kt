package org.example.multicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

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

    if (complete.value && operation.value.isNotEmpty()) {
        var answer = 0
        when (operation.value) {
            "+" -> {
                answer = leftNumber.intValue + rightNumber.intValue
            }
            "-" -> {
                answer = leftNumber.intValue - rightNumber.intValue
            }
            "*" -> {
                answer = leftNumber.intValue * rightNumber.intValue
            }
            "/" -> {
                answer = leftNumber.intValue / rightNumber.intValue
            }
        }
        displayText.value = answer.toString()
    }else if (operation.value.isNotEmpty() && !complete.value) {
            displayText.value = rightNumber.intValue.toString();
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

            if (operation.value.isNotEmpty() && !complete.value) {
                rightNumber.intValue = rightNumber.intValue * 10 + btnNum
            } else {
                leftNumber.intValue = leftNumber.intValue * 10 + btnNum
            }
    }


    fun operationPress(op: String) {
        if (!complete.value) {
            operation.value = op
        }
    }

    fun equalsPress() {
        complete.value = true
    }

    fun reset(){
        displayText.value = "0"
    }

    fun twilioRest(){
        val accountSid = "AC29bc8545692d6d112f1b0de0ef097d63"
        val authToken = "6f997613ab3f0dadd7bffd0b9743bb39"

        val fromPhoneNumber = "+17723104281"
        val toPhoneNumber = "+17059216266"
        val messageBody = "36"

        //val messageCreator = Message.creator(toPhoneNumber, fromPhoneNumber, messageBody)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.twilio.com/2010-04-01/Accounts/$accountSid/Messages")
            .post(
                FormBody.Builder()
                    .add("To", toPhoneNumber.toString())
                    .add("From", fromPhoneNumber.toString())
                    .add("Body", messageBody)
                    .build()
            )
            .header("Authorization", Credentials.basic(accountSid, authToken))
            .header("Content-Type","application/x-www-form-urlencoded")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                displayText.value = "Failed to send SMS: ${e.message}"
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    displayText.value = "SMS sent successfully!"
                } else {
                    displayText.value = "Failed to send SMS: ${response.message} " +
                            "and code is ${response.code}"

                    println("error : ${response.networkResponse}")
                }
                response.close()
            }
        })
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
                    ResetDisplaysButton(onPress = {
//                        twilioClient()
                        twilioRest()
                    })
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                CalcOperationButton(
                    operation = "+",
                    onPress = { op -> operationPress(op) },
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2B311B)
                    )
                )
                CalcOperationButton(
                    operation = "-",
                    onPress = { op -> operationPress(op) },
                           ButtonDefaults.buttonColors(
                               containerColor = Color(0xFF7E5A4B)
                            )
                )
                CalcOperationButton(
                    operation = "*",
                    onPress = { op -> operationPress(op) },
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF413A3C)
                    )
                )
                CalcOperationButton(
                    operation = "/",
                    onPress = { op -> operationPress(op) },
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE91E63)
                    )
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
 * Composable to  handle Operation buttons. Any feature on the buttons can be
 * modified from this method
 * @param operation: String : Operation that is displayed on the button
 */
@Composable
fun CalcOperationButton(operation: String, onPress: (operation: String) -> Unit, bgColor: ButtonColors) {
    Button(
        onClick = { onPress(operation) },
        modifier = Modifier.padding(4.dp),
        colors = bgColor
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
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) Color(0xFF00CC00) else Color(0xFF905CC4), label = ""
    )

    Button(
        onClick = onPress,
        modifier = Modifier
            .padding(4.dp)
            .hoverable(interactionSource)
        ,colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Text(text = "=")
    }
}

/**
 * Composable to reset the display. Any feature on the buttons can be
 * modified from this method
 */
@Composable
fun ResetDisplaysButton(onPress: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) Color(0xFF00CC00) else Color(0xFF00CCDD), label = ""
    )

    Button(
        onClick = onPress,
        modifier = Modifier
            .padding(4.dp)
            .hoverable(interactionSource)
        ,colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Text(text = "C")
    }
}
