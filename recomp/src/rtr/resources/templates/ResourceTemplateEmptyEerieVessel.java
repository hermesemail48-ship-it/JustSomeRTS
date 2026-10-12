package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateEmptyEerieVessel extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.emptyEerieVessel.single");
      this.namePlural = Text.getText("resource.emptyEerieVessel.plural");
      this.description = Text.getText("resource.emptyEerieVessel.description");
      this.baseResources.add(ResourceModule.ResourceType.GOLD_INGOT);
      this.baseResources.add(ResourceModule.ResourceType.IRON_INGOT);
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.resourceCategory = ResourceModule.ResourceCategory.MISCELLANEOUS;
      this.canTrade = true;
      this.baseValue = 200;
      this.dontAddMisc = true;
      this.baseDurability = 10000;
   }
}
