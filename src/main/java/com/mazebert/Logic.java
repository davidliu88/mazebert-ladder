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
import com.mazebert.plugins.message.EmailMessageFakePlugin;
import com.mazebert.plugins.message.EmailMessagePlugin;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.random.SecureRandomNumberGenerator;
import com.mazebert.plugins.security.GameContentVerifier;
import com.mazebert.usecases.GetStatus;
import com.mazebert.usecases.GetVersion;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import com.mazebert.usecases.player.*;
import com.mazebert.usecases.quest.CompleteQuests;
import com.mazebert.usecases.quest.ReplaceQuest;
import com.mazebert.usecases.security.VerifyGameSignature;
import org.jusecase.executors.guice.GuiceUsecaseExecutor;

import javax.sql.DataSource;

public class Logic extends GuiceUsecaseExecutor {
    public static final Logic instance = new Logic(C3p0DataSourceProvider.class);

    public void start() {
    }

    public void shutdown() {
    }

    private static class GatewayModule extends AbstractModule {
        private final Class<? extends Provider<DataSource>> dataSourceProvider;

        public GatewayModule(Class<? extends Provider<DataSource>> dataSourceProvider) {
            this.dataSourceProvider = dataSourceProvider;
        }

        @Override
        protected void configure() {
            bind(DataSource.class).toProvider(dataSourceProvider).asEagerSingleton();
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
        }
    }

    private static class PluginModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(RandomNumberGenerator.class).to(SecureRandomNumberGenerator.class);
            bind(EmailMessagePlugin.class).to(EmailMessageFakePlugin.class);
            bind(GameContentVerifier.class).asEagerSingleton();
        }
    }

    public Logic(Class<? extends Provider<DataSource>> dataSourceProvider) {
        super(Guice.createInjector(
                new GatewayModule(dataSourceProvider),
                new PluginModule()
        ));

        addSystemUsecases();
        addPlayerUsecases();
        addBonusTimeUsecases();
        addQuestUsecases();
    }

    private void addSystemUsecases() {
        addUsecase(VerifyGameSignature.class);
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
    }

    private void addBonusTimeUsecases() {
        addUsecase(GetBonusTimes.class);
        addUsecase(UpdateBonusTime.class);
    }

    private void addQuestUsecases() {
        addUsecase(CompleteQuests.class);
        addUsecase(ReplaceQuest.class);
    }

    @Override
    public <Request, Response> Response execute(Request request) {
        try {
            return super.execute(request);
        } catch (GatewayError gatewayError) {
            throw new InternalServerError(gatewayError.getMessage(), gatewayError.getCause());
        }
    }
}
