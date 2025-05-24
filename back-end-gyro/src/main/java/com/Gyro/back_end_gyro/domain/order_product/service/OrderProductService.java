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

        var order = orderService.existsOrderById(orderId);
        var product = productService.existsProductById(productId);

        thisIsAPossibleTransaction(product, orderQuantity);

        OrderProduct orderProduct = new OrderProduct(
                null,
                product,
                order,
                orderQuantity,
                product.getPrice() * orderQuantity);

        order.getOrderProducts().add(orderProduct);

        order.setPurchaseTotal(BigDecimal.valueOf(order.getOrderProducts().stream()
                        .mapToDouble(OrderProduct::getPriceAtPurchase)
                        .sum())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());


        order.setProductQuantity(order.getProductQuantity() + orderQuantity);
        calculatorTransactionChangeOrNo(order);
        calculatorTotalOfSalesAndRevenueForEmployee(order);
        calculatorCompanyTotalRevenue(order);


        return new OrderProductResponseDTO(orderProductRepository.save(orderProduct));


    }


    private void thisIsAPossibleTransaction(Product product, Integer orderQuantity) {

        if (product.getQuantity() < orderQuantity) {
            throw new ConflitException
                    ("A quantidade do produto %s impossibilita a conclusão da venda".formatted(product.getName())
                    );
        }

        product.setQuantity(product.getQuantity() - orderQuantity);


        product.setTotalSales(product.getTotalSales() + orderQuantity);

    }


    private void calculatorTransactionChangeOrNo(Order order) {

        if (order.getPaymentMethod() == PaymentMethod.CASH) {
            if (order.getCashForPayment() == order.getPurchaseTotal()) {
                order.setChange(order.getCashForPayment() - order.getPurchaseTotal());
                order.setHaveAChange(false);
            } else if (order.getCashForPayment() < order.getPurchaseTotal()) {
                throw new ConflitException("Dinheiro insuficiente");
            } else {
                order.setChange(order.getCashForPayment() - order.getPurchaseTotal());
                order.setHaveAChange(true);
            }
        } else {
            order.setHaveAChange(false);
            order.setChange(0.0);
            order.setCashForPayment(0.0);
        }
    }

    private void calculatorTotalOfSalesAndRevenueForEmployee(Order order) {

        Employee employee = order.getEmployee();

        Integer employeeSales = employee.getTotalSales();

        List<Order> orders = order.getCompany().getOrders();
        Double totalRevenueOfEmployee = 0.0;

        for (Order oIndex : orders) {
            if (oIndex.getEmployee().equals(employee)) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + oIndex.getPurchaseTotal());
                totalRevenueOfEmployee += oIndex.getPurchaseTotal();

                if (oIndex.getOrderProducts().size() == 1) {
                    employeeSales++;
                }

            }
        }

        employee.setTotalRevenue(totalRevenueOfEmployee);
        employee.setTotalSales(employeeSales);
    }


    private void calculatorCompanyTotalRevenue(Order order) {
        Company company = order.getCompany();
        List<Order> orders = company.getOrders();
        Double sumOfTotalRevenue = 0.0;

        for (Order oIndex : orders) {
            sumOfTotalRevenue += oIndex.getPurchaseTotal();

        }

        company.setTotalRevenuOfSales(sumOfTotalRevenue);
    }
}
