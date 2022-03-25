package sg.edu.nus.iss.assessment.purchaseorderapp.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.assessment.purchaseorderapp.model.Quotation;
import sg.edu.nus.iss.assessment.purchaseorderapp.service.QuotationService;

@RestController
@RequestMapping(path="/api", consumes="application/json")
public class PurchaseOrderRestController {
    private static Logger logger = Logger.getLogger(PurchaseOrderRestController.class.getName());

    @Autowired
    private QuotationService service;

    @PostMapping(path="/po", consumes="application/json")
    public ResponseEntity<String> showQuotation(@RequestBody String payload){
        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())){
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
            logger.log(Level.INFO, "Payload secured");
        } catch (Exception e) {
            logger.log(Level.INFO, "Unable to get payload");
            body = Json.createObjectBuilder().add("error", e.getMessage()).build();
        }
        
        List<String> poList = new ArrayList<>();
        for (int i=0; i< body.getJsonArray("lineItems").size(); i++){
            String itemToAdd = body.getJsonArray("lineItems").getJsonObject(i).get("item").toString();
            // if (!poList.contains(itemToAdd))
            poList.add(itemToAdd);
        }  
        logger.log(Level.INFO, poList.toString());

        Quotation quotation = new Quotation();
        quotation = service.getQuotations(poList).get();

        Float totalAmount = (float) 0;
        for (String items : poList){
            JsonObject item = (JsonObject) body.getJsonArray("lineItems")
                    .stream()
                    .filter(v-> ((JsonObject) v).get("item").toString().equals(items))
                    .findFirst()
                    .get();
            logger.log(Level.INFO, "item found >>> " + item);
            int numberOfItems = item.getInt("quantity");
            Float cost = quotation.getQuotation(items) * numberOfItems;
            logger.log(Level.INFO, "item costs >>> " + cost);
            logger.log(Level.INFO, items + "  qty: " + numberOfItems + " >>>>  $" + cost);
            totalAmount += cost;
        }

        JsonObject result = Json.createObjectBuilder()
                .add("invoiceId", quotation.getQuoteId())
                .add("name", body.getString("name"))
                .add("total", totalAmount)
                .build();

        return ResponseEntity.ok(result.toString());
    }
    
}
