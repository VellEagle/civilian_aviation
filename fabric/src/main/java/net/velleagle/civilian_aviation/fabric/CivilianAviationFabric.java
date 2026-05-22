package net.velleagle.civilian_aviation.fabric;

import immersive_aircraft.fabric.CommonFabric;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.fabricmc.api.ModInitializer;

public class CivilianAviationFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Force loading the Immersive Aircraft class to have networking and registration loaded
        new CommonFabric();
        CivilianAviation.init();
    }
}
