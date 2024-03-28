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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jesus Bautista
 */
public class ScannerServer {

    private final int portNumber;
    private ServerSocket socket;

    public ScannerServer(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getIpAddress() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ScannerServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getStatus() {
        if (socket != null && socket.isBound()) {
            return String.format("El servicio se encuentra activo %nIP: %s%nPuerto: %d", getIpAddress(), getPortNumber());
        } else {
            return "El servicio no está activo";
        }
    }

    public void start() {

        try {
            socket = new ServerSocket(portNumber);
            Logger.getLogger(ScannerServer.class.getName()).log(Level.INFO, "Escuchando en {0}:{1}", new Object[]{getIpAddress(), getPortNumber()});
            ScannerConnectionHandler worker = new ScannerConnectionHandler(socket);
            Thread thread = new Thread(worker);
            thread.start();
        } catch (IOException ex) {
            Logger.getLogger(ScannerServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
