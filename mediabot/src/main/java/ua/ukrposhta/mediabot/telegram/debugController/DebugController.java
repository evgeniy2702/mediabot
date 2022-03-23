package ua.ukrposhta.mediabot.telegram.debugController;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/Media-Bot/","/Media-Bot","/"})
public class DebugController {

    @GetMapping("")
    public String hello(){
        return "<center>" +
                "$$__$$__$$$$$___$$______$$_______$$$$_____<br>\n" +
                 "$$__$$__$$______$$______$$______$$__$$____<br>\n" +
                  "$$$$$$__$$$$____$$______$$______$$__$$____<br>\n" +
                 "$$__$$__$$______$$______$$______$$__$$__$$<br>\n" +
                "$$__$$__$$$$$___$$$$$$__$$$$$$___$$$$____$<br>\n" +
                "<br>\n" +
                "$$___$$_$$$$$_$$$$____$$__$$$$______$$$$$____$$$$___$$$$$$<br>\n" +
                 "$$$_$$$_$$____$$__$$__$$__$$__$$____$$__$$__$$__$$____$$__<br>\n" +
                "$$_$_$$_$$$$__$$__$$$_$$__$$$$$$____$$$$$___$$__$$____$$__<br>\n" +
                "$$___$$_$$____$$__$$__$$__$$__$$____$$__$$__$$__$$____$$__<br>\n" +
                "$$___$$_$$$$$_$$$$____$$__$$__$$____$$$$$____$$$$_____$$__<br>\n" +
                "</center>";
    }
}
