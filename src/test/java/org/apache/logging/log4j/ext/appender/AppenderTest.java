package org.apache.logging.log4j.ext.appender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.status.StatusLogger;
import org.junit.After;

public abstract class AppenderTest {
    protected Logger logger = LogManager.getLogger(AppenderTest.class);

    protected void setup(final String configPath) {
        System.setProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY, configPath);
        final LoggerContext ctx = (LoggerContext) LogManager.getContext();
        final Configuration config = ctx.getConfiguration();
    }

    @After
    public void cleanup() {
        System.clearProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY);
        final LoggerContext ctx = (LoggerContext) LogManager.getContext();
        ctx.reconfigure();
        StatusLogger.getLogger().reset();
    }
}
