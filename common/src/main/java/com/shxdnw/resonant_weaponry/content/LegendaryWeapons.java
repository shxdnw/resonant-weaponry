package com.shxdnw.resonant_weaponry.content;

import net.minecraft.ChatFormatting;

import java.util.List;

public final class LegendaryWeapons {
    public static final List<LegendaryWeapon> ALL = List.of(
            new LegendaryWeapon("the_real_knife", "The Real Knife", WeaponType.DAGGER, ChatFormatting.RED,
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
            new LegendaryWeapon("gilded_arbiter", "Gilded Arbiter", WeaponType.HALBERD, ChatFormatting.GOLD,
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
                                            "Hitting while moving backward applies\nSlowness I for 5 seconds.")))),
            new LegendaryWeapon("gale_cutter", "Gale Cutter", WeaponType.SCYTHE, ChatFormatting.GREEN,
                    LegendaryWeapon.Activation.instant(400),
                    new LegendaryWeapon.Text(
                            List.of(
                                    "A scythe that commands the storm.",
                                    "Each swing whistles with the force",
                                    "of gale, leaving a vacuum",
                                    "in its wake."),
                            "Cyclone",
                            "Unleash a violent wind blast in a 7-block radius,\ndealing magic damage and launching\nenemies away from you.",
                            List.of(
                                    new LegendaryWeapon.Passive("Tailwind",
                                            "On hit, gain Speed II for 5s.\n10s cooldown."),
                                    new LegendaryWeapon.Passive("Aftercut",
                                            "Every 3rd hit applies an aftercut.\nAfter 4s, deals magic damage\nand knocks the target back."))))
    );

    private LegendaryWeapons() {
    }
}
