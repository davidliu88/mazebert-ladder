package com.mazebert.gateways.transaction;

public interface TransactionTask<Response> {
    Response run();
}
