import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.pow

fun main(args: Array<String>) {
    val n = 9
    val x = Array(n) { i -> i * 0.1 }
    val y = Array(n) { i -> x[i].pow(4) + 3 * x[i].pow(3) - 2 * x[i].pow(2) + x[i] + 4.2 }
    printData(x, y)

    val dy = arrayOfNulls<DoubleArray>(n)
    for (i in 0 until n) {
        dy[i] = DoubleArray(n - i)
    }

    for (i in 0 until n) {
        dy[i]!![0] = y[i]
    }

    printMatrix(dy)

    print("eps = ")
    val eps = readLine()?.toDouble() ?: 0.0001

    while (true) {
        print("x0 = ")
        val x0 = readLine()?.toDouble() ?: 0.0

        val result = if (x0 >= x[0] && x0 <= x[n / 2 - 1] || abs(x0 - x[0]) <= eps) {
            firstInterPolNewton(dy, x, x0)
        } else if (x0 >= x[n / 2 + 1] && x0 <= x[n - 1] || abs(x0 - x[n - 1]) <= eps) {
            secondInterPolNewton(dy, x, x0)
        } else if (x0 > x[n / 2 - 1] && x0 <= x[n / 2]) {
            secondInterPolGauss(dy, x, x0);
        } else if (x0 > x[n / 2] && x0 < x[n / 2 + 1]) {
            firstInterPolGauss(dy, x, x0);
        } else break
        println(result)

    }
}

private fun printMatrix(x: Array<DoubleArray?>) {
    x.forEach { print(it?.map { elem -> DecimalFormat("#.00000").format(elem) }?.joinToString(" ") + "\n") }
}

private fun firstInterPolNewton(dy: Array<DoubleArray?>, x: Array<Double>, varX: Double): Double {
    fun combination(i: Int, t: Double): Double {
        var m = 1
        var A = 1.0
        for (j in 1 until i + 1) {
            m *= j
        }
        for (j in 0 until i) {
            A *= t - j
        }
        return A / m
    }

    var P = 0.0
    val h = x[1] - x[0]
    val t = (varX - x[x.size - 1]) / h

    for (i in x.indices) {
        P += combination(i, t) * dy[x.size - 1 - i]!![i]
    }
    return P
}

private fun secondInterPolNewton(dy: Array<DoubleArray?>, x: Array<Double>, varX: Double): Double {
    fun combination(i: Int, t: Double): Double {
        var m = 1
        var A = 1.0
        for (j in 1 until i + 1) { m *= j }
        for (j in 0 until i) { A *= t + j }
        return A / m
    }

    var P = 0.0
    val h: Double = x[1] - x[0]
    val t: Double = (varX - x[x.size - 1]) / h

    for (i in x.indices) {
        P += combination(i, t) * dy[x.size - 1 - i]!![i]
    }
    return P
}

private fun firstInterPolGauss(dy: Array<DoubleArray?>, x: Array<Double>, varX: Double): Double {
    fun combination(i: Int, t: Double): Double {
        var m = 1
        var A = 1.0
        for (j in 1 until i + 1) {
            m *= j
        }
        var l = 0
        for (j in 0 until i) {
            if (j % 2 == 1) {
                l++
            }
            A *= t + (-1.0).pow(j) * l
        }
        return A / m
    }

    var P = 0.0
    val h: Double = x[1] - x[0]
    val t: Double = (varX - x[(x.size - 1) / 2]) / h

    for (i in x.indices) {
        P += dy[(x.size - i) / 2]!![i] * combination(i, t)
    }
    return P
}

private fun secondInterPolGauss(dy: Array<DoubleArray?>, x: Array<Double>, varX: Double): Double {
    fun combination(i: Int, t: Double): Double {
        var m = 1
        var A = 1.0
        for (j in 1 until i + 1) {
            m *= j
        }
        var l = 0
        for (j in 0 until i) {
            if (j % 2 == 1) {
                l++
            }
            A *= t + (-1.0).pow(j + 1) * l
        }
        return A / m
    }

    var P = 0.0
    val h: Double = x[1] - x[0]
    val t: Double = (varX - x[(x.size - 1) / 2]) / h

    for (i in x.indices) {
        P += dy[(x.size - i - 1) / 2]!![i] * combination(i, t)
    }
    return P
}