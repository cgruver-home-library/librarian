package org.labmonkeys.home_library.librarian.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class BookEventDeserializer extends ObjectMapperDeserializer<BorrowedBookEvent> {

    public BookEventDeserializer() {
        super(BorrowedBookEvent.class);
    }
}
