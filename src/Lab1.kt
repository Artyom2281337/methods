import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.pow

fun main(args: Array<String>) {
    val n = 11
    val x = Array(n) { i -> 1.0 + i * 0.1 }
    val y = Array(n) { i -> Math.E.pow(x[i]) }
    printData(x, y)

    print("eps = ")
    val eps = readLine()?.toDouble()

    print("method (1 - InterLagrangePolygon, 2 - AitkenScheme) = ")
    val method = readLine()?.toInt()

    while (true) {
        print("x0 = ")
        val x0 = readLine()
        if (x0 == "exit") {
            return
        }

        val result = if (method == 1) {
            lagrangePolygon(x0?.toDouble()!!, x, y)
        } else {
            aitkenScheme(Array(n) { i -> y[i]}, x, x0?.toDouble()!!, eps!!)
        }
        println(result)
    }
}

private fun lagrangePolygon(x0: Double, x: Array<Double>, y: Array<Double>): Double {
    var L = 0.0
    var P = 1.0

    for (i in x.indices) {
        for (j in x.indices) {
            P *= if (j != i) (x0 - x[j]) / (x[i] - x[j]) else continue
        }
        L += y[i] * P
        P = 1.0
    }
    return L
}

private fun aitkenScheme(L: Array<Double>, x: Array<Double>, tempX: Double, eps: Double): Double {
    var j = 0
    var Lm: Double
    val n = x.size
    do {
        j++
        Lm = L[0]

        for (i in 0 until n - j) {
            L[i] = (L[i + 1] * (tempX - x[i]) - L[i] * (tempX - x[i + j])) / (x[i + j] - x[i])
        }
    } while (abs(Lm - L[0]) > eps && n - j != 0)

    return L[0]
}


fun printData(x: Array<Double>, y: Array<Double>) {
    println("x : %s".format(x.map { DecimalFormat("#.00000").format(it) }.joinToString(" ")))
    println("y : %s".format(y.map { DecimalFormat("#.00000").format(it) }.joinToString(" ")))
}