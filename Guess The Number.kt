import kotlin.random.Random     //I use this class for generating random numbers

//This is the first message you seen on the console
fun introScreen() = """
    GUESS THE NUMBER APPLICATION
    
    I am thinking of a number between 1 - 100 inclusive.
    I am giving you 5 chances to guess that number.
    
""".trimIndent()    //Trims all horizontal whitespaces

//I use this function to ask the user for an input
fun getInput(str: String): String{
    print("$str: ")
    return readLine().toString()
}

//This function is responsible for printing the win messages(thus if the user guesses the secret number)
fun winMessages(randomNumber: Int): String {
    return when (randomNumber) {
        1 -> "\n>> Wow..congratulation you nailed it!!"
        2 -> "\n>> Good job, you guessed it!!"
        3 -> "\n>> Ohh wow..did you read my mind??"
        else -> ""      //Can't return a when statement without an `Else` clause
    }
}

//This function is responsible for printing the lose messages(thus if the user fails to guess the secret number)
fun loseMessages(randomNumber: Int): String {
    return when  (randomNumber)  {
         1 -> "\n>> Sorry...you are out of guesses"
         2 -> "\n>> You fucked up!!"
         3 -> "\n>> You couldn't guess my number :("
        else -> ""      //Can't return a when statement without an `Else` clause
    }
}

//This function is responsible for giving the user hint messages to aid them in guessing the right number
fun highOrLow(userInput: Int, secretNumber: Int): String{
    return when{
        userInput > 100 -> "I recall telling you that am thinking of a number between 1 - 100 inclusive\nDon't you think $userInput is above 100?"
        userInput < 1 -> "I recall telling you that am thinking of a number between 1 - 100 inclusive\nDon't you think $userInput is below 1?"
        userInput in secretNumber - 3..secretNumber + 3 -> "You are really really close to guessing my number"
        userInput in secretNumber - 5..secretNumber + 5 -> "You are getting close to guessing my number"
        userInput > secretNumber -> "Oops..you guessed too high"
        userInput < secretNumber -> "Oops..you guessed too low"
        else -> ""      //Can't return a when statement without an `Else` clause
    }
}

//This function tells the user the number of times it took them to guess the secret number(thus if the user wins)
fun numOfGuesses(numOfGuessesMade: Int) = "It took you ${if(numOfGuessesMade == 1) "just 1 chance" else "$numOfGuessesMade chances"} to guess my number"

fun main() {

    do{
        val randomNumber = Random.nextInt(1,101)    //generates random numbers from 1 - 100
        var userGuess = 0
        val guessLimit = 5
        val alreadyGuessed = mutableSetOf<Int>()              // Stores all the numbers guessed by the user
        var numberOfGuessesMade = 0

        println(introScreen())

        //The real game starts from this loop
        //In order to prevent the secret number(randomNumber) from changing everything the user takes a guess i needed to place some part of the code in a separate nested loop
        while(userGuess != randomNumber) {
            try {
                userGuess = getInput("Take a guess").toInt()
            } catch (error: NumberFormatException) {        //Checks to make sure the user enters only integers
                println("Thus not a number...Try again")
                continue
            }

            numberOfGuessesMade++

            if(userGuess in alreadyGuessed) {
                //Checks if the user has already guessed a number
                println("You already guessed this number..Try again!!!")
            } else if(numberOfGuessesMade <= guessLimit && userGuess == randomNumber) {
                //Else If the number of guesses made is less than or equal to the guess limit and the user guess equals the secret number then the user has won
                println(winMessages(Random.nextInt(1, 4)))
                println(numOfGuesses(numberOfGuessesMade))
            } else if(numberOfGuessesMade == guessLimit && userGuess != randomNumber) {
                //Else If the number of guesses equals to the guess limit and the user guess does not equals the secret number then the user has lost
                println(loseMessages(Random.nextInt(1, 4)))
                println("The number i was thinking about was: $randomNumber")
                break
            }else {
                //Else print the hint messages to help the user guess the right number
                println(highOrLow(userGuess,randomNumber))
            }

            alreadyGuessed.add(userGuess)
        }

        println()       //prints a new line for readability purpose

        //Asks the user if the want to play again( if the user enters 'y' it loads the game again anything else will cause the program to end execution
        if((numberOfGuessesMade == guessLimit && userGuess != randomNumber) || userGuess == randomNumber) {
            if(getInput("Would you like to play again?(y/n)").toLowerCase() == "y") continue else break
        }
    }while (true)
}