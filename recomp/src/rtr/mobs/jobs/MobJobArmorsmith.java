package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobArmorsmith extends MobJobBase {
   public MobJobArmorsmith() {
      this.mobJobName = Text.getText("mobJob.armorsmith.single");
      this.mobJobNamePlural = Text.getText("mobJob.armorsmith.plural");
      this.description = Text.getText("mobJob.armorsmith.description");
      this.baseColor = new Color(-30208);
      this.accentColor = new Color(-11534189);
      this.refineSpeedBase = 1000;
      this.preferredTools.add(ResourceModule.ResourceType.HAMMER);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.IRON_HELMET);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.IRON_SHIELD);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.LEATHER_HELMET);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.LEATHER_SHIELD);
      this.canWorkerSortClosest.add(ResourceModule.ResourceType.RAW_HIDE);
      this.canWorkerRefine.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
      this.canWorkerRefine.add(ResourceModule.ResourceType.IRON_HELMET);
      this.canWorkerRefine.add(ResourceModule.ResourceType.IRON_SHIELD);
      this.canWorkerRefine.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
      this.canWorkerRefine.add(ResourceModule.ResourceType.LEATHER_HELMET);
      this.canWorkerRefine.add(ResourceModule.ResourceType.LEATHER_SHIELD);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_HELMET);
      this.canWorkerStore.add(ResourceModule.ResourceType.IRON_SHIELD);
      this.canWorkerStore.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
      this.canWorkerStore.add(ResourceModule.ResourceType.LEATHER_HELMET);
      this.canWorkerStore.add(ResourceModule.ResourceType.LEATHER_SHIELD);
      this.canWorkerStore.add(ResourceModule.ResourceType.RAW_HIDE);
      this.canWorkerStoreTrashCanOnly.add(ResourceModule.ResourceType.TRASHY_TRASH);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_INGOT);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_HELMET);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.IRON_SHIELD);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.LEATHER_HELMET);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.LEATHER_SHIELD);
      this.canWorkerRedistribute.add(ResourceModule.ResourceType.RAW_HIDE);
   }
}
