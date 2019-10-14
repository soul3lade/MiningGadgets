package com.direwolf20.mininggadgets;

import com.direwolf20.mininggadgets.common.blocks.ModBlocks;
import com.direwolf20.mininggadgets.common.containers.ModContainers;
import com.direwolf20.mininggadgets.common.network.PacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod(MiningGadgets.MOD_ID)
public class MiningGadgets
{
    public static final String MOD_ID = "mininggadgets";

    private static final Logger LOGGER = LogManager.getLogger();
    public static Setup setup = new Setup();

    public MiningGadgets() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ModBlocks::registerRenderers);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        setup.init();
        PacketHandler.register();
    }

    /**
     * Only run on the client making it a safe place to register client
     * components. Remember that you shouldn't reference client only
     * methods in this class as it'll crash the mod :P
     */
    private void doClientStuff(final FMLClientSetupEvent event) {
        // Register the container screens.
        ModContainers.registerContainerScreens();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}

    /**
     * Intermod communication
     */
    // Register the enqueueIMC method for modloading
    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(MOD_ID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    // Register the processIMC method for modloading
    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }


    public static Logger getLogger() {
        return LOGGER;
    }
}
