package org.ergoplatform.mosaik.BackendDemoKotlin

import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.jackson.MosaikSerializer
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.Image
import org.ergoplatform.mosaik.model.ui.layout.Grid
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@CrossOrigin
class ViewElementsDemoController {

    @GetMapping("/viewelements")
    fun viewElementsApp(request: HttpServletRequest) =
        mosaikApp(
            "View elements demo",
            appVersion = APP_VERSION,
            targetCanvasDimension = CANVAS,

            ) {

            // add the reload app action so that we can use it in the views by naming its ID
            reloadApp(RELOAD_APP_ACTION_ID)

            column(spacing = Padding.DEFAULT) {

                label("View Elements Overview", LabelStyle.HEADLINE2)

                label(
                    "Click or tap a button to see an overview for the available view elements " +
                            "per group. Also check out the document linked on the GitHub repo!"
                )

                label("Open GitHub repo", LabelStyle.BODY1LINK) {
                    onClickAction(openBrowser("https://github.com/MrStahlfelge/mosaik"))
                    textColor = ForegroundColor.PRIMARY
                }

                button("Layout elements") {
                    onClickAction(backendRequest("layoutview", layoutViewRequestId))
                }

                button("Text elements") {
                    onClickAction(backendRequest("textelementview"))
                }

                button("Input elements") {
                    onClickAction(backendRequest("inputelementview"))
                }

                button("Other elements") {
                    onClickAction(backendRequest("otherelementview"))
                }

            }

        }

    fun Map<String, String>.toContext() = MosaikSerializer.fromContextHeadersMap(this)

    // the following are the defined responses for backend requests
    // Since they are constant, a backend request wouldn't be necessary, but is done
    // here to demonstrate the implementation

    @PostMapping("/viewelements/layoutview")
    fun changeToLayoutView(servletRequest: HttpServletRequest) = backendResponse(
        APP_VERSION,
        changeView(
            ViewElementsDemoLayoutView.getView(
                servletRequest.requestURL.toString().substringBefore("/viewelements")
            )
        )
    )

    @PostMapping("/viewelements/textelementview")
    fun changeToTextElementView(@RequestHeader headers: Map<String, String>) = backendResponse(
        APP_VERSION,
        changeView(ViewElementsDemoTextView.getView(headers.toContext()))
    )

    @PostMapping("/viewelements/inputelementview")
    fun changeToInputElementView(@RequestHeader headers: Map<String, String>) = backendResponse(
        APP_VERSION,
        changeView(ViewElementsDemoInputView.getView(headers.toContext()))
    )

    @PostMapping("/viewelements/otherelementview")
    fun changeToOtherElementView() = backendResponse(
        APP_VERSION,
        changeView(ViewElementsDemoOthersView.getView())
    )

    @GetMapping("/viewelements/grid")
    fun gridViewApp(
        @RequestHeader headers: Map<String, String>,
    ) = mosaikApp(
        "View elements Grid demo",
        appVersion = APP_VERSION,
    ) {
        val context = MosaikSerializer.fromContextHeadersMap(headers)

        if (context.mosaikVersion < 2) {
            box {
                needHigherMosaikVersionLabel(2)
            }
        } else {
            val urls = listOf(
                "https://picsum.photos/700",
                "https://picsum.photos/400/600",
                "https://picsum.photos/700/400",
            )
            grid(elementSize = Grid.ElementSize.MEDIUM, padding = Padding.HALF_DEFAULT) {
                for (i in 1..9) {

                    card(Padding.HALF_DEFAULT) {
                        val gridelmentid = "gridelement$i"
                        id = gridelmentid
                        onClickAction(changeView(mosaikView {
                            card {
                                id = gridelmentid
                                label("Element $i clicked")
                            }
                        }))
                        column(Padding.HALF_DEFAULT) {
                            layout(HAlignment.CENTER, 1) {
                                image(urls[i % urls.size]) {
                                    size = Image.Size.LARGE
                                }
                            }
                            label("Element $i", LabelStyle.HEADLINE2)
                        }
                    }
                }
            }
        }
    }

    // Lazy box view content responses
    @GetMapping("/viewelements/slowView")
    fun lazyLoadBoxView() = mosaikView {
        Thread.sleep(5000)

        box(Padding.NONE) {
            label("This took long to load")
        }
    }
}

const val layoutViewRequestId = "layoutviewrequest"