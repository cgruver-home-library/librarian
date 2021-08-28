package org.labmonkeys.home_library.librarian.messaging;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class BorrowedBookEventPublisher {

    @Inject @Channel("book-event")
    Emitter<BorrowedBookEvent> bookEventEmitter;

    public void sendEvent(BorrowedBookEvent bookEvent) {
        bookEventEmitter.send(bookEvent);
    }
}
