package com.mazebert;

import com.mazebert.error.Error;
import com.mazebert.error.InternalServerError;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.mysql.connection.FakeDataSourceProvider;
import com.mazebert.usecases.GetStatus;
import com.mazebert.usecases.GetVersion;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import com.mazebert.usecases.player.*;
import com.mazebert.usecases.quest.CompleteQuests;
import com.mazebert.usecases.quest.ReplaceQuest;
import com.mazebert.usecases.security.SecureRequest;
import org.junit.Test;
import org.jusecase.Usecase;
import org.jusecase.UsecaseExecutorTest;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class LogicTest extends UsecaseExecutorTest {
    private Error error;

    @Test
    public void usecasesCanBeExecuted() {
        givenTestLogic();

        thenUsecaseCanBeExecuted(GetVersion.class);
        thenUsecaseCanBeExecuted(GetStatus.class);

        thenUsecaseCanBeExecuted(CreateAccount.class);
        thenUsecaseCanBeExecuted(UpdateProgress.class);
        thenUsecaseCanBeExecuted(GetPlayers.class);
        thenUsecaseCanBeExecuted(GetPlayer.class);
        thenUsecaseCanBeExecuted(SynchronizePlayer.class);
        thenUsecaseCanBeExecuted(ForgotSavecode.class);
        thenUsecaseCanBeExecuted(RegisterEmail.class);

        thenUsecaseCanBeExecuted(GetBonusTimes.class);
        thenUsecaseCanBeExecuted(UpdateBonusTime.class);

        thenUsecaseCanBeExecuted(CompleteQuests.class);
        thenUsecaseCanBeExecuted(ReplaceQuest.class);
    }

    @Test
    public void secureUsecases() {
        thenUsecaseIsSecured(CreateAccount.class);
        thenUsecaseIsSecured(UpdateProgress.class);
        thenUsecaseIsSecured(ForgotSavecode.class);
        thenUsecaseIsSecured(RegisterEmail.class);
        thenUsecaseIsSecured(UpdateBonusTime.class);
        thenUsecaseIsSecured(CompleteQuests.class);
        thenUsecaseIsSecured(ReplaceQuest.class);
    }

    @Test
    public void gatewayErrorInUsecase() {
        givenTestLogic();
        logic().addUsecase(ThrowingUsecase.class);

        try {
            logic().execute(new ThrowingUsecase.Request());
        } catch (Error e) {
            error = e;
        }

        assertNotNull(error);
        assertEquals(InternalServerError.class, error.getClass());
        assertEquals("Something went wrong down here in the gateway.", error.getMessage());
        assertEquals(SQLException.class, error.getCause().getClass());
    }

    @Test
    public void noErrorInUsecase() {
        givenTestLogic();
        logic().addUsecase(GoodUsecase.class);

        assertEquals("ok", logic().execute(new GoodUsecase.Request()));
    }

    private void givenTestLogic() {
        givenExecutor(new Logic(FakeDataSourceProvider.class));
    }

    private Logic logic() {
        return (Logic)executor;
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