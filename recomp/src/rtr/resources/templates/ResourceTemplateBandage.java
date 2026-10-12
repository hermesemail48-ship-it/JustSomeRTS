package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateBandage extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.bandage.single");
      this.namePlural = Text.getText("resource.bandage.plural");
      this.description = Text.getText("resource.bandage.description");
      this.baseResources.add(ResourceModule.ResourceType.SILK);
      this.trashResource = ResourceModule.ResourceType.TRASHY_TRASH;
      this.resourceSlot = ResourceModule.ResourceSlot.RECOVERY;
      this.resourceCategory = ResourceModule.ResourceCategory.RECOVERY;
      this.baseValue = 40;
      this.usesMax = 5;
      this.useTime = 400;
      this.healthPercentPerUse = 20;
      this.baseDurability = 1000;
   }
}
