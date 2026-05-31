package cxim.attributetooltipfix116x;

import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Utility class for swapping base attribute UUIDs.
 * Extends Item solely to access the protected static UUID constants.
 */
public class UUIDSwap extends Item {
    public UUIDSwap(Item.Properties properties) {
        super(properties);
        throw new IllegalStateException("This class should never be instantiated");
    }

    /**
     * Swaps the given UUID with the canonical instance if it matches
     * base attack damage or base attack speed UUIDs.
     */
    public static UUID swapOrSelf(@Nullable UUID uuid) {
        // 1.16.5 MCP names: ATTACK_DAMAGE_MODIFIER / ATTACK_SPEED_MODIFIER
        if (ATTACK_DAMAGE_MODIFIER.equals(uuid)) {
            return ATTACK_DAMAGE_MODIFIER;
        }
        if (ATTACK_SPEED_MODIFIER.equals(uuid)) {
            return ATTACK_SPEED_MODIFIER;
        }
        return uuid;
    }
}
