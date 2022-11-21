package baguchan.enchantwithmob.compat;

import baguchan.enchantwithmob.client.render.layer.GeoEnchantAuraLayer;
import baguchan.enchantwithmob.client.render.layer.GeoEnchantLayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class GeckoLibCompatClient {
	public static void addLayer(EntityRenderer<?> r) {
		if (r instanceof GeoEntityRenderer) {
			((GeoEntityRenderer) r).addLayer(new GeoEnchantLayer((GeoEntityRenderer) r));

		}
		if (r instanceof LivingEntityRenderer<?, ?>) {
			((LivingEntityRenderer<?, ?>) r).addLayer(new GeoEnchantAuraLayer((LivingEntityRenderer<?, ?>) r));
		}
	}
}
