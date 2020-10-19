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
    static final String STYLE_RESET = "\033[0m";
    static final String STYLE_YELLOW_BOLD = "\033[1;33m";
    static final String STYLE_RED = "\033[0;31m";
    static final String MESSAGE_NO_DATA = "no data";
    static final String PROMPT_MESSAGE_CHOICE = "Enter your choice: ";
    static final String PROMPT_MESSAGE_NAME = "Enter Employee's name: ";
    static final String PROMPT_MESSAGE_EMAIL = "Enter Employee's email: ";
    static final String PROMPT_MESSAGE_DOB = "Enter Employee's DOB";
    static final String PROMPT_MESSAGE_JOINED_DATE = "Enter Employee's Joined Date";
    static final String PROMPT_MESSAGE_ID = "Enter Employee's ID: ";
    static final String SUCCESS_MESSAGE_ADD = "Employee Added";
    static final String SUCCESS_MESSAGE_UPDATE = "Employee Updated";
    static final String SUCCESS_MESSAGE_DELETE = "Employee Deleted";
    static final String ERROR_MESSAGE_INPUT_NUMBER = "Please enter a number!";
    static final String ERROR_MESSAGE_INPUT_DATE = "Date is incorrect format!";
    static final String ERROR_MESSAGE_SERVER = "Something wrong Happened! Please try again";
    static final String ERROR_MESSAGE_SERVICE_BEAN_NOT_FOUND = "ERROR: Bean Not Found";

    private static EmployeeService employeeService;


    static {
        try {
            employeeService = lookUpEmployeeServiceBeanFromServer();
        } catch (NamingException e) {
            System.out.printf(STYLE_RED + "%s" + STYLE_RESET, ERROR_MESSAGE_SERVICE_BEAN_NOT_FOUND);
        }
    }

    private static EmployeeService lookUpEmployeeServiceBeanFromServer() throws NamingException {
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
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int choice;
            drawMenu();

            while (true) {
                System.out.print(PROMPT_MESSAGE_CHOICE);
                try {
                    choice = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println(STYLE_RED + ERROR_MESSAGE_INPUT_NUMBER + STYLE_RESET);
                    scanner.nextLine();
                }
            }
            if (choice == 5) {
                break;
            }

            scanner.nextLine();
            int id;
            switch (choice) {
                case 1:
                    getAllEmployee();
                    break;

                case 2:
                    EmployeeEntity employeeAdd = new EmployeeEntity();
                    updateEmployeeData(scanner, employeeAdd);
                    EmployeeEntity added = employeeService.addNewEmployee(employeeAdd);
                    if (Objects.nonNull(added)) {
                        System.out.println(SUCCESS_MESSAGE_ADD);
                    } else {
                        System.out.printf(STYLE_RED + "%s" + STYLE_RESET, ERROR_MESSAGE_SERVER);
                    }
                    break;

                case 3:
                    EmployeeEntity employeeUpdate = new EmployeeEntity();
                    System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, PROMPT_MESSAGE_ID);
                    id = scanner.nextInt();
                    employeeUpdate.setId(id);
                    scanner.next();
                    updateEmployeeData(scanner, employeeUpdate);
                    EmployeeEntity updated = employeeService.updateEmployee(employeeUpdate);
                    if (Objects.nonNull(updated)) {
                        System.out.println(SUCCESS_MESSAGE_UPDATE);
                    } else {
                        System.out.printf(STYLE_RED + "%s" + STYLE_RESET, ERROR_MESSAGE_SERVER);
                    }
                    break;

                case 4:
                    System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, PROMPT_MESSAGE_ID);
                    id = scanner.nextInt();
                    if (employeeService.deleteEmployee(id)) {
                        System.out.println(SUCCESS_MESSAGE_DELETE);
                    } else {
                        System.out.printf(STYLE_RED + "%s" + STYLE_RESET, ERROR_MESSAGE_SERVER);
                    }
                    break;
            }
        }
    }

    private static void updateEmployeeData(Scanner scanner, EmployeeEntity employeeEntity) {
        String datePattern = "dd-MM-yyyy";
        System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, PROMPT_MESSAGE_NAME);
        employeeEntity.setName(scanner.nextLine());
        System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, PROMPT_MESSAGE_EMAIL);
        employeeEntity.setEmail(scanner.nextLine());

        LocalDate dob;
        while (true) {
            try {
                System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, PROMPT_MESSAGE_DOB + " (" + datePattern + "): ");
                dob = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(datePattern));
                employeeEntity.setDateOfBirth(Timestamp.valueOf(dob.atStartOfDay()));
                break;
            } catch (DateTimeParseException e) {
                System.out.print(STYLE_RED + ERROR_MESSAGE_INPUT_DATE + STYLE_RESET);
                scanner.nextLine();
            }
        }

        LocalDate joinedDate;
        while (true) {
            try {
                System.out.printf(STYLE_YELLOW_BOLD + "%s" + STYLE_RESET, PROMPT_MESSAGE_JOINED_DATE + " (" + datePattern + "): ");
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
                    Objects.nonNull(employeeEntity.getDateOfBirth())
                            ? dateFormat.format(employeeEntity.getDateOfBirth()) : MESSAGE_NO_DATA,
                    Objects.nonNull(employeeEntity.getJoinedDate())
                            ? dateFormat.format(employeeEntity.getJoinedDate()) : MESSAGE_NO_DATA);
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
