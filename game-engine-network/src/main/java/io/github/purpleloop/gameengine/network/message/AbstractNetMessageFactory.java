package io.github.purpleloop.gameengine.network.message;

/** An abstract class for message factories. */
public abstract class AbstractNetMessageFactory implements INetMessageFactory {

	/** A dummy message string. */
	private static final String DUMMY = "DUMMY";

	/** A goodbye message string. */
	private static final String GOODBYE = "GOODBYE";

	/** The dummy message. */
	private static final GameEngineNetMessage GAME_ENGINE_DUMMY_MESSAGE = new GameEngineNetMessage(DUMMY);

	/** The goodbye message. */
	private static final GameEngineNetMessage GAME_ENGINE_GOODBYE_MESSAGE = new GameEngineNetMessage(GOODBYE);

	@Override
	public INetMessage getGoodbyeMessage() {
		return GAME_ENGINE_GOODBYE_MESSAGE;
	}

	@Override
	public INetMessage getDummyMessage() {
		return GAME_ENGINE_DUMMY_MESSAGE;
	}

	@Override
	public boolean isGoodByeMessage(INetMessage testMessage) {
		return GAME_ENGINE_GOODBYE_MESSAGE.equals(testMessage);
	}

	@Override
	public boolean isDummyMessage(INetMessage testMessage) {
		return GAME_ENGINE_DUMMY_MESSAGE.equals(testMessage);
	}	
	
	@Override
	public final INetMessage decodeMessage(String sourceString) {

		if (sourceString.equals(DUMMY)) {
			return GAME_ENGINE_DUMMY_MESSAGE;
		} else if (sourceString.equals(GOODBYE)) {
			return GAME_ENGINE_GOODBYE_MESSAGE;
		}

		return decodeGameMessage(sourceString);
	}

	/**
	 * Decodes the game message.
	 * 
	 * @param sourceString the source string
	 * @return the game message
	 */
	protected abstract INetMessage decodeGameMessage(String sourceString);

}
