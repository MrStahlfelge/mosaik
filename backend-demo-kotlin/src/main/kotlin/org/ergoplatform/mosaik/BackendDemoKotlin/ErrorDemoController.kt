package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.ui.Icon
import org.ergoplatform.mosaik.model.ui.IconType
import org.ergoplatform.mosaik.model.ui.LoadingIndicator
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.springframework.core.io.ClassPathResource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class ErrorDemoController {
    @GetMapping("/errors")
    fun viewElementsApp(request: HttpServletRequest) =
        mosaikApp(
            "Mosaik errors demo",
            appVersion = APP_VERSION,

            ) {

            // add the reload app action so that we can use it in the views by naming its ID
            reloadApp(RELOAD_APP_ACTION_ID)

            column {

                label("Mosaik Errors Overview", LabelStyle.HEADLINE2)

                box(Padding.DEFAULT)

                label(
                    "Mosaik App backends and executors communicate via serialized JSON strings. " +
                            "It is not safe to assume that sent strings are valid. This demonstration " +
                            "shows some typical failures that can happen. It is meant to test graceful " +
                            "error handling by Mosaik executors"
                )

                box(Padding.HALF_DEFAULT)

                button("Backend request URL not found") {
                    onClickAction(backendRequest("notfound"))
                    // see [ActionsDemoBackendRequestController]
                }

                box(Padding.HALF_DEFAULT)

                card {
                    column(Padding.DEFAULT) {
                        label("Lazyloadbox URL error not found")

                        lazyLoadBox("error") {
                            loadingIndicator(LoadingIndicator.Size.SMALL)

                            // do not use DSL here - using icon {   } will add the icon to the box
                            // while loading
                            errorView = Icon().apply { iconType = IconType.WARN }
                        }
                    }
                }

                box(Padding.HALF_DEFAULT)

                button("Backend request replying with id set twice not found") {
                    onClickAction(backendRequest("doubleId"))
                    // see [ActionsDemoBackendRequestController]
                }

                box(Padding.HALF_DEFAULT)

                button("Adds a number input text field with initial value string") {
                    id = "invalidInitialValue"
                    onClickAction(backendRequest("invalidInitialValue"))
                }

                box(Padding.HALF_DEFAULT)

                button("Adds a non-existant element type") {
                    id = "invalidElement"
                    onClickAction(backendRequest("invalidElement"))
                }

                box(Padding.HALF_DEFAULT)

                button("Adds an icon with a non-existant icon type") {
                    id = "invalidIconType"
                    onClickAction(backendRequest("invalidIconType"))
                }
            }
        }

    // the following are the defined responses for backend requests
    // Since they are constant, a backend request wouldn't be necessary, but is done
    // here to demonstrate the implementation

    @PostMapping("/errors/changesite")
    fun changeToChangeSiteView() = backendResponse(
        APP_VERSION,
        changeView(ActionsDemoChangeSiteView.getView())
    )

    @PostMapping("/errors/doubleId")
    fun changeToDoubleIdView() = backendResponse(
        APP_VERSION,
        changeView(mosaikView {
            box {
                id = "sameid"

                label("Text") {
                    id = "sameid"
                }
            }
        })
    )

    @GetMapping("/errors/error")
    fun lazyLoadBoxError(): ViewContent {
        Thread.sleep(2000)
        error("An error")
    }

    @PostMapping("/errors/invalidInitialValue")
    fun changeToInvalidInitialValue(): String {
        val classPathResource = ClassPathResource("invalidInitialValue.json")

        return String(classPathResource.inputStream.readAllBytes()).replace(
            "%APPVERSION%",
            APP_VERSION.toString()
        )
    }

    @PostMapping("/errors/invalidElement")
    fun changeToInvalidElement(): String {
        val classPathResource = ClassPathResource("invalidElement.json")

        return String(classPathResource.inputStream.readAllBytes()).replace(
            "%APPVERSION%",
            APP_VERSION.toString()
        )
    }

    @PostMapping("/errors/invalidIconType")
    fun changeToInvalidIconType(): String {
        val classPathResource = ClassPathResource("invalidIconType.json")

        return String(classPathResource.inputStream.readAllBytes()).replace(
            "%APPVERSION%",
            APP_VERSION.toString()
        )
    }

}