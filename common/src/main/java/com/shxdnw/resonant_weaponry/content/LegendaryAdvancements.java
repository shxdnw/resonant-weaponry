package com.shxdnw.resonant_weaponry.content;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;

import java.util.List;

public final class LegendaryAdvancements {
    public static final Entry ROOT =
            new Entry("root", "gale_cutter", "Resonant Weaponry", "Obtain any weapon.");

    public static final List<Entry> ALL = List.of(
            new Entry("cataclysm", "calamity", "Cataclysm", "Obtained the Calamity"),
            new Entry("determination", "the_real_knife", "DETERMINATION.", "Obtained The Real Knife"),
            new Entry("golden_verdict", "gilded_arbiter", "The Golden Verdict", "Obtained the Gilded Arbiter"),
            new Entry("spirit_blade", "yashas_edge", "Spirit Blade", "Obtained the Yasha's Edge"),
            new Entry("the_harvest", "blood_scourge", "The Harvest", "Obtained the Blood Scourge"),
            new Entry("voidborne", "voidfang", "Voidborne", "Obtained the Voidfang"),
            new Entry("wind_shear", "gale_cutter", "Wind Shear", "Obtained the Gale Cutter"));

    public record Entry(String file, String weaponId, String title, String description) {
    }

    public static String titleKey(Entry entry) {
        return "advancements." + ResonantWeaponry.MOD_ID + "." + entry.file() + ".title";
    }

    public static String descriptionKey(Entry entry) {
        return "advancements." + ResonantWeaponry.MOD_ID + "." + entry.file() + ".description";
    }

    private LegendaryAdvancements() {
    }
}
