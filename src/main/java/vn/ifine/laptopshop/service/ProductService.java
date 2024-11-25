package vn.ifine.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.ifine.laptopshop.domain.Cart;
import vn.ifine.laptopshop.domain.CartDetail;
import vn.ifine.laptopshop.domain.Product;
import vn.ifine.laptopshop.domain.User;
import vn.ifine.laptopshop.repository.CartDetailRepository;
import vn.ifine.laptopshop.repository.CartRepository;
import vn.ifine.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
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

    public void handleAddProductToCart(String email, long productId) {
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            // check user đã có Cart chưa ? nếu chưa -> tạo mới
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                // Tạo mới cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(1);

                cart = this.cartRepository.save(otherCart);

            }
            // lưu cart_detail
            // Tìm product by id
            Product p = this.productRepository.findById(productId);
            if (p != null) {
                CartDetail cartDetail = new CartDetail();
                cartDetail.setCart(cart);
                cartDetail.setProduct(p);
                cartDetail.setPrice(p.getPrice());
                cartDetail.setQuatity(1);

                this.cartDetailRepository.save(cartDetail);
            }

        }
    }

}
