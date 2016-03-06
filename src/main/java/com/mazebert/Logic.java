package com.mazebert;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import com.mazebert.error.InternalServerError;
import com.mazebert.gateways.*;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.mysql.*;
import com.mazebert.gateways.mysql.connection.C3p0DataSourceProvider;
import com.mazebert.gateways.mysql.connection.Credentials;
import com.mazebert.gateways.mysql.connection.CredentialsProvider;
import com.mazebert.gateways.mysql.connection.DataSourceProvider;
import com.mazebert.gateways.transaction.TransactionRunner;
import com.mazebert.gateways.transaction.datasource.DataSourceTransactionManager;
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
import com.mazebert.usecases.GetStatus;
import com.mazebert.usecases.GetVersion;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import com.mazebert.usecases.player.*;
import com.mazebert.usecases.quest.CompleteQuests;
import com.mazebert.usecases.quest.ReplaceQuest;
import com.mazebert.usecases.security.SignServerResponse;
import com.mazebert.usecases.security.VerifyGameRequest;
import com.mazebert.usecases.supporters.GetSupporters;
import org.jusecase.executors.guice.GuiceUsecaseExecutor;

import javax.sql.DataSource;

public class Logic extends GuiceUsecaseExecutor {
    private static class LogicHolder {
        public static Logic instance = new Logic(C3p0DataSourceProvider.class, SystemEnvironmentPlugin.class);
    }
    private final Class<? extends DataSourceProvider> dataSourceProvider;

    public static Logic getInstance() {
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
            bind(TransactionRunner.class).to(DataSourceTransactionManager.class);
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
            bind(EnvironmentPlugin.class).to(environmentPlugin).asEagerSingleton();
            bind(SettingsPlugin.class).to(JsonSettingsPlugin.class).asEagerSingleton();
            bind(RandomNumberGenerator.class).to(SecureRandomNumberGenerator.class);
            bind(EmailMessagePlugin.class).toProvider(MazebertMailMessagePluginProvider.class).asEagerSingleton();
            bind(GameContentVerifier.class).asEagerSingleton();
            bind(ServerContentSigner.class).asEagerSingleton();
        }
    }

    public <T extends Provider<DataSource> & DataSourceProvider> Logic(Class<T> dataSourceProvider, Class<? extends EnvironmentPlugin> environmentPlugin) {
        super(Guice.createInjector(
                new GatewayModule(dataSourceProvider),
                new PluginModule(environmentPlugin)
        ));

        this.dataSourceProvider = dataSourceProvider;

        addSystemUsecases();
        addPlayerUsecases();
        addBonusTimeUsecases();
        addQuestUsecases();
        addSupporterUsecases();
    }

    private void addSystemUsecases() {
        addUsecase(VerifyGameRequest.class);
        addUsecase(SignServerResponse.class);
        addUsecase(GetVersion.class);
        addUsecase(GetStatus.class);
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

    @Override
    public <Request, Response> Response execute(Request request) {
        try {
            return super.execute(request);
        } catch (GatewayError gatewayError) {
            throw new InternalServerError(gatewayError.getMessage(), gatewayError.getCause());
        }
    }

    <T> T getInstance(Class<T> clazz) {
        return getInjector().getInstance(clazz);
    }
}
