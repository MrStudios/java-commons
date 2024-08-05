import org.junit.jupiter.api.Test;
import pl.mrstudios.commons.inject.Injector;
import pl.mrstudios.commons.inject.annotation.Inject;

import java.net.Proxy;

import static java.net.Proxy.NO_PROXY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InjectorTest {

    private final Injector injector; {
        this.injector = new Injector()
                .registerService(String.class, "127.0.0.1")
                .registerService(new Connection.Scope[] { Connection.Scope.APPLICATION })
                .registerService(Proxy.class, NO_PROXY);
    }

    @Test
    void testInjectConstructor() {

        Connection connection = this.injector.inject(Connection.class);

        assertNotNull(connection);

        assertEquals("127.0.0.1", connection.host);
        assertEquals(NO_PROXY, connection.proxy);
        assertEquals(Connection.Scope.APPLICATION, connection.scopes[0]);

    }

    static class Connection {

        private final String host;
        private final Proxy proxy;
        private final Scope[] scopes;

        @Inject
        public Connection(
                String host,
                Proxy proxy,
                Scope[] scopes
        ) {
            this.host = host;
            this.proxy = proxy;
            this.scopes = scopes;
        }

        enum Scope {
            APPLICATION
        }

    }

}
