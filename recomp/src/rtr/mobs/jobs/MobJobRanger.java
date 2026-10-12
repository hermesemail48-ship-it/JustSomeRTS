package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;
import rtr.resources.ResourceModule;

public class MobJobRanger extends MobJobBase {
   public MobJobRanger() {
      this.mobJobName = Text.getText("mobJob.ranger.single");
      this.mobJobNamePlural = Text.getText("mobJob.ranger.plural");
      this.description = Text.getText("mobJob.ranger.description");
      this.baseColor = new Color(-12303292);
      this.accentColor = new Color(-12320768);
      this.bonusDefensePercent = 60;
      this.bonusDamagePercent = 40;
      this.preferredTools.add(ResourceModule.ResourceType.IRON_CHEST_ARMOR);
      this.preferredTools.add(ResourceModule.ResourceType.IRON_HELMET);
      this.preferredTools.add(ResourceModule.ResourceType.LEATHER_CHEST_ARMOR);
      this.preferredTools.add(ResourceModule.ResourceType.LEATHER_HELMET);
      this.preferredTools.add(ResourceModule.ResourceType.BANDAGE);
      this.preferredTools.add(ResourceModule.ResourceType.HEALING_POTION);
      this.preferredTools.add(ResourceModule.ResourceType.IRON_SWORD);
      this.preferredTools.add(ResourceModule.ResourceType.BOW);
      this.preferredTools.add(ResourceModule.ResourceType.QUIVER);
      this.isGuard = true;
      this.canCaptureAnimals = true;
   }
}
