package com.shxdnw.resonant_weaponry.content;

import net.minecraft.ChatFormatting;

import java.util.List;

public final class LegendaryWeapons {
    public static final List<LegendaryWeapon> ALL = List.of(
            new LegendaryWeapon("the_real_knife", "The Real Knife", WeaponType.DAGGER, ChatFormatting.RED, 5.0f, -1.0f,
                    LegendaryWeapon.Activation.channeled(600, 20, 100),
                    new LegendaryWeapon.Text(
                            List.of(
                                    "A simple kitchen knife.",
                                    "Its edge is impossibly sharp.",
                                    "Holding it fills you with an",
                                    "unsettling sense of determination."),
                            "Erasure",
                            "Channel energy into your knife, becoming still for 5s.\nUpon ending, release a violent explosion\nwhile teleporting to safety.",
                            List.of(
                                    new LegendaryWeapon.Passive("The First Move",
                                            "Double damage on your first strike to any target."),
                                    new LegendaryWeapon.Passive("Relentless",
                                            "40% armor penetration when below 40% health."))))
    );

    private LegendaryWeapons() {
    }
}
