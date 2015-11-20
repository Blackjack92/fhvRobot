package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import communication.IClientConfiguration;
import communication.managers.CommunicationManager;
import communication.managers.IAnswerHandler;
import communication.managers.IDataReceivedHandler;
import models.Client;
import network.receiver.INetworkReceiver;
import network.receiver.LoggerNetworkReceiver;
import network.sender.INetworkSender;
import network.sender.LoggerNetworkSender;

public class Communication implements Runnable, IDataReceivedHandler, IAnswerHandler {
	
	private boolean isRunning;
	private int receivePacketSize = 1024;
	private final INetworkReceiver receiver;
	private final INetworkSender sender;
	private final DatagramSocket socket;
	private final CommunicationManager manager;
	
	public Communication(CommunicationManager manager, DatagramSocket socket) {
		this.manager = manager;
		this.socket = socket;
		this.receiver = new LoggerNetworkReceiver(socket);
		this.sender = new LoggerNetworkSender(socket);
	}
	
	@Override
	public void run() {
		isRunning = true;
		
		while (isRunning) {
			byte[] receiveData = new byte[receivePacketSize];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			receiver.receive(receivePacket);

			if (socket.isClosed()) {
				continue;
			}

			manager.readDatagramPacket(receivePacket, this, this);
		}
	}

	@Override
	public boolean handleDataReceived(DatagramPacket packet, byte[] data, IAnswerHandler sender) {
		try {
			String name = new String(data);

			Client client = (Client) manager.getCurrentClientConfiguration();
			client.setName(name);
			client.setReceiveData(name);

			// TODO: handle other message

			// Only for test purposes
			client.setSendData(name);
			sendToClient(client);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void answer(IClientConfiguration configuration, byte[] data) {
		InetAddress address;
		try {
			address = InetAddress.getByName(configuration.getIpAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			address = InetAddress.getLoopbackAddress();
		}

		int port = configuration.getPort();
		DatagramPacket answerPacket = new DatagramPacket(data, data.length, address, port);
		sender.send(answerPacket);
	}

	public void sendToClient(Client client) throws IOException {
		if (client == null) {
			return;
		}

		DatagramPacket sendPacket = manager.createDatagramPacket(client, client.getSendData());
		sender.send(sendPacket);
	}

	public void stop() {
		isRunning = false;
	}
}
