package org.ergoplatform.mosaik.backenddemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView mainPage() {
        // we always serve nobrowser error page for the main url. If the request came from a
        // Mosaik executor, it will pick up the <link rel="mosaik" ...> entry
        return new ModelAndView("nobrowser.html");
    }
}
