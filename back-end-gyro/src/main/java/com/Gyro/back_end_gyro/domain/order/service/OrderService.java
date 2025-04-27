package com.Gyro.back_end_gyro.domain.order.service;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.order.dto.OrderRequestDTO;
import com.Gyro.back_end_gyro.domain.order.dto.OrderResponseDTO;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.order.enums.PaymentMethod;
import com.Gyro.back_end_gyro.domain.order.repository.OrderRepository;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.ConflitException;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public OrderResponseDTO createOrder(Employee employee, OrderRequestDTO orderRequestDTO) {
        Order order = new Order(employee, orderRequestDTO);

        if (orderRequestDTO.paymentMethod() != PaymentMethod.CASH && orderRequestDTO.cashForPayment() != null) {
            throw new ConflitException("Passe o valor de pagamento registrado pelo cliente apenas em caso do método de pagamento ser dinheiro");
        }

        return new OrderResponseDTO(orderRepository.save(order));
    }

    public List<OrderResponseDTO> getAllOrdersByCompany(Company company) {
        return orderRepository.findAllByCompanyId(company.getId())
                .stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
    }


    public OrderResponseDTO getById(Long id) {
        return new OrderResponseDTO(existsOrderById(id));
    }


    public Order existsOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }
}
