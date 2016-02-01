package com.mazebert;

import com.mazebert.usecases.CreateAccount;
import com.mazebert.usecases.GetVersion;
import org.junit.Test;
import org.jusecase.UsecaseExecutorTest;

public class LogicTest extends UsecaseExecutorTest {

    @Test
    public void usecasesCanBeExecuted() {
        givenExecutor(Logic.instance);

        thenUsecaseCanBeExecuted(GetVersion.class);
        thenUsecaseCanBeExecuted(CreateAccount.class);
    }
}