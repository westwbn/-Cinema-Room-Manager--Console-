package cinema

const val SOLD = "B"
const val ON_SALE = "S"
const val PRICE = 10
const val DISCOUNT = 8

fun main() {
    println("Enter the number of rows:")
    val rows = readlnOrNull()?.toIntOrNull() ?: ON_SALE
    println("Enter the number of seats in each row:")
    val seats = readlnOrNull()?.toIntOrNull() ?: ON_SALE
    val cinema = MutableList(rows as Int) { MutableList(seats as Int) { ON_SALE } }
    println()
    menu(rows, seats as Int, cinema)
}

fun drawGrid(cinema: List<List<String>>) {
    println("\nCinema:")
    for (line in 0..cinema.size) {
        if (line != 0) {
            print("$line")
        }
        for (colum in 0..cinema[0].size) {
            if (colum == 0) {
                print(" ")
            } else if (line == 0) {
                print(" $colum")
            } else {
                print("${cinema[line - 1][colum - 1]} ")
            }
        }
        println()
    }
    println()
}

fun menu(row: Int, seat: Int, cinema: MutableList<MutableList<String>>) {
    var purchasedTickets = 0
    var currentIncome = 0
    var rowNumber: Int
    var seatNumber: Int
    val totalIncome: Int = if (row * seat < 60 || row <= 2) {
        row * seat * PRICE
    } else if (row * seat > 60) {
        (row * seat / 2) * PRICE + (row * seat / 2) * DISCOUNT
    } else {
        DISCOUNT
    }

    fun chosenSeat(row: Int, seat: Int, cinema: MutableList<MutableList<String>>): Boolean {
        do {
            println()
            println("Enter a row number:")
            rowNumber = readln().toIntOrNull()!!
            println("Enter a seat number in that row:")
            seatNumber = readln().toIntOrNull()!!

            try {
                if (cinema[rowNumber - 1][seatNumber - 1] == SOLD) {
                    println("That ticket has already been purchased!")
                    continue
                }
                currentIncome += if (row * seat < 60 || rowNumber <= row / 2) {
                    println("\nTicket price: $$PRICE")
                    PRICE
                } else {
                    println("\nTicket price: $$DISCOUNT")
                    DISCOUNT
                }
                cinema[rowNumber - 1][seatNumber - 1] = SOLD
                println()
                return true
            } catch (e: IndexOutOfBoundsException) {
                if (rowNumber !in 1..row || seatNumber !in 1..seat) {
                    println("Wrong input!")
                }
            }

        } while (true)
    }

    fun statistics(purchasedTickets: Int, totalIncome: Int, currentIncome: Int, row: Int, seat: Int): String {
        val totalSeats = row * seat
        val percentage = (purchasedTickets.toDouble() / totalSeats.toDouble()) * 100
        val formatPercentage = "%.2f".format(percentage).replace(',', '.')
        val data = mutableListOf(
            " ",
            "Number of purchased tickets: $purchasedTickets",
            "Percentage: $formatPercentage%",
            "Current income: $$currentIncome",
            "Total income: $$totalIncome",
            " "
        )

        return data.joinToString("\n")
    }

    while (true) {
        println("1. Show the seats")
        println("2. Buy a ticket")
        println("3. Statistics")
        println("0. Exit")
        when (readln().toInt()) {
            1 -> drawGrid(cinema)
            2 -> {
                if (chosenSeat(row, seat, cinema)) {
                    purchasedTickets++
                }
            }

            3 -> println(statistics(purchasedTickets, totalIncome, currentIncome, row, seat))
            0 -> break
        }
    }
}