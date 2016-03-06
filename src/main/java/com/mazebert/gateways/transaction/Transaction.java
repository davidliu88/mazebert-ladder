package com.mazebert.gateways.transaction;

public interface Transaction {
    void rollback();
    void commit();
}
