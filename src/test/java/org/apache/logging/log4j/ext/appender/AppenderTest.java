package org.apache.logging.log4j.ext.appender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.status.StatusLogger;

import java.io.File;

public abstract class AppenderTest {
    protected Logger logger = LogManager.getLogger(AppenderTest.class);

    private static final String DIR = "target/log";

    protected static void setup(String configPath) {
        deleteDir();
        System.setProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY, configPath);
        final LoggerContext ctx = (LoggerContext) LogManager.getContext();
        final Configuration config = ctx.getConfiguration();
    }

    public static void cleanup() {
        //deleteDir();
        System.clearProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY);
        final LoggerContext ctx = (LoggerContext) LogManager.getContext();
        ctx.reconfigure();
        StatusLogger.getLogger().reset();
    }

    private static void deleteDir() {
        final File dir = new File(DIR);
        if (dir.exists()) {
            final File[] files = dir.listFiles();
            for (final File file : files) {
                file.delete();
            }
            dir.delete();
        }
    }
}
