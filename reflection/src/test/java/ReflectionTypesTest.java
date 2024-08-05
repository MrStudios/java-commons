import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.mrstudios.commons.reflection.types.Fields.readField;
import static pl.mrstudios.commons.reflection.types.Fields.writeField;
import static pl.mrstudios.commons.reflection.types.Methods.supplyMethod;

public class ReflectionTypesTest {

    private final Credentials credentials; {
        this.credentials = new Credentials("127.0.0.1", "root");
    }

    @Test
    void testWriteFields() {

        writeField("host", this.credentials, "localhost");
        writeField("login", this.credentials, "admin");

        assertEquals("admin", this.credentials.login);
        assertEquals("localhost", this.credentials.host);

    }

    @Test
    void testReadFields() {
        assertEquals(this.credentials.host, readField("host", this.credentials));
        assertEquals(this.credentials.login, readField("login", this.credentials));
    }

    @Test
    void testSupplyMethod() {
        assertEquals(
                format("ssh %s@%s -p %s", this.credentials.login, this.credentials.host, "password"),
                supplyMethod("createCommand", this.credentials, new Object[] { "password" })
        );
    }

    @SuppressWarnings("FieldMayBeFinal")
    static class Credentials {

        private String host;
        private String login;

        public Credentials(
                String host,
                String login
        ) {
            this.host = host;
            this.login = login;
        }

        public String createCommand(
                String password
        ) {
            return format("ssh %s@%s -p %s", this.login, this.host, password);
        }

    }

}
