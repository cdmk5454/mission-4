package org.example;

import org.example.enums.LevelSet;
import org.example.enums.ModSet;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.runWishSay(LevelSet.LEVEL_8.getValue(), ModSet.MAIN.getValue());
    }
}