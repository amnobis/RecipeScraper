package com.anobis.scraper.listener;

import com.anobis.scraper.data.Recipe;

/**
 * @author anobis <austin.nobis@amd.com>
 */
public interface IStatusListener {
   void recipeFound(final Recipe recipe);
}
