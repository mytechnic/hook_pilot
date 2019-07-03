package org.jnativehook.example.capture.item;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;

@Slf4j
@Getter
public class Clipboard {

    public static String executeCopy() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            Thread.sleep(150);

            Object ret = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            if (ret instanceof String) {
                return (String) ret;
            }
        } catch (Exception e) {
            log.debug("{}", e);
        }

        return null;
    }
}
