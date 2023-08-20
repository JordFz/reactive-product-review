package jfc.microservices.core.productcomposite;

import jfc.microservices.api.core.product.Product;
import jfc.microservices.api.core.recommendation.Recommendation;
import jfc.microservices.api.core.review.Review;
import jfc.microservices.api.exceptions.InvalidInputExceptions;
import jfc.microservices.api.exceptions.NotFoundException;
import jfc.microservices.core.productcomposite.Service.ProductCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductCompositeServiceApplicationTests {
	private static final int PRODUCT_ID_OK = 1;
	private static final int PRODUCT_ID_NOT_FOUND = 2;
	private static final int PRODUCT_ID_INVALID = 3;

	@Autowired
	private WebTestClient client;

	@MockBean
	private ProductCompositeIntegration compositeIntegration;

	@BeforeEach
	void setUp() {
		when(compositeIntegration.getProduct(PRODUCT_ID_OK))
				.thenReturn(new Product(PRODUCT_ID_OK, "name", 1, "Mock address"));
		when(compositeIntegration.getRecommendations(PRODUCT_ID_OK))
				.thenReturn(Collections.singletonList(new Recommendation(PRODUCT_ID_OK, 1, "Author", 1, "Content", "Mock Address")));
		when(compositeIntegration.getReviews(PRODUCT_ID_OK))
				.thenReturn(Collections.singletonList(new Review(PRODUCT_ID_OK, 1, "Author", "Subject", "Content", "Mock address")));

		when(compositeIntegration.getProduct(PRODUCT_ID_NOT_FOUND))
				.thenThrow(new NotFoundException("NOT FOUND: " + PRODUCT_ID_NOT_FOUND));
		when(compositeIntegration.getProduct(PRODUCT_ID_INVALID))
				.thenThrow(new InvalidInputExceptions("INVALID: " + PRODUCT_ID_INVALID));
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getProductById() {
		client.get()
				.uri("/product-composite/" + PRODUCT_ID_OK)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.productId").isEqualTo(PRODUCT_ID_OK)
				.jsonPath("$.recommendations.length()").isEqualTo(1)
				.jsonPath("$.reviews.length()").isEqualTo(1);
	}

	@Test
	void getProductNotFound() {
		client.get()
				.uri("/product-composite/" + PRODUCT_ID_NOT_FOUND)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_NOT_FOUND)
				.jsonPath("$.message").isEqualTo("NOT FOUND: " + PRODUCT_ID_NOT_FOUND);
	}

	@Test
	void getProductInvalidInput() {
		client.get()
				.uri("/product-composite/" + PRODUCT_ID_INVALID)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_INVALID)
				.jsonPath("$.message").isEqualTo("INVALID: " + PRODUCT_ID_INVALID);
	}

}
