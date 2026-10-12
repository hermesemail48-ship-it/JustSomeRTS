package rtr.gui.buttons;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import rtr.font.Text;

public class GUIButtonInputText extends GUIButton {
   private GameContainer gc;
   private String defaultText;
   private TextField textInput;
   private int fontSize;

   public GUIButtonInputText(GameContainer gc, String bName, String dText, int maxChar, int fS) throws SlickException {
      this(gc, bName, dText, maxChar, maxChar, fS);
   }

   public GUIButtonInputText(GameContainer gc, String bName, String dText, int maxChar, int boxSize, int fS) throws SlickException {
      this.fontSize = fS;
      this.gc = gc;
      this.intersectBox = new Rectangle(
         0.0F, 0.0F, (boxSize + 1) * Text.getWidth("M", Text.FontType.BODY, this.fontSize), Text.getHeight("M", Text.FontType.BODY, this.fontSize)
      );
      this.buttonName = bName;
      this.defaultText = dText;
      this.textInput = new TextField(
         gc,
         this.font.getFont(Text.FontType.BODY, this.fontSize),
         0,
         0,
         (boxSize + 1) * Text.getWidth("M", Text.FontType.BODY, this.fontSize),
         Text.getHeight("M", Text.FontType.BODY, this.fontSize),
         new ComponentListener() {
            @Override
            public void componentActivated(AbstractComponent source) {
               GUIButtonInputText.this.textInput.setFocus(false);
            }
         }
      );
      this.textInput.setMaxLength(maxChar);
   }

   public void render(Graphics g, Rectangle mouse, int positionX, int positionY, boolean debug) {
      this.intersectBox.setX(positionX);
      this.intersectBox.setY(positionY);
      this.textInput.setLocation(positionX, positionY);
      g.setColor(new Color(0, 0, 0, 100));
      g.fillRect(positionX, positionY, this.intersectBox.getWidth(), this.intersectBox.getHeight());
      if (mouse.intersects(this.intersectBox)) {
         if (this.textInput.hasFocus()) {
            g.setColor(new Color(255, 255, 255, 255));
            this.textInput.render(this.gc, g);
         } else if (this.textInput.getText().equals("")) {
            this.font.drawString(positionX + 3, positionY + 1, "$ORA1" + this.defaultText, Text.FontType.BODY, this.fontSize);
         } else {
            g.setColor(new Color(0, 255, 0, 255));
            this.textInput.render(this.gc, g);
         }
      } else if (this.textInput.hasFocus()) {
         g.setColor(new Color(255, 255, 255, 255));
         this.textInput.render(this.gc, g);
      } else if (this.textInput.getText().equals("")) {
         this.font.drawString(positionX + 3, positionY + 1, "$RED1" + this.defaultText, Text.FontType.BODY, this.fontSize);
      } else {
         g.setColor(new Color(0, 255, 0, 255));
         this.textInput.render(this.gc, g);
      }

      if (debug) {
         g.setColor(Color.cyan);
         g.draw(this.intersectBox);
      }
   }

   public void setFocus(boolean bool) {
      this.textInput.setFocus(bool);
   }

   public boolean hasFocus() {
      return this.textInput.hasFocus();
   }

   public String getText() {
      return this.textInput.getText();
   }

   public void setText(String text) {
      this.textInput.setText(text);
      this.textInput.setCursorPos(text.length());
   }
}
