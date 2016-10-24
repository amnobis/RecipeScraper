package com.anobis.scraper.runner;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author anobis
 */
public interface IPageRunner {
    /**
     *
     * @param queue
     */
    void start(LinkedBlockingQueue<PageReader> queue);

    void shutdown();
}
