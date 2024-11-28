package vn.ifine.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.ifine.laptopshop.domain.Product;
import vn.ifine.laptopshop.service.ProductService;
import vn.ifine.laptopshop.service.UploadService;

@Controller
public class ProductController {

    // DI: dependency injection
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model,
            @RequestParam("page") Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            } else {
                // page = 1 vì đã khai báo ở trên
            }
        } catch (Exception e) {
            // page = 1 vì đã khai báo ở trên
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Product> prs = this.productService.fetchProducts(pageable);
        List<Product> listProducts = prs.getContent();
        model.addAttribute("products", listProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prs.getTotalPages());
        return "admin/product/show";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping(value = "/admin/product/create")
    public String createProduct(Model model,
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        // List<FieldError> errors = newUserBindingResult.getFieldErrors();
        // for (FieldError error : errors) {
        // System.out.println(">>>>" + error.getField() + " - " +
        // error.getDefaultMessage());
        // }
        // Validate

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }
        String imgProduct = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(imgProduct);
        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}") // GET
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product currentProduct = productService.getProductById(id);
        model.addAttribute("currentProduct", currentProduct);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProduct(Model model, @ModelAttribute("currentProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        if (newProductBindingResult.hasErrors()) {
            // Lấy thông tin gốc của sản phẩm từ database
            Product currentProduct = productService.getProductById(product.getId());
            product.setImage(currentProduct.getImage()); // Giữ lại ảnh cũ
            return "admin/product/update";
        }

        Product currentProduct = productService.getProductById(product.getId());
        if (currentProduct != null) {
            // Cập nhật thông tin cơ bản
            currentProduct.setName(product.getName());
            currentProduct.setPrice(product.getPrice());
            currentProduct.setDetailDesc(product.getDetailDesc());
            currentProduct.setShortDesc(product.getShortDesc());
            currentProduct.setQuantity(product.getQuantity());
            currentProduct.setSold(product.getSold());
            currentProduct.setFactory(product.getFactory());
            currentProduct.setTarget(product.getTarget());
            // Xử lý avatar nếu có upload file mới
            if (!file.isEmpty()) {
                String image = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(image);
            }
            this.productService.handleSaveProduct(currentProduct);
        }
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}") // GET
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete") // GET
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product product) {
        this.productService.deleteAProduct(product.getId());
        return "redirect:/admin/product";
    }
}
