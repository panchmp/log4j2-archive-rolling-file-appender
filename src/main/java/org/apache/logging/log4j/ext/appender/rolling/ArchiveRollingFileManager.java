package org.apache.logging.log4j.ext.appender.rolling;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

public class ArchiveRollingFileManager extends RollingFileManager {
    private static ArchiveRollingFileManagerFactory factory = new ArchiveRollingFileManagerFactory();

    private final boolean bufferedIO;

    protected ArchiveRollingFileManager(final String fileName, final String pattern, final OutputStream os,
                                        final boolean append, final boolean bufferedIO, final long size, final long time, final TriggeringPolicy triggeringPolicy,
                                        final RolloverStrategy rolloverStrategy, final String advertiseURI, final Layout<? extends Serializable> layout) {
        super(fileName, pattern, os, append, size, time, triggeringPolicy, rolloverStrategy, advertiseURI, layout);
        this.bufferedIO = bufferedIO;
    }

    public boolean isBufferedIO() {
        return bufferedIO;
    }

    /**
     * Returns a ArchiveRollingFileManager.
     *
     * @param fileName     The file name.
     * @param pattern      The pattern for rolling file.
     * @param append       true if the file should be appended to.
     * @param bufferedIO   true if data should be buffered.
     * @param policy       The TriggeringPolicy.
     * @param strategy     The RolloverStrategy.
     * @param advertiseURI the URI to use when advertising the file
     * @param layout       The Layout.
     * @return A ArchiveRollingFileManager.
     */
    public static ArchiveRollingFileManager getFileManager(final String fileName, final String pattern, final boolean append,
                                                           final boolean bufferedIO, final TriggeringPolicy policy,
                                                           final RolloverStrategy strategy, final String advertiseURI,
                                                           final Layout<? extends Serializable> layout) {

        return (ArchiveRollingFileManager) getManager(fileName, new FactoryData(pattern, append,
                bufferedIO, policy, strategy, advertiseURI, layout), factory);
    }

    protected void createFileAfterRollover() throws IOException {
        final OutputStream os = getOutputStream(getFileName(), isAppend(), isBufferedIO());
        setOutputStream(os);
    }

    /**
     * Factory data.
     */
    private static class FactoryData {
        private final String pattern;
        private final boolean append;
        private final boolean bufferedIO;
        private final TriggeringPolicy policy;
        private final RolloverStrategy strategy;
        private final String advertiseURI;
        private final Layout<? extends Serializable> layout;

        /**
         * Create the data for the factory.
         *
         * @param pattern      The pattern.
         * @param append       The append flag.
         * @param bufferedIO   The bufferedIO flag.
         * @param advertiseURI
         * @param layout       The Layout.
         */
        public FactoryData(final String pattern, final boolean append, final boolean bufferedIO,
                           final TriggeringPolicy policy, final RolloverStrategy strategy, final String advertiseURI,
                           final Layout<? extends Serializable> layout) {
            this.pattern = pattern;
            this.append = append;
            this.bufferedIO = bufferedIO;
            this.policy = policy;
            this.strategy = strategy;
            this.advertiseURI = advertiseURI;
            this.layout = layout;
        }
    }

    /**
     * Factory to create a ArchiveRollingFileManager.
     */
    private static class ArchiveRollingFileManagerFactory implements ManagerFactory<ArchiveRollingFileManager, FactoryData> {

        /**
         * Create the ArchiveRollingFileManager.
         *
         * @param name The name of the entity to manage.
         * @param data The data required to create the entity.
         * @return a ArchiveRollingFileManager.
         */
        @Override
        public ArchiveRollingFileManager createManager(final String name, final FactoryData data) {
            final File file = new File(name);
            final File parent = file.getParentFile();
            if (null != parent && !parent.exists()) {
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (final IOException ioe) {
                LOGGER.error("Unable to create file " + name, ioe);
                return null;
            }
            final long size = data.append ? file.length() : 0;

            try {
                OutputStream os = getOutputStream(name, data.append, data.bufferedIO);
                final long time = file.lastModified(); // LOG4J2-531 create file first so time has valid value
                return new ArchiveRollingFileManager(name, data.pattern, os, data.append, data.bufferedIO, size, time, data.policy,
                        data.strategy, data.advertiseURI, data.layout);
            } catch (final IOException ex) {
                LOGGER.error("FileManager (" + name + ") " + ex);
            }
            return null;
        }
    }

    private static OutputStream getOutputStream(String name, boolean append, boolean bufferedIO) throws IOException {
        OutputStream os = new FileOutputStream(name, append);
        if (name.endsWith(".gz")) {
            os = new GZIPOutputStream(os, 124);
        } else if (name.endsWith(".zip")) {
            os = new ZipOutputStream(os);
        } else if (bufferedIO) {
            os = new BufferedOutputStream(os);
        }
        return os;
    }
}

