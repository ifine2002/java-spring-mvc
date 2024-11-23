package vn.ifine.laptopshop.repository;

import org.springframework.stereotype.Repository;

import vn.ifine.laptopshop.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByName(String name);
}
