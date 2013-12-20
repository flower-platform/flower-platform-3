package DjProject;/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 *
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Christopher Deckers
 */
public class SimpleWebBrowserExample {

    JComponent contentPane;


    public SimpleWebBrowserExample(){
        contentPane = new JPanel(new BorderLayout());
    }
  public JComponent createContent(String file) {
    JPanel webBrowserPanel = new JPanel(new BorderLayout());
    webBrowserPanel.setBorder(BorderFactory.createTitledBorder("Native Web Browser component"));
    //todo
    //final JWebBrowser webBrowser = new JWebBrowser();
    final JWebBrowser webBrowser = new JWebBrowser(JWebBrowser.destroyOnFinalization());

    webBrowser.setBarsVisible(false);
    //TODO
    webBrowser.navigate("http://localhost:51485/flower-platform/main.jsp?openResources=" + file);

//      browser.setUrl(EclipsePlugin.getInstance().getFlowerJettyServer()
//              .getUrl()
//              + "?openResources="
//              + ((FileEditorInput) getEditorInput()).getFile().getFullPath());
      //http://localhost:51485/flower-platform/main.jsp?openResources=/test2/New Folder/NewDiagram2.notation

    webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
    contentPane.add(webBrowserPanel, BorderLayout.CENTER);
    // Create an additional bar allowing to show/hide the menu bar of the web browser.
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
    JCheckBox menuBarCheckBox = new JCheckBox("Menu Bar", webBrowser.isMenuBarVisible());
    menuBarCheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        webBrowser.setMenuBarVisible(e.getStateChange() == ItemEvent.SELECTED);
      }
    });
    buttonPanel.add(menuBarCheckBox);
    contentPane.add(buttonPanel, BorderLayout.SOUTH);
    return contentPane;
  }

  /* Standard main method to try that test as a standalone application. */
//  public static void main(String[] args) {
//
//      int i = SWT.getVersion();
//      System.out.println(i);
//      NativeInterface.open();
//      UIUtils.setPreferredLookAndFeel();
//
//
//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//        JFrame frame = new JFrame("DJ Native Swing Test");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(createContent(), BorderLayout.CENTER);
//        frame.setSize(800, 600);
//        frame.setLocationByPlatform(true);
//        frame.setVisible(true);
//
//      }
//    });
//    NativeInterface.runEventPump();
//  }

}
