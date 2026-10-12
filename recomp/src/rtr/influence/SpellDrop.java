package rtr.influence;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.resources.ResourceBase;

public class SpellDrop extends SpellBase {
   protected int targetX;
   protected int targetY;
   protected int tileX;
   protected int tileY;
   protected int duration;
   protected int durationTick;

   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.DROP;
      this.name = Text.getText("spell.drop.name");
      this.description = Text.getText("spell.drop.description");
      this.castCost = freeCast ? 0 : 0;
   }

   @Override
   public void update() throws SlickException {
      switch (this.spellStep) {
         case 0:
            MobBase mobSelection = this.influence.getMobGrabbed();
            if (mobSelection != null) {
               mobSelection.setGrabbed(false);
               this.influence.setMobGrabbed(null);
               this.influence.setSelectedSpell(SpellBase.SpellType.GRAB);
            }

            ResourceBase resourceSelection = this.influence.getResourceGrabbed();
            if (resourceSelection != null) {
               resourceSelection.drop();
               this.influence.setResourceGrabbed(null);
               this.influence.setSelectedSpell(SpellBase.SpellType.GRAB);
            }

            this.mob.tryPanicInArea(this.tileX, this.tileY, 2, true, 1);
            this.mob.tryConfuseInArea(this.tileX, this.tileY, 4, true, 1);
            this.influence.removeSpell(this);
      }
   }

   @Override
   public boolean canCast() {
      return true;
   }
}
