package com.anobis.scraper.runner;

/**
 * @author anobis
 */
public interface PageReader extends Runnable {
    /**
     * Returns the URL for the given page that is to be read
     * once the {@link PageReader} is submitted to and executed
     * by its runner.
     *
     * @return the URL for the page to be read
     */
    String getUrl();
}
