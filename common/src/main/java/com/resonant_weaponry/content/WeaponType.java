package com.resonant_weaponry.content;

public enum WeaponType {
    DAGGER("dagger", 0.5F, -1.0F, 0.65F),
    SICKLE("sickle", 1.0F, -1.2F, 0.75F),
    RAPIER("rapier", 1.0F, -1.4F, 0.80F),
    KATANA("katana", 1.5F, -1.6F, 0.90F),
    LONGSWORD("longsword", 3.5F, -2.4F, 1.00F),
    SCYTHE("scythe", 4.5F, -2.7F, 1.10F),
    HALBERD("halberd", 6.0F, -2.9F, 1.20F),
    GREATSWORD("greatsword", 7.5F, -3.1F, 1.35F);

    private final String id;
    private final float baseDamage;
    private final float attackSpeed;
    private final float durabilityMultiplier;

    WeaponType(String id, float baseDamage, float attackSpeed, float durabilityMultiplier) {
        this.id = id;
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.durabilityMultiplier = durabilityMultiplier;
    }

    public String id() {
        return id;
    }

    public float baseDamage() {
        return baseDamage;
    }

    public float attackSpeed() {
        return attackSpeed;
    }

    public float durabilityMultiplier() {
        return durabilityMultiplier;
    }
}
