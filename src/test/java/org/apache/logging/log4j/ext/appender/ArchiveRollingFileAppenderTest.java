package org.apache.logging.log4j.ext.appender;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class ArchiveRollingFileAppenderTest extends AppenderTest {


    @Test
    public void testLog() throws Exception {
        final String config = "log4j2-rolling00-gz.xml";
        final String pathLog = "target/log/rolling00";

        setup(config);

        for (int i = 0; i < 100; ++i) {
            //Thread.sleep(100);
            logger.debug("This is test message number " + i);
        }

        final File dir = new File(pathLog);
        assertTrue("Directory not created", dir.exists());

        final File[] files = dir.listFiles();
        assertTrue("No files created", files.length > 0);

        assertTrue("Expected 7 archive files", files.length == 8);

        for (final File file : files) {
            assertTrue("No compressed files found", file.getName().endsWith(".gz"));
        }
    }
}
