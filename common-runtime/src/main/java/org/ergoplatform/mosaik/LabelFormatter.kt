package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.input.ErgAddressInputField
import org.ergoplatform.mosaik.model.ui.text.*

object LabelFormatter {

    /**
     * returns formatted text to show to the user. If null is returned, no element should be
     * shown at all
     */
    fun getFormattedText(element: StyleableTextLabel<*>, treeElement: TreeElement): String? {
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

            is FiatAmountLabel -> {
                val fiatString =
                    treeElement.viewTree.mosaikRuntime.convertErgToFiat(element.text ?: 0)

                return if (fiatString == null && element.isFallbackToErg)
                    ErgoAmount(element.text ?: 0).toStringRoundToDecimals(
                        4,
                        false
                    ) + " $ergoCurrencyText"
                else
                    fiatString
            }

            else -> element.text.toString()
        }
    }

    fun hasAlternativeText(element: ViewElement, treeElement: TreeElement): Boolean =
        element is ErgoAddressLabel || element is ErgAddressInputField

    /**
     * returns an alternative text to be shown instead of the formatted text, if available.
     * This is suspending and can be used to load from database or network
     */
    suspend fun getAlternativeText(
        element: ViewElement,
        treeElement: TreeElement
    ): String? {

        return when (element) {

            is ErgoAddressLabel ->
                getFormattedText(element, treeElement)?.let {
                    treeElement.viewTree.mosaikRuntime.getErgoAddressLabel(it)
                }

            is ErgAddressInputField ->
                treeElement.viewTree.mosaikRuntime.getErgoAddressLabel(treeElement.currentValueAsString)

            else -> null
        }
    }
}