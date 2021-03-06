/*
 * Copyright (c) 2015 - 2015, Kevin Wallis, All rights reserved.
 * 
 * Projectname: RoboServer
 * Filename: LoggerNetworkReceiver.java
 * 
 * @author: Kevin Wallis
 * @version: 1
 */
package network.receiver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import utils.NetworkUtils;

public class LoggerNetworkReceiver extends NetworkReceiver {

	// Constructor
	public LoggerNetworkReceiver(DatagramSocket socket) {
		super(socket);
	}

	// Methods
	@Override
	public void receive(DatagramPacket packet) {
		super.receive(packet);

		System.out.println("[" + System.currentTimeMillis() + "] Received message:");
		NetworkUtils.printDatagramPacket(packet);
	}
}
