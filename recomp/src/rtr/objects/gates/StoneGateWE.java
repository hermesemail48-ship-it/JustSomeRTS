package rtr.objects.gates;

import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.save.YMLDataMap;

public class StoneGateWE extends GateBase {
   public StoneGateWE(int x, int y, ObjectBase g) throws SlickException {
      super(x, y, g);
      this.gateType = GateBase.GateType.STONE_GATE_WE;
      this.sprite = this.object.createGateSpriteSheet(this.gateType);
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }

   public StoneGateWE(YMLDataMap dataMap, ObjectBase g) throws SlickException {
      super(dataMap, g);
   }
}
