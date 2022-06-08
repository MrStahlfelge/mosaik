package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.text.ErgAmountLabel
import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.StyleableTextLabel

object LabelFormatter {
    fun getFormattedText(element: StyleableTextLabel<*>): String {
        return when (element) {

            is Label -> element.text ?: ""

            is ErgAmountLabel -> {
                val ergAmountString = ErgoAmount(element.text ?: 0).toStringRoundToDecimals(
                    element.maxDecimals,
                    element.isTrimTrailingZero
                )

                return if (element.isWithCurrencySymbol)
                    "$ergAmountString $ergoCurrencyText"
                else
                    ergAmountString
            }

            else -> element.text.toString()
        }
    }
}