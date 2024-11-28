package vn.ifine.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import vn.ifine.laptopshop.domain.User;
import vn.ifine.laptopshop.service.UploadService;
import vn.ifine.laptopshop.service.UserService;

@Controller
public class UserController {

    // DI: dependency injection
    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin/user")
    public String getUserPage(Model model, @RequestParam("page") Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            } else {
                // page = 1
            }
        } catch (Exception e) {
            // page = 1
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<User> pageUser = this.userService.getAllUsers(pageable);
        List<User> users = pageUser.getContent();
        System.out.println("check users: " + users);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageUser.getTotalPages());
        return "admin/user/show";
    }

    @GetMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create") // GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping(value = "/admin/user/create")
    public String createUser(Model model,
            @ModelAttribute("newUser") @Valid User hoidanit,
            BindingResult newUserBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        // List<FieldError> errors = newUserBindingResult.getFieldErrors();
        // for (FieldError error : errors) {
        // System.out.println(">>>>" + error.getField() + " - " +
        // error.getDefaultMessage());
        // }
        // Validate

        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(hoidanit.getPassword());
        hoidanit.setAvatar(avatar);
        hoidanit.setPassword(hashPassword);
        hoidanit.setRole(this.userService.getRoleByName(hoidanit.getRole().getName()));
        this.userService.handleSaveUser(hoidanit);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/update/{id}") // GET
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = userService.getUserById(id);
        model.addAttribute("currentUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("currentUser") User user,
            @RequestParam("hoidanitFile") MultipartFile file) {

        // if (newUserBindingResult.hasErrors()) {
        // // Lấy thông tin gốc của user từ database
        // User currentUser = userService.getUserById(user.getId());
        // user.setEmail(currentUser.getEmail()); // Giữ lại email
        // user.setPassword(currentUser.getPassword()); // Giữ lại password
        // model.addAttribute("currentUser", user);
        // return "admin/user/update";
        // }
        // // List<FieldError> errors = newUserBindingResult.getFieldErrors();
        // for (FieldError error : errors) {
        // System.out.println(">>>>" + error.getField() + " - " +
        // error.getDefaultMessage());
        // }
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
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}") // GET
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete") // GET
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User user) {
        this.userService.deleteAUser(user.getId());
        return "redirect:/admin/user";
    }

}