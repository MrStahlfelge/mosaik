package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class ViewElementsDemoController {

    @GetMapping("/viewelements")
    fun viewElementsApp(request: HttpServletRequest) =
        mosaikApp(
            "App Selector",
            appVersion = APP_VERSION,
            targetMosaikVersion = 0,
            appBaseUrl = request.requestURL.toString(),

            ) {

            column {

                label("View Elements Overview", LabelStyle.HEADLINE2)

                box(Padding.DEFAULT)

                label("Click or tap a button to see an overview for the available view elements " +
                        "per group. Also check out the document linked on the GitHub repo!")

                box(Padding.HALF_DEFAULT)

                label("Open GitHub repo", LabelStyle.BODY1LINK) {
                    onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik"))
                    textColor = ForegroundColor.PRIMARY
                }

                box(Padding.DEFAULT) {
                    button("Layout elements") {
                        onClickAction(changeView(ViewElementsDemoLayoutView.getView()))
                    }
                }

            }

        }


}