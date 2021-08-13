package io.github.purpleloop.gameengine.network.message;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/** A message box for network communication. */
public class MessageBox {

	/** List of messages. */
	private List<INetMessage> messages;

	/** Constructor of the message box. */
	public MessageBox() {
		this.messages = new LinkedList<>();
	}

	/** Receive a message.
	 * @param message incoming message */
	public void receive(INetMessage message) {
		messages.add(message);		
	}

	/**
	 * @return optional next incoming message
	 */
	public Optional<INetMessage> getNextMessage() {
		if (messages.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(messages.remove(0));
	}

}
