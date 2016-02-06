package com.mazebert;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.PlayerRowGateway;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.mysql.C3p0DataSourceProvider;
import com.mazebert.gateways.mysql.MySqlPlayerGateway;
import com.mazebert.gateways.mysql.MySqlPlayerRowGateway;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.random.SecureRandomNumberGenerator;
import com.mazebert.usecases.GetStatus;
import com.mazebert.usecases.player.CreateAccount;
import com.mazebert.usecases.player.GetPlayers;
import com.mazebert.usecases.GetVersion;
import com.mazebert.usecases.player.UpdateProgress;
import org.jusecase.executors.guice.GuiceUsecaseExecutor;

import javax.sql.DataSource;

public class Logic extends GuiceUsecaseExecutor {
    public static Logic instance = new Logic();

    private static class GatewayModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(DataSource.class).toProvider(C3p0DataSourceProvider.class);

            bind(PlayerGateway.class).to(MySqlPlayerGateway.class);
            bind(PlayerRowGateway.class).to(MySqlPlayerRowGateway.class);
        }
    }

    private static class PluginModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(RandomNumberGenerator.class).to(SecureRandomNumberGenerator.class);
        }
    }

    public Logic() {
        super(Guice.createInjector(
                new GatewayModule(),
                new PluginModule()
        ));

        addUsecase(GetVersion.class);
        addUsecase(GetStatus.class);

        addUsecase(CreateAccount.class);
        addUsecase(UpdateProgress.class);
        addUsecase(GetPlayers.class);
    }

    @Override
    public <Request, Response> Response execute(Request request) {
        try {
            return super.execute(request);
        } catch (GatewayError gatewayError) {
            throw new Error(Type.INTERNAL_SERVER_ERROR, gatewayError.getMessage(), gatewayError.getCause());
        }
    }
}
