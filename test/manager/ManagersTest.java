package manager;


import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    private File file;

    @Test
    void shouldNotNull() {
        assertNotNull(Managers.getDefault(file));
    }

    @Test
    void shouldNotNullHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }
}