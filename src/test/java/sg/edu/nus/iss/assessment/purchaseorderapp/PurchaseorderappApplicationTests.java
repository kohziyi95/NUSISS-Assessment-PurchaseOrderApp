package sg.edu.nus.iss.assessment.purchaseorderapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.assessment.purchaseorderapp.controller.PurchaseOrderRestController;
import sg.edu.nus.iss.assessment.purchaseorderapp.service.QuotationService;

@SpringBootTest
class PurchaseorderappApplicationTests {

	
	@Autowired
	private PurchaseOrderRestController	controller;

	@Autowired
	private QuotationService service;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(controller);
	}

	@Test
	void shouldReturnQuotation() {
		
		List<String> testList = new ArrayList<>();
		testList.add("durian");
		testList.add("plum");
		testList.add("pear");

		Assertions.assertEquals(Optional.empty(), service.getQuotations(testList));

	}
}