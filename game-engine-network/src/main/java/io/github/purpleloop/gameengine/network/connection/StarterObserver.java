package io.github.purpleloop.gameengine.network.connection;

import io.github.purpleloop.gameengine.network.exception.NetException;

/** Connection start observer. */
public interface StarterObserver {

  /** Returns the result of the connection start.
   * @param success true if the connection was successful, false otherwise
   * @param netException NetException in case of error
    */
  void startResult(boolean success, NetException netException);
  
}
