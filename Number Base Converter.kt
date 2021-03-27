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
    return scanner.nextLine().trim()
}

fun convertToBaseTen(): String {
    println("\nCONVERTING TO BASE 10")
    val number = getInput("Enter number")
    val base : Int

    try{
        base = getInput("Enter the base of the number").toInt()
    }catch(e: NumberFormatException){
        println("Thus not a number")
        return ""
    }

    for(digits in number) {
        if(digits == 'E' || digits == 'T') continue
        else if(digits in 'A'..'Z' || digits in 'a'..'z') return "Invalid Input"
        else if(number.contains('.')) return decimalBaseTenConversion(number, base)
        else if (Integer.parseInt(digits.toString()) > base) return "Invalid Question.\nbase number must be greater than digits in number but less than or equal to 12"
    }



    val disunited = splitNumbers(number)
    if(disunited.isEmpty()) return  ""

    return "\n$number in base $base converted to base 10 = ${calculateConversionToBaseTen(disunited, base)}"
}

fun convertBaseTenToOtherBases(): String{
    println("\nCONVERTING BASE 10 TO OTHER BASES")
    val number : Int
    val base : Int

    try{
        number = getInput("Enter the number").toInt()
        base = getInput("Enter the base number you want to convert to").toInt()
    }catch(e: NumberFormatException){
        println("Thus not a number")
        return ""
    }

    return "\n$number in base 10 converted to base $base = ${calculateConversionFromBaseTen(number, base)}"
}

fun convertFromOneBaseToAnother(): String {
    println("\nCONVERTING FROM ONE BASE TO ANOTHER")
    val number = getInput("Enter the number")
    val firstBase : Int
    val secondBase : Int

    try {
        firstBase = getInput("Enter the base of the number").toInt()
    }catch(e: NumberFormatException){
        println("Thus not a number")
        return ""
    }

    for(digits in number) {
        if(digits == 'E' || digits == 'T') continue
        else if(digits in 'A'..'Z' || digits in 'a'..'z') return "Invalid Input"
        else if(number.contains('.')) return ""
        else if (Integer.parseInt(digits.toString()) > firstBase) return "Invalid Question.\nbase number must be greater than digits in number but less than or equal to 12"
    }

    try{
        secondBase = getInput("Enter the base number you want to convert to").toInt()
    }catch(e: NumberFormatException){
        println("Thus not a number")
        return ""
    }

    /*
        This function is basically the combination of convertToBaseTen() and convertBaseTenToOtherBases()

        Illustration 1:
            convertToBaseTen() does it stuff
            the result return from convertToBaseTen() is given to convertBaseTenToOtherBases()
            convertBaseTenToOtherBases() finalizes everything and returns the final output result
     */

    val disunited = splitNumbers(number)
    if(disunited.isEmpty()) return  ""
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
         *
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
    val listOfNumbers = mutableListOf<Int>()
    /*
        This function splits all digits in the variable(numbers) and appends each to the mutableList(listOfNumbers)
        Illustration 1:
              numbers = "12345" ends up as a mutableList of [1, 2, 3, 4, 5]
              Note that in number base :- E represents 11 && T represents 10
        Illustration 2:
              numbers = "12EET" ends up as a mutableList of [1, 2, 11, 11, 10]
     */

    for(value in numbers){
        when (value) {
            'E' -> listOfNumbers.add(11)
            'T' -> listOfNumbers.add(10)
            else -> {
                try {
                    listOfNumbers.add(Integer.parseInt(value.toString()))
                }catch(e: NumberFormatException){
                    println("Thus not a number")
                    listOfNumbers.clear()
                    return listOfNumbers
                }
            }
        }
    }

    return listOfNumbers
}

fun decimalBaseTenConversion(numbers: String, base : Int): String{
    //When converting decimal numbers in number bases, the process is a little different. Thus why i created a special
    // function `decimalBaseTenConversion()` for that very purpose

    //Example: Convert 110.101 in base 2 to base 10 = 6.625

    //Note: I wrote code in the `convertToBaseTen()` function to differentiate decimal digits from normal integers
    //If it's a decimal digit, the `convertToBaseTen()` function calls this function `decimalBaseTenConversion()` and
    // it takes the user number and the base of the number as arguments

    /*
    Let's say the user enters 110.101 in base 2 and is converting to base 10

    The user number is then split in a mutableListOf<String> in the `newList variable`

    Illustration 1:
        110.101 ->  [1, 1, 0, ., 1, 0, 1]
    */
    val newList = mutableListOf<String>()
    for(values in numbers) newList.add(values.toString())

    for(num in newList){
        try {
            Integer.parseInt(num)
        }catch (e : NumberFormatException){
            println("Invalid Question")
            return ""
        }
    }

    var result = 0.0

    /*
      We needed to loop through the mutableListOf<String> in the `newList variable` and perform some basic calculations
      but we have one problem. Thus the dot(`.`). We cannot convert the dot into a integer so i had no choice than to use
      a try and catch block

      Anyways here's how it works

      Illustration 2:
            user number = 110.101       base = 2
            result = [(1 * 2 ^ 2) + (1 * 2 ^ 1) + (0 * 2 ^ 0)] + [(1 * 2 ^ -1) + (0 * 2 ^ -2) + (1 * 2 ^ -3)]

            **Now let's note a few things
      Illustration 3:
            First 101 the numbers before the dot are calculated which evaluate to 6
            Thus:
                (1 * 2 ^ 2) + (1 * 2 ^ 1) + (0 * 2 ^ 0)
                   4        +     2       +      0     = 6

                result = 6

            When the for loop gets to the dot(`.`), it throws an NumberFormatException because the dot cannot be converted into an integer
            But thus a good thing because it makes things easier(believe it or not). :)
            The numbers after the dot are calculated in a slightly different way

            The code in the catch block is run due to the `NumberFormatException` and here's what happens

              (1 * 2 ^ -1) + (0 * 2 ^ -2) + (1 * 2 ^ -3)
                   0.5     + 0 (infinity) +  0.125    = 0.625(infinity)

               You can see that this powers start from -1 and continue in a descending order
               When you run this code you'll get `infinity` as an answer but we don't want that so i replaced infinity with 0

              result += 0.625
              result = 6.625
     */

    try {
        var newIndex = newList.indexOf(".") - 1

        for(value in newList){
            result += (value.toInt() * (base.toDouble().pow((newIndex).toDouble())))
            --newIndex
        }
    }catch (error: NumberFormatException){
        val increase = newList.indexOf(".") + 1
        var size = - 1

        for(increment in increase until newList.size){
            val decimal = (newList[increment].toDouble() * base).pow(size)
            result += if(decimal == Double.POSITIVE_INFINITY) 0.0 else decimal     //This ensures our answer is not `infinity`
            --size
        }
    }

    return "\n$numbers in base $base converted to base 10 = $result"
}

fun calculateConversionToBaseTen(listOfNumbers: MutableList<Int>, base: Int) : Int{
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

    for(values in listOfNumbers) sum += (values * (base.toDouble().pow((--size).toDouble())))

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
