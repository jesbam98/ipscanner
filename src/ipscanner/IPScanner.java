/*
 * Copyright (C) 2024 Jesus Bautista
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ipscanner;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Jesus Bautista
 */
public class IPScanner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "System Tray is not supported!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SystemTray systemTray = SystemTray.getSystemTray();
        URL iconUrl = ClassLoader.getSystemResource("qr-code.png");

        Image image = Toolkit.getDefaultToolkit().getImage(iconUrl);
        ScannerServer server = new ScannerServer(4444);
        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem status = new MenuItem("Estado");
        status.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(null, server.getStatus());
        });
        trayPopupMenu.add(status);
        
        
        MenuItem close = new MenuItem("Cerrar");
        close.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        trayPopupMenu.add(close);

        //setting tray icon
        TrayIcon trayIcon = new TrayIcon(image, "IPScanner", trayPopupMenu);
        //adjust to default size as per system recommendation 
        trayIcon.setImageAutoSize(true);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException ex) {
            Logger.getLogger(IPScanner.class.getName()).log(Level.SEVERE, null, ex);
        }

        server.start();
    }

}
