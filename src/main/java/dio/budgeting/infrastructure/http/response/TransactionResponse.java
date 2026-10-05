package dio.budgeting.infrastructure.http.response;

import dio.budgeting.application.output.TransactionOutput;

/**
 * TransactionResponse
 */
public record TransactionResponse(String id, String description, double value, String category) {

    public static TransactionResponse from(TransactionOutput output) {
        return new TransactionResponse(output.id(), output.description(), output.value(), output.category());
    }
}
