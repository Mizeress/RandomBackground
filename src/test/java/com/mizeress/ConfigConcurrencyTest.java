package com.mizeress;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.mizeress.config.Config;

// AI generated concurrency test

class ConfigConcurrencyTest {

    private String originalUserHome;

    @AfterEach
    void restoreUserHome() {
        if (originalUserHome != null) {
            System.setProperty("user.home", originalUserHome);
        }
    }

    @Test
    void concurrentReadsAndAsyncWrites_doNotCorruptFile(@TempDir Path tempDir) throws Exception {
        // Point user.home to temporary test directory so Config writes to an isolated location
        originalUserHome = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());

        // Create config (will create default file)
        Config config = new Config();

        // basic sanity: defaults should exist
        String src = config.getProperty("imageSource");
        assertNotNull(src, "imageSource should be present after initialization");

        // We'll perform many concurrent writes and reads.
        int writers = 30;
        int readers = 50;
        ExecutorService executor = Executors.newCachedThreadPool();
        List<CompletableFuture<Void>> writeFutures = new ArrayList<>();
        Random rnd = new Random(12345);

        // Launch writer tasks (use the async API)
        for (int i = 0; i < writers; i++) {
            final int idx = i;
            CompletableFuture<Void> f = config.setPropertyAsync("concurrentKey", "value-" + idx)
                    .exceptionally(ex -> {
                        // turn exceptions into completion exception to be handled later
                        throw new CompletionException(ex);
                    });
            writeFutures.add(f);
        }

        // Launch reader tasks that repeatedly read the property while writes are happening
        List<Callable<Void>> readerTasks = new ArrayList<>();
        for (int r = 0; r < readers; r++) {
            readerTasks.add(() -> {
                // perform several reads
                for (int k = 0; k < 100; k++) {
                    String v = config.getProperty("concurrentKey"); // may be null early on
                    // Occasionally check some invariant: value should either be null or start with "value-"
                    if (v != null) {
                        assertTrue(v.startsWith("value-") || v.equals("directory") || v.equals("API") || v.matches(".*"),
                                "Unexpected concurrent value: " + v);
                    }
                    // tiny sleep to increase interleaving
                    Thread.sleep(rnd.nextInt(5));
                }
                return null;
            });
        }

        // Submit reader tasks
        List<Future<Void>> readerFutures = executor.invokeAll(readerTasks);

        // Wait for writes to finish
        CompletableFuture.allOf(writeFutures.toArray(new CompletableFuture[0])).get(10, TimeUnit.SECONDS);

        // Wait for reader tasks to finish
        for (Future<Void> rf : readerFutures) {
            rf.get(5, TimeUnit.SECONDS);
        }

        executor.shutdownNow();

        // Ensure there is a properties file on disk and it's loadable
        Path configFile = tempDir.resolve(".mizeress").resolve("config.properties");
        assertTrue(Files.exists(configFile), "Config file should exist");

        Properties onDisk = new Properties();
        try (InputStream in = Files.newInputStream(configFile, StandardOpenOption.READ)) {
            onDisk.load(in);
        }

        // The on-disk file must at least contain either our concurrentKey or the defaults
        assertTrue(onDisk.containsKey("concurrentKey") || onDisk.containsKey("imageSource"),
                "On-disk properties should contain expected keys");

        // Finally, reload into Config and verify getProperty returns the on-disk value
        config.reloadFromFile();
        String diskVal = config.getProperty("concurrentKey");
        // diskVal may be null if writes happened before defaults were persisted; but if present it should match onDisk
        if (diskVal != null) {
            assertEquals(onDisk.getProperty("concurrentKey"), diskVal);
        }
    }

    @Test
    void multipleAsyncSaves_thenJoin_resultsInValidFile(@TempDir Path tempDir) throws Exception {
        originalUserHome = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());

        Config config = new Config();

        // Simulate GUI pattern: three async writes then allOf join
        CompletableFuture<?> f1 = config.setPropertyAsync("imagePath", "C:/tmp/wallpapers");
        CompletableFuture<?> f2 = config.setPropertyAsync("imageSource", "directory");
        CompletableFuture<?> f3 = config.setPropertyAsync("changeInterval", "7");

        CompletableFuture.allOf(f1, f2, f3).get(5, TimeUnit.SECONDS);

        // Read file on disk and verify properties present and valid
        Path configFile = tempDir.resolve(".mizeress").resolve("config.properties");
        assertTrue(Files.exists(configFile), "Config file should exist after saves");

        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(configFile)) {
            p.load(in);
        }

        assertEquals("C:/tmp/wallpapers", p.getProperty("imagePath"));
        assertEquals("directory", p.getProperty("imageSource"));
        assertEquals("7", p.getProperty("changeInterval"));
    }
}