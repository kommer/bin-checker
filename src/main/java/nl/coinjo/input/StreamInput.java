package nl.coinjo.input;

import nl.coinjo.input.types.Event;

public interface StreamInput {

    public void onEvent(Event event);
}
