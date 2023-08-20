package jfc.microservices.core.product.service;

import jfc.microservices.api.core.product.Product;
import jfc.microservices.api.core.product.ProductService;
import jfc.microservices.api.exceptions.InvalidInputExceptions;
import jfc.microservices.api.exceptions.NotFoundException;
import jfc.microservices.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        log.debug("/product return the found product for productId = {}", productId);
        if (productId < 1) {
            throw new InvalidInputExceptions("Invalid product id: " + productId);
        }
        if (productId == 13) {
            throw new NotFoundException("No product found for productId: " + productId);
        }
        return new Product().setName("name-" + productId).setProductId(productId).setWeight(123).setServiceAddress(serviceUtil.getServiceAddress());
    }
}
