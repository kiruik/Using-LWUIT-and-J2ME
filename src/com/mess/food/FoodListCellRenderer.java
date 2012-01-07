package com.mess.food;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Border;

/**
 *
 * @author kirui
 * Note that we extending the Container class and implementing the ListCellRenderer interface
 */
public class FoodListCellRenderer extends Container implements ListCellRenderer {

    /*
     * We are using labels to create the list. That is how LWUIT implements a list
     */
    //label to hold the name of the food
    private Label name = new Label("");
    //label to hold the image of the food
    private Label icon = new Label("");
    //this label is used to create the focus effect on scrolling
    private Label focus = new Label("");

    /*
     * We use the constructor to modify how the default list looks like
     */
    public FoodListCellRenderer() {
        //first define the layout
        setLayout(new BorderLayout());
        //the name of the food will be in a single container and the focus label will be outside
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        //do some styling on the name label,play around with this to see the various appearence you can create
        name.getStyle().setBgTransparency(0);
        name.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
        icon.getStyle().setMargin(4, 4, 4, 4);
        //add the name and icon label to our container
        cnt.addComponent(name);
        //we want the icon to be on the left so we use BorderLayout.WEST when adding it
        addComponent(BorderLayout.WEST, icon);
        //we want the name to be in the middle so we use BorderLayout.CENTER when adding it
        addComponent(BorderLayout.CENTER, cnt);
        focus.getStyle().setBgTransparency(100);
        focus.getStyle().setBgColor(0xFFFFFF);

    }

    /**
     * getListCellRendererComponent is a method from ListCellRenderer interface, we are bound to implement
     * it. In here we set the list values
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @return
     */
    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        //we are getting the list values from the FoodList class so let us instantiate it first
        FoodList content = (FoodList) value;
        //using the getters we are able to assign values to our labels
        name.setText(content.getName());
        icon.setIcon(content.getIcon());
        this.getStyle().setBorder(Border.createLineBorder(1, 0x666666));
        return this;
    }

    /**
     * getListFocusComponent is a method implemented by the ListCellRenderer interface.
     * this method creates the scrolling focus effect
     * @param list
     * @return
     */
    public Component getListFocusComponent(List list) {
        return focus;
    }
}
