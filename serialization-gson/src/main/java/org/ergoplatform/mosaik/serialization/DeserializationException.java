package org.ergoplatform.mosaik.serialization;

public class DeserializationException extends RuntimeException {
    public DeserializationException(Throwable cause) {
        super("Error deserializing elements", cause);
    }
}
