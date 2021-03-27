import java.util.Scanner
import java.util.InputMismatchException


var scanner = Scanner(System.`in`)

data class Book(var title : String, val author : String, val edition : String, val price : Double, val quantity : Int){
    constructor() : this("","","",0.0,0)

    private val books: MutableList<Book> = ArrayList()

    fun printBooks() {
        var index = 1
        if (books.isEmpty()) {
            println("You must add a book first")
        } else {
            for (book in books) {
                println(
                    """
                    
                    BOOK$index
                    Title: ${book.title}
                    Author: ${book.author}
                    Edition: ${book.edition}
                    Price: ${book.price}
                    Quantity: ${book.quantity}
                    
                    """.trimIndent()
                )
                ++index
            }
        }
    }

    fun updateBook() {
        if (books.isEmpty()) {
            println("You must add a book first")
        } else {
            print("Enter book title: ")
            val bookTitle = scanner.nextLine().trim { it <= ' ' }
            var isFound = false
            for ((title1) in books) {
                if (title1 == bookTitle) {
                    isFound = true
                    break
                }
            }

            if (!isFound) {
                println("Book title not found!")
            } else {
                val title = mutableListOf<String>()
                for((booksTitle, _, _, _ ,_) in books) title.add(booksTitle)

                print("Enter new book title: ")
                val newBookTitle = scanner.nextLine().trim { it <= ' ' }

                if(newBookTitle in title) {
                    println("Book title already exits!")
                    return
                }else{
                    var index = 0
                    while (index < books.size) {
                        if (books[index].title == bookTitle) {
                            books[index].title = newBookTitle
                            println("Book title has been updated.")
                            return
                        }
                        ++index
                    }
                }
            }
        }
    }

    fun removeBook() {
        if (books.isEmpty()) {
            println("You must add a book first")
        } else {
            print("Enter book title: ")
            val bookTitle =scanner.nextLine().trim { it <= ' ' }
            var isFound = false
            for ((title1) in books) {
                if (title1 == bookTitle) {
                    isFound = true
                    break
                }
            }

            if (!isFound) {
                println("Book title not found!")
            } else {
                for (book in books) {
                    if(book.title == bookTitle){
                        books.remove(book)
                        println("Book has been removed.")
                        break
                    } else continue
                }
            }
        }
    }

    fun sellBooks(): Double {
        var totalAmount = 0.0
        if (books.isEmpty()) {
            println("You must add a book first")
        } else {
            print("Enter book title: ")
            val bookTitle = scanner.nextLine().trim { it <= ' ' }
            var isFound = false
            for ((title1) in books) {
                if (title1 == bookTitle) {
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                println("Book title not found!")
            } else {
                for (book in books) {
                    if (book.title == bookTitle) {
                        println("Book has been sold with additional rate of 10%.")
                        totalAmount = book.price / book.quantity * 10.0
                    }
                }
            }
        }
        return totalAmount
    }

    fun printRevenue() {
        if (books.isEmpty()) {
            println("You must add a book first")
        } else {
            val sell = sellBooks()
            if (sell == 0.0) {
                println("You have no revenue.")
            } else {
                println("Revenue : $$sell")
            }
        }
    }

    fun addNewBook() {
        print("Enter book title: ")
        val bookTitle = scanner.nextLine().trim { it <= ' ' }
        for ((title1) in books) {
            if (title1 == bookTitle) {
                println("Book Title Already Exits!")
                return
            }
        }

        print("Enter book author: ")
        val bookAuthor = scanner.nextLine().trim { it <= ' ' }

        print("Enter book edition: ")
        val bookEdition = scanner.nextLine().trim { it <= ' ' }

        print("Enter book price: ")
        val bookPrice: Double = try {
            scanner.nextDouble()
        } catch (e: InputMismatchException) {
            println("That is not a number")
            return
        }

        print("Enter book quantity: ")
        val bookQuantity: Int = try {
            scanner.nextInt()
        } catch (e: InputMismatchException) {
            println("That is not a number")
            return
        }

        books.add(Book(bookTitle, bookAuthor, bookEdition, bookPrice, bookQuantity))
        println("Book has been added.")
    }
}

fun main() {
    println("\nBOOK APPLICATION\n1.Adding New Book\n2.Update A Book\n3.Remove A Book\n4.Print All Books\n5.Print Revenue\n6.Sell Book\n7.Exit")
    val book = Book()
    var userOption: String

    while(true){
        print("\nEnter an option(1-6): ")
        userOption = scanner.nextLine()

        when(userOption){
            "1" -> {
                book.addNewBook()
                scanner = Scanner(System.`in`)
            }
            "2" -> book.updateBook()
            "3" -> book.removeBook()
            "4" -> book.printBooks()
            "5" -> book.printRevenue()
            "6" -> book.sellBooks()
            "7" -> {
                println("Thanks for using out application.")
                return
            }
        }
    }
}
