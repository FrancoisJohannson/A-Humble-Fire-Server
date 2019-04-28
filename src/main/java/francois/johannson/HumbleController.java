package francois.johannson;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//You can make HTTP-Requests with the Tools "Postman" or "Fiddler"

@RestController
public class HumbleController {

    private final String filename = "list-of-members.txt";

    @DeleteMapping(path = "/members")
    public void deleteMember(@RequestBody String member) {
        System.out.println("Processing a DELETE");
    }


    private String readFileContents() {
        String sContent = "";
        try {
            sContent = Files.readString(Paths.get("./", this.filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sContent;
    }


    private void writeToFile(String sText) {
        PrintWriter writer = null;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename, false));
            writer = new PrintWriter( bw );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(sText);
        writer.close();

    }

    /*
    Processing a real JSON-Construct as Body of the PUT, for Example:
    PUT http://localhost:8080/members
    {"surname":"Frida","name":"Kahlo"}
     */
    @PutMapping(path = "/members")
    public void addMember(@RequestBody Member member) {
        String sTmp = this.readFileContents()+member.toString();
        System.out.println("Processing a PUT: " + member.toString() );
        writeToFile(sTmp);

    }

    @PostMapping(path = "/members")
    public void changeMember(@RequestBody String member) {
        System.out.println("Processing a POST");
    }



    @GetMapping("/members")
    public String getMembers() {
        System.out.println("Processing a GET");
        return readFileContents();
    }

    @RequestMapping("/")
    public String index() {
        return "Stay calm, its going to work!";
    }


}
