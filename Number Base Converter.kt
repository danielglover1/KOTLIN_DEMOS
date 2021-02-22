// Just to inform you, this program doesn't handle errors(ALL INVALID INPUTS) yet

import java.util.Scanner
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

fun introScreen(): String{
    print("""
        CHOOSE AN OPTION BELOW
        1.Converting to base 10
        2.Converting from base 10 to other bases
        3.Converting from one base to another base
        
    """.trimIndent())

    return getInput("Pick an option")
}

fun getInput(command: String): String{
    print("$command: ")
    val scanner = Scanner(System.`in`)
    return scanner.nextLine()
}

fun convertToBaseTen(): String {
    println("\nCONVERTING TO BASE 10")
    val number = getInput("Enter number")
    val base = getInput("Enter the base of the number").toInt()
    val disunited = splitNumbers(number)

    return "\n$number in base $base converted to base 10 = ${calculateConversionToBaseTen(disunited, base)}"
}

fun convertBaseTenToOtherBases(): String{
    println("\nCONVERTING BASE 10 TO OTHER BASES")
    val number = getInput("Enter the number").toInt()
    val base = getInput("Enter the base number you want to convert to").toInt()

    return "\n$number in base 10 converted to base $base = ${calculateConversionFromBaseTen(number, base)}"
}

fun convertFromOneBaseToAnother(): String {
    println("\nCONVERTING FROM ONE BASE TO ANOTHER")
    val number = getInput("Enter the number")
    val firstBase = getInput("Enter the base of the number").toInt()
    val secondBase = getInput("Enter the base number you want to convert to").toInt()

    /*
        This function is basically the combination of convertToBaseTen() and convertBaseTenToOtherBases()

        Illustration 1:
            convertToBaseTen() does it stuff
            the result return from convertToBaseTen() is given to convertBaseTenToOtherBases()
            convertBaseTenToOtherBases() finalizes everything and returns the final output result
     */
    val disunited = splitNumbers(number)
    val firstResult = calculateConversionToBaseTen(disunited, firstBase)
    val secondResult = calculateConversionFromBaseTen(firstResult, secondBase)

    return """
        |
        |$number in base $firstBase converted to base 10 = $firstResult
        |$firstResult in base 10 converted to base $secondBase = $secondResult
        |Therefore the final answer = $secondResult
    """.trimMargin()
}

fun calculateConversionFromBaseTen(number: Int, base: Int): String{
    /*
        This function uses a division and remainder procedure to convert numbers from base 10 to other bases
        Let's take number = 986 && base = 8     :- means that we want to convert 986 from base 10 to base 8

        *****Note :- The function uses Integer division******

        Formula :-
            division = number / 8
            remainder = number - (division * base)      **remainder is always a positive integer**

        The result of the remainder is appended to a String variable

        Illustration 1:
        base | number | remainder
          8  | 986    | nothing_here      8 = base   ||   number is currently = 986
          8  | 123    | 2                 :-  986 / 8 = 123  (Integer Division)   ||  remainder = 986 - (123 * 8)
          8  | 15     | 3                 :-  123 / 8 = 15   (Integer Division)   ||  remainder = 123 - (15 * 8)
          8  | 1      | 7                 :-  15  / 8 = 1    (Integer Division)   ||  remainder = 15  - (1 * 8)
             | 0      | 1                 :-   1  / 8 = 0    (Integer Division)   ||  remainder =  1  - (0 * 8)

        In short the answer ends up being all digits under the remainder column in a `bottom to top` order

        Illustration 2:
            In the calculation above the remainder = 2371
            The function will return the reminder in a reversed order

            :- So 1732 is the final answer
     */
    var result = ""
    var initialCurrent = number
    var current = number
    while(current != 0) {
        current /= base

        /*
         * We don't want negative numbers in our result variable so this ensures that
         * the function always subtracts the largest digit from the smallest digit
         *
         * Illustration 1:
         *   We have         :-  986 - (123 * 8)
         *   Instead of      :-  984 - 986 = -2
         *   What happens is :-  984 - 986 = 2      Therefore we always get a positive integer
         */
        when (val calculation = max((current * base), initialCurrent) - min((current * base), initialCurrent)) {
            10 -> result += "T"
            11 -> result += "E"
            else -> result += calculation
        }
        initialCurrent = current
    }

    if(isFound(result)) println("\nIn number bases :- 10 is represented by a `T` and 11 is represented by an `E`")

    return result.reversed()
}

fun isFound(numbers: String) = 'T' in numbers || 'E' in numbers

fun splitNumbers(numbers: String): MutableList<Int>{
    var increment = 0
    val listOfNumbers = mutableListOf<Int>()
    /*
        This function splits all digits in the variable(numbers) and appends each to the mutableList(listOfNumbers)
        Illustration 1:
              numbers = "12345" ends up as a mutableList of [1, 2, 3, 4, 5]
              Note that in number base :- E represents 11 && T represents 10
        Illustration 2:
              numbers = "12EET" ends up as a mutableList of [1, 2, 11, 11, 10]
     */
    while(increment < numbers.length){
        when {
            numbers[increment] == 'E' -> listOfNumbers.add(11)
            numbers[increment] == 'T' -> listOfNumbers.add(10)
            else -> listOfNumbers.add(Integer.parseInt(numbers[increment].toString()))
        }
        ++increment
    }
    return listOfNumbers
}

fun calculateConversionToBaseTen(listOfNumbers: MutableList<Int>, base: Int) : Int{
    var increment = 0
    var sum = 0.0
    var size = listOfNumbers.size
    /*
        This is where the real calculation for converting a number to base 10 happens
        Illustration 1:
            listOfNumbers = [1,2,11,10,4] && base = 12
            Formula :- (number * base ^ `the size of listOfNumbers - 1`)
            Basically the size variable(`listOfNumbers.size`) keep decrementing until it reaches 0
        Illustration 2:
            result = (1 * 12 ^ 4) +  (2 * 12 ^ 3) +  (11 * 12 ^ 2) +  (10 * 12 ^ 1) +  (4 * 12 ^ 0)
            result =     20736    +      3456     +      1584      +        120     +       4
            result = 25900
     */
    while(increment < listOfNumbers.size){
       sum += (listOfNumbers[increment] * (base.toDouble().pow((--size).toDouble())))
        ++increment
    }
    return sum.toInt()
}

fun main() {
    when (introScreen()){
         "1" -> println(convertToBaseTen())
         "2" -> println(convertBaseTenToOtherBases())
         "3" -> println(convertFromOneBaseToAnother())
        else -> print("Invalid Option")
    }
}