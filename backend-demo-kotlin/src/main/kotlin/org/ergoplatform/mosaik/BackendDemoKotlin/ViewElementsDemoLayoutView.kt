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

                label("Layout elements", style = LabelStyle.HEADLINE1)

                label(
                    "View source on GitHub",
                    style = LabelStyle.BODY1LINK,
                    textColor = ForegroundColor.PRIMARY
                ) {
                    onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik/blob/develop/backend-demo-kotlin/src/main/kotlin/org/ergoplatform/mosaik/BackendDemoKotlin/ViewElementsDemoLayoutView.kt"))
                }

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

                button("Back to main page") {
                    onClickAction(reloadApp())
                }
            }

        }

}