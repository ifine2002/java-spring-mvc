package vn.ifine.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.ifine.laptopshop.domain.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
