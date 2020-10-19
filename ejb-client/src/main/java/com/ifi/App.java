package com.ifi;

import com.ifi.api.EmployeeService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {
    static final String EJB_JNDI_NAME = "ejb:/ejb-server-1.0-SNAPSHOT/EmployeeService!com.ifi.api.EmployeeService";
    static final String EJB_SERVER_ADDRESS = "http-remoting://localhost:8080";
    static final String EJB_CLIENT_NAMING = "org.jboss.ejb.client.naming";
    static final String CLIENT_TITLE = "Employee Manager";
    static final List<String> SUB_MENUS = Arrays.asList(
            "1. View all Employee",
            "2. Add New Employee",
            "3. Update Exist Employee",
            "4. Delete Employee",
            "5. Exit");
    static final String USER_CHOICE_PROMPT = "Enter your choice: ";
    static final String ERROR_MESSAGE_INPUT = "Please enter a number!";
    static final String STYLE_RESET = "\033[0m";

    private static EmployeeService employeeService;

    static {
        try {
            employeeService = lookUpEmployeeServiceBean();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private static EmployeeService lookUpEmployeeServiceBean() throws NamingException {
        final Properties jndiProps = createJNDIProperties();
        final Context context = new InitialContext(jndiProps);
        return (EmployeeService) context.lookup(EJB_JNDI_NAME);
    }

    private static Properties createJNDIProperties() {
        Properties jndiProps = new Properties();
        jndiProps.put(Context.URL_PKG_PREFIXES, EJB_CLIENT_NAMING);
        jndiProps.put(Context.PROVIDER_URL, EJB_SERVER_ADDRESS);
        return jndiProps;
    }

    public static void main(String[] args) {
        if (Objects.isNull(employeeService)) {
            System.err.println("ERROR: Bean Not Found");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        final String STYLE_RED = "\033[0;31m";
        while (true) {
            int choice;
            drawMenu();
            while (true) {
                System.out.print(USER_CHOICE_PROMPT);
                try {
                    choice = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println(STYLE_RED + ERROR_MESSAGE_INPUT + STYLE_RESET);
                    scanner.nextLine();
                }
            }
            if (choice == 5) {
                break;
            }
            switch (choice) {
                case 1:
                    getAllEmployee();
                    break;
                case 2:
                    addNewEmployee();
                    break;
            }
        }
    }

    private static void addNewEmployee() {

    }

    private static void getAllEmployee() {
        final String STYLE_YELLOW_BOLD = "\033[1;33m";
        final String STYLE_SPACING = "%n %-5s %-15s %-25s %-15s %-15s";
        final String STYLE_CYAN_REGULAR = "\033[0;36m";

        String title = String.format(STYLE_YELLOW_BOLD + STYLE_SPACING + STYLE_RESET, "ID", "Name", "Email", "DOB", "Join Date");
        System.out.print(title);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        employeeService.getAllEmployee().forEach(employeeEntity -> {
            String content = String.format(STYLE_CYAN_REGULAR + STYLE_SPACING + STYLE_RESET,
                    employeeEntity.getId(),
                    employeeEntity.getName(),
                    employeeEntity.getEmail(),
                    dateFormat.format(employeeEntity.getDateOfBirth()),
                    dateFormat.format(employeeEntity.getJoinedDate()));
            System.out.print(content);
        });
        System.out.printf("%n");
    }

    private static void drawMenu() {
        int width = 75;
        String dot = "*";
        String whiteSpace = " ";

        System.out.printf("%n");
        drawHorizontalBorder(width, dot);
        System.out.printf("%n");
        int center = (width - CLIENT_TITLE.length()) / 2;
        System.out.print(dot);
        drawHorizontalBorder(center, whiteSpace);
        System.out.print(CLIENT_TITLE);
        drawHorizontalBorder(center - 1, whiteSpace);
        System.out.println(dot);
        drawHorizontalBorder(width, dot);
        System.out.printf("%n");

        SUB_MENUS.forEach(menu -> {
            System.out.print(dot + "  ");
            System.out.print(menu);
            for (int i = 0; i < width - menu.length() - 4; i++) {
                System.out.print(whiteSpace);
            }
            System.out.printf("%s %n", dot);
        });

        drawHorizontalBorder(width, dot);
        System.out.printf("%n");
    }

    private static void drawHorizontalBorder(int i2, String s) {
        for (int i = 0; i < i2; i++) {
            System.out.print(s);
        }
    }
}
