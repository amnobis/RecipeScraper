package com.anobis.scraper.runner;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author anobis
 */
public interface IPageRunner {
    void start(LinkedBlockingQueue<PageReader> queue);

    void shutdown(LinkedBlockingQueue<PageReader> queue);
}
