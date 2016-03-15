package com.mazebert;

import com.mazebert.error.BadRequest;
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
import com.mazebert.plugins.system.mocks.LoggerMock;
import com.mazebert.usecases.shop.CommitShopTransactions;
import com.mazebert.usecases.system.GetStatus;
import com.mazebert.usecases.system.GetVersion;
import com.mazebert.usecases.blackmarket.BuyBlackMarketOffer;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import com.mazebert.usecases.player.*;
import com.mazebert.usecases.quest.CompleteQuests;
import com.mazebert.usecases.quest.ReplaceQuest;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.SignServerResponse;
import com.mazebert.usecases.security.VerifyGameRequest;
import com.mazebert.usecases.security.VerifyRequest;
import com.mazebert.usecases.shop.PrepareShopTransaction;
import com.mazebert.usecases.supporters.GetSupporters;
import org.junit.Test;
import org.jusecase.Usecase;
import org.jusecase.UsecaseExecutorTest;

import java.sql.SQLException;
import java.util.logging.Level;

import static org.junit.Assert.*;

public class BusinessLogicTest extends UsecaseExecutorTest {
    private Error error;

    private static BusinessLogic testBusinessLogic;
    private static LoggerMock logger = new LoggerMock();

    public static BusinessLogic getTestBusinessLogic() {
        if (testBusinessLogic == null) {
            testBusinessLogic = new BusinessLogic(
                    StubDataSourceProvider.class,
                    EnvironmentPluginStub.class,
                    logger.getLogger());

            testBusinessLogic.addUsecase(ThrowingUsecase.class);
        }
        return testBusinessLogic;
    }

    @Test
    public void usecasesCanBeExecuted() {
        givenTestLogic();

        thenSystemUsecasesCanBeExecuted();
        thenSecurityUsecasesCanBeExecuted();
        thenPlayerUsecasesCanBeExecuted();
        thenTradeUsecasesCanBeExecuted();
        thenBlackMarketUsecasesCanBeExecuted();
        thenBonusTimeUsecasesCanBeExecuted();
        thenQuestUsecasesCanBeExecuted();
        thenSupporterUsecasesCanBeExecuted();
        thenShopUsecasesCanBeExecuted();
    }

