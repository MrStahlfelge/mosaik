package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.IconType
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

object ViewElementsDemoInputView {
    fun getView() = mosaikView {
        column {

            // example for reusing parts of views - see ViewElementsDemoCommon.kt for source
            addHeader("Input elements")

            label(
                "View source on GitHub",
                style = LabelStyle.BODY1LINK,
                textColor = ForegroundColor.PRIMARY
            ) {
                onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik/blob/develop/backend-demo-kotlin/src/main/kotlin/org/ergoplatform/mosaik/BackendDemoKotlin/ViewElementsDemoInputView.kt"))
            }

            label(
                "Input elements hold a value that can be altered by the user. " +
                        "If you are using the debug environment on desktop, " +
                        "you will see your changes reflected on the right panel.\n" +
                        "Entered values are transferred to your backend on backend requests."
            )

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("TextInputField", style = LabelStyle.HEADLINE2)

                    label(
                        "An input field the user can type any text in. Min/max controls how many " +
                                "characters are needed for the input to be considered valid."
                    )

                    textInputField("textinput", "Enter max 10 chars here") {
                        endIcon = IconType.INFO
                        maxValue = 10
                    }
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("IntegerInputField", style = LabelStyle.HEADLINE2)

                    label(
                        "An input field the user can type integer numbers text in. " +
                                "Min/max controls what numbers are considered valid."
                    )

                    integerInputField("integerinput", "Numbers from 3 to 300 allowed") {
                        maxValue = 300
                        minValue = 3
                    }
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("Other InputField", style = LabelStyle.HEADLINE2)

                    label(
                        "Input fields for EMail, Password, Decimal, ErgAmount, FiatOrErgAmount"
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("WalletChooseButton", style = LabelStyle.HEADLINE2)

                    label(
                        "A button type element (styleable the same way) that let the user " +
                                "choose one of his wallets. " +
                                "The element's value might contain a list of wallet addresses or " +
                                "a single address."
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("AddressChooseButton", style = LabelStyle.HEADLINE2)

                    label(
                        "A button type element (styleable the same way) that let the user " +
                                "choose a single wallet address."
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")
                }
            }

        }
    }
}