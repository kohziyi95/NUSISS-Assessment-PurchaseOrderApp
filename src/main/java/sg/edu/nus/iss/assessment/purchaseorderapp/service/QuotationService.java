package sg.edu.nus.iss.assessment.purchaseorderapp.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.util.logging.Level;


import sg.edu.nus.iss.assessment.purchaseorderapp.model.Quotation;

@Service
public class QuotationService {
    private Logger logger = Logger.getLogger(QuotationService.class.getName());

    public Optional<Quotation> getQuotations (List<String> items) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        JsonArray jsonArray = null;
        for (String item : items) {
            jsonArrayBuilder.add(item);
        }
        jsonArray = jsonArrayBuilder.build();
        logger.log(Level.INFO, "jsonArray for request >>> " + jsonArray.toString());

        String url = "https://quotation.chuklee.com/quotation";

        RequestEntity<String> req = RequestEntity
            .post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(items.toString() ,String.class);
        
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);

        InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject();
        logger.log(Level.INFO, data.toString());
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // logger.log(Level.INFO, "Headers >>> " + headers.toString());

        
        // HttpEntity<String> entity = new HttpEntity<>(items.toString(), headers);

        // ResponseEntity<String> response = template.postForEntity(url, entity, String.class);
        
        // logger.log(Level.INFO, "response status >>>> " + response.getStatusCode().toString());
        
        
        // JsonObject data = null;

        // if (response.getStatusCode() == HttpStatus.CREATED) {
        //     InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
        //     JsonReader reader = Json.createReader(is);
        //     data = reader.readObject();
        //     logger.log(Level.INFO, "HTTP created");


        // } else {
        //     logger.log(Level.SEVERE, "Fail to get response");
        //     return null;
        // }

        
        Quotation quotation = new Quotation();
        quotation.setQuoteId(data.getString("quoteId"));
        // quotation.setQuotations("quotations");
        JsonArray quoteData = data.getJsonArray("quotations");
        // quoteData.keySet();
        // Iterator<String> itr = quoteData.keySet().iterator();
        for (int i = 0; i < quoteData.size(); i++){
            Float unitPrice = Float.parseFloat(quoteData.getJsonObject(i).get("unitPrice").toString());
            logger.log(Level.INFO, "unitPrice >>>> " + unitPrice.toString());
            quotation.addQuotation(
                quoteData.getJsonObject(i).getString("item"), unitPrice
            );
        }

        logger.log(Level.INFO, "quotation>>>> " + quotation.getQuotations());

        Optional<Quotation> opt = Optional.of(quotation);

        return opt;
        // return null;
    }


}
