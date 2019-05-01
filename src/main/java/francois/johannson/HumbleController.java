package francois.johannson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

//You can make HTTP-Requests with the Tools "Postman" or "Fiddler"

@RestController
public class HumbleController {

    private final String filename = "list-of-members.txt";

    @DeleteMapping(path = "/members/{id}")
    public @ResponseBody ResponseEntity<String> deleteMember(@PathVariable String id) {

        System.out.println("Processing a DELETE");

        ArrayList<Member> memberlist = readMemberlist();

        boolean idfound = false;

        for(int i=0; i<memberlist.size(); i++) {

            if ( memberlist.get(i).getId() == Integer.parseInt(id)) {
                memberlist.remove(i);
                idfound = true;
            }

        }

        String json = new Gson().toJson(memberlist);
        writeToFile(json);

        if ( idfound ) {
            return new ResponseEntity<>("Member deleted", HttpStatus.OK);
        }

        return new ResponseEntity<>("id not found", HttpStatus.NOT_FOUND);

        }


    private String readFileContents() {
        String sContent = "";
        Path p = Paths.get("./", this.filename);
        if ( Files.exists(p) ) {
            try {
                sContent = Files.readString(p, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sContent;
    }

    private ArrayList<Member> readMemberlist() {
        String sOldContent = this.readFileContents();

        ArrayList<Member> memberlist = new ArrayList<>();

        if ( sOldContent.length() > 0 ) {
            Type listType = new TypeToken<ArrayList<Member>>(){}.getType();
            memberlist = new Gson().fromJson(sOldContent, listType);

        }

        return memberlist;
    }

    private void writeToFile(String sText) {

        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(this.filename,false), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                out.write(sText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
    POST is for insterting a new element
    Processing a real JSON-Construct as Body of the POST, for Example:
    POST http://localhost:8080/members
    {"id":1,"surname":"Frida","name":"Kahlo"}
     */
    @PostMapping(path = "/members")
    public @ResponseBody ResponseEntity<String> addMember(@RequestBody Member member) {

        ArrayList<Member> memberlist = readMemberlist();

        for( Member m:memberlist) {
            // don't write, if already existing
            if ( m.getId()==member.getId() ) {
                return new ResponseEntity<String>("id aleady exists : " + member.getName() + " " + member.getSurname(), HttpStatus.CONFLICT);
            }

            if (m.getName().contains(member.getName()) && m.getSurname().contains(member.getSurname())) {
                return new ResponseEntity<String>("member aleady exists : " + member.getName() + " " + member.getSurname(), HttpStatus.CONFLICT);
            }

        }

        memberlist.add(member);

        String json = new Gson().toJson(memberlist);
        writeToFile(json);

        return new ResponseEntity<String>("Member added", HttpStatus.OK);

    }

    // PUT is for changing the content of an element
    @PutMapping(path = "/members")
    public @ResponseBody ResponseEntity<String> changeMember(@RequestBody Member member) {

        ArrayList<Member> memberlist = readMemberlist();

        boolean idfound = false;

        for( Member m:memberlist) {
            if ( m.getId()==member.getId() ) {
                m.setName(member.getName());
                m.setSurname(member.getSurname());
                idfound = true;
            }
        }

        String json = new Gson().toJson(memberlist);
        writeToFile(json);

        if ( idfound ) {
            return new ResponseEntity<>("Member changed", HttpStatus.OK);
        }

        return new ResponseEntity<>("id not found", HttpStatus.NOT_FOUND);
    }


    //  火
    @GetMapping("/members")
    public @ResponseBody ResponseEntity<String> getMembers() {

        ArrayList<Member> memberlist = readMemberlist();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        String json = new Gson().toJson(memberlist);

        ResponseEntity re = ResponseEntity
            .ok().
            headers(responseHeaders).
            body(json);

        return re;

    }

    // Return one Member by Id
    @GetMapping("/members/{id}")
    public @ResponseBody ResponseEntity<String> getMemberById(@PathVariable String id) {


        ArrayList<Member> memberlist = readMemberlist();

        Member mfound = null;

        for( Member m:memberlist) {
            if ( m.getId()==Integer.parseInt(id) ) {
                m.setSurname(" 火");
                mfound = m;
            }
        }

        if ( mfound!=null ) {

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));

            String json = new Gson().toJson(mfound);
            ResponseEntity re = ResponseEntity
                .ok().
                headers(responseHeaders).
                //contentType(MediaType.APPLICATION_JSON).
                body(json);

            return re;
        }

        return new ResponseEntity<String>("id not found : " + id, HttpStatus.OK);
    }

    @RequestMapping("/")
    public String index() {
        return "Stay calm, its going to work!";
    }


}
