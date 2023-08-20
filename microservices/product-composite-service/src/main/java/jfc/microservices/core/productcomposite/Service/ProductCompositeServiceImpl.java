package jfc.microservices.core.productcomposite.Service;

import jfc.microservices.api.composite.product.ProductAgregate;
import jfc.microservices.api.composite.product.ProductCompositeService;
import jfc.microservices.api.composite.product.RecommendationSummary;
import jfc.microservices.api.composite.product.ReviewSummary;
import jfc.microservices.api.composite.product.ServiceAddresses;
import jfc.microservices.api.core.product.Product;
import jfc.microservices.api.core.recommendation.Recommendation;
import jfc.microservices.api.core.review.Review;
import jfc.microservices.api.exceptions.NotFoundException;
import jfc.microservices.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
    private final ServiceUtil serviceUtil;
    private ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public ProductAgregate getProduct(int productId) {
        Product product = integration.getProduct(productId);
        if (product == null) {
            throw new NotFoundException("No product found for productId: " + productId);
        }
        List<Recommendation> recommendations = integration.getRecommendations(productId);
        List<Review> reviews = integration.getReviews(productId);
        return createProductAgregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }

    private ProductAgregate createProductAgregate(Product product, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress) {
        // 1. setup product info
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // 2. Copy summary recommendation info, if available
        List<RecommendationSummary> recommendationSummaries = (recommendations == null) ? null : recommendations.stream()
                .map(rec -> new RecommendationSummary().setRecommendationId(rec.getRecommendationId()).setAuthor(rec.getAuthor()).setRate(rec.getRate()))
                .collect(Collectors.toList());

        // 3. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries = (reviews == null) ? null : reviews.stream()
                .map(r ->  new ReviewSummary().setReviewId(r.getReviewId()).setAuthor(r.getAuthor()).setSubject(r.getSubject()))
                .collect(Collectors.toList());

        // 4. Create info regarding the involved microservices address
        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
        ServiceAddresses serviceAddresses = new ServiceAddresses().setCmp(serviceAddress).setPro(productAddress).setRec(recommendationAddress).setRev(reviewAddress);

        return new ProductAgregate()
                .setProductId(productId)
                .setName(name)
                .setWeight(weight)
                .setRecommendations(recommendationSummaries)
                .setReviews(reviewSummaries)
                .setServiceAddresses(serviceAddresses);
    }
}
