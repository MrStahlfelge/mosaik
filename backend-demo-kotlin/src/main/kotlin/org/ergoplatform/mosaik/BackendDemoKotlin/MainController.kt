package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.actions.Action
import org.ergoplatform.mosaik.model.ui.Image
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.layout.VAlignment
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

@Controller
class MainController {

    @GetMapping("/")
    fun mainPage(): ModelAndView {
        // we always serve nobrowser error page for the main url. If the request came from a
        // Mosaik executor, it will pick up the <link rel="mosaik" ...> entry
        return ModelAndView("nobrowser.html")
    }

    @GetMapping("/appselect")
    @ResponseBody
    fun selectorApp(request: HttpServletRequest): MosaikApp {
        val baseUrl = request.requestURL.toString()
        val serverRequestUrl = baseUrl.substring(0, baseUrl.indexOf("appselect"))

        return mosaikApp(
            "App Selector",
            appVersion = APP_VERSION,
            targetMosaikVersion = 0,

            // when writing your own app on a fixed server, you can use a constant here. Since
            // this demo might run locally on various ports and contacted through localhost or an IP
            // address, this is a safe bet to use
            appBaseUrl = baseUrl,

            ) {

            column {
                label("Welcome to the Mosaik demo backend") {
                    style = LabelStyle.HEADLINE2
                }

                label("Choose a demo to view from the buttons below") {
                    textAlignment = HAlignment.CENTER
                }

                row {
                    layout(VAlignment.CENTER, 1) {
                        addAppButton(
                            navigateToApp(serverRequestUrl + "viewelements"),
                            "View Elements demo"
                        )

                        box {
                            label("Placeholder")
                        }
                    }

                }
                row {
                    layout(VAlignment.CENTER, 1) {
                        addAppButton(null, "coming soon")

                        box {
                            // just a blank placeholder
                        }
                    }
                }
            }
        }
    }

    private fun ViewGroup.addAppButton(
        action: Action?,
        text: String,
    ): ViewElement = box(Padding.DEFAULT) {
        action?.let {
            onClickAction(action)
        }

        column {
            padding = Padding.DEFAULT

            image("https://picsum.photos/400") {
                size = Image.Size.MEDIUM
            }

            box(Padding.HALF_DEFAULT)

            label(text, style = LabelStyle.BODY1BOLD, textAlignment = HAlignment.CENTER)
        }
    }
}