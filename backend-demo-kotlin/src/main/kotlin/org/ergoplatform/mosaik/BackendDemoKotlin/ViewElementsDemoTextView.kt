package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

object ViewElementsDemoTextView {
    fun getView() = mosaikView {
        column {

            // example for reusing parts of views - see ViewElementsDemoCommon.kt for source
            addHeader("Text elements")

            label(
                "View source on GitHub",
                style = LabelStyle.BODY1LINK,
                textColor = ForegroundColor.PRIMARY
            ) {
                onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik/blob/develop/backend-demo-kotlin/src/main/kotlin/org/ergoplatform/mosaik/BackendDemoKotlin/ViewElementsDemoTextView.kt"))
            }

            label("Text elements show informational text to the user.")

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("Label", style = LabelStyle.HEADLINE2)

                    label(
                        "The text you are reading here is placed in a label. Labels are " +
                                "styleable. A view examples:"
                    )

                    column(Padding.DEFAULT) {
                        LabelStyle.values().forEach { style ->
                            label("LabelStyle ${style.name}", style = style)
                        }
                        ForegroundColor.values().forEach { color ->
                            label("Color ${color.name}", textColor = color)
                        }
                    }

                    label(
                        "Labels can have a line restriction and are truncated when exceeding. For example, " +
                                "this label has a restriction of 1 line."
                    ) {
                        maxLines = 1
                    }

                    layout(HAlignment.START) {
                        label(
                            "Labels have a text alignment property\n" +
                                    "that controls how the text is\n" +
                                    "aligned within the label element.\n" +
                                    "This element is aligned left, the\n" +
                                    "text inside is aligned right."
                        ) {
                            textAlignment = HAlignment.END
                        }
                    }
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("ErgAmountLabel", style = LabelStyle.HEADLINE2)

                    label(
                        "Having the same styling capabilities as the label, ErgAmountLabel " +
                                "shows a Long value and not a String. This nanoERG amount is " +
                                "automatically formatted to ERG."
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("FiatAmountLabel", style = LabelStyle.HEADLINE2)

                    label(
                        "Same as ErgAmountLabel, but the nanoERG amount is " +
                                "automatically formatted to user's fiat currency, if set. " +
                                "The fallbackToErg attribute controls what is shown when no fiat " +
                                "currency is available"
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("TokenLabel", style = LabelStyle.HEADLINE2)

                    label(
                        "Label showing a token name and balance formatted. The label is " +
                                "clickable which will open detailed token information provided by " +
                                "the Mosaik executor app. Not styleable. "
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("AddressLabel", style = LabelStyle.HEADLINE2)

                    label(
                        "Styleable as the normal label, this label can show an Ergo network " +
                                "address. In contrast to the normal label, it is provi share/copy " +
                                "and address book functionality (in case application supports it)."
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("MarkupLabel", style = LabelStyle.HEADLINE2)

                    label(
                        "Shows (some) formatted text elements."
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("Button", style = LabelStyle.HEADLINE2)

                    label(
                        "A button is a text element inviting the user to interact. " +
                                "Any other element is clickable in Mosaik as well when an onClickAction " +
                                "is set. " +
                                "Button is styleable, supports multiple lines and line restrictions and " +
                                "can be shown disabled."
                    )

                    column(Padding.DEFAULT) {

                        val showDialog = showDialog("Clicked")

                        Button.ButtonStyle.values().forEach { style ->
                            button("${style.name} Button", style) {
                                onClickAction(showDialog)
                            }
                        }

                        button("Disabled button") {
                            isEnabled = false
                        }
                    }

                }
            }

        }
    }
}