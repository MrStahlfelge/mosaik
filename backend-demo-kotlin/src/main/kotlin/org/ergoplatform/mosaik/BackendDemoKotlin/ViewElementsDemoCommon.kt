package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.ui.layout.Column
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.VAlignment
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

// reload app action is always the same, so make its ID a const
const val RELOAD_APP_ACTION_ID = "RELOAD_APP"

fun @MosaikDsl Column.addHeader(title: String) {
    layout(HAlignment.JUSTIFY) {
        box {
            layout(HAlignment.START, VAlignment.CENTER) {
                button("Back") {
                    onClickAction(RELOAD_APP_ACTION_ID)
                }
            }
            label(
                title,
                style = LabelStyle.HEADLINE1
            )
        }
    }
}

