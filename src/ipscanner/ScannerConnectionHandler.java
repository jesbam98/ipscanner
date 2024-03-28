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

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jesus Bautista
 */
public class ScannerConnectionHandler implements Runnable {

    private final ServerSocket socket;

    public ScannerConnectionHandler(ServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            Socket clientSocket = socket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientInput;

            while ((clientInput = in.readLine()) != null) {
                writeln(clientInput);
            }
        } catch (IOException ex) {
            Logger.getLogger(ScannerConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ScannerConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void write(String text) {
        try {
            StringSelection stringSelection = new StringSelection(text);

            // Send text to clipboard
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, stringSelection);

            // Paste clipboard contents
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (AWTException ex) {
            Logger.getLogger(ScannerConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeln(String text) {
        write(text + System.lineSeparator());
    }
}
