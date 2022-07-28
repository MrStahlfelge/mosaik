package org.ergoplatform.mosaik

import java.math.BigDecimal
import java.math.RoundingMode

const val nanoPowerOfTen = 9
const val ergoCurrencyText = "ERG"

class ErgoAmount(val nanoErgs: Long) {

    companion object {
        val ZERO = ErgoAmount(0)
    }

    constructor(bigDecimal: BigDecimal) :
            this(
                bigDecimal.setScale(nanoPowerOfTen, RoundingMode.HALF_UP)
                    .movePointRight(nanoPowerOfTen).longValueExact()
            )

    constructor(ergString: String) : this(
        if (ergString.isBlank()) 0 else ergString.toBigDecimal()
            .movePointRight(nanoPowerOfTen).longValueExact()
    )

    override fun toString(): String {
        return toBigDecimal().toPlainString()
    }

    /**
     * converts the amount to a string rounded to given number of decimals places
     */
    fun toStringRoundToDecimals(numDecimals: Int, trimTrailingZeros: Boolean): String {
        val numAsString = toBigDecimal()
            .setScale(numDecimals, RoundingMode.HALF_UP)
            .toPlainString()

        return if (trimTrailingZeros)
            numAsString.trimEnd('0').trimEnd('.')
        else numAsString
    }

    fun toBigDecimal() = nanoErgs.toBigDecimal().movePointLeft(nanoPowerOfTen)

    /**
     * @return double amount, only for representation purposes because double has floating point issues
     */
    fun toDouble(): Double {
        val microErgs = nanoErgs / (1000L * 100L)
        val ergs = microErgs.toDouble() / 10000.0
        return ergs
    }
}

fun String.toErgoAmount(): ErgoAmount? {
    try {
        return ErgoAmount(this)
    } catch (t: Throwable) {
        return null
    }
}