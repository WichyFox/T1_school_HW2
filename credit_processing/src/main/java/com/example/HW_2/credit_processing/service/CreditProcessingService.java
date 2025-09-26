package com.example.HW2.credit_processing.service;

import com.example.HW2.credit_processing.dto.CreditRequestDto;
import com.example.HW2.credit_processing.entity.PaymentRegistry;
import com.example.HW2.credit_processing.entity.ProductRegistry;
import com.example.HW2.credit_processing.repository.PaymentRegistryRepository;
import com.example.HW2.credit_processing.repository.ProductRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditProcessingService {

    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;

    @Value("${credit.limit}")
    private BigDecimal creditLimit;

    public boolean isLimitExceeded(Long clientId, BigDecimal newAmount) {
        List<ProductRegistry> products = productRegistryRepository.findByClientId(clientId);

        BigDecimal total = products.stream()
                .map(ProductRegistry::getInterestRate) // ⚠️ здесь у тебя, скорее всего, будет поле "amount" или "debtAmount"
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.add(newAmount).compareTo(creditLimit) > 0;
    }

    public boolean hasOverduePayments(Long clientId) {
        List<ProductRegistry> products = productRegistryRepository.findByClientId(clientId);

        for (ProductRegistry product : products) {
            List<PaymentRegistry> payments = paymentRegistryRepository.findByProductRegistryId(product.getId());
            boolean overdue = payments.stream().anyMatch(PaymentRegistry::getExpired);
            if (overdue) return true;
        }
        return false;
    }

    public void openCreditProduct(CreditRequestDto request) {
        // Создаём запись о кредите
        ProductRegistry product = ProductRegistry.builder()
                .clientId(request.getClientId())
                .productId(request.getProductId())
                .accountId(null) // можно связать позже, если надо
                .interestRate(request.getInterestRate())
                .openDate(LocalDate.now())
                .monthCount(request.getMonthCount())
                .build();

        product = productRegistryRepository.save(product);

        // Формула аннуитета:
        // A = S × [i × (1 + i)^n] / [(1 + i)^n - 1]
        BigDecimal loanAmount = request.getAmount();
        BigDecimal annualRate = request.getInterestRate(); // например 0.22 (22%)
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        int months = request.getMonthCount();

        BigDecimal onePlusIToN = (BigDecimal.ONE.add(monthlyRate)).pow(months);
        BigDecimal numerator = loanAmount.multiply(monthlyRate).multiply(onePlusIToN);
        BigDecimal denominator = onePlusIToN.subtract(BigDecimal.ONE);
        BigDecimal annuityPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        // Генерируем график платежей
        List<PaymentRegistry> payments = new ArrayList<>();
        BigDecimal remainingDebt = loanAmount;

        for (int month = 1; month <= months; month++) {
            LocalDate paymentDate = LocalDate.now().plusMonths(month);

            // проценты за месяц = остаток долга × месячная ставка
            BigDecimal interestPart = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);

            // основной долг = платёж − проценты
            BigDecimal principalPart = annuityPayment.subtract(interestPart).setScale(2, RoundingMode.HALF_UP);

            // уменьшаем остаток
            remainingDebt = remainingDebt.subtract(principalPart).setScale(2, RoundingMode.HALF_UP);

            PaymentRegistry payment = PaymentRegistry.builder()
                    .productRegistryId(product.getId())
                    .paymentDate(paymentDate)
                    .amount(annuityPayment)
                    .interestRateAmount(interestPart)
                    .debtAmount(principalPart)
                    .expired(false)
                    .paymentExpirationDate(paymentDate.plusDays(5)) // например, 5 дней на оплату
                    .build();

            payments.add(payment);
        }

        paymentRegistryRepository.saveAll(payments);
    }
}
