package com.anobis.scraper;

import java.util.concurrent.*;

/**
 * @author anobis <austin.nobis@amd.com>
 */
public class PageRunner implements IPageRunner {
    private static final PageReader POISON_PILL = new PageReader() {
        @Override
        public String getUrl() {
            return null;
        }

        @Override
        public void run() {

        }
    };
    private MasterThread masterThread;

    @Override
    public void start(LinkedBlockingQueue<PageReader> queue) {
        if (masterThread == null) {
            CountDownLatch latch = new CountDownLatch(1);
            masterThread = new MasterThread(queue, latch);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                queue.add(POISON_PILL);
                masterThread.shutdown();
            }));
            masterThread.start();
        }
    }

    public void shutdown(LinkedBlockingQueue<PageReader> queue) {
        queue.add(POISON_PILL);
        masterThread.shutdown();
        try {
            masterThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static class MasterThread extends Thread {
        private final ExecutorService executor;
        private final LinkedBlockingQueue<PageReader> queue;
        private final CountDownLatch latch;

        public MasterThread(LinkedBlockingQueue<PageReader> queue,
                            CountDownLatch latch) {
            this.queue = queue;
            this.latch = latch;
            executor = Executors.newCachedThreadPool();
        }

        @Override
        public void run() {
            while (!executor.isTerminated()) {
                try {
                    PageReader reader = queue.take();
                    if (reader == POISON_PILL) {
                        latch.countDown();
                        break;
                    }
                    executor.execute(reader);
                    System.out.println("A new thread has been started for reader " + reader.getUrl());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.out.println("Error executing thread");
                }
            }
        }

        public void shutdown() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            executor.shutdown();
            try {
                executor.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();;
            }
        }
    }
}
