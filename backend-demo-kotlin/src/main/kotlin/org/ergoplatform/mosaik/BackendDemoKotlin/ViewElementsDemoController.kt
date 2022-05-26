package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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

            // add the reload app action so that we can use it in the views by naming its ID
            reloadApp(RELOAD_APP_ACTION_ID)

            column {

                label("View Elements Overview", LabelStyle.HEADLINE2)

                box(Padding.DEFAULT)

                label(
                    "Click or tap a button to see an overview for the available view elements " +
                            "per group. Also check out the document linked on the GitHub repo!"
                )

                box(Padding.HALF_DEFAULT)

                label("Open GitHub repo", LabelStyle.BODY1LINK) {
                    onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik"))
                    textColor = ForegroundColor.PRIMARY
                }

                box(Padding.HALF_DEFAULT)

                button("Layout elements") {
                    onClickAction(backendRequest("layoutview"))
                }

                box(Padding.HALF_DEFAULT)

                button("Text elements") {
                    onClickAction(backendRequest("textelementview"))
                }

                box(Padding.HALF_DEFAULT)

                button("Input elements") {
                    onClickAction(backendRequest("inputelementview"))
                }

            }

        }

    // the following are the defined responses for backend requests
    // Since they are constant, a backend request wouldn't be necessary, but is done
    // here to demonstrate the implementation

    @PostMapping("/viewelements/layoutview")
    fun changeToLayoutView() = backendResponse(
        APP_VERSION,
        changeView(ViewElementsDemoLayoutView.getView())
    )

    @PostMapping("/viewelements/textelementview")
    fun changeToTextElementView() = backendResponse(
        APP_VERSION,
        changeView(ViewElementsDemoTextView.getView())
    )

    @PostMapping("/viewelements/inputelementview")
    fun changeToInoutElementView() = backendResponse(
        APP_VERSION,
        changeView(ViewElementsDemoInputView.getView())
    )

}