package org.provebit.daemon;

import java.io.Serializable;

import org.simplesockets.protocol.SimpleSocketsProtocol;

public interface DaemonProtocol extends SimpleSocketsProtocol {
	/**
	 * DaemonMessageType : Object \ (optional) REPLY : Object
	 * 
	 * START : String (ignored) \ REPLY : Boolean
	 * SUSPEND : String (ignored) \ REPLY : Boolean
	 * KILL : String (ignored) \ no reply
	 * ADDFILES : Map<String, Boolean> \ REPLY : Boolean
	 * REMOVEFILES : List<String> \ REPLY : Boolean
	 * SETPERIOD : Integer \ REPLY : Boolean
	 * GETLOG : String (ignored) \ REPLY : String
	 * GETTRACKED : String (ignored) \ REPLY : List<List<String>> where List.get(0) is tracked files, List.get(1) is tracked dirs
	 * HEARTBEAT : String (ignored) \ REPLY : String (null)
	 * ISTRACKED : String (absolute file path) \ REPLY : Boolean
	 * GETSTATE : String (ignored) \ REPLY : Integer (0 means suspended, 1 means active)
	 * RESET : String (ignored)
	 */
	public class DaemonMessage implements Serializable {
		private static final long serialVersionUID = 2515667167455084448L;
		
		public DaemonMessageType type;
		public Object data;
		public enum DaemonMessageType {START, // Start tracking file changes (from suspended state)
									   SUSPEND, // Stop tracking file changes (from start state)
									   KILL, // Shutdown merkle daemon
									   ADDFILES, // Add files to tracking
									   REMOVEFILES, // Remove files from tracking
									   SETPERIOD, // Change period of updates
									   GETLOG, // Get change log
									   REPLY, 
									   HEARTBEAT,  
									   GETTRACKED, // Get lists of tracked files and directories 
									   ISTRACKED, // Check if something is being tracked
									   GETSTATE, // Get current state (ACTIVE/SUSPENDED) of daemon
									   RESET, // Completely reset the daemon to initial launch configuration
									   };
		public DaemonMessage(DaemonMessageType type, Object data) {
			this.type = type;
			this.data = data;
		}
	}
	
	/**
	 * Protocol receieve callback
	 * Convert to DaemonMessage and call message handler
	 * @return DaemonMessage to send back or null if no reply required
	 */
	public default Object receive(Object data) {
		DaemonMessage request = (DaemonMessage) data;
		return handleMessage(request);
	}

	/**
	 * Protocol send callback
	 */
	public default Object send(Object data) {
		return data;
	}
	
	/**
	 * Message handler for DaemonMessage objects
	 * @param request - DaemonMessage received
	 * @return - DameonMessage reply for event or null if no reply required
	 */
	abstract DaemonMessage handleMessage(DaemonMessage request);
}