    private void thenSystemUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(GetVersion.class);
        thenUsecaseCanBeExecuted(GetStatus.class);
    }

    private void thenSecurityUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(VerifyGameRequest.class);
        thenUsecaseCanBeExecuted(SignServerResponse.class);
    }

    private void thenPlayerUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(CreateAccount.class);
        thenUsecaseCanBeExecuted(UpdateProgress.class);
        thenUsecaseCanBeExecuted(GetPlayers.class);
        thenUsecaseCanBeExecuted(GetPlayer.class);
        thenUsecaseCanBeExecuted(SynchronizePlayer.class);
        thenUsecaseCanBeExecuted(ForgotSavecode.class);
        thenUsecaseCanBeExecuted(RegisterEmail.class);
        thenUsecaseCanBeExecuted(GetPlayerProfile.class);
        thenUsecaseCanBeExecuted(ForgeCard.class);
    }

    private void thenTradeUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(TradeDuplicateCards.class);
    }

    private void thenBlackMarketUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(BuyBlackMarketOffer.class);
    }

    private void thenBonusTimeUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(GetBonusTimes.class);
        thenUsecaseCanBeExecuted(UpdateBonusTime.class);
    }

    private void thenQuestUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(CompleteQuests.class);
        thenUsecaseCanBeExecuted(ReplaceQuest.class);
    }

    private void thenSupporterUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(GetSupporters.class);
    }

    private void thenShopUsecasesCanBeExecuted() {
        thenUsecaseCanBeExecuted(PrepareShopTransaction.class);
        thenUsecaseCanBeExecuted(CommitShopTransactions.class);
    }

    @Override
    public void thenUsecaseCanBeExecuted(Class<?> usecaseClass) {
        super.thenUsecaseCanBeExecuted(usecaseClass);

        // Usecases are stateless anyways. So we can register them as singletons.
        thenOnlyOneInstanceExists(usecaseClass);
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
        thenRequestIsVerified(BuyBlackMarketOffer.class);
        thenRequestIsVerified(ForgeCard.class);
        thenRequestIsVerified(PrepareShopTransaction.class);
        thenRequestIsVerified(CommitShopTransactions.class);
    }

    @Test
    public void signedResponses() {
        thenResponseIsSigned(SynchronizePlayer.class);
        thenResponseIsSigned(ReplaceQuest.class);
        thenResponseIsSigned(TradeDuplicateCards.class);
        thenResponseIsSigned(BuyBlackMarketOffer.class);
        thenResponseIsSigned(ForgeCard.class);
        thenResponseIsSigned(PrepareShopTransaction.class);
        thenResponseIsSigned(CommitShopTransactions.class);
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
        GatewayError expected = new GatewayError("Something went wrong down here in the gateway.", new SQLException("SQL error details."));

        try {
            businessLogic().execute(new ThrowingUsecase.Request(expected));
        } catch (Error e) {
            error = e;
        }

        assertNotNull(error);
        assertEquals(InternalServerError.class, error.getClass());
        assertEquals("Something went wrong down here in the gateway. (SQL error details.)", error.getMessage());
        assertEquals(SQLException.class, error.getCause().getClass());
        logger.thenMessageIsLogged(Level.SEVERE, "An unexpected gateway error occured during usecase execution.", expected);
    }

    @Test
    public void unexpectedErrorInUsecase() {
        givenTestLogic();
        NullPointerException expected = new NullPointerException("This must not be null.");

        try {
            businessLogic().execute(new ThrowingUsecase.Request(expected));
        } catch (Error e) {
            error = e;
        }

        assertNotNull(error);
        assertEquals(InternalServerError.class, error.getClass());
        assertEquals("An unexpected exception occured during usecase execution. (This must not be null.)", error.getMessage());
        assertEquals(NullPointerException.class, error.getCause().getClass());
        logger.thenMessageIsLogged(Level.SEVERE, "An unexpected exception occured during usecase execution.", expected);
    }

    @Test
    public void expectedErrorInUsecase() {
        givenTestLogic();
        BadRequest expected = new BadRequest("This was expected.");

        try {
            businessLogic().execute(new ThrowingUsecase.Request(expected));
        } catch (Error e) {
            error = e;
        }

        assertNotNull(error);
        assertSame(expected, error);
        logger.thenNoMessageIsLogged();
    }

    @Test
    public void noErrorInUsecase() {
        givenTestLogic();
        businessLogic().addUsecase(GoodUsecase.class);

        assertEquals("ok", businessLogic().execute(new GoodUsecase.Request()));
    }

    private void givenTestLogic() {
        givenExecutor(getTestBusinessLogic());
        logger.reset();
    }

    private BusinessLogic businessLogic() {
        return (BusinessLogic)executor;
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
        assertSame(businessLogic().getInstance(clazz), businessLogic().getInstance(clazz));
    }

    private void thenOnlyOneInstanceExists(Class<?> clazz1, Class<?> clazz2) {
        assertSame(businessLogic().getInstance(clazz1), businessLogic().getInstance(clazz1));
        assertSame(businessLogic().getInstance(clazz2), businessLogic().getInstance(clazz2));
        assertSame(businessLogic().getInstance(clazz1), businessLogic().getInstance(clazz2));
    }

    private static class ThrowingUsecase implements Usecase<ThrowingUsecase.Request, Void> {
        public static class Request {
            public RuntimeException error;

            public Request(RuntimeException error) {
                this.error = error;
            }
        }

        @Override
        public Void execute(Request request) {
            throw request.error;
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