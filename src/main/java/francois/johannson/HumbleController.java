package francois.johannson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        Path p = Paths.get("./", this.filename);
        if ( Files.exists(p) ) {
            try {
                sContent = Files.readString(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        if (writer != null) {
            writer.println(sText);
            writer.close();
        }
    }

    /*
    Processing a real JSON-Construct as Body of the PUT, for Example:
    PUT http://localhost:8080/members
    {"id":1,"surname":"Frida","name":"Kahlo"}
     */
    @PutMapping(path = "/members")
    public void addMember(@RequestBody Member member) {
        String sOldContent = this.readFileContents();

        ArrayList<Member> memberlist = new ArrayList<>();

        if ( sOldContent.length() > 0 ) {
            Type listType = new TypeToken<ArrayList<Member>>(){}.getType();
            memberlist = new Gson().fromJson(sOldContent, listType);
        }

        for( Member m:memberlist) {
            // don't write, if already existing
            if (m.getName().contains(member.getName()) && m.getSurname().contains(member.getSurname())) {
                return;
            }
        }

        memberlist.add(member);

        String json = new Gson().toJson(memberlist);
        System.out.println("Processing a PUT: " + member.toString() );
        writeToFile(json);

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
