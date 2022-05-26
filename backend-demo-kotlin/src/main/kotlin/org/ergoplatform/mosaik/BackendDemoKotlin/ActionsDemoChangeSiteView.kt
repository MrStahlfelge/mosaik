package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.Icon
import org.ergoplatform.mosaik.model.ui.IconType
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

object ActionsDemoChangeSiteView {
    val iconId = "iconId"

    fun getView() = mosaikView {
        column {

            // example for reusing parts of views - see ViewElementsDemoCommon.kt for source
            addHeader("Change site action")

            label(
                "View source on GitHub",
                style = LabelStyle.BODY1LINK,
                textColor = ForegroundColor.PRIMARY
            ) {
                onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik/blob/develop/backend-demo-kotlin/src/main/kotlin/org/ergoplatform/mosaik/BackendDemoKotlin/ActionsDemoChangeSiteView.kt"))
            }

            box(Padding.DEFAULT)

            label(
                "ChangeSiteAction can change parts of the view. If the root element of the " +
                        "new view returned has the ID of a view already shown, this view is replaced. " +
                        "The whole view can be replaced by giving no root ID."
            )

            card(Padding.DEFAULT) {
                column(Padding.DEFAULT) {
                    id = iconId
                    icon(IconType.WARN, Icon.Size.LARGE)

                    val buttonText = "Click to replace the icon above"
                    button(buttonText) {
                        onClickAction(changeView(mosaikView {
                            column(Padding.DEFAULT) {
                                id = iconId
                                icon(
                                    IconType.WALLET,
                                    Icon.Size.LARGE,
                                    tintColor = ForegroundColor.PRIMARY
                                )
                                button(buttonText) { isEnabled = false }
                            }
                        }))
                    }

                }
            }
        }
    }
}