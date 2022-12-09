package baguchan.enchantwithmob.compat;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeckoLibCompatClient {
	public static void addLayer(EntityRenderer<?> r) {
		/*if (r instanceof GeoEntityRenderer) {
			((GeoEntityRenderer) r).addLayer(new GeoEnchantLayer((GeoEntityRenderer) r));

		}
		if (r instanceof LivingEntityRenderer<?, ?>) {
			((LivingEntityRenderer<?, ?>) r).addLayer(new GeoEnchantAuraLayer((LivingEntityRenderer<?, ?>) r));
		}*/
	}
}
