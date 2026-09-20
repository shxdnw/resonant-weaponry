package com.shxdnw.resonant_weaponry.content;

public enum WeaponType {
    DAGGER("dagger"),
    SICKLE("sickle"),
    RAPIER("rapier"),
    KATANA("katana"),
    LONGSWORD("longsword"),
    SCYTHE("scythe"),
    HALBERD("halberd"),
    GREATSWORD("greatsword");

    private final String id;

    WeaponType(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
