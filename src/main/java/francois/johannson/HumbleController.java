package francois.johannson;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;


@RestController
public class HumbleController {

    @DeleteMapping(path = "/members")
    public void deleteMember(@RequestBody String member) {
        System.out.println("Processing a DELETE");
    }

    @PutMapping(path = "/members")
    public void addMember(@RequestBody String member) {
        System.out.println("Processing a PUT");
    }

    @PostMapping(path = "/members")
    public void changeMember(@RequestBody String member) {
        System.out.println("Processing a POST");
    }

    @GetMapping("/members")
    public String getMembers() {

        System.out.println("Processing a GET");

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
