package rtr.mobs.jobs;

import org.newdawn.slick.Color;
import rtr.font.Text;

public class MobJobNone extends MobJobBase {
   public MobJobNone() {
      this.mobJobName = Text.getText("mobJob.unassigned.single");
      this.mobJobNamePlural = Text.getText("mobJob.unassigned.plural");
      this.description = Text.getText("mobJob.unassigned.description");
      this.baseColor = new Color(-16777216);
      this.accentColor = new Color(-16777216);
      this.notCounted = true;
   }
}
