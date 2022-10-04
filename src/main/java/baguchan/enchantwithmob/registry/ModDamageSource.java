package baguchan.enchantwithmob.registry;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class ModDamageSource {
	public static DamageSource indirectMortal(Entity p_19368_, @Nullable Entity p_19369_) {
		return (new IndirectEntityDamageSource("enchantwithmob.indirectVoid", p_19368_, p_19369_)).bypassInvul().bypassArmor();
	}
}
