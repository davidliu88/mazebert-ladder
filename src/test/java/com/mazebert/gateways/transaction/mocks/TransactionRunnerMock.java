package com.mazebert.gateways.transaction.mocks;

import com.mazebert.gateways.transaction.TransactionError;
import com.mazebert.gateways.transaction.TransactionRunner;
import com.mazebert.gateways.transaction.TransactionTask;

public class TransactionRunnerMock implements TransactionRunner {
    private Object currentTask;

    @Override
    public void runAsTransaction(Runnable task) {
        beforeTask(task);
        task.run();
        afterTask();
    }

    @Override
    public <Value> Value runAsTransaction(TransactionTask<Value> task) {
        beforeTask(task);
        Value result = task.run();
        afterTask();

        return result;
    }

    private void beforeTask(Object task) {
        if (currentTask != null) {
            throw new TransactionError("Nested transactions are not supported!");
        }

        currentTask = task;
    }

    private void afterTask() {
        currentTask = null;
    }
}
