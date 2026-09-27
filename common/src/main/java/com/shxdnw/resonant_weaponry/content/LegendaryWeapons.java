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
                                            "Every 3rd hit applies an aftercut.\nAfter 4s, deals magic damage\nand knocks the target back.")))),
            new LegendaryWeapon("blood_scourge", "Blood Scourge", WeaponType.SICKLE, ChatFormatting.DARK_RED,
                    LegendaryWeapon.Activation.instant(300),
                    new LegendaryWeapon.Text(
                            List.of(
                                    "An ancient cultist's tool repurposed for war."),
                            "Eviscerate",
                            "Unleash a bloody slash in a 5-block reach,\npulling enemies closer and afflicting them\nwith Wither, Blindness, and Slowness.",
                            List.of(
                                    new LegendaryWeapon.Passive("Sanguine",
                                            "Killing a hostile mob restores 50% of your HP.\n30s cooldown."),
                                    new LegendaryWeapon.Passive("Hemorrhagic Shock",
                                            "Hitting a debuffed enemy triggers a\nblood explosion dealing AoE damage.\n20s cooldown.")))),
            new LegendaryWeapon("voidfang", "Voidfang", WeaponType.RAPIER, ChatFormatting.DARK_PURPLE,
                    LegendaryWeapon.Activation.instant(300),
                    new LegendaryWeapon.Text(
                            List.of(
                                    "A rapier that doesn't reflect light.",
                                    "The blade seems to bend toward the Void."),
                            "Rupture",
                            "Surge forward 5 blocks, leaving a void rift.\n0.5s later, your trail erupts,\ndealing heavy damage and knockback.",
                            List.of(
                                    new LegendaryWeapon.Passive("Voidscar",
                                            "Every hit builds a Voidscar stack.\nAt 4 stacks, deals bonus true damage."),
                                    new LegendaryWeapon.Passive("Event Horizon",
                                            "20% chance to nullify incoming damage\nwhile you have Voidscar stacks.")))),
            new LegendaryWeapon("yashas_edge", "Yasha's Edge", WeaponType.KATANA, ChatFormatting.DARK_AQUA,
                    LegendaryWeapon.Activation.instant(400),
                    new LegendaryWeapon.Text(
                            List.of(
                                    "A swift Nether blade.",
                                    "They say if you're fast enough with the draw,",
                                    "you can hit someone twice with a single swing."),
                            "Yasha's Vengeance",
                            "Dash forward with a sweeping strike.\nEnemies hit take magic damage.\nAn aftercut deals additional magic damage\nto everything in your wake.",
                            List.of(
                                    new LegendaryWeapon.Passive("Momentum",
                                            "Each hit grants +5% attack speed (max 40%).\nDecays after 5s without hitting."),
                                    new LegendaryWeapon.Passive("Anchor",
                                            "Hitting an enemy applies Slowness for 3s.\n8s cooldown per weapon."))))
    );

    private LegendaryWeapons() {
    }
}
