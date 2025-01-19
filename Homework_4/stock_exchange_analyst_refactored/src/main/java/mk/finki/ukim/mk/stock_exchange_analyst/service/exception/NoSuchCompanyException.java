package mk.finki.ukim.mk.stock_exchange_analyst.service.exception;

public class NoSuchCompanyException extends RuntimeException{

    public NoSuchCompanyException(String message) {
        super(message);
    }
}
