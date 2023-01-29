package org.example.item1;

public class LOLTier {

    public static <E extends LOLTier> LOLTier getLoLTier(Integer score) {
        if (score < 100) {
            return new BronzeTier();
        } else if (score > 100 & score <= 300) {
            return new SilverTier();
        } else {
            return new GoldTier();
        }
    }
}
