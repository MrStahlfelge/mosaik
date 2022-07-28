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

object ViewElementsDemoOthersView {
    fun getView() = mosaikView {
        column {

            // example for reusing parts of views - see ViewElementsDemoCommon.kt for source
            addHeader("Other elements")

            label(
                "View source on GitHub",
                style = LabelStyle.BODY1LINK,
                textColor = ForegroundColor.PRIMARY
            ) {
                onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik/blob/develop/backend-demo-kotlin/src/main/kotlin/org/ergoplatform/mosaik/BackendDemoKotlin/ViewElementsDemoOthersView.kt"))
            }

            label(
                "Elements not fitting into the other categories are shown and explained here."
            )

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("LazyLoadBox and LoadingIndicator", style = LabelStyle.HEADLINE2)

                    label(
                        "LazyLoadBox gives Mosaik apps the ability to show a view's main content " +
                                "and let the user interact with it, while contents of this box may not " +
                                "be present yet. Use this when some information might need some time " +
                                "to load."
                    )

                    box(Padding.DEFAULT) {
                        lazyLoadBox("slowView") {
                            // the box contents are shown while the box is loading and replaced when
                            // loading finished
                            loadingIndicator()
                        }
                    }

                    label(
                        "LoadingIndicator is an element displaying process. It is shown in " +
                                "the LazyLoadBox above while it has not finished loading."
                    )


                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("Image", style = LabelStyle.HEADLINE2)

                    label(
                        "Image can be used to show and load any image from an URL"
                    )

                    box(Padding.DEFAULT) {
                        image("https://picsum.photos/400", Image.Size.LARGE)
                    }

                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("Icon", style = LabelStyle.HEADLINE2)

                    label(
                        "Shows platform-dependant icons that can be tinted."
                    )

                    val iconsPerRow = 4
                    val rows = (IconType.values().size + iconsPerRow - 1) / iconsPerRow

                    column(Padding.DEFAULT) {
                        layout(HAlignment.CENTER, weight = 1) {
                            for (r in 0 until rows) {
                                row(packed = true) {
                                    layout(VAlignment.CENTER, weight = 1) {
                                        for (i in 0 until iconsPerRow) {
                                            val idx = r * iconsPerRow + i
                                            if (idx < IconType.values().size)
                                                icon(
                                                    IconType.values()[idx],
                                                    size = Icon.Size.MEDIUM
                                                )
                                            else
                                                box()
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    label("QrCode", style = LabelStyle.HEADLINE2)

                    label(
                        "Renders a given text as a QR code"
                    )

                    box(Padding.DEFAULT)

                    label("(Not implemented yet)")

                }
            }

        }
    }
}
