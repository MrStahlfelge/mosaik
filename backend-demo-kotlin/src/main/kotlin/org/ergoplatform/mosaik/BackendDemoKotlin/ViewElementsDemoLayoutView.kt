package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.Icon
import org.ergoplatform.mosaik.model.ui.IconType
import org.ergoplatform.mosaik.model.ui.Image
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.layout.VAlignment
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

object ViewElementsDemoLayoutView {
    fun getView() =
        mosaikView {

            column {

                layout(HAlignment.JUSTIFY) {
                    box {
                        layout(HAlignment.START, VAlignment.CENTER) {
                            button("Back") {
                                onClickAction(reloadApp())
                            }
                        }
                        label(
                            "Layout elements",
                            style = LabelStyle.HEADLINE1
                        )
                    }
                }

                label(
                    "View source on GitHub",
                    style = LabelStyle.BODY1LINK,
                    textColor = ForegroundColor.PRIMARY
                ) {
                    onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik/blob/develop/backend-demo-kotlin/src/main/kotlin/org/ergoplatform/mosaik/BackendDemoKotlin/ViewElementsDemoLayoutView.kt"))
                }

                label(
                    "Layout elements can have a padding and serve as a container for other elements, " +
                            "or can be nested."
                )

                card(Padding.DEFAULT) {

                    column(Padding.DEFAULT) {

                        label("Box", style = LabelStyle.HEADLINE2)

                        label(
                            "The box is typically used to stack multiple elements on top of each" +
                                    "other, or to define a padding around a single element. For example, the " +
                                    "small space between the two headlines of this page is an empty box with " +
                                    "padding.\n" +
                                    "The following box shows two elements stacked with different alignment."
                        )

                        box {
                            box(Padding.DEFAULT) {
                                image("https://picsum.photos/400", size = Image.Size.LARGE)
                            }
                            layout(HAlignment.END, VAlignment.BOTTOM) {
                                icon(
                                    IconType.INFO,
                                    Icon.Size.MEDIUM,
                                    tintColor = ForegroundColor.PRIMARY
                                )
                            }
                        }

                    }

                }

                card(Padding.DEFAULT) {

                    column(Padding.DEFAULT) {

                        label("Card", style = LabelStyle.HEADLINE2)

                        label(
                            "The card can organize your views by grouping elements" +
                                    "graphically. You'll see cards in this demo used to group" +
                                    "information for each element demonstrated."
                        )

                        box(Padding.HALF_DEFAULT)

                        card {
                            image("https://picsum.photos/400", size = Image.Size.LARGE)
                        }

                    }

                }

                card(Padding.DEFAULT) {

                    column(Padding.DEFAULT) {

                        label("Row", style = LabelStyle.HEADLINE2)

                        label(
                            "Row aligns multiple elements in a row. Elements can have different " +
                                    "vertical alignments and weights. " +
                                    "Elements with same weight take the same horizontal space, demonstrated" +
                                    "by the two information icons below."
                        )

                        box(Padding.HALF_DEFAULT)

                        row(packed = true) {
                            layout(VAlignment.BOTTOM) {
                                card { icon(IconType.WARN) }
                            }
                            layout(VAlignment.TOP, weight = 1) {
                                card { icon(IconType.INFO) }
                            }
                            layout(VAlignment.CENTER, weight = 1) {
                                card { icon(IconType.INFO, size = Icon.Size.LARGE) }
                            }

                        }
                    }

                }

                card(Padding.DEFAULT) {

                    column(Padding.DEFAULT) {

                        label("Column", style = LabelStyle.HEADLINE2)

                        label(
                            "Column aligns multiple elements in a column. Same properties as Row. " +
                                    "The following examples show the use of weight and the use of alignments"
                        )

                        box(Padding.HALF_DEFAULT)

                        column {
                            layout(HAlignment.START) {
                                card { icon(IconType.WARN) }
                            }
                            layout(HAlignment.END, weight = 1) {
                                card { icon(IconType.INFO) }
                            }
                            layout(HAlignment.CENTER, weight = 1) {
                                card { icon(IconType.INFO, size = Icon.Size.LARGE) }
                            }

                        }

                        box(Padding.HALF_DEFAULT)

                        column {
                            HAlignment.values().forEach { alignment ->
                                layout(alignment) {
                                    button("Alignment ${alignment.name}")
                                }
                            }
                        }
                    }

                }

            }

        }

}