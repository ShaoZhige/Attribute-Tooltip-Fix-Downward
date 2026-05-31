package cxim.attributetooltipfix116x.mixin;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import cxim.attributetooltipfix116x.UUIDSwap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;
import java.util.UUID;

/**
 * Mixin for ItemStack to:
 * 1. Replace the attribute modifier multimap with a LinkedHashMultimap,
 *    preserving insertion order for tooltips.
 * 2. Normalize AttributeModifier UUIDs to canonical instances.
 *    This ensures CraftTweaker / KubeJS added modifiers with matching
 *    UUID values are recognized as base attributes by the tooltip renderer.
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {

    @ModifyVariable(
        method = "getAttributeModifiers",
        at = @At(value = "RETURN"),
        ordinal = 0
    )
    private Multimap<Attribute, AttributeModifier> attributetooltipfix$getAttributeModifiers(
            Multimap<Attribute, AttributeModifier> original) {
        LinkedHashMultimap<Attribute, AttributeModifier> result = LinkedHashMultimap.create();

        for (Map.Entry<Attribute, AttributeModifier> entry : original.entries()) {
            AttributeModifier modifier = entry.getValue();
            UUID canonicalId = UUIDSwap.swapOrSelf(modifier.getID());

            if (canonicalId != modifier.getID()) {
                // UUID value matches but object reference differs.
                // Create a new AttributeModifier with the canonical UUID instance
                // so Minecraft's tooltip renderer recognises it via == comparison.
                AttributeModifier replacement = new AttributeModifier(
                    canonicalId,
                    modifier.getName(),
                    modifier.getAmount(),
                    modifier.getOperation()
                );
                result.put(entry.getKey(), replacement);
            } else {
                result.put(entry.getKey(), modifier);
            }
        }

        return result;
    }
}
