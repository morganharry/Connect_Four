package connectfour

class Players (_name:String, _sign: Char) {
    val name = _name
    val sign = _sign
    var num = 0
}

fun giveName(num: String): Players {
    println("$num player's name:")
    val name = readln()
    val sign:Char
    if (num =="First") sign = 'o'
    else sign = '*'
    val player = Players (name, sign)
    return player
}

fun checkInput(num: Int, type: String): Int {
    val range = 5..9
    if (num in range) return num
    else {
        println("Board $type should be from 5 to 9")
        return 0
    }
}

fun giveNumbers (): Int {
    var numbers = 0
    while (true) {
        println("Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:")
        var input = readln()
        if (input == "") numbers = 1
        else {
            if (input.toIntOrNull() != null) numbers = input.toInt()
            else {
                println("Invalid input")
                continue
            }
        }
        if (numbers>0) break
        else println ("Invalid input")
    }
    return numbers
}

fun makeField(): MutableList<MutableList<Char>> {
    val regex = Regex("[0-9]?[0-9][xX][0-9]?[0-9]")
    var rows = 0
    var columns = 0
    var statusInput = true
    while (statusInput) {
        println(
            "Set the board dimensions (Rows x Columns)\n" +
                    "Press Enter for default (6 x 7)"
        )
        val inputSize = readln().lowercase().replace(" ", "").replace("\t", "")
        when {
            inputSize == "" -> {
                rows = 6
                columns = 7
            }

            regex.matches(inputSize) -> {
                val list = inputSize.split("x")
                rows = checkInput(list.first().toInt(), "rows")
                columns = checkInput(list.last().toInt(), "columns")
            }

            else -> println("Invalid input")
        }
        if (rows != 0 && columns != 0) statusInput = false
    }
    val field = mutableListOf<MutableList<Char>>()
    for (i in 0 until rows) {
        val lineField = mutableListOf<Char>()
        for (n in 0 until columns) {
            lineField.add(' ')
        }
        field.add(lineField)
    }
    return field
}

fun printField(field: MutableList<MutableList<Char>>) {
    for (i in 1..field[0].size) {
        print(" $i")
    }
    println()
    for (i in field.indices) {
        print("║")
        for (n in field[i].indices) {
            print("${field[i][n]}║")
        }
        println()
    }
    print("╚")
    for (i in 1 until field[0].size) {
        print("═╩")
    }
    print("═╝")
    println()
}

fun addSignToField(field: MutableList<MutableList<Char>>,  sign: Char, status: String,): MutableList<MutableList<Char>> {
    val row = status.toInt()-1
    for (i in field.lastIndex downTo 0) {
        if (field[i][row] == ' ') {
            field[i][row] = sign
            break
        }
    }
    return field
}

fun checkMove (field: MutableList<MutableList<Char>>, name: String) :String {
    val range = 1..field.first().size
    var statusmove = "move"
    while (statusmove == "move") {
        println("${name}'s turn:")
        val move = readln()
        when {
            move == "end" ->  statusmove = "end"
            move.toIntOrNull() == null -> println("Incorrect column number")
            move.toInt() !in range -> println("The column number is out of range (1 - ${field.first().size})")
            else -> {
                val list = mutableListOf<Char>()
                val row = move.toInt() - 1
                for (i in field.indices) {
                    list.add(field[i][row])
                }
                if (' ' in list) {
                    statusmove = "${move}"
                }
                else println("Column ${move.toInt()} is full")
            }
        }
    }
    return statusmove
}

fun checkGame (field: MutableList<MutableList<Char>>):String {
    val column= 0 .. (field.first().lastIndex-3)
    val row = field.lastIndex downTo 3
    var status = "start"
    for (i in field.lastIndex downTo 0) {
        for (n in column) {
            if (field[i][n] == ' ') {
                continue
            }
            if (field[i][n] == field[i][n + 1] && field[i][n + 1] == field[i][n + 2] && field[i][n + 2] == field[i][n + 3]) {
                status = "win"
                break
            }
        }
    }
    if (status != "win") {
        for (i in row) {
            for (n in 0 .. (field.first().lastIndex)) {
                if (field[i][n] == ' ') {
                    continue
                }
                if (field[i][n] == field[i-1][n] && field[i-1][n] == field[i-2][n] && field[i-2][n] == field[i-3][n]) {
                    status = "win"
                    break
                }
            }
        }
    }
    if (status != "win") {
        for (i in row) {
            for (n in column) {
                if (field[i][n] == ' ') {
                    continue
                }
                if (field[i][n] == field[i-1][n+1] && field[i-1][n+1] == field[i-2][n+2] && field[i-2][n+2] == field[i-3][n+3]) {
                    status = "win"
                    break
                }
            }
        }
    }
    if (status != "win") {
        for (i in 0 ..field.lastIndex-3 ) {
            for (n in 0 ..field.first().lastIndex-3) {
                if (field[i][n] == ' ') {
                    continue
                }
                if (field[i][n] == field[i+1][n+1] && field[i+1][n+1] == field[i+2][n+2] && field[i+2][n+2] == field[i+3][n+3]) {
                    status = "win"
                    break
                }
            }
        }
    }
    if (status != "win") {
        for (i in field.indices) {
            if (' ' in field[i]) break
            else status = "end"
        }
    }
    return status
}

fun changePlayer (player: Players, player1: Players, player2: Players): Players {
    if (player.name == player1.name) return player2
    else return player1
}

fun cleanField (field: MutableList<MutableList<Char>>) : MutableList<MutableList<Char>> {
    for (i in field.indices) {
        for (n in field.first().indices) {
            field[i][n] = (' ')
        }
    }
    return field
}

fun main() {
    println("Connect Four")
    val player1 = giveName("First")
    val player2 = giveName("Second")
    var field = makeField()
    val numbersOfGames = giveNumbers()
    var textofNum = ""
    if (numbersOfGames == 1) textofNum = "Single game"
    else textofNum = "Total ${numbersOfGames} games"
    println("${player1.name} VS ${player2.name}\n" +
            "${field.size} X ${field[0].size} board\n" +
            textofNum)
    var numOfGameOver = 1
    var movingPLayer = player2
    do  {
        if (numbersOfGames !=1) println ("Game #${numOfGameOver}")
        field = cleanField(field)
        printField(field)
        var statusGame = "start"
        while (true) {
            movingPLayer = changePlayer (movingPLayer, player1, player2)
            statusGame = checkMove (field, movingPLayer.name)
            if (statusGame == "end") {
                break
            }
            field = addSignToField(field, movingPLayer.sign, statusGame)
            printField(field)
            statusGame = checkGame (field)
            when (statusGame) {
                "win" -> {
                    println("Player ${movingPLayer.name} won")
                    if (movingPLayer.name == player1.name) player1.num +=2
                    else player2.num +=2
                    break
                }
                "end" -> {
                    println("It is a draw")
                    player1.num ++
                    player2.num ++
                    break
                }
            }
        }
        numOfGameOver++
        if (numbersOfGames >1) {
            println ("Score\n" +
                    "${player1.name}: ${player1.num} ${player2.name}: ${player2.num}")
        }
    } while (numOfGameOver <= numbersOfGames)
    println ("Game over!")
}