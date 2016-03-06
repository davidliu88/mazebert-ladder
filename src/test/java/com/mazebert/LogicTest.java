package com.mazebert;

import com.mazebert.error.Error;
import com.mazebert.error.InternalServerError;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.mysql.connection.StubDataSourceProvider;
import com.mazebert.gateways.transaction.TransactionRunner;
import com.mazebert.gateways.transaction.datasource.DataSourceTransactionManager;
import com.mazebert.plugins.message.EmailMessagePlugin;
import com.mazebert.plugins.security.GameContentVerifier;
import com.mazebert.plugins.security.ServerContentSigner;
import com.mazebert.plugins.system.SettingsPlugin;
import com.mazebert.plugins.system.mocks.EnvironmentPluginStub;
import com.mazebert.usecases.GetStatus;
import com.mazebert.usecases.GetVersion;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import com.mazebert.usecases.player.*;
import com.mazebert.usecases.quest.CompleteQuests;
import com.mazebert.usecases.quest.ReplaceQuest;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.SignServerResponse;
import com.mazebert.usecases.security.VerifyGameRequest;
import com.mazebert.usecases.security.VerifyRequest;
import com.mazebert.usecases.supporters.GetSupporters;
import org.junit.Test;
import org.jusecase.Usecase;
import org.jusecase.UsecaseExecutorTest;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class LogicTest extends UsecaseExecutorTest {
    private Error error;

    private static Logic testLogic;

    public static Logic getTestLogic() {
        if (testLogic == null) {
            testLogic = new Logic(StubDataSourceProvider.class, EnvironmentPluginStub.class);
        }
        return testLogic;
    }

    @Test
    public void usecasesCanBeExecuted() {
        givenTestLogic();

        thenUsecaseCanBeExecuted(VerifyGameRequest.class);
        thenUsecaseCanBeExecuted(SignServerResponse.class);
        thenUsecaseCanBeExecuted(GetVersion.class);
        thenUsecaseCanBeExecuted(GetStatus.class);

        thenUsecaseCanBeExecuted(CreateAccount.class);
        thenUsecaseCanBeExecuted(UpdateProgress.class);
        thenUsecaseCanBeExecuted(GetPlayers.class);
        thenUsecaseCanBeExecuted(GetPlayer.class);
        thenUsecaseCanBeExecuted(SynchronizePlayer.class);
        thenUsecaseCanBeExecuted(ForgotSavecode.class);
        thenUsecaseCanBeExecuted(RegisterEmail.class);
        thenUsecaseCanBeExecuted(GetPlayerProfile.class);

        thenUsecaseCanBeExecuted(TradeDuplicateCards.class);

        thenUsecaseCanBeExecuted(GetBonusTimes.class);
        thenUsecaseCanBeExecuted(UpdateBonusTime.class);

        thenUsecaseCanBeExecuted(CompleteQuests.class);
        thenUsecaseCanBeExecuted(ReplaceQuest.class);

        thenUsecaseCanBeExecuted(GetSupporters.class);
    }

    @Test
    public void verifiedRequests() {
        thenRequestIsVerified(CreateAccount.class);
        thenRequestIsVerified(UpdateProgress.class);
        thenRequestIsVerified(ForgotSavecode.class);
        thenRequestIsVerified(RegisterEmail.class);
        thenRequestIsVerified(UpdateBonusTime.class);
        thenRequestIsVerified(CompleteQuests.class);
        thenRequestIsVerified(ReplaceQuest.class);
        thenRequestIsVerified(TradeDuplicateCards.class);
    }

    @Test
    public void signedResponses() {
        thenResponseIsSigned(SynchronizePlayer.class);
        thenResponseIsSigned(ReplaceQuest.class);
        thenResponseIsSigned(TradeDuplicateCards.class);
    }

    @Test
    public void singletons() {
        givenTestLogic();
        thenOnlyOneInstanceExists(SettingsPlugin.class);
        thenOnlyOneInstanceExists(GameContentVerifier.class);
        thenOnlyOneInstanceExists(ServerContentSigner.class);
        thenOnlyOneInstanceExists(EmailMessagePlugin.class);
        thenOnlyOneInstanceExists(TransactionRunner.class, DataSourceTransactionManager.class);
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
        givenExecutor(getTestLogic());
    }

    private Logic logic() {
        return (Logic)executor;
    }

    private void thenRequestIsVerified(Class<? extends Usecase> usecaseClass) {
        Class<?> requestClass = requestResolver.getRequestClass(usecaseClass);
        assertTrue("Request for " + usecaseClass.getName() + " needs to be annotated with @VerifyRequest", requestClass.isAnnotationPresent(VerifyRequest.class));
    }

    private void thenResponseIsSigned(Class<? extends Usecase> usecaseClass) {
        Class<?> requestClass = requestResolver.getRequestClass(usecaseClass);
        assertTrue("Request for " + usecaseClass.getName() + " needs to be annotated with @SignResponse", requestClass.isAnnotationPresent(SignResponse.class));
    }

    private void thenOnlyOneInstanceExists(Class<?> clazz) {
        assertSame(logic().getInstance(clazz), logic().getInstance(clazz));
    }

    private void thenOnlyOneInstanceExists(Class<?> clazz1, Class<?> clazz2) {
        assertSame(logic().getInstance(clazz1), logic().getInstance(clazz1));
        assertSame(logic().getInstance(clazz2), logic().getInstance(clazz2));
        assertSame(logic().getInstance(clazz1), logic().getInstance(clazz2));
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