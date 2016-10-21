package com.anobis.scraper;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author anobis <austin.nobis@amd.com>
 */
public interface IPageRunner {
    void start(LinkedBlockingQueue<PageReader> queue);
}
