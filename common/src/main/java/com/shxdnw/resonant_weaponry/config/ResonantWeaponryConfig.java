package com.shxdnw.resonant_weaponry.config;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import com.shxdnw.resonant_weaponry.content.MaterialTier;
import com.shxdnw.resonant_weaponry.content.WeaponType;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.minecraft.resources.Identifier;

import java.util.List;

public final class ResonantWeaponryConfig {
    public static final General general =
            ConfigApiJava.registerAndLoadConfig(General::new, RegisterType.BOTH);
    public static final MaterialTiers materialTiers =
            ConfigApiJava.registerAndLoadConfig(MaterialTiers::new, RegisterType.BOTH);
    public static final WeaponTypes weaponTypes =
            ConfigApiJava.registerAndLoadConfig(WeaponTypes::new, RegisterType.BOTH);
    public static final LegendaryWeapons legendaryWeapons =
            ConfigApiJava.registerAndLoadConfig(LegendaryWeapons::new, RegisterType.BOTH);

    private ResonantWeaponryConfig() {
    }

    public static void init() {
    }

    public static final class General extends Config {
        public General() {
            super(id("general"));
        }

        public boolean debugLogging = false;
        @ValidatedFloat.Restrict(min = 0f, max = 1f)
        public float legendaryLootChance = 0.01f;
        public List<String> lootTableBlacklist = List.of("village");
        public boolean friendlyFire = false;
    }

    @RequiresAction(action = Action.RESTART)
    public static final class MaterialTiers extends Config {
        public MaterialTiers() {
            super(id("material_tiers"));
        }

        public Iron iron = new Iron();
        public Gold gold = new Gold();
        public Diamond diamond = new Diamond();
        public Netherite netherite = new Netherite();

        public TierSection forTier(MaterialTier tier) {
            return switch (tier) {
                case IRON -> iron;
                case GOLD -> gold;
                case DIAMOND -> diamond;
                case NETHERITE -> netherite;
            };
        }

        public static class TierSection extends ConfigSection {
            public int durability;
            public float attackDamageBonus;
            public int enchantability;

            TierSection(int durability, float attackDamageBonus, int enchantability) {
                this.durability = durability;
                this.attackDamageBonus = attackDamageBonus;
                this.enchantability = enchantability;
            }
        }

        public static final class Iron extends TierSection {
            public Iron() {
                super(250, 2.0f, 14);
            }
        }

        public static final class Gold extends TierSection {
            public Gold() {
                super(32, 0.0f, 22);
            }
        }

        public static final class Diamond extends TierSection {
            public Diamond() {
                super(1561, 3.0f, 10);
            }
        }

        public static final class Netherite extends TierSection {
            public Netherite() {
                super(2031, 4.0f, 15);
            }
        }
    }

    @RequiresAction(action = Action.RESTART)
    public static final class WeaponTypes extends Config {
        public WeaponTypes() {
            super(id("weapon_types"));
        }

        public Dagger dagger = new Dagger();
        public Sickle sickle = new Sickle();
        public Rapier rapier = new Rapier();
        public Katana katana = new Katana();
        public Longsword longsword = new Longsword();
        public Scythe scythe = new Scythe();
        public Halberd halberd = new Halberd();
        public Greatsword greatsword = new Greatsword();

        public TypeSection forType(WeaponType type) {
            return switch (type) {
                case DAGGER -> dagger;
                case SICKLE -> sickle;
                case RAPIER -> rapier;
                case KATANA -> katana;
                case LONGSWORD -> longsword;
                case SCYTHE -> scythe;
                case HALBERD -> halberd;
                case GREATSWORD -> greatsword;
            };
        }

        public static class TypeSection extends ConfigSection {
            public float basedmg;
            public float atkspeed;
            public float duramulti;

            TypeSection(float basedmg, float atkspeed, float duramulti) {
                this.basedmg = basedmg;
                this.atkspeed = atkspeed;
                this.duramulti = duramulti;
            }
        }

        public static final class Dagger extends TypeSection {
            public Dagger() {
                super(0.5f, -1.0f, 0.65f);
            }
        }

        public static final class Sickle extends TypeSection {
            public Sickle() {
                super(1.0f, -1.2f, 0.75f);
            }
        }

        public static final class Rapier extends TypeSection {
            public float armorPenetration;

            public Rapier() {
                super(1.0f, -1.4f, 0.80f);
                this.armorPenetration = 0.20f;
            }
        }

        public static final class Katana extends TypeSection {
            public Katana() {
                super(1.5f, -1.6f, 0.90f);
            }
        }

        public static final class Longsword extends TypeSection {
            public Longsword() {
                super(3.5f, -2.4f, 1.00f);
            }
        }

        public static final class Scythe extends TypeSection {
            public Scythe() {
                super(4.5f, -2.7f, 1.10f);
            }
        }

        public static final class Halberd extends TypeSection {
            public Halberd() {
                super(6.0f, -2.9f, 1.20f);
            }
        }

        public static final class Greatsword extends TypeSection {
            public Greatsword() {
                super(7.5f, -3.1f, 1.35f);
            }
        }
    }

    @RequiresAction(action = Action.RESTART)
    public static final class LegendaryWeapons extends Config {
        public LegendaryWeapons() {
            super(id("legendary_weapons"));
        }

        public int durability = 3249;
        public TheRealKnife theRealKnife = new TheRealKnife();
        public GildedArbiter gildedArbiter = new GildedArbiter();

        public static final class TheRealKnife extends ConfigSection {
            public float armorPen = 0.40f;
            public int firstHitWindow = 100;
            public float relentlessBelow = 0.40f;
            public float explosionRadius = 25f;
            public float explosionDamage = 30f;
            public int teleportDistance = 30;
            public int downReach = 20;
            public int upReach = 50;
            public int riseDuration = 20;
            public int persistDuration = 100;
            public int columnheight = 100;
            public int columnDepth = 20;
        }

        public static final class GildedArbiter extends ConfigSection {
            public int defenseSlow = 200;
            public int defenseResistTime = 400;
            public int defenseResistLevel = 1;
            public float showstopperMult = 1.5f;
            public int witherTime = 60;
            public int witherLevel = 0;
            public int counterSlowTime = 100;
        }
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, path);
    }
}
