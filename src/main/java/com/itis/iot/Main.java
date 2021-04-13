package com.itis.iot;

public class Main {

    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        try {
            int i = 0;
            while (true) {
                System.out.println(controller.getIllumination());
                Thread.sleep(1000);
                if (i < 20) i++;
                else break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            controller.shutdown();
        }
    }
}
