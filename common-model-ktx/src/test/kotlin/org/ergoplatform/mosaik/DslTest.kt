package org.ergoplatform.mosaik

import junit.framework.TestCase
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.ergoplatform.mosaik.serialization.MosaikSerializer
import org.junit.Assert

class DslTest : TestCase() {

    fun testDsl() {
        val app = mosaikApp(
            appName = "Test",
            appVersion = 0,
            targetMosaikVersion = 0
        ) {

            showDialog("This is an error message") {
                positiveButtonText = "Nothing"
            }

            box {
                // Drawback in Kotlin DSL: This box will be overwritten by next column
            }

            column {
                box(Padding.DEFAULT) {
                    button("A button")
                }

                layout(HAlignment.START, 1) {
                    label("This is a label") {
                        style = LabelStyle.BODY1BOLD
                    }
                }

                box {

                    label("Another label. without weight and not bold.")
                }
            }

        }

        val json = println(MosaikSerializer().toJson(app))
        Assert.assertNotNull(json)
    }
}