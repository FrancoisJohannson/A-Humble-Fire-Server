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

    private final String filename = "list-of-chinese-words.txt";

    @DeleteMapping(path = "/chinesewords/{id}")
    public @ResponseBody ResponseEntity<String> deleteMember(@PathVariable String id) {

        System.out.println("Processing a DELETE");

        ArrayList<ChineseWords> memberlist = readWordList();

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
            return new ResponseEntity<>("ChineseWords deleted", HttpStatus.OK);
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

    private ArrayList<ChineseWords> readWordList() {
        String sOldContent = this.readFileContents();

        ArrayList<ChineseWords> memberlist = new ArrayList<>();

        if ( sOldContent.length() > 0 ) {
            Type listType = new TypeToken<ArrayList<ChineseWords>>(){}.getType();
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
    @PostMapping(path = "/chinesewords")
    public @ResponseBody ResponseEntity<String> addMember(@RequestBody ChineseWords chineseWords) {

        ArrayList<ChineseWords> memberlist = readWordList();

        for( ChineseWords m:memberlist) {
            // don't write, if already existing
            if ( m.getId()== chineseWords.getId() ) {
                return new ResponseEntity<String>("id aleady exists : " + chineseWords.getHanzi() + " " + chineseWords.getEnglish(), HttpStatus.CONFLICT);
            }

            if (m.getHanzi().contains(chineseWords.getHanzi()) && m.getEnglish().contains(chineseWords.getEnglish())) {
                return new ResponseEntity<String>("chineseWords aleady exists : " + chineseWords.getHanzi() + " " + chineseWords.getEnglish(), HttpStatus.CONFLICT);
            }

        }

        memberlist.add(chineseWords);

        String json = new Gson().toJson(memberlist);
        writeToFile(json);

        return new ResponseEntity<String>("ChineseWords added", HttpStatus.OK);

    }

    // PUT is for changing the content of an element
    @PutMapping(path = "/chinesewords")
    public @ResponseBody ResponseEntity<String> changeMember(@RequestBody ChineseWords chineseWords) {

        ArrayList<ChineseWords> memberlist = readWordList();

        boolean idfound = false;

        for( ChineseWords m:memberlist) {
            if ( m.getId()== chineseWords.getId() ) {
                m.setHanzi(chineseWords.getHanzi());
                m.setEnglish(chineseWords.getEnglish());
                idfound = true;
            }
        }

        String json = new Gson().toJson(memberlist);
        writeToFile(json);

        if ( idfound ) {
            return new ResponseEntity<>("ChineseWords changed", HttpStatus.OK);
        }

        return new ResponseEntity<>("id not found", HttpStatus.NOT_FOUND);
    }


    //  火
    @GetMapping("/chinesewords")
    public @ResponseBody ResponseEntity<String> getMembers() {

        ArrayList<ChineseWords> memberlist = readWordList();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        String json = new Gson().toJson(memberlist);

        ResponseEntity re = ResponseEntity
            .ok().
            headers(responseHeaders).
            body(json);

        return re;

    }

    // Return one ChineseWords by Id
    @GetMapping("/chinesewords/{id}")
    public @ResponseBody ResponseEntity<String> getMemberById(@PathVariable String id) {


        ArrayList<ChineseWords> memberlist = readWordList();

        ChineseWords mfound = null;

        for( ChineseWords m:memberlist) {
            if ( m.getId()==Integer.parseInt(id) ) {
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
