package vn.ifine.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.ifine.laptopshop.domain.Product;
import vn.ifine.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product handleSaveProduct(Product product) {
        Product newproduct = this.productRepository.save(product);
        return newproduct;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product getProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteAProduct(long id) {
        this.productRepository.deleteById(id);
    }

}
