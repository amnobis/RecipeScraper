package com.anobis.scraper.runner;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author anobis <austin.nobis@amd.com>
 */
public interface IPageRunner {
    void start(LinkedBlockingQueue<PageReader> queue);

    void shutdown(LinkedBlockingQueue<PageReader> queue);
}
