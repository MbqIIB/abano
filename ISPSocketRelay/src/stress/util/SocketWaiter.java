/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stress.util;

import javax.swing.JLabel;
import java.awt.Color;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * wait for incoming connections, spawn a connection thread when
 * stuff comes in.
 */
class SocketWaiter extends Thread {

   /**
    * Field sSocket
    */
   ServerSocket sSocket = null;

   /**
    * Field port
    */
   int port;

   /**
    * Field pleaseStop
    */
   boolean pleaseStop = false;

   /**
    * Constructor SocketWaiter
    *
    * @param l
    * @param p
    */
   public SocketWaiter(int p) {
       port = p;
       start();
   }

   /**
    * Method run
    */
   public void run() {
       try {
           sSocket = new ServerSocket(port);
           for (; ;) {
               Socket inSocket = sSocket.accept();
               if (pleaseStop) {
                   break;
               }
               new Connection(inSocket);
               inSocket = null;
           }
       } catch (Exception exp) {
    	   exp.printStackTrace();
           if (!"socket closed".equals(exp.getMessage())) {
               JLabel tmp = new JLabel(exp.toString());
               tmp.setForeground(Color.red);
           }
       }
   }

   /**
    * force a halt by connecting to self and then closing the server socket
    */
   public void halt() {
       try {
           pleaseStop = true;
           new Socket("127.0.0.1", port);
           if (sSocket != null) {
               sSocket.close();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
