package francois.johannson;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;


@RestController
public class HumbleController {

    @PostMapping(path = "/members")
    public void addMember(@RequestBody String member) {
        System.out.println("Processing a POST");
    }

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
