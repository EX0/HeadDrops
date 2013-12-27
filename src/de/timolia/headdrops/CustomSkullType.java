/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.headdrops;

public enum CustomSkullType {

	// @formatter:off
	
    BLAZE("MHF_Blaze", "Blaze Head", "blaze"),
    CAVE_SPIDER("MHF_CaveSpider", "Cave Spider Head", "cavespider"),
    CHICKEN("MHF_Chicken", "Chicken Head", "chicken"),
    COW("MHF_Cow", "Cow Head", "cow"),
    ENDERMAN("MHF_Enderman", "Enderman Head", "enderman"),
    GHAST("MHF_Ghast", "Ghast Head", "ghast"),
    IRON_GOLEM("MHF_Golem", "Iron Golem Head", "irongolem", "golem"),
    MAGMA_CUBE("MHF_LavaSlime", "Magma Cube Head", "magmacube", "lava_slime", "lavaslime"),
    MUSHROOM_COW("MHF_MushroomCow", "Mushroom Cow Head", "mushroomcow", "mooshrom"),
    OCELOT("MHF_Ocelot", "Ocelot Head", "cat"),
    PIG("MHF_Pig", "Pig Head", "pig"),
    PIG_ZOMBIE("MHF_PigZombie", "Pig Zombie Head", "pigzombie", "pigman", "zombie_pigman", "zombiepigman"),
    SHEEP("MHF_Sheep", "Sheep Head", "sheep"),
    SLIME("MHF_Slime", "Slime Head", "slime"),
    SPIDER("MHF_Spider", "Spider Head", "spider"),
    SQUID("MHF_Squid", "Squid Head", "squid"),
    VILLAGER("MHF_Villager", "Villager Head", "villager"),
    WITHER("MHF_Wither", "Wither Head", "wither");

    // @formatter:on

	private String skin;
	private String displayName;
	private String[] otherNames;

	private CustomSkullType(String skin, String displayName, String... otherNames) {
		this.skin = skin;
		this.displayName = displayName;
		this.otherNames = otherNames;
	}

	public String getSkinName() {
		return this.skin;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public static CustomSkullType forSkullOwnerString(String skullOwner) {
		for (CustomSkullType t : values())
			if (t.getSkinName().equalsIgnoreCase(skullOwner))
				return t;

		return null;
	}

	public static CustomSkullType forGeneralEntityName(String entityName) {
		for (CustomSkullType t : values()) {
			if (entityName.equalsIgnoreCase(t.toString()))
				return t;
			else
				for (String otherName : t.otherNames)
					if (otherName.equalsIgnoreCase(entityName))
						return t;
		}

		return null;
	}

}
