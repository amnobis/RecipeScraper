package com.anobis.scraper.runner;

/**
 * @author anobis <austin.nobis@amd.com>
 */
public interface PageReader extends Runnable {
    String getUrl();
}
