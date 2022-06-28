package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.jackson.MosaikSerializer
import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.input.TextField
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class ActionsDemoBackendRequestController {
    val textInputId = "textinput"

    // the following are the defined responses for backend requests
    // Since they are constant, a backend request wouldn't be necessary, but is done
    // here to demonstrate the implementation

    @PostMapping("/actions/backendrequest")
    fun changeToBackendRequestView(@RequestHeader headers: Map<String, String>): FetchActionResponse {
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
                                val backendRequest = backendRequest("slowDownDialog")
                                textInputField(textInputId, "Enter something") {
                                    imeActionType = TextField.ImeActionType.DONE
                                    onImeAction = backendRequest.id
                                }

                                button("Backend request, slowed down, showing a dialog") {
                                    onClickAction(backendRequest)
                                }
                            }
                        }

                        try {
                            box(Padding.HALF_DEFAULT)

                            val context = MosaikSerializer.fromContextHeadersMap(headers)

                            label(
                                "Every backend requests get information about the user's context. " +
                                        "This is your's:\n" +
                                        "App: ${context.walletAppName} ${context.walletAppVersion}\n" +
                                        "Platform: ${context.walletAppPlatform}\n" +
                                        "GUID: ${context.guid}\n" +
                                        "Language: ${context.language}\n" +
                                        "Mosaik Executor version: ${context.mosaikVersion}\n",
                                LabelStyle.BODY2
                            )
                        } catch (t: Throwable) {
                            // could not deserialize the context from headers
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