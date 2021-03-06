package org.labmonkeys.home_library.librarian.messaging;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class BookEventPublisher {

    @Inject @Channel("book-event")
    Emitter<BookEvent> bookEventEmitter;

    public void sendEvent(BookEvent bookEvent) {
        bookEventEmitter.send(bookEvent);
    }
}
