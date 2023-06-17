package baguchan.enchantwithmob.api;

import baguchan.enchantwithmob.capability.MobEnchantCapability;

import javax.annotation.Nullable;

public interface IEnchantCap {

    @Nullable
    MobEnchantCapability getEnchantCap();

    void setEnchantCap(MobEnchantCapability capability);
}
