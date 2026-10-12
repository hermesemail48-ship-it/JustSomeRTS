package rtr.resources.templates;

import rtr.font.Text;
import rtr.resources.ResourceModule;

public class ResourceTemplateRock extends ResourceTemplateBase {
   @Override
   public void load() {
      super.load();
      this.name = Text.getText("resource.rock.single");
      this.namePlural = Text.getText("resource.rock.plural");
      this.description = Text.getText("resource.rock.description");
      this.resourceSlot = ResourceModule.ResourceSlot.NONE;
      this.trashResource = ResourceModule.ResourceType.ROCKY_TRASH;
      this.resourceCategory = ResourceModule.ResourceCategory.RAW;
      this.baseValue = 5;
      this.baseDurability = 4000;
   }
}
