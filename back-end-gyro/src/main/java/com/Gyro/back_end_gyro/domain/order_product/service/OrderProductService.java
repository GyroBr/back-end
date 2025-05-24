package com.Gyro.back_end_gyro.domain.order_product.service;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.order.enums.PaymentMethod;
import com.Gyro.back_end_gyro.domain.order.service.OrderService;
import com.Gyro.back_end_gyro.domain.order_product.dto.OrderProductResponseDTO;
import com.Gyro.back_end_gyro.domain.order_product.entity.OrderProduct;
import com.Gyro.back_end_gyro.domain.order_product.repository.OrderProductRepository;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.service.ProductService;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.ConflitException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderService orderService;
    private final ProductService productService;
    private final OrderProductRepository orderProductRepository;

    public OrderProductResponseDTO create(Long orderId, Long productId, Integer orderQuantity) {
        Order order = orderService.existsOrderById(orderId);
        Product product = productService.existsProductAndCompany(productId,order.getCompany());

        validateProductCompany(order, product);
        validateStock(product, orderQuantity);

        OrderProduct orderProduct = createOrderProduct(order, product, orderQuantity);
        updateOrderDetails(order, orderProduct);
        updateFinancialMetrics(order);

        return new OrderProductResponseDTO(orderProductRepository.save(orderProduct));
    }

    private void validateProductCompany(Order order, Product product) {
        if (!product.getCompany().getId().equals(order.getCompany().getId())) {
            throw new ConflitException("Produto não pertence à empresa do pedido");
        }
    }

    private void validateStock(Product product, Integer orderQuantity) {
        if (product.getQuantity() < orderQuantity) {
            throw new ConflitException(
                    "Quantidade insuficiente do produto %s".formatted(product.getName())
            );
        }
    }

    private OrderProduct createOrderProduct(Order order, Product product, Integer quantity) {
        double totalPrice = calculateTotalPrice(product.getPrice(), quantity);

        return OrderProduct.builder()
                .product(product)
                .order(order)
                .orderQuantity(quantity)
                .priceAtPurchase(totalPrice)
                .build();
    }

    private double calculateTotalPrice(Double unitPrice, Integer quantity) {
        return BigDecimal.valueOf(unitPrice * quantity)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private void updateOrderDetails(Order order, OrderProduct orderProduct) {
        order.getOrderProducts().add(orderProduct);
        order.setProductQuantity(order.getProductQuantity() + orderProduct.getOrderQuantity());
        order.setPurchaseTotal(calculateOrderTotal(order));
    }

    private double calculateOrderTotal(Order order) {
        return order.getOrderProducts().stream()
                .mapToDouble(OrderProduct::getPriceAtPurchase)
                .sum();
    }

    private void updateFinancialMetrics(Order order) {
        updateProductStock(order);
        calculateTransactionChange(order);
        updateEmployeeSalesMetrics(order);
        updateCompanyRevenue(order);
    }

    private void updateProductStock(Order order) {
        order.getOrderProducts().forEach(op -> {
            Product product = op.getProduct();
            product.setQuantity(product.getQuantity() - op.getOrderQuantity());
            product.setTotalSales(product.getTotalSales() + op.getOrderQuantity());
        });
    }

    private void calculateTransactionChange(Order order) {
        if (order.getPaymentMethod() == PaymentMethod.CASH) {
            handleCashPayment(order);
        } else {
            resetNonCashPaymentFields(order);
        }
    }

    private void handleCashPayment(Order order) {
        double difference = order.getCashForPayment() - order.getPurchaseTotal();

        if (difference < 0) {
            throw new ConflitException("Dinheiro insuficiente");
        }

        order.setChange(Math.abs(difference));
        order.setHaveAChange(difference > 0);
    }

    private void resetNonCashPaymentFields(Order order) {
        order.setHaveAChange(false);
        order.setChange(0.0);
        order.setCashForPayment(0.0);
    }

    private void updateEmployeeSalesMetrics(Order order) {
        Employee employee = order.getEmployee();
        List<Order> employeeOrders = filterOrdersByEmployee(order.getCompany().getOrders(), employee);

        double totalRevenue = calculateTotalRevenue(employeeOrders);
        int totalSales = calculateTotalSales(employeeOrders);

        employee.setTotalRevenue(totalRevenue);
        employee.setTotalSales(totalSales);
    }

    private List<Order> filterOrdersByEmployee(List<Order> orders, Employee employee) {
        return orders.stream()
                .filter(o -> o.getEmployee().equals(employee))
                .toList();
    }

    private double calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
                .mapToDouble(Order::getPurchaseTotal)
                .sum();
    }

    private int calculateTotalSales(List<Order> orders) {
        return (int) orders.stream()
                .flatMap(o -> o.getOrderProducts().stream())
                .count();
    }

    private void updateCompanyRevenue(Order order) {
        Company company = order.getCompany();
        double totalRevenue = calculateTotalRevenue(company.getOrders());
        company.setTotalRevenuOfSales(totalRevenue);
    }
}