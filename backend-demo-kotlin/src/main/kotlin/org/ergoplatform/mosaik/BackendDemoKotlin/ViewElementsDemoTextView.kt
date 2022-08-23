package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

object ViewElementsDemoTextView {
    fun getView(mosaikContext: MosaikContext) = mosaikView {
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

                    ergAmount(1000L * 1000 * 1000)
                    ergAmount(1000L * 1000 * 1000, trimTrailingZero = true, withCurrency = false)
                    ergAmount(1000L * 1000 * 1000 + 1000000, maxDecimals = 6, withCurrency = false)
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

                    fiatAmount(1000L * 1000L * 1000L)
                    fiatAmount(1000L * 1000L * 1000L, fallbackToErg = true)
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("TokenLabel", style = LabelStyle.HEADLINE2)

                    label(
                        "Label showing a token name and optional balance formatted. The label is " +
                                "clickable which will open detailed token information provided by " +
                                "the Mosaik executor app. " +
                                "It can be decorated or undecorated. The decoration is up to the Mosaik executor, " +
                                "most will show a verification sign and/or token logos."
                    )

                    box(Padding.DEFAULT)

                    tokenLabel(
                        "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
                        "SigUSD",
                        2000,
                        2
                    )
                    tokenLabel(
                        "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
                        null,
                        decorated = false
                    )
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("AddressLabel", style = LabelStyle.HEADLINE2)

                    label(
                        "Styleable as the normal label, this label can show an Ergo network " +
                                "address. In contrast to the normal label, it is providing a share/copy " +
                                "and address book functionality (in case application supports it)."
                    )

                    box(Padding.DEFAULT)

                    ergAddress("9ewA9T53dy5qvAkcR5jVCtbaDW2XgWzbLPs5H4uCJJavmA4fzDx") {
                        maxLines = 3 // applies only to the expanded view
                    }
                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("MarkDown label", style = LabelStyle.HEADLINE2)

                    label(
                        "Shows (some) formatted text elements."
                    )

                    box(Padding.DEFAULT)

                    if (mosaikContext.mosaikVersion >= 1) {
                        markDown(
                            """
                        ## Test
                                                
                        * List 1
                        * List 2
                        
                        And a [link to the repo](https://github.com/MrStahlfelge/mosaik), **bold** text, 
                        _italic_ text, and a link to www.ergoplatform.com
                        
                        Some special chars: < > " ' & and `code`
                        
                        1. List 1
                        2. List 2
                        """.trimIndent(), alignment = HAlignment.CENTER
                        )
                    } else
                        needHigherMosaikVersionLabel(1)
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