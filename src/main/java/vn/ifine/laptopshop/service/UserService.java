package vn.ifine.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.ifine.laptopshop.domain.Role;
import vn.ifine.laptopshop.domain.User;
import vn.ifine.laptopshop.domain.dto.RegisterDTO;
import vn.ifine.laptopshop.repository.RoleRepository;
import vn.ifine.laptopshop.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public List<User> getAllUsersByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User handleSaveUser(User user) {
        User hoidanit = this.userRepository.save(user);
        System.out.println(hoidanit);
        return hoidanit;
    }

    public User getUserById(long id) {
        User user = this.userRepository.findById(id);
        return user;
    }

    public void deleteAUser(long id) {
        this.userRepository.deleteById(id);
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public User registerDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;
    }
}
