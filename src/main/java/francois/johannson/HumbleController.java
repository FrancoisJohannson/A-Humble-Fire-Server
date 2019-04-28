package francois.johannson;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

//You can make HTTP-Requests with the Tools "Postman" or "Fiddler"

@RestController
public class HumbleController {

    @DeleteMapping(path = "/members")
    public void deleteMember(@RequestBody String member) {
        System.out.println("Processing a DELETE");
    }

    /*
    Processing a real JSON-Construct as Body of the PUT, for Example:
    PUT http://localhost:8080/members
    {"surname":"Frida","name":"Kahlo"}
     */
    @PutMapping(path = "/members")
    public void addMember(@RequestBody Member member) {
        System.out.println("Processing a PUT: name=" + member.getName() + ", surname=" + member.getSurname() );
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
