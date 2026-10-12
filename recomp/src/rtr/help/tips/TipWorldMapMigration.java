package rtr.help.tips;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.states.WorldMapGUIData;
import rtr.mobs.MobBase;

public class TipWorldMapMigration extends TipBase {
   public TipWorldMapMigration() {
      this.tipName = "tipHarvestingResources";
      this.tipHeader = Text.getText("helpTip.tip.worldMapMigrationHeader");
      this.finalize();
   }

   @Override
   protected void findTipConditions() throws SlickException {
      if (SettingsParser.getLastGameMode() != null && WorldMapGUIData.getFullMigrantList().size() > 0) {
         HashMap<String, ArrayList<MobBase>> allMigratingRegions = this.migration.getAllEnroutePopulation();

         for (String s : allMigratingRegions.keySet()) {
            ArrayList<MobBase> regions = allMigratingRegions.get(s);
            if (regions.size() >= 10) {
               Text.setVariableText("regionName", s);
               this.tipBody = Text.getText("helpTip.tip.worldMapMigration");
               this.worldMapTip = true;
               this.foundTipCondition = true;
               return;
            }
         }
      }

      this.clearAttachmentData();
      this.foundTipCondition = false;
   }
}
