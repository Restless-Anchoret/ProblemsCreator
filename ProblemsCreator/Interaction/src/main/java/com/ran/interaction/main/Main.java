package com.ran.interaction.main;

import com.ran.interaction.controllers.MainController;

public class Main {

    public static void main(String[] args) {
        MainController controller = new MainController();
        controller.init();
        controller.showFrame();
    }

}
