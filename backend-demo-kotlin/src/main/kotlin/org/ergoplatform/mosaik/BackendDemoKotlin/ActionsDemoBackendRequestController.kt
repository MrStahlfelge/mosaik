package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ActionsDemoBackendRequestController {
    val textInputId = "textinput"

    // the following are the defined responses for backend requests
    // Since they are constant, a backend request wouldn't be necessary, but is done
    // here to demonstrate the implementation

    @PostMapping("/actions/backendrequest")
    fun changeToBackendRequestView(): FetchActionResponse {
        return backendResponse(
            APP_VERSION,
            changeView(
                mosaikView {

                    column {

                        // example for reusing parts of views - see ViewElementsDemoCommon.kt for source
                        addHeader("Backend request demo")

                        label(
                            "View source on GitHub",
                            style = LabelStyle.BODY1LINK,
                            textColor = ForegroundColor.PRIMARY
                        ) {
                            onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik/blob/develop/backend-demo-kotlin/src/main/kotlin/org/ergoplatform/mosaik/BackendDemoKotlin/ActionsDemoBackendRequestController.kt"))
                        }
                        box(Padding.HALF_DEFAULT)

                        label(
                            "A backend request action contacts your backend, transferring all " +
                                    "user inputs with the request. The response from your backend is a " +
                                    "new action (that must not be a new BackendRequest). The new action " +
                                    "can change the view, show a dialog or whatever you want based on the " +
                                    "user's inputs."
                        )

                        card(Padding.DEFAULT) {
                            column(Padding.DEFAULT) {
                                textInputField(textInputId, "Enter something")

                                button("Backend request, slowed down, showing a dialog") {
                                    onClickAction(backendRequest("slowDownDialog"))
                                }
                            }
                        }
                    }
                }
            )
        )
    }

    @PostMapping("/actions/slowDownDialog")
    fun showSlowDialog(@RequestBody values: Map<String, *>): FetchActionResponse {
        Thread.sleep(1000)

        return backendResponse(
            APP_VERSION,
            showDialog(values[textInputId]?.let { "You have entered $it" }
                ?: "You have entered nothing")
        )
    }

}