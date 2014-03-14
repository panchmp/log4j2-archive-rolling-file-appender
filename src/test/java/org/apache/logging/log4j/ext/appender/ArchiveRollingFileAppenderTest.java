package org.apache.logging.log4j.ext.appender;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArchiveRollingFileAppenderTest extends AppenderTest {
    private static final String CONFIG = "log4j2-rolling00-gz.xml";
    private static final String PATH = "target/log/rolling00";

    @BeforeClass
    public static void setupClass() {
        setup(CONFIG);
    }

    @Test
    public void testAppender() throws Exception {
        for (int i = 0; i < 1000; ++i) {
            logger.debug("This is test message number " + i);
        }
        final File dir = new File(PATH);
        assertTrue("Directory not created", dir.exists());

        final File[] files = dir.listFiles();

        assertTrue("No files created", files.length > 0);

        assertEquals("Expected 5 archive files", files.length , 6);

        for (final File file : files) {
            if (!file.getName().equals("test.log"))
                assertTrue("No compressed files found", file.getName().endsWith(".gz"));
        }
    }
}
