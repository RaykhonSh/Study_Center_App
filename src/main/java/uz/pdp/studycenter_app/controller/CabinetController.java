package uz.pdp.studycenter_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {
    @GetMapping
    public String getCabinet() {
        return "cabinet";
    }
}
