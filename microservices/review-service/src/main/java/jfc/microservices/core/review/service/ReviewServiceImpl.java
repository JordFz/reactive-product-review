package jfc.microservices.core.review.service;

import jfc.microservices.api.core.review.Review;
import jfc.microservices.api.core.review.ReviewService;
import jfc.microservices.api.exceptions.InvalidInputExceptions;
import jfc.microservices.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ServiceUtil serviceUtil;

    @Autowired
    public ReviewServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getReviews(int productId) {
        if (productId < 1) {
            throw new InvalidInputExceptions("Invalid productId: " + productId);
        }
        if (productId == 13) {
            log.debug("No reviews found for productId: " + productId);
            return new ArrayList<>();
        }
        return List.of(
                new Review().setReviewId(1).setProductId(productId).setAuthor("Author 1").setContent("Content 1").setServiceAddress(serviceUtil.getServiceAddress()),
                new Review().setReviewId(2).setProductId(productId).setAuthor("Author 2").setContent("Content 2").setServiceAddress(serviceUtil.getServiceAddress()),
                new Review().setReviewId(3).setProductId(productId).setAuthor("Author 3").setContent("Content 3").setServiceAddress(serviceUtil.getServiceAddress())
        );
    }
}
