package com.mazebert.gateways.transaction;

public interface TransactionRunner {
    void runAsTransaction(Runnable task);
    <Value> Value runAsTransaction(TransactionTask<Value> task);
}
