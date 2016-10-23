package com.anobis.scraper.listener;

import com.anobis.scraper.data.Recipe;
import com.anobis.scraper.runner.PageReader;

/**
 * @author anobis
 */
public interface IStatusListener {
    /**
     * This event is submitted to the listener any time the
     * {@link com.anobis.scraper.runner.PageRunner} successfully finishes
     * a {@link PageReader} job and successfully constructs a {@link Recipe}.
     *
     * This {@link Recipe} is then sent in the notification
     *
     * @param recipe The {@link Recipe} that was found
     */
    void recipeFound(final Recipe recipe);

    /**
     * This event is triggered whenever a particular {@link PageReader} job
     * fails for some reason. The notification provided to the listener
     * is the specific {@link PageReader} that failed to complete.
     *
     * @param pageReader
     */
    void queryFailed(final PageReader pageReader);
}
