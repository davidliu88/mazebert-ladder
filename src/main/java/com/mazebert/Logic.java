package com.mazebert;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.mysql.MySqlPlayerGateway;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.random.SecureRandomNumberGenerator;
import com.mazebert.usecases.CreateAccount;
import com.mazebert.usecases.GetPlayers;
import com.mazebert.usecases.GetVersion;
import org.jusecase.executors.guice.GuiceUsecaseExecutor;

public class Logic extends GuiceUsecaseExecutor {
    public static Logic instance = new Logic();

    private static class GatewayModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(PlayerGateway.class).to(MySqlPlayerGateway.class);
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
        addUsecase(CreateAccount.class);
        addUsecase(GetPlayers.class);
    }
}
