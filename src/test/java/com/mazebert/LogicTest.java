package com.mazebert;

import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.usecases.GetStatus;
import com.mazebert.usecases.player.CreateAccount;
import com.mazebert.usecases.player.GetPlayers;
import com.mazebert.usecases.GetVersion;
import com.mazebert.usecases.player.UpdateProgress;
import com.mazebert.usecases.security.SecureRequest;
import org.junit.Test;
import org.jusecase.Usecase;
import org.jusecase.UsecaseExecutorTest;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    @Test
    public void gatewayErrorInUsecase() {
        Error error = null;
        Logic logic = new Logic();
        logic.addUsecase(ThrowingUsecase.class);

        try {
            logic.execute(new ThrowingUsecase.Request());
        } catch (Error e) {
            error = e;
        }

        assertNotNull(error);
        assertEquals(Type.INTERNAL_SERVER_ERROR, error.getType());
        assertEquals("Something went wrong down here in the gateway.", error.getMessage());
        assertEquals(SQLException.class, error.getCause().getClass());
    }

    @Test
    public void noErrorInUsecase() {
        Logic logic = new Logic();
        logic.addUsecase(GoodUsecase.class);

        assertEquals("ok", logic.execute(new GoodUsecase.Request()));
    }

    private void thenUsecaseIsSecured(Class<? extends Usecase> usecaseClass) {
        Class<?> requestClass = requestResolver.getRequestClass(usecaseClass);
        assertTrue("Request for " + usecaseClass.getName() + " needs to be annotated with @SecureRequest", requestClass.isAnnotationPresent(SecureRequest.class));
    }

    private static class ThrowingUsecase implements Usecase<ThrowingUsecase.Request, Void> {
        public static class Request {}

        @Override
        public Void execute(Request request) {
            throw new GatewayError("Something went wrong down here in the gateway.", new SQLException());
        }
    }

    private static class GoodUsecase implements Usecase<GoodUsecase.Request, String> {
        public static class Request {}

        @Override
        public String execute(Request request) {
            return "ok";
        }
    }
}