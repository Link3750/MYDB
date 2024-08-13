package top.guoziyang.mydb.client;

import java.util.Scanner;

public class Shell {
    private final Client client;

    public Shell(Client client) {
        this.client = client;
    }

    public void run() {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print(":> ");
                String statStr = sc.nextLine();
                if ("exit".equals(statStr) || "quit".equals(statStr)) {
                    break;
                }
                try {
                    byte[] res = client.execute(statStr.getBytes());
                    System.out.println(new String(res));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        } finally {
            client.close();
        }
    }
}
