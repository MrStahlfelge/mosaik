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
    fun getView(baseUrl: String) =
        mosaikView {

            column {

                // example for reusing parts of views - see ViewElementsDemoCommon.kt for source
                addHeader("Layout elements")

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
                            icon(IconType.CROSS, tintColor = ForegroundColor.PRIMARY)
                        }

                    }

                }

                card(Padding.DEFAULT) {

                    column(Padding.DEFAULT) {

                        label("Card", style = LabelStyle.HEADLINE2)

                        label(
                            "The card can organize your views by grouping elements " +
                                    "graphically. You'll see cards in this demo used to group " +
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
                                    "Elements with same weight take the same horizontal space, demonstrated " +
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

                        box(Padding.DEFAULT)

                        label(
                            "Use rows with weights to layout tables:"
                        )
                        box(Padding.HALF_DEFAULT)

                        row {
                            layout(weight = 1) {
                                label("Column 1", textAlignment = HAlignment.END)
                            }
                            // some margin between columns
                            box(Padding.HALF_DEFAULT)
                            layout(weight = 1) {
                                label("Column 2", LabelStyle.BODY1BOLD)
                            }
                        }
                        row {
                            layout(weight = 1) {
                                label("A1", textAlignment = HAlignment.END)
                            }
                            // some margin between columns
                            box(Padding.HALF_DEFAULT)
                            layout(weight = 1) {
                                label("A2", LabelStyle.BODY1BOLD)
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

                        layout(HAlignment.JUSTIFY) {
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

                card(Padding.DEFAULT) {

                    column(Padding.DEFAULT, spacing = Padding.DEFAULT) {

                        label("Grid", style = LabelStyle.HEADLINE2)

                        label(
                            "Grid is a responsive element presenting child elements in a grid-style layout. " +
                                    "The number of columns is determined by the min element width.\n" +
                                    "It is best suited to be used on full width, so it is presented as an own " +
                                    "app here."
                        )

                        button("Show as own app") {
                            onClickAction(navigateToApp("$baseUrl/viewelements/grid"))
                        }
                    }

                }

                card(Padding.DEFAULT) {

                    column(Padding.DEFAULT) {

                        label("Tree root", style = LabelStyle.HEADLINE2)

                        label(
                            "Layout elements can be used as the root of the view tree. " +
                                    "There is a slightly different rendering behaviour based on the " +
                                    "type of the tree root: If it is a Column, this root Column is " +
                                    "vertically packed, aligned at the top and scrollable. This " +
                                    "is demonstrated by the whole screen you see here. " +
                                    "Other elements (box, card, etc.) will be sized to the full screen size and " +
                                    "are not scrollable."
                        )

                        button("Show a Card as tree root") {
                            onClickAction(changeView(boxAsRootView()))
                        }
                    }

                }
                card(Padding.DEFAULT) {

                    column(Padding.NONE) {

                        column(Padding.DEFAULT) {
                            label("Horizontal rule", style = LabelStyle.HEADLINE2)

                            label(
                                "Use the horizontal rule as a separator when needed."
                            )
                        }

                        hr(Padding.TWICE)

                        column(Padding.DEFAULT) {
                            label(
                                "This is another section."
                            )
                        }
                    }

                }

            }

        }

    private fun boxAsRootView() = mosaikView {
        card {
            button("Single button, centered\nClick to go back") {
                onClickAction(layoutViewRequestId)
            }
        }
    }

}