package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobOccultist extends MobJobBase {
   public MobJobOccultist() {
      this.mobJobName = Text.getText("mobJob.occultist.single");
      this.mobJobNamePlural = Text.getText("mobJob.occultist.plural");
      this.description = Text.getText("mobJob.occultist.description");
      this.baseColor = new Color(-11513606);
      this.accentColor = new Color(-3618561);
      this.refineSpeedBase = 10000;
      this.prayingSpeedBase = 1800;
      this.capturingSoulSpeedBase = 2500;
      this.canPray = true;
      this.canCaptureSouls = true;
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerSortEvenly.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL);
      this.canWorkerRefine.add(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL);
      this.canWorkerStore.add(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerStore.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.EMPTY_EERIE_VESSEL);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.GOLD_INGOT);
   }
}
