package communication.managers;

import java.util.concurrent.ThreadLocalRandom;

import models.Client;

public class SessionManager extends LayerManager<Integer> {

	// Fields
	private final int minSessionNumber = 0;
	private final int maxSessionNumber = 255;
	
	// Methods
	public int getSession(Client client) {
		return clientMap.get(client);
	}
	
	public int createSession(Client client) {
		addClient(client);
		
		// Set new session id
		int oldSession = getValue(client);
		int newSession = createNewSessionNumber(oldSession);
		setValueOfClient(client, newSession);
		
		return newSession;
	}
	
	private int createNewSessionNumber(int oldSessionNumber) {
		int newNumber = oldSessionNumber;
		{
			// Create new session number between min and max
			newNumber = ThreadLocalRandom.current().nextInt(minSessionNumber, maxSessionNumber + 1);
		} while(newNumber == oldSessionNumber);
		
		return newNumber;
	}

	
	@Override
	protected Integer getDefaultValue() {
		return -1;
	}
}
