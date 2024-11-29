package vn.ifine.laptopshop.controller.client;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.ifine.laptopshop.domain.Order;
import vn.ifine.laptopshop.domain.Product;
import vn.ifine.laptopshop.domain.User;
import vn.ifine.laptopshop.domain.dto.RegisterDTO;
import vn.ifine.laptopshop.service.OrderService;
import vn.ifine.laptopshop.service.ProductService;
import vn.ifine.laptopshop.service.UploadService;
import vn.ifine.laptopshop.service.UserService;

@Controller
public class HomePageController {
    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;
    private final UploadService uploadService;

    public HomePageController(ProductService productService,
            UserService userService, PasswordEncoder passwordEncoder, OrderService orderService,
            UploadService uploadService) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
        this.uploadService = uploadService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> prs = this.productService.fetchProducts(pageable);
        List<Product> products = prs.getContent();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult bindingResult) {
        // List<FieldError> errors = bindingResult.getFieldErrors();
        // for (FieldError error : errors) {
        // System.out.println(">>>>" + error.getField() + " - " +
        // error.getDefaultMessage());
        // }
        if (bindingResult.hasErrors()) {
            return "client/auth/register";
        }
        User user = this.userService.registerDTOtoUser(registerDTO);
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        user.setRole(this.userService.getRoleByName("USER"));
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "client/auth/login";
    }

    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {
        return "client/auth/deny";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);
        List<Order> orders = this.orderService.fetchOrderByUser(currentUser);
        model.addAttribute("orders", orders);
        return "client/cart/order-history";
    }

    @GetMapping("/account")
    public String getSettingAccountPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("currentUser", currentUser);
        return "client/auth/account";
    }

    @PostMapping("/account")
    public String postSettingAccountPage(Model model, @ModelAttribute("currentUser") User user,
            @RequestParam("hoidanitFile") MultipartFile file, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        User currentUser = userService.getUserById(user.getId());
        if (currentUser != null) {
            // Cập nhật thông tin cơ bản
            currentUser.setFullName(user.getFullName());
            currentUser.setAddress(user.getAddress());
            currentUser.setPhone(user.getPhone());
            currentUser.setRole(this.userService.getRoleByName(user.getRole().getName()));
            // Xử lý avatar nếu có upload file mới
            if (!file.isEmpty()) {
                String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
                currentUser.setAvatar(avatar);
            }

            // Lưu người dùng và cập nhật session
            User savedUser = this.userService.handleSaveUser(currentUser);

            // Cập nhật các thuộc tính trong session
            session.setAttribute("user", savedUser);
            session.setAttribute("fullName", savedUser.getFullName());
            session.setAttribute("avatar", savedUser.getAvatar());
            session.setAttribute("id", savedUser.getId());
            session.setAttribute("email", savedUser.getEmail());
        }
        return "redirect:/account";
    }
}
