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
import org.springframework.web.client.HttpClientErrorException;
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
        // JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        // JsonArray jsonArray = null;
        // for (String item : items) {
        //     jsonArrayBuilder.add(item);
        // }
        // jsonArray = jsonArrayBuilder.build();
        // logger.log(Level.INFO, "jsonArray for request >>> " + jsonArray.toString());

        String url = "https://quotation.chuklee.com/quotation";
        
        ResponseEntity<String> resp;
        
        try{
            RequestEntity<String> req = RequestEntity
            .post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(items.toString() ,String.class);
        
            RestTemplate template = new RestTemplate();
            
            resp = template.exchange(req, String.class);
            logger.log(Level.INFO, "Status>>>> " + resp.getStatusCodeValue());
        } catch (HttpClientErrorException e) {
            return Optional.empty();
        }
        

        if (resp.getStatusCodeValue() <= 200) {
            InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            logger.log(Level.INFO, data.toString());
    
            
            Quotation quotation = new Quotation();
            quotation.setQuoteId(data.getString("quoteId"));
            JsonArray quoteData = data.getJsonArray("quotations");

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

        } else {
            return Optional.empty();
        }

        
    }


}
