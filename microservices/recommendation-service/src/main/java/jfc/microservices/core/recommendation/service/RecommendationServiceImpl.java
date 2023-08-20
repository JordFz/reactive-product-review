package jfc.microservices.core.recommendation.service;

import jfc.microservices.api.core.recommendation.Recommendation;
import jfc.microservices.api.core.recommendation.RecommendationService;
import jfc.microservices.api.exceptions.InvalidInputExceptions;
import jfc.microservices.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {
    private final ServiceUtil serviceUtil;

    @Autowired
    public RecommendationServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        if (productId < 1) {
            throw new InvalidInputExceptions("Invalid ProductId: " + productId);
        }
        if (productId == 13) {
            log.debug("No recommendations found for productId: {}", productId);
            return new ArrayList<>();
        }
        return List.of(new Recommendation().setRecommendationId(1).setProductId(productId).setContent("Content 1").setAuthor("Author 1").setServiceAddress(serviceUtil.getServiceAddress()),
                new Recommendation().setRecommendationId(2).setProductId(productId).setContent("Content 2").setAuthor("Author 2").setServiceAddress(serviceUtil.getServiceAddress()),
                new Recommendation().setRecommendationId(3).setProductId(productId).setContent("Content 3").setAuthor("Author 3").setServiceAddress(serviceUtil.getServiceAddress()));
    }
}
