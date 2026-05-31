package cxim.attributetooltipfix116x.mixin;

import cxim.attributetooltipfix116x.UUIDSwap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.UUID;

/**
 * Mixin for AttributeModifier to canonicalize UUIDs at the source.
 *
 * <p>Two injection points are used:</p>
 * <ul>
 *   <li><b>Constructor (&lt;init&gt;):</b> intercepts UUID passed to
 *       {@code new AttributeModifier(UUID, String, double, Operation)}.
 *       This covers CraftTweaker / KubeJS direct instantiation and any other
 *       mod that creates modifiers via {@code new}.</li>
 *   <li><b>read(CompoundNBT):</b> intercepts UUID loaded from NBT.
 *       This covers attribute modifiers stored on item stacks.</li>
 * </ul>
 */
@Mixin(AttributeModifier.class)
public class AttributeModifierMixin {

    /**
     * Constructor injection — the key to covering CraftTweaker / KubeJS.
     *
     * <p>In 1.16.5 MCP the constructor signature is:</p>
     * <pre>(UUID, String, double, AttributeModifier.Operation)</pre>
     *
     * <p>{@code argsOnly = true} + {@code ordinal = 0} targets the first
     * real parameter (the UUID), excluding the implicit {@code this}.</p>
     */
    @ModifyVariable(
        method = "<init>(Ljava/util/UUID;Ljava/lang/String;DLnet/minecraft/entity/ai/attributes/AttributeModifier$Operation;)V",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0
    )
    private static UUID attributetooltipfix$initSwapUUID(UUID original) {
        return UUIDSwap.swapOrSelf(original);
    }

    /**
     * NBT deserialization injection.
     *
     * <p>In 1.16.5 MCP the static factory method is {@code read(CompoundNBT)}
     * (SRG: func_233800_a_).</p>
     */
    @ModifyVariable(
        method = "read",
        at = @At(value = "STORE"),
        ordinal = 0
    )
    private static UUID attributetooltipfix$readSwapUUID(UUID uuid) {
        return UUIDSwap.swapOrSelf(uuid);
    }
}
