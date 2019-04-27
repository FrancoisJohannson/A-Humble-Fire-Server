package francois.johannson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class HumbleController {

    @GetMapping("/infos")
    public String infos() {
        return "This are the informations";
    }

    @RequestMapping("/")
    public String index() {
        return "Stay calm, its going to work!";
    }


}
