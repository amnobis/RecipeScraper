package com.anobis.scraper.listener;

import com.anobis.scraper.data.Recipe;
import com.anobis.scraper.runner.PageReader;

/**
 * @author anobis
 */
public interface IStatusListener {
    void recipeFound(final Recipe recipe);

    void queryFailed(final PageReader pageReader);
}
