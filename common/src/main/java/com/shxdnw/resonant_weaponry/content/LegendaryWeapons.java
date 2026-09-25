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
                                            "40% armor penetration when below 40% health.")))),
            new LegendaryWeapon("gilded_arbiter", "Gilded Arbiter", WeaponType.HALBERD, ChatFormatting.GOLD, 11.5f, -2.8f,
                    LegendaryWeapon.Activation.instant(1200),
                    new LegendaryWeapon.Text(
                            List.of(
                                    "A heavy ceremonial polearm forged",
                                    "for the vanguard of the deep bastions.",
                                    "It remains as unyielding as the day it was cast."),
                            "Impenetrable Defense",
                            "Grants Slowness I for 10s and\nResistance II for 20s.",
                            List.of(
                                    new LegendaryWeapon.Passive("Showstopper",
                                            "Deals 1.5x damage to sprinting or airborne enemies.\nApplies Wither I to hostile mobs on hit."),
                                    new LegendaryWeapon.Passive("Counterweight",
                                            "Hitting while moving backward applies\nSlowness I for 5 seconds."))))
    );

    private LegendaryWeapons() {
    }
}
