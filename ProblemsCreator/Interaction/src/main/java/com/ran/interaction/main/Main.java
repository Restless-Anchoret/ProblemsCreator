package com.ran.interaction.main;

import com.ran.interaction.controllers.MainController;
import com.ran.interaction.support.JavaClassChecker;

public class Main {

    private static void plugInClasses() {
        JavaClassChecker.plugInClass();
    }

    public static void main(String[] args) {
        plugInClasses();
        MainController controller = new MainController();
        controller.init();
        controller.showFrame();
    }

}
