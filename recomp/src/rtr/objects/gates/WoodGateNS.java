package rtr.objects.gates;

import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.save.YMLDataMap;

public class WoodGateNS extends GateBase {
   public WoodGateNS(int x, int y, ObjectBase g) throws SlickException {
      super(x, y, g);
      this.gateType = GateBase.GateType.WOOD_GATE_NS;
      this.sprite = this.object.createGateSpriteSheet(this.gateType);
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }

   public WoodGateNS(YMLDataMap dataMap, ObjectBase g) throws SlickException {
      super(dataMap, g);
   }
}
