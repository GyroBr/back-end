package com.Gyro.back_end_gyro.domain.order_combo_product.service;

import com.Gyro.back_end_gyro.domain.combo.entity.Combo;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.combo_product.entity.ComboProduct;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.order.entity.Order;
import com.Gyro.back_end_gyro.domain.order.enums.PaymentMethod;
import com.Gyro.back_end_gyro.domain.order_combo_product.dto.OrderComboProductResponseDTO;
import com.Gyro.back_end_gyro.domain.order_combo_product.entity.OrderComboProduct;
import com.Gyro.back_end_gyro.domain.order_combo_product.repository.OrderComboProductRepository;
import com.Gyro.back_end_gyro.domain.order_product.entity.OrderProduct;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.ConflitException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderComboProductService {

    private final OrderComboProductRepository orderComboProductRepository;

    public OrderComboProductResponseDTO create(Order order, Combo combo) {
        double comboPrice = combo.getPriceByStore();

        double newTotal = BigDecimal.valueOf(order.getOrderProducts().stream()
                        .mapToDouble(OrderProduct::getPriceAtPurchase)
                        .sum() + order.getOrderComboProducts().stream()
                        .mapToDouble(c -> c.getComboProduct().getCombo().getPriceByStore())
                        .sum() + comboPrice)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        validateTransaction(order, comboPrice);

        for (ComboProduct comboProduct : combo.getComboProducts()) {
            validateComboProductStock(comboProduct);
            OrderComboProduct orderComboProduct = new OrderComboProduct(order, comboProduct);
            order.getOrderComboProducts().add(orderComboProduct);
            orderComboProductRepository.save(orderComboProduct);
        }

        int totalProducts = combo.getComboProducts().stream().mapToInt(ComboProduct::getProductQuantity).sum();

        order.setPurchaseTotal(newTotal);
        order.setProductQuantity(order.getProductQuantity() + totalProducts);

        updateEmployeeRevenue(order);
        updateCompanyRevenue(order);

        return new OrderComboProductResponseDTO(order,combo);
    }


    private void validateComboProductStock(ComboProduct comboProduct) {
        if (comboProduct.getProduct().getQuantity() < comboProduct.getProductQuantity()) {
            throw new ConflitException(
                    "A quantidade do combo %s impossibilita a conclusão da venda"
                            .formatted(comboProduct.getCombo().getComboName())
            );
        }

        comboProduct.getProduct().setQuantity(comboProduct.getProduct().getQuantity() - comboProduct.getProductQuantity());
        comboProduct.getProduct().setTotalSales(comboProduct.getProduct().getTotalSales() + comboProduct.getProductQuantity());
    }


    private void validateTransaction(Order order, double comboPrice) {
        if (order.getPaymentMethod() == PaymentMethod.CASH) {
            if (order.getCashForPayment() < comboPrice) {
                throw new ConflitException("Dinheiro insuficiente para a compra");
            }
            order.setChange(order.getCashForPayment() - comboPrice);
            order.setHaveAChange(order.getChange() > 0);
        } else {
            order.setHaveAChange(false);
            order.setChange(0.0);
            order.setCashForPayment(0.0);
        }
    }


    private void updateEmployeeRevenue(Order order) {
        Employee employee = order.getEmployee();
        List<Order> orders = order.getCompany().getOrders();

        double totalRevenueOfEmployee = orders.stream()
                .filter(o -> o.getEmployee().equals(employee))
                .mapToDouble(Order::getPurchaseTotal)
                .sum();

        employee.setTotalRevenue(totalRevenueOfEmployee);
        employee.setTotalSales(employee.getTotalSales() + 1);
    }


    private void updateCompanyRevenue(Order order) {
        Company company = order.getCompany();
        double totalRevenue = company.getOrders().stream()
                .mapToDouble(Order::getPurchaseTotal)
                .sum();

        company.setTotalRevenuOfSales(totalRevenue);
    }

}
