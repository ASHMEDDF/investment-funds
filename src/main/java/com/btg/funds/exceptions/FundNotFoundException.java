package com.btg.funds.exceptions;

public class FundNotFoundException extends RuntimeException{

    public FundNotFoundException(String message) {
        super(message);
    }

}
