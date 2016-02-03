package com.mazebert;

import com.mazebert.usecases.GetStatus;
import com.mazebert.usecases.player.CreateAccount;
import com.mazebert.usecases.player.GetPlayers;
import com.mazebert.usecases.GetVersion;
import com.mazebert.usecases.player.UpdateProgress;
import com.mazebert.usecases.security.SecureRequest;
import org.junit.Test;
import org.jusecase.Usecase;
import org.jusecase.UsecaseExecutorTest;

import static org.junit.Assert.assertTrue;

public class LogicTest extends UsecaseExecutorTest {

    @Test
    public void usecasesCanBeExecuted() {
        givenExecutor(Logic.instance);

        thenUsecaseCanBeExecuted(GetVersion.class);
        thenUsecaseCanBeExecuted(GetStatus.class);

        thenUsecaseCanBeExecuted(CreateAccount.class);
        thenUsecaseCanBeExecuted(UpdateProgress.class);
        thenUsecaseCanBeExecuted(GetPlayers.class);
    }

    @Test
    public void secureUsecases() {
        thenUsecaseIsSecured(CreateAccount.class);
        thenUsecaseIsSecured(UpdateProgress.class);
    }

    private void thenUsecaseIsSecured(Class<? extends Usecase> usecaseClass) {
        Class<?> requestClass = requestResolver.getRequestClass(usecaseClass);
        assertTrue("Request for " + usecaseClass.getName() + " needs to be annotated with @SecureRequest", requestClass.isAnnotationPresent(SecureRequest.class));
    }
}