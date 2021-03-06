package network.communication;

import java.net.DatagramPacket;
import java.net.SocketException;

import communication.commands.Commands;
import communication.managers.IAnswerHandler;
import communication.pdu.ApplicationPDU;
import controllers.PersistencyController;
import models.Client;
import network.IClientController;

public class RoboCommunication extends Communication {

	// Constructor
	public RoboCommunication(IClientController<Client> clientController, Delegator delegator, int port,
			PersistencyController persistencyController) throws SocketException {
		super(clientController, delegator, port, persistencyController);
	}

	// Methods
	@Override
	public boolean handleDataReceivedCore(DatagramPacket packet, ApplicationPDU pdu, IAnswerHandler sender,
			Client client) {
		try {
			byte[] payload = pdu.getPayload();
			int command = pdu.getCommand();
			int flags = pdu.getFlags();

			// Only for test purposes
			// client.setSendData(new String(payload));
			// sendToClient(client);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	protected boolean isCommandToDelegate(int command) {
		return command == Commands.ORIENTATION_DATA;
	}
}
