package dio.budgeting.application.output;

import java.math.BigDecimal;
import java.math.RoundingMode;

import dio.budgeting.domain.Transaction;

public record TransactionOutput(String id, String description, double value, String category) {

    public static TransactionOutput from(Transaction transaction) {
        return new TransactionOutput(
                transaction.getId().uuid().toString(),
                transaction.getDescription(),
                BigDecimal.valueOf(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                transaction.getCategory().name());
    }
}
