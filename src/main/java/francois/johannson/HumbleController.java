package francois.johannson;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class HumbleController {

    @GetMapping("/infos")
    public String infos() {

        JSONObject json = new JSONObject();
        json .put("name", "Francois");
        json.put("surname", "Johannson");
        return json.toString();
    }

    @RequestMapping("/")
    public String index() {
        return "Stay calm, its going to work!";
    }


}
