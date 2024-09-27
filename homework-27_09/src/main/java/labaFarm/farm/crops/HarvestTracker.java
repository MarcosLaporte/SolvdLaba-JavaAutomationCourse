package labaFarm.farm.crops;

import labaFarm.services.LoggerService;
import org.apache.logging.log4j.Level;

import static labaFarm.farm.crops.CropSector.GrowthStage;

public class HarvestTracker extends Thread {
    public final CropSector cropSector;

    public HarvestTracker(CropSector cropSector) {
        this.cropSector = cropSector;
    }

    public static String getThreadName(CropSector cropSector) {
        return "Tracker-" + cropSector.hashCode();
    }

    @Override
    public void run() {
        try {
            this.setName("Tracker-" + this.cropSector.hashCode());
            LoggerService.consoleLog(Level.INFO, String.format("%s-%s is being tracked!", this.cropSector.type, this.cropSector.hashCode()));
            while (this.cropSector.currentGrowthStage != GrowthStage.HARVEST) {
                Thread.sleep(5000);
                // Simulate growth progress
                this.progressGrowthStage(this.cropSector);
            }

            LoggerService.consoleLog(Level.INFO, String.format("%s-%s is ready to harvest!", this.cropSector.type, this.cropSector.hashCode()));
        } catch (InterruptedException e) {
            LoggerService.consoleLog(Level.ERROR, "Harvest tracker interrupted!");
        }
    }

    private void progressGrowthStage(CropSector cropSector) {
        GrowthStage gs = cropSector.currentGrowthStage;
        if (gs != GrowthStage.HARVEST)
            cropSector.currentGrowthStage = GrowthStage.getGrowthStage(gs.getValue() + 1);
    }
}
