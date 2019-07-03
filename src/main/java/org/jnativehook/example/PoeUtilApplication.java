package org.jnativehook.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.example.capture.hook.KeyboardHook;
import org.jnativehook.example.ui.Const;
import org.jnativehook.example.ui.PoeTrade;
import org.jnativehook.example.ui.Ui;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Slf4j
@RequiredArgsConstructor
@Component
public class PoeUtilApplication {
    private final Ui ui;

    public static void main(String[] args) throws NativeHookException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.refresh();

        PoeUtilApplication application = ctx.getBean(PoeUtilApplication.class);
        application.run();

        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        Const.poeTrade = new PoeTrade();

        JFrame frame = new JFrame("Path Of Exile Game Helper");
        frame.setContentPane(Const.poeTrade.getMainForm());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(500, 700));
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);

        GlobalScreen.setEventDispatcher(new SwingDispatchService());
        GlobalScreen.addNativeKeyListener(new KeyboardHook());
        GlobalScreen.registerNativeHook();
    }

    public void run() {

    }
}
