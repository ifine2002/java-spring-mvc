package vn.ifine.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.ifine.laptopshop.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
