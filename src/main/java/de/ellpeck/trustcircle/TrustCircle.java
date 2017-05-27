package de.ellpeck.trustcircle;

import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TrustCircle.MOD_ID, name = TrustCircle.NAME, version = TrustCircle.VERSION, guiFactory = "de.ellpeck.trustcircle.GuiFactory")
public class TrustCircle{

    public static final String MOD_ID = "trustcircle";
    public static final String NAME = "TrustCircle";
    public static final String VERSION = "@VERSION@";

    public static Potion potionTrust;

    public static Configuration config;

    public static double maxRange;
    public static int updateInterval;
    public static boolean allowMultiplePlayers;

    public static double baseCalcModifier;
    public static double durationModifier;
    public static double amplifierModifier;

    public static double baseStrength;
    public static float baseRegen;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        defineConfigValues();

        potionTrust = new PotionTrust("trust");

        MinecraftForge.EVENT_BUS.register(new Events());
    }

    public static void defineConfigValues(){
        maxRange = config.get(Configuration.CATEGORY_GENERAL, "maxRange", 8D, "The maximum distance a player can be away from another for the trust effect to take place", 1D, 50D).getDouble();
        updateInterval = config.get(Configuration.CATEGORY_GENERAL, "updateInterval", 20, "The amount of ticks between updates and reapplication of the potion effect", 1, 100).getInt();
        allowMultiplePlayers = config.get(Configuration.CATEGORY_GENERAL, "allowMultiplePlayers", true, "If multiple players can influence the strength of the trust effect").getBoolean();

        baseCalcModifier = config.get(Configuration.CATEGORY_GENERAL, "baseCalcModifier", 5D, "The modifier used to determine both the duration and the amplifier of the effect", 0D, 100D).getDouble();
        durationModifier = config.get(Configuration.CATEGORY_GENERAL, "durationModifier", 50D, "The modifier used to determine the duration of the effect", 0D, 500D).getDouble();
        amplifierModifier = config.get(Configuration.CATEGORY_GENERAL, "amplifierModifier", 0.5D, "The modifier used to determine the amplifier of the effect", 0D, 100D).getDouble();

        baseStrength = config.get(Configuration.CATEGORY_GENERAL, "baseStrength", 1D, "The base strength effect that will be applied through the trust potion effect, set to 0 to disable", 0D, 10D).getDouble();
        baseRegen = (float)config.get(Configuration.CATEGORY_GENERAL, "baseRegen", 0.75D, "The base regen effect that will be applied through the trust potion effect, set to 0 to disable", 0D, 10D).getDouble();

        if(config.hasChanged()){
            config.save();
        }
    }
}
