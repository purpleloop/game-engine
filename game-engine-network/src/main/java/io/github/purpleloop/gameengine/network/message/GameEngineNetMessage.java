package io.github.purpleloop.gameengine.network.message;

/** Basic game engine net message. */
public class GameEngineNetMessage implements INetMessage {

	/** The text of the message. */
	private String plainMessage;

	/**
	 * Constructor of messages.
	 * 
	 * @param plainMessage the message string
	 */
	protected GameEngineNetMessage(String plainMessage) {
		this.plainMessage = plainMessage;
	}

	@Override
	public byte[] getBytes() {
		return plainMessage.getBytes();
	}

	@Override
	public int hashCode() {
		return plainMessage.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GameEngineNetMessage)) {
			return false;
		}
		GameEngineNetMessage otherGameMessage = (GameEngineNetMessage) obj;
		return plainMessage.equals(otherGameMessage.toString());
	}

	@Override
	public String toString() {
		return plainMessage;
	}

}
