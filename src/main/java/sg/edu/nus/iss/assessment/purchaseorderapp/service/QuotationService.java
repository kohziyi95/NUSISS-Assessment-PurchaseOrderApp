package sg.edu.nus.iss.assessment.purchaseorderapp.service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.util.logging.Level;


import sg.edu.nus.iss.assessment.purchaseorderapp.model.Quotation;

public class QuotationService {
    private Logger logger = Logger.getLogger(QuotationService.class.getName());

    public Optional<Quotation> getQuotations (List<String> items){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        JsonArray jsonArray = null;
        for (String item : items) {
            jsonArrayBuilder.add(item);
        }
        jsonArray = jsonArrayBuilder.build();

        String url = "https://quotation.chuklee.com";

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<JsonArray> entity = new HttpEntity<>(jsonArray, headers);
        
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> response = template.postForEntity(url, entity, String.class);
        JsonObject data = null;

        if (response.getStatusCode() == HttpStatus.CREATED) {
            InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            data = reader.readObject();

        } else {
            logger.log(Level.SEVERE, "Fail to get response");
            return null;
        }

        Quotation quotation = new Quotation();
        quotation.setQuoteId(data.getString("quoteId"));
        // quotation.setQuotations("quotations");





        



    }


}
