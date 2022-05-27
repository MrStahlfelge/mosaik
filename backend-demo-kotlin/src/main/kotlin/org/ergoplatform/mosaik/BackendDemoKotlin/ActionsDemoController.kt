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
class ActionsDemoController {
    @GetMapping("/actions")
    fun viewElementsApp(request: HttpServletRequest) =
        mosaikApp(
            "Mosaik actions demo",
            appVersion = APP_VERSION,
            appBaseUrl = request.requestURL.toString(),

            ) {

            // add the reload app action so that we can use it in the views by naming its ID
            reloadApp(RELOAD_APP_ACTION_ID)

            column {

                label("Mosaik Actions Overview", LabelStyle.HEADLINE2)

                label(
                    "View source on GitHub",
                    style = LabelStyle.BODY1LINK,
                    textColor = ForegroundColor.PRIMARY
                ) {
                    onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik/blob/develop/backend-demo-kotlin/src/main/kotlin/org/ergoplatform/mosaik/BackendDemoKotlin/ActionsDemoController.kt"))
                }

                box(Padding.DEFAULT)

                label(
                    "For security reasons, Mosaik apps do not run foreign code within the " +
                            "Mosaik executor app, which is usually a wallet application. Custom " +
                            "dApp code is only executed on a backend process that does not has any " +
                            "access to the wallet app's process.\n" +
                            "To enable user interaction and interactive apps, some types of " +
                            "predefined Mosaik actions can be launched within the wallet app.\n" +
                            "These are demonstrated here."
                )

                box(Padding.HALF_DEFAULT)

                button("Backend request action") {
                    onClickAction(backendRequest("backendrequest"))
                    // see [ActionsDemoBackendRequestController]
                }

                box(Padding.HALF_DEFAULT)

                button("Change site action") {
                    onClickAction(backendRequest("changesite"))
                }

            }

        }


    // the following are the defined responses for backend requests
    // Since they are constant, a backend request wouldn't be necessary, but is done
    // here to demonstrate the implementation

    @PostMapping("/actions/changesite")
    fun changeToChangeSiteView() = backendResponse(
        APP_VERSION,
        changeView(ActionsDemoChangeSiteView.getView())
    )
}