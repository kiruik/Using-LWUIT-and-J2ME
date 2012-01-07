/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mess.food;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.html.DocumentInfo;
import com.sun.lwuit.html.DocumentRequestHandler;
import com.sun.lwuit.html.HTMLComponent;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

import javax.microedition.midlet.*;

/**
 * @author kirui
 */
public class MyFood extends MIDlet implements ActionListener {

    Form mainForm, detailsForm, listForm;
    List foodList;
    Button breakfastBtn, lunchBtn, dinnerBtn;
    Image breakfastImg, lunchImg, dinnerImg;
    Resources res;
    Command exitCmd;
    FoodList[] foodArray;

    public void startApp() {
        Display.init(this);
        try {
            res = Resources.open("/com/mess/res/Ushahidi.res");
            UIManager.getInstance().setThemeProps(res.getTheme("Ushahidi"));
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert uiManAlert = new Alert("UIManager error", ex.getMessage(), null, AlertType.ERROR);
            uiManAlert.setTimeout(50);
        }
        mainForm = new Form("Eat out-JKUAT Mess");
        mainForm.setLayout(new FlowLayout());
        exitCmd = new Command("Exit");
        try {
            /*
             * i am doing the image resize on the fly, this is processor intensive.
             * it is advisable to resize the images to first elsewhere before using them
             */
            breakfastImg = Image.createImage("/com/mess/images/breakfast.jpeg").scaled(60, 50);
            lunchImg = Image.createImage("/com/mess/images/lunch.jpeg").scaled(60, 50);
            dinnerImg = Image.createImage("/com/mess/images/dinner.jpeg").scaled(60, 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = mainForm.getWidth();
        breakfastBtn = new Button("Breakfast", breakfastImg);
        breakfastBtn.setPreferredW(width);
        breakfastBtn.addActionListener(this);
        lunchBtn = new Button("Lunch", lunchImg);
        lunchBtn.setPreferredW(width);
        lunchBtn.addActionListener(this);
        dinnerBtn = new Button("Dinner", dinnerImg);
        dinnerBtn.setPreferredW(width);
        dinnerBtn.addActionListener(this);
        mainForm.addComponent(breakfastBtn);
        mainForm.addComponent(lunchBtn);
        mainForm.addComponent(dinnerBtn);
        mainForm.addCommandListener(this);
        mainForm.addCommand(exitCmd);
        mainForm.show();
    }

    public void showFoodList() {
        //instantiate our form
        listForm = new Form("Food List");
        //set the layout
        listForm.setLayout(new FlowLayout());
        /*
         * We are going to create the food list manually but in the real world situation you are
         * going to pull this data from a database. I will do another post to show how to access
         * data remotely. We will use a string array and an image array to hold this data
         */
        String[] breakfastMenu = {"Tea", "French Toast", "Mandazi"};
        //we first need to create the images separatly before we create the image array
        Image teaImg = null, toastImg = null, mandaziImg = null;
        try {
            /*
             * i am doing the image resize on the fly, this is processor intensive.
             * it is advisable to resize the images to first elsewhere before using them.
             * if the images are being downloaded from a server you can resize first on the server
             */
            teaImg = Image.createImage("/com/mess/images/tea.jpeg").scaled(70, 60);
            toastImg = Image.createImage("/com/mess/images/toast.jpeg").scaled(70, 60);
            mandaziImg = Image.createImage("/com/mess/images/mandazi.jpeg").scaled(70, 60);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //let us now create the image array
        Image[] theImgs = {teaImg, toastImg, mandaziImg};
        /*
         * We have created two more String arrays to hold some more details about
         * our food. Just like the other data, this data should be downloaded from
         * a server somewhere
         */
        String[] prizeArray = {"8", "25", "10"};
        String[] descArray = {"The perfect beverage to wake you up in the morning", "Bread and eggs mashed up together! Can it get better?", "Soft, "
            + "sweet mandazis. It will leave you yearning for more!"};
        /*
         * Next we are going to create an array of the FoodList class, populate
         * it with the array data we created then pass it to the list for display
         *
         */
        foodArray = new FoodList[theImgs.length];
        
        for (int i = 0; i < theImgs.length; i++) {
            foodArray[i] = new FoodList();
            foodArray[i].setName(breakfastMenu[i]);
            foodArray[i].setIcon(theImgs[i]);
            foodArray[i].setPrice(prizeArray[i]);
            foodArray[i].setDetails(descArray[i]);
        }
        /*
         * now that we have our object array with all the list data we now instantiate the
         * list and pass the data to it.
         */
        foodList = new List(foodArray);
        //next we need to set the ListCellRenderer to the one that we created
        foodList.setListCellRenderer(new FoodListCellRenderer());
        //for the list to cover the entire screen width we need to set it
        foodList.setPreferredW(listForm.getWidth());
        //the code below makes the list look better, the list can work without it
        foodList.setFixedSelection(List.FIXED_NONE);
        foodList.setItemGap(0);
        //to handle actions on the list we need to set the actionListener
        foodList.addActionListener(this);
        //let us now add the list to the form
        listForm.addComponent(foodList);
        //create the back button for the form
        listForm.addCommand(new Command("Back") {

            public void actionPerformed(ActionEvent e) {
                mainForm.showBack();
            }
        });
        //we then show the form
        listForm.show();

    }

    public int getScreenWidth() {
        Form dummy = new Form();
        return dummy.getWidth();
    }

    public void showDetails(int index) {
        detailsForm = new Form("Showing breakfast");
        detailsForm.setLayout(new FlowLayout());
        int width = getScreenWidth();
        /*
         * We are going to use the index to determine which object in the foodArray
         * ws selected. From there we can use the getters we created in the FoodList
         * class to access each item in the object
         */
        //we are going to create a container to hold our image. It helps us to center it
        Container imgCont=new Container(new BorderLayout());
        
        //Label to hold the image
        Label imgLbl = new Label();
        //we now set it an icon by accessing the object and then using the getter for the icon
        imgLbl.setIcon(foodArray[index].getIcon());
        //play around with the code fragment below to center the image
        imgLbl.getStyle().setMargin(0, 0, width/4, width/4);
        //add the imgLbl to imgCont and center it
        imgCont.addComponent(BorderLayout.CENTER, imgLbl);

        //Let us create a container to hold the rest of the stuff. This makes it easier to style
        Container cont = new Container(new FlowLayout());
        //Display the name of the food
        Label nameLbl = new Label(foodArray[index].getName());
        nameLbl.setPreferredW(width);
        cont.addComponent(nameLbl);
        //display the price of the food
        String price = "Price: " + foodArray[index].getPrice()+" shillings";
        Label priceLbl = new Label(price);
        priceLbl.setPreferredW(width);
        cont.addComponent(priceLbl);

        /*
         * For the details we are going to use the HtmlComponent to render it
         * This is because it spans over several lines. Another option would have been
         * an uneditable textarea but the HtmlComponent is far much better
         */
        /*
         * we first need to set a document request handler for the HtmlComponent. Check the
         * LWUIT documentation for more details about this
         */
        DocumentRequestHandler handler = new DocumentRequestHandler() {

            public InputStream resourceRequested(DocumentInfo di) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        //create the HTMLComponent and set the handler
        HTMLComponent html = new HTMLComponent(handler);
        //do some styling on it
        html.getStyle().setPadding(0, 0, 3, 3);
        /*
         * LWUIT allows us to define a style just like how we do using css
         * We can then use this style on different components. You can do a
         * lot more with the style object, I just picked a few attributes
         * Please refer to the LWUIT documentation to know more
         */
        Style s = new Style();
        s.setBgColor(0x5C5C5C);
        s.setFgColor(0xffffff);
        s.setBorder(Border.createRoundBorder(15, 15));
        //set the HTMLComponent to use the style we just created
        html.setPageStyle(s);
        /*
         * String theHtml will hold the data we want to show
         * In this string we can define tags we want to use just like in html
         */
        String theHtml = "";
        //let us first pick the details from our object
        String theDetails =foodArray[index].getDetails();
        theHtml += "<div style=\"color: #ffffff; background: #5C5C5C;\">";
        theHtml += "<br/><div style=\"background: #5C5C5C;\">";
        theHtml += "<div style=\"color:ffd200; margin-left:5px; font-size: 14px; \"><b>Details</b></div><p>";
        theHtml += theDetails + "</p><p style=\"color:#5C5C5C\"></p></div>";
        theHtml += "</div>";
        //set the html we just created to the HTMLComponent
        html.setBodyText(theHtml);
        //now we just need to add the components to the form then show it
        detailsForm.addComponent(imgCont);
        detailsForm.addComponent(cont);
        detailsForm.addComponent(html);
        detailsForm.addCommand(new Command("Back"){
           public void actionPerformed(ActionEvent e){
               listForm.showBack();
           }
        });
        detailsForm.show();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == breakfastBtn) {
            showFoodList();
        } else if (ae.getSource() == dinnerBtn) {
        } else if (ae.getSource() == lunchBtn) {
        } else if (ae.getSource() == foodList) {
            /*
             * you handle your list action here. by using the getSelectedIndex
             * method we are able to determine which list item was selected
             */
            System.out.println("Selected index: " + foodList.getSelectedIndex());
            int index = foodList.getSelectedIndex();
            showDetails(index);

        } else if (ae.getCommand() == exitCmd) {
            destroyApp(true);
            notifyDestroyed();
        }
    }
}
