package com.noob.storage.common.event;

import com.sun.javafx.event.EventUtil;
import javafx.event.EventTarget;

/**
 * @author luyun
 * @since 1.0
 */
public class Event {

    /**
     * Whether this event has been consumed by any filter or handler.
     */
    protected volatile boolean consumed;

    /**
     * Indicates whether this {@code Event} has been consumed by any filter or
     * handler.
     *
     * @return {@code true} if this {@code Event} has been consumed,
     *     {@code false} otherwise
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Fires the specified event. The given event target specifies the path
     * through which the event will travel.
     *
     * @param eventTarget the target for the event
     * @param event the event to fire
     * @throws NullPointerException if eventTarget or event is null
     */
    public static void fireEvent(EventTarget eventTarget, javafx.event.Event event) {
        if (eventTarget == null) {
            throw new NullPointerException("Event target must not be null!");
        }

        if (event == null) {
            throw new NullPointerException("Event must not be null!");
        }

        EventUtil.fireEvent(eventTarget, event);
    }

}
