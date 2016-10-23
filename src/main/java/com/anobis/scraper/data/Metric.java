package com.anobis.scraper.data;

import com.google.common.base.MoreObjects;

/**
 * @author anobis
 */
public class Metric {
    public static enum Unit {
        gram,
        milligram,
        kilocalorie
    }
    private final Unit unit;
    private final int value;

    public Metric(Unit unit, int value) {
        this.unit = unit;
        this.value = value;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("unit", unit)
                .add("value", value)
                .toString();
    }
}
