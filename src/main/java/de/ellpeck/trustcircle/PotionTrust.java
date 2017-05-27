package de.ellpeck.trustcircle;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionTrust extends Potion{

    private static final ResourceLocation RES_LOC = new ResourceLocation(TrustCircle.MOD_ID, "textures/potion.png");

    public PotionTrust(String name){
        super(false, 0);
        this.setBeneficial();

        this.setPotionName(TrustCircle.MOD_ID+".potion."+name+".name");
        this.setIconIndex(0, 0);

        this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "39EC7064-6A60-4F59-8BBE-C2C23A6DD7A9", 0D, 0);

        GameRegistry.register(this, new ResourceLocation(TrustCircle.MOD_ID, name));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex(){
        Minecraft.getMinecraft().renderEngine.bindTexture(RES_LOC);
        return super.getStatusIconIndex();
    }

    @Override
    public boolean isReady(int duration, int amplifier){
        if(TrustCircle.baseRegen > 0){
            int k = 40 >> amplifier;
            return k <= 0 || duration%k == 0;
        }
        else{
            return false;
        }
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier){
        if(TrustCircle.baseRegen > 0){
            if(entity.getHealth() < entity.getMaxHealth()){
                entity.heal(TrustCircle.baseRegen);
            }
        }
    }

    @Override
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier){
        if(TrustCircle.baseStrength > 0){
            return TrustCircle.baseStrength*(amplifier+1);
        }
        else{
            return super.getAttributeModifierAmount(amplifier, modifier);
        }
    }
}
