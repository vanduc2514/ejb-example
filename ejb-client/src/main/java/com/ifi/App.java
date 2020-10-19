package com.ifi;

import com.ifi.api.EmployeeService;
import com.ifi.model.EmployeeEntity;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    static final String STYLE_YELLOW_BOLD = "\033[1;33m";
    static final String STYLE_RED = "\033[0;31m";
    static final String ERROR_MESSAGE_INPUT_DATE = "Date is incorrect format!";
    static final String SUCCESS_MESSAGE_ADDNEW = "Employee Added";
    static final String ERROR_MESSAGE_ADDNEW = "Something wrong Happened! Please try again";

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
            scanner.nextLine();
            switch (choice) {
                case 1:
                    getAllEmployee();
                    break;
                case 2:
                    EmployeeEntity employee = new EmployeeEntity();
                    updateEmployee(scanner, employee);
                    EmployeeEntity added = employeeService.addNewEmployee(employee);
                    if (Objects.nonNull(added)) {
                        System.out.println(SUCCESS_MESSAGE_ADDNEW);
                    } else {
                        System.out.printf(STYLE_RED + "%s" + STYLE_RESET, ERROR_MESSAGE_ADDNEW);
                    }
                    break;
            }
        }
    }

    private static void updateEmployee(Scanner scanner, EmployeeEntity employeeEntity) {
        String datePattern = "dd-MM-yyyy";
        System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, "Enter Employee's name: ");
        employeeEntity.setName(scanner.nextLine());
        System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, "Enter Employee's email: ");
        employeeEntity.setEmail(scanner.nextLine());

        LocalDate dob;
        while (true) {
            try {
                System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, "Enter Employee's DOB (" + datePattern + "): ");
                dob = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(datePattern));
                employeeEntity.setJoinedDate(Timestamp.valueOf(dob.atStartOfDay()));
                break;
            } catch (DateTimeParseException e) {
                System.out.print(STYLE_RED + "Date is incorrect format!" + STYLE_RESET);
                scanner.nextLine();
            }
        }

        LocalDate joinedDate;
        while (true) {
            try {
                System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, "Enter Employee's Joined Date (" + datePattern + "): ");
                joinedDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(datePattern));
                employeeEntity.setJoinedDate(Timestamp.valueOf(joinedDate.atStartOfDay()));
                break;
            } catch (DateTimeParseException e) {
                System.out.print(STYLE_RED + ERROR_MESSAGE_INPUT_DATE + STYLE_RESET);
                scanner.nextLine();
            }
        }
    }

    private static void getAllEmployee() {
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
