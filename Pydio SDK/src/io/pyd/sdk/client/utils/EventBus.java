package io.pyd.sdk.client.utils;

import io.pyd.sdk.client.model.PydioEvent;

/**
 * Envent bus interface
 * @author pydio
 *
 */
public interface EventBus {	

	public interface Subscriber{
		public <E extends PydioEvent> void  onNewEvent(E e);
		public boolean once();
	}
	
	/**
	 * Called to dispatch an Event
	 * @param e The Event object to publish
	 */
	public <E extends PydioEvent> void publish(E e);
	
	
	/**
	 * Subscribe to a class of Event
	 * @param sub A call-back to handle event
	 * @param type The event type name to subscribe to.
	 */
	public <E extends PydioEvent> void subscribe(Subscriber sub, String type);
	
	
	/**
	 * Call to retire a subscriber for a class
	 * @param sub A call-back previously set when subscribing. 
	 * @param type The type of Event to unsubscribe to. Set null to unsubscribe to all type of event.
	 */
	public void unsubscribe(Subscriber sub, String type);
	
}
