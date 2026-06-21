package Test;

import ui.MyPanel;
import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.*;

public class MyPanelTest {

    @Test
    public void testMyPanelInitialization() {
        MyPanel panel = new MyPanel(20, Color.RED);

        assertNotNull("Le panneau doit être correctement instancié", panel);
        assertFalse("Un MyPanel ne doit pas être opaque (setOpaque doit être false)", panel.isOpaque());
    }
}