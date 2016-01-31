package com.mazebert;

import com.mazebert.usecases.CreateAccount;
import org.junit.Test;
import org.jusecase.UsecaseExecutorTest;

public class LogicTest extends UsecaseExecutorTest {

    @Test
    public void usecasesCanBeExecuted() {
        givenExecutor(Logic.instance);

        thenUsecaseCanBeExecuted(CreateAccount.class);
    }
}