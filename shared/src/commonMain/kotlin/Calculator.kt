/*
Simple Class that defines methods to perform basic Arithmetic calculations
 */
class Calculator {

    /**
     * Adds two integers and returns the result.
     * @param left The first integer to add.
     * @param right The second integer to add.
     * @return The sum of the two integers.
     */
    fun Add(left: Int, right: Int): Int {
        return left + right
    }

    /**
     * Performs subtraction operation between two parameters.
     * @param left The first integer to be subtracted from.
     * @param right The integer to subtract.
     * @return The difference between the two integers.
     */
    fun Subtract(left: Int, right: Int): Int {
        return left - right
    }

    /**
     * Performs multiplication operation between two parameters.
     * @param left The first integer to multiply.
     * @param right The second integer to multiply.
     * @return The product of the two integers.
     */
    fun Multiply(left: Int, right: Int): Int {
        return left * right
    }

    /**
     * Performs division operation between two parameters
     * @exception IllegalArgumentException : catches exception if second parameter is Zero
     * @param left The integer to be divided.
     * @param right The integer to divide.
     * @return The quotient of the two integers.
     */
    fun Divide(left: Int, right: Int): Int {
        var result: Int = 0
        try {
            result = left / right
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }

        return result
    }
}
