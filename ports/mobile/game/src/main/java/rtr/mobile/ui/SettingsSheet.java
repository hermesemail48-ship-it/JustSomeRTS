package rtr.mobile.ui;

/** A full-screen sheet of battery/quality options, opened from the gear button. */
public final class SettingsSheet {
   public boolean open;

   public void render(final Ui ui) {
      float w = ui.width();
      float h = ui.height();
      float u = ui.unit();
      // Dim the game and swallow every touch while open.
      ui.rect(0.0F, 0.0F, w, h, Ui.DIM_SCREEN);
      ui.block(0.0F, 0.0F, w, h);
      float pw = Math.min(w * 0.86F, u * 12.0F);
      float rowH = u * 0.78F;
      float ph = u * 1.0F + rowH * 6.0F + u * 1.3F;
      if (ph > h * 0.94F) {
         rowH = (h * 0.94F - u * 2.3F) / 6.0F;
         ph = h * 0.94F;
      }

      float px = (w - pw) / 2.0F;
      float py = (h - ph) / 2.0F;
      ui.panel(px, py, pw, ph);
      ui.textCentered(ui.title, "Phone Settings", px + pw / 2.0F, py + u * 0.5F, Ui.TEXT);
      float y = py + u * 1.0F;
      float labelW = pw * 0.36F;
      float optX = px + labelW;
      float optW = pw - labelW - u * 0.3F;
      this.row(ui, "Frame rate", px, y, labelW, optX, optW, rowH, new String[]{"30 fps", "60 fps"}, PhoneSettings.fps >= 60 ? 1 : 0, new Choice() {
         @Override
         public void pick(int i) {
            PhoneSettings.fps = i == 1 ? 60 : 30;
            PhoneSettings.save();
         }
      });
      y += rowH;
      this.row(ui, "Battery saver", px, y, labelW, optX, optW, rowH, new String[]{"On", "Off"}, PhoneSettings.batterySaver ? 0 : 1, new Choice() {
         @Override
         public void pick(int i) {
            PhoneSettings.batterySaver = i == 0;
            PhoneSettings.save();
         }
      });
      y += rowH;
      int res = 0;

      for (int i = 0; i < PhoneSettings.RESOLUTIONS.length; i++) {
         if (Math.abs(PhoneSettings.RESOLUTIONS[i] - PhoneSettings.resolution) < 0.01F) {
            res = i;
         }
      }

      this.row(ui, "Resolution", px, y, labelW, optX, optW, rowH, PhoneSettings.RESOLUTION_NAMES, res, new Choice() {
         @Override
         public void pick(int i) {
            PhoneSettings.resolution = PhoneSettings.RESOLUTIONS[i];
            PhoneSettings.save();
         }
      });
      y += rowH;
      this.row(ui, "Clouds", px, y, labelW, optX, optW, rowH, new String[]{"On", "Off"}, PhoneSettings.clouds() ? 0 : 1, new Choice() {
         @Override
         public void pick(int i) {
            PhoneSettings.setClouds(i == 0);
         }
      });
      y += rowH;
      this.row(ui, "Shadows", px, y, labelW, optX, optW, rowH, new String[]{"On", "Off"}, PhoneSettings.shadows() ? 0 : 1, new Choice() {
         @Override
         public void pick(int i) {
            PhoneSettings.setShadows(i == 0);
         }
      });
      y += rowH;
      this.row(ui, "Particles", px, y, labelW, optX, optW, rowH, new String[]{"Minimal", "Reduced", "Full"}, PhoneSettings.particles(), new Choice() {
         @Override
         public void pick(int i) {
            PhoneSettings.setParticles(i);
         }
      });
      float bw = u * 2.6F;
      float bh = u * 0.8F;
      ui.button(px + (pw - bw) / 2.0F, py + ph - bh - u * 0.25F, bw, bh, "Done", true, Ui.BUTTON_ON, new Ui.Action() {
         @Override
         public void run() {
            SettingsSheet.this.open = false;
         }
      });
   }

   private interface Choice {
      void pick(int index);
   }

   private void row(Ui ui, String label, float px, float y, float labelW, float optX, float optW, float rowH, String[] options, int selected, final Choice choice) {
      float u = ui.unit();
      ui.text(ui.body, label, px + u * 0.35F, y + (rowH - ui.body.getCapHeight()) / 2.0F, Ui.TEXT_LIGHT);
      float gap = u * 0.12F;
      float bw = (optW - gap * (options.length - 1)) / options.length;
      float bh = rowH * 0.78F;

      for (int i = 0; i < options.length; i++) {
         final int index = i;
         ui.button(optX + i * (bw + gap), y + (rowH - bh) / 2.0F, bw, bh, options[i], true, i == selected ? Ui.BUTTON_ON : Ui.BUTTON, new Ui.Action() {
            @Override
            public void run() {
               choice.pick(index);
            }
         }, null, ui.small);
      }
   }
}
