package com.josetesan.oracle.rest;

import com.josetesan.oracle.rest.db.TaskDao;
import com.josetesan.oracle.rest.resources.TaskResource;
import com.josetesan.oracle.rest.service.TaskService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.sql.Connection;
import java.util.EnumSet;

public class BackendApplication extends Application<BackendConfiguration> {


    private static final Logger LOGGER = LoggerFactory.getLogger(BackendApplication.class);

    public static void main(final String[] args) throws Exception {
        new BackendApplication().run(args);
    }

    @Override
    public String getName() {
        return "Backend";
    }

    @Override
    public void initialize(final Bootstrap<BackendConfiguration> bootstrap) {
        bootstrap.addBundle(new JdbiExceptionsBundle());
        bootstrap.addBundle(new MigrationsBundle<BackendConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(BackendConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        CollectorRegistry.defaultRegistry.register(new DropwizardExports(bootstrap.getMetricRegistry()));

    }

    @Override
    public void run(final BackendConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "database");
        jdbi.installPlugin(new SqlObjectPlugin());
        environment.jersey().register(new TaskResource(jdbi.onDemand(TaskDao.class)));
        migrate(configuration, environment);
        configureCors(environment);
    }


    private void migrate(BackendConfiguration configuration, Environment environment) {
        ManagedDataSource dataSource = createMigrationDataSource(configuration, environment);

        try (Connection connection = dataSource.getConnection()) {
            JdbcConnection conn = new JdbcConnection(connection);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(conn);
            try (Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), database)) {
                liquibase.update("");
            }
            LOGGER.info("Database migrated");
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to migrate database", ex);
        } finally {
            try {
                dataSource.stop();
            } catch (Exception ex) {
                LOGGER.error("Unable to stop data source used to execute schema migration", ex);
            }
        }
    }

    private ManagedDataSource createMigrationDataSource(BackendConfiguration configuration, Environment environment) {
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        return dataSourceFactory.build(environment.metrics(), "migration-ds");
    }

    private void configureCors(Environment environment) {
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }
}
