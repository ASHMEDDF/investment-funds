package com.btg.funds.exceptions;

public class FundAlreadyExistsException extends RuntimeException {

    public FundAlreadyExistsException(String message) {
        super(message);
    }
}
