package org.apache.logging.log4j.ext.appender;

import org.junit.BeforeClass;
import org.junit.Test;

public class ArchiveRollingFileAppenderTest extends AppenderTest {
    private static final String CONFIG = "log4j2-rolling-gz.xml";

    @BeforeClass
    public static void setupClass() {
        setup(CONFIG);
    }

    @Test
    public void testAppender() throws Exception {
        for (int i = 0; i < 100; ++i) {
            logger.debug("This is test message number " + i);
        }
        /*final File dir = new File(DIR);
        assertTrue("Directory not created", dir.exists() && dir.listFiles().length > 0);
        final File[] files = dir.listFiles();
        assertTrue("No files created", files.length > 0);
        boolean found = false;
        for (final File file : files) {
            if (file.getName().endsWith(fileExtension)) {
                found = true;
                break;
            }
        }
        assertTrue("No compressed files found", found);
        */
    }
}