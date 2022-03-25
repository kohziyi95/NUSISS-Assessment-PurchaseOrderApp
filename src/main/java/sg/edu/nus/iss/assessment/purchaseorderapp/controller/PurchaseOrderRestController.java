package sg.edu.nus.iss.assessment.purchaseorderapp.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
@RequestMapping(path="/")
public class PurchaseOrderRestController {
    private static Logger logger = Logger.getLogger(PurchaseOrderRestController.class.getName());

    @PostMapping(consumes="application/json")
    public ResponseEntity<String> showQuotation(@RequestBody String payload){
        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())){
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
        } catch (Exception e) {
            body = Json.createObjectBuilder().add("error", e.getMessage()).build();
        }
        
        List<String> poList = new ArrayList<>();
        for (int i=0; i< body.getJsonArray("lineItems").size(); i++){
            String itemToAdd = body.getJsonArray("lineItems").getJsonObject(i).get("item").toString();
            // if (!poList.contains(itemToAdd))
            poList.add(itemToAdd);
        }  
        logger.log(Level.INFO, poList.toString());

        return null;

    }
    
}
