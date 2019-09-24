/*  Name: Robert Stein  
 *  Course: CNT 4714 Spring 2019 
 *  Assignment title: Project 3 
 *  Date:  3/14/2019
 *  Class:  AppRunner
  */ 

package ucf;

import java.awt.BorderLayout;


public class AppRunner
{
    public static void main(String[] args) throws Exception
    {
        AppFrame appWindow = new AppFrame();
        appWindow.setTitle("SQL Client GUI - Spring 2019");
        appWindow.pack(); //automatically set the window sizes to fit in all components
        appWindow.setLayout(new BorderLayout(0,0));
        appWindow.setVisible(true);
    }
}
