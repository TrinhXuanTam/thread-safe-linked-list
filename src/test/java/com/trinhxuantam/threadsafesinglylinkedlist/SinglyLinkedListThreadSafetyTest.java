package com.trinhxuantam.threadsafesinglylinkedlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trinhxuantam.threadsafesinglylinkedlist.utils.collections.SinglyLinkedList;

/**
 * This class tests the thread safety and deadlock management of the
 * {@link SinglyLinkedList}.
 */
public class SinglyLinkedListThreadSafetyTest {

    private SinglyLinkedList<Integer> list;

    private ExecutorService executor; // Executor service for running concurrent tasks

    /**
     * Sets up the test fixture.
     * Called before every test method.
     */
    @BeforeEach
    public void setUp() {
        list = new SinglyLinkedList<>();
        executor = Executors.newFixedThreadPool(4);
    }

    /**
     * Waits for the executor service to shut down, ensuring that all tasks have
     * completed.
     * Throws an assertion error if the executor does not terminate within the
     * expected time.
     *
     * @throws InterruptedException if the current thread is interrupted while
     *                              waiting.
     */
    private void awaitTerminationAfterShutdown() throws InterruptedException {
        // Shut down the executor
        executor.shutdown();

        // Wait for all tasks to complete
        boolean terminated = executor.awaitTermination(1, TimeUnit.MINUTES);

        // Assert that the executor has terminated
        assertTrue(terminated, "Executor did not terminate in the expected time");
    }

    /**
     * Checks for deadlocks and asserts that no deadlocks are present.
     * Throws an assertion error if any deadlocks are detected.
     */
    private void assertNoDeadlocks() {
        // Get the thread MX bean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // Find deadlocked threads
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();

        // Assert that no deadlocks are present
        assertTrue(deadlockedThreads == null || deadlockedThreads.length == 0,
                "Deadlocks detected in thread ids: " + Arrays.toString(deadlockedThreads));
    }

    /**
     * Tests the thread safety of the list by performing multiple concurrent pop
     * operations.
     * Verifies that the list size and content match expected values after all
     * operations are completed.
     *
     * @throws InterruptedException if the current thread is interrupted while
     *                              waiting.
     */
    @Test
    public void testMultiThreadPop() throws InterruptedException {
        int size = 1000;
        int expected = 2;
        int toPop = size - expected;
        CountDownLatch latch = new CountDownLatch(toPop);
        for (int i = 0; i < size; i++) {
            list.push(i);
        }

        // Pop all elements from the list concurrently
        for (int i = 0; i < toPop; i++) {
            executor.execute(() -> {
                list.pop();
                latch.countDown();
            });
        }

        try {
            latch.await(45, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            assertNoDeadlocks();
        }

        assertEquals(expected, list.size(), "Check the number of remaining elements");
        assertEquals(List.of(0, 1), list.getAll(), "Check the content of the list matches expected");
        awaitTerminationAfterShutdown();
    }

    /**
     * Tests the thread safety of the list by performing concurrent push and pop
     * operations.
     * Verifies that the list maintains integrity and correctness throughout the
     * operations.
     *
     * @throws InterruptedException if the current thread is interrupted while
     *                              waiting.
     */
    @Test
    public void testConcurrentPushAndPop() throws InterruptedException {
        int initialSize = 1000;
        int numOperations = 1000;
        CountDownLatch latch = new CountDownLatch(numOperations * 2); // Total operations
        for (int i = 0; i < initialSize; i++) {
            list.push(i);
        }

        for (int i = 0; i < numOperations; i++) {
            final int element = i;
            executor.submit(() -> {
                list.push(element);
                latch.countDown();
            });
            executor.submit(() -> {
                if (!list.isEmpty()) {
                    list.pop();
                }
                latch.countDown();
            });
        }

        try {
            latch.await(45, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            assertNoDeadlocks();
        }

        // The expected size could vary, but should be the initialSize unless all pops
        // were executed after all pushes
        assertTrue(list.getAll().size() <= initialSize,
                "Expected list size to be no more than the initial size after concurrent operations.");
        awaitTerminationAfterShutdown();
    }

    /**
     * Tests the thread safety of the insertAfter method by performing multiple
     * concurrent insert operations.
     * Verifies that all inserts are executed correctly without corrupting the
     * list structure.
     *
     * @throws InterruptedException if the current thread is interrupted while
     *                              waiting.
     */
    @Test
    public void testConcurrentInsertAfter() throws InterruptedException {
        int initialSize = 1000;
        int numInserts = 1000;
        CountDownLatch latch = new CountDownLatch(numInserts);

        for (int i = 0; i < initialSize; i++) {
            list.push(i);
        }

        for (int i = 0; i < numInserts; i++) {
            // Insert elemenets outside the initial list bounds
            final int element = i + initialSize;

            executor.execute(() -> {
                int position = element % initialSize; // Choose positions within the list bounds
                list.insertAfter(element, position);
                latch.countDown();
            });
        }
        try {
            latch.await(45, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            assertNoDeadlocks();
        }

        assertEquals(initialSize + numInserts, list.getAll().size(),
                "Check the list size matches the expected number after insertions.");
        awaitTerminationAfterShutdown();
    }
}
