package com.mazebert;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import com.mazebert.error.Error;
import com.mazebert.error.InternalServerError;
import com.mazebert.gateways.*;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.mysql.*;
import com.mazebert.gateways.mysql.connection.Credentials;
import com.mazebert.gateways.mysql.connection.CredentialsProvider;
import com.mazebert.gateways.mysql.connection.DataSourceProvider;
import com.mazebert.gateways.mysql.connection.HikariDataSourceProvider;
import com.mazebert.plugins.message.EmailMessagePlugin;
import com.mazebert.plugins.message.MazebertMailMessagePluginProvider;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.random.SecureRandomNumberGenerator;
import com.mazebert.plugins.security.GameContentVerifier;
import com.mazebert.plugins.security.ServerContentSigner;
import com.mazebert.plugins.system.EnvironmentPlugin;
import com.mazebert.plugins.system.JsonSettingsPlugin;
import com.mazebert.plugins.system.SettingsPlugin;
import com.mazebert.plugins.system.SystemEnvironmentPlugin;
import com.mazebert.usecases.blackmarket.BuyBlackMarketOffer;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import com.mazebert.usecases.player.*;
import com.mazebert.usecases.quest.CompleteQuests;
import com.mazebert.usecases.quest.ReplaceQuest;
import com.mazebert.usecases.security.SignServerResponse;
import com.mazebert.usecases.security.VerifyGameRequest;
import com.mazebert.usecases.shop.CommitShopTransactions;
import com.mazebert.usecases.shop.PrepareShopTransaction;
import com.mazebert.usecases.supporters.GetSupporters;
import com.mazebert.usecases.system.GetStatus;
import com.mazebert.usecases.system.GetVersion;
import org.jusecase.executors.guice.GuiceUsecaseExecutor;
import org.jusecase.transaction.TransactionRunner;
import org.jusecase.transaction.simple.SimpleTransactionRunner;
import org.jusecase.transaction.simple.ThreadLocalTransactionManager;
import org.jusecase.transaction.simple.TransactionManager;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusinessLogic extends GuiceUsecaseExecutor {
    private static class LogicHolder {
        public static BusinessLogic instance = new BusinessLogic(
                HikariDataSourceProvider.class,
                SystemEnvironmentPlugin.class,
                Logger.getLogger(BusinessLogic.class.getName()));
    }
    private final Logger logger;
    private final Class<? extends DataSourceProvider> dataSourceProvider;

    public static BusinessLogic getInstance() {
        return LogicHolder.instance;
    }

    public void start() {
        getDataSourceProvider().prepare();
    }

    public void shutdown() {
        getDataSourceProvider().dispose();
    }

    private DataSourceProvider getDataSourceProvider() {
        return getInjector().getInstance(dataSourceProvider);
    }

    private static class GatewayModule extends AbstractModule {
        private final Class<? extends Provider<DataSource>> dataSourceProvider;

        public GatewayModule(Class<? extends Provider<DataSource>> dataSourceProvider) {
            this.dataSourceProvider = dataSourceProvider;
        }

        @Override
        protected void configure() {
            bind(DataSource.class).toProvider(dataSourceProvider);
            bind(TransactionRunner.class).to(SimpleTransactionRunner.class);
            bind(TransactionManager.class).to(ThreadLocalTransactionManager.class);
            bind(Credentials.class).toProvider(CredentialsProvider.class);

            bind(PlayerGateway.class).to(MySqlPlayerGateway.class);
            bind(PlayerRowGateway.class).to(MySqlPlayerRowGateway.class);
            bind(PurchaseGateway.class).to(MySqlPurchaseGateway.class);
            bind(FoilCardGateway.class).to(MySqlFoilCardGateway.class);
            bind(QuestGateway.class).to(MySqlQuestGateway.class);
            bind(BlackMarketOfferGateway.class).to(MySqlBlackMarketOfferGateway.class);
            bind(BlackMarketSettingsGateway.class).to(MySqlBlackMarketSettingsGateway.class);
            bind(CardGateway.class).to(MySqlCardGateway.class);
            bind(VersionInfoGateway.class).to(MySqlVersionInfoGateway.class);
            bind(BonusTimeGateway.class).to(MySqlBonusTimeGateway.class);
            bind(SupporterGateway.class).to(MySqlSupporterGateway.class);
        }
    }

    private static class PluginModule extends AbstractModule {
        private final Class<? extends EnvironmentPlugin> environmentPlugin;

        public PluginModule(Class<? extends EnvironmentPlugin> environmentPlugin) {
            this.environmentPlugin = environmentPlugin;
        }

        @Override
        protected void configure() {
            bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(4));
            bind(EnvironmentPlugin.class).to(environmentPlugin).asEagerSingleton();
            bind(SettingsPlugin.class).to(JsonSettingsPlugin.class).asEagerSingleton();
            bind(RandomNumberGenerator.class).to(SecureRandomNumberGenerator.class);
            bind(EmailMessagePlugin.class).toProvider(MazebertMailMessagePluginProvider.class).asEagerSingleton();
            bind(GameContentVerifier.class).asEagerSingleton();
            bind(ServerContentSigner.class).asEagerSingleton();
        }
    }

    public <T extends Provider<DataSource> & DataSourceProvider> BusinessLogic(
            Class<T> dataSourceProvider,
            Class<? extends EnvironmentPlugin> environmentPlugin,
            Logger logger) {
        super(Guice.createInjector(
                new GatewayModule(dataSourceProvider),
                new PluginModule(environmentPlugin)
        ));

        this.dataSourceProvider = dataSourceProvider;
        this.logger = logger;

        addSystemUsecases();
        addSecurityUsecases();
        addPlayerUsecases();
        addTradeUsecases();
        addBlackMarketUsecases();
        addBonusTimeUsecases();
        addQuestUsecases();
        addSupporterUsecases();
        addShopUsecases();
    }

    private void addSystemUsecases() {
        addUsecase(GetVersion.class);
        addUsecase(GetStatus.class);
    }

    private void addSecurityUsecases() {
        addUsecase(VerifyGameRequest.class);
        addUsecase(SignServerResponse.class);
    }

    private void addPlayerUsecases() {
        addUsecase(CreateAccount.class);
        addUsecase(UpdateProgress.class);
        addUsecase(GetPlayers.class);
        addUsecase(GetPlayer.class);
        addUsecase(SynchronizePlayer.class);
        addUsecase(ForgotSavecode.class);
        addUsecase(RegisterEmail.class);
        addUsecase(GetPlayerProfile.class);
        addUsecase(ForgeCard.class);
        addUsecase(BuyCard.class);
    }

    private void addTradeUsecases() {
        addUsecase(TradeDuplicateCards.class);
    }

    private void addBlackMarketUsecases() {
        addUsecase(BuyBlackMarketOffer.class);
    }

    private void addBonusTimeUsecases() {
        addUsecase(GetBonusTimes.class);
        addUsecase(UpdateBonusTime.class);
    }

    private void addQuestUsecases() {
        addUsecase(CompleteQuests.class);
        addUsecase(ReplaceQuest.class);
    }

    private void addSupporterUsecases() {
        addUsecase(GetSupporters.class);
    }

    private void addShopUsecases() {
        addUsecase(PrepareShopTransaction.class);
        addUsecase(CommitShopTransactions.class);
    }

    @Override
    public <Request, Response> Response execute(Request request) {
        try {
            return super.execute(request);
        } catch (GatewayError gatewayError) {
            logger.log(Level.SEVERE, "An unexpected gateway error occured during usecase execution.", gatewayError);
            throw new InternalServerError(gatewayError.getMessage() + " (" + gatewayError.getCause().getMessage() + ")", gatewayError.getCause());
        } catch (Throwable throwable) {
            if (!(throwable instanceof Error)) {
                logger.log(Level.SEVERE, "An unexpected exception occured during usecase execution.", throwable);
                throw new InternalServerError("An unexpected exception occured during usecase execution. (" + throwable.getMessage() + ")", throwable);
            } else {
                throw  throwable;
            }
        }
    }

    <T> T getInstance(Class<T> clazz) {
        return getInjector().getInstance(clazz);
    }
}
