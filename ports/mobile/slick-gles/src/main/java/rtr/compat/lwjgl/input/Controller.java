package rtr.compat.lwjgl.input;

/** Game controllers are not supported by the port; Slick only ever sees an empty controller list. */
public interface Controller {
   String getName();

   int getAxisCount();

   float getAxisValue(int axis);

   String getAxisName(int axis);

   float getXAxisValue();

   float getYAxisValue();

   float getPovX();

   float getPovY();

   int getButtonCount();

   boolean isButtonPressed(int index);
}
