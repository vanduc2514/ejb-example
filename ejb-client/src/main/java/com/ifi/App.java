package com.ifi;

import com.ifi.api.EmployeeService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class App {
    static final String EJB_JNDI_NAME = "ejb:/ejb-server-1.0-SNAPSHOT/EmployeeService!com.ifi.api.EmployeeService";
    static final String EJB_SERVER_ADDRESS = "http-remoting://localhost:8080";
    static final String EJB_CLIENT_NAMING = "org.jboss.ejb.client.naming";
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
        Scanner scanner = new Scanner(System.in);
        if (Objects.isNull(employeeService)) {
            System.err.println("ERROR: Bean Not Found");
            return;
        }

        getAllEmployee();
    }

    private static void getAllEmployee() {
        final String STYLE_YELLOW_BOLD = "\033[1;33m";
        final String STYLE_RESET = "\033[0m";
        final String STYLE_SPACING = "%n %-5s %-15s %-25s %-15s %-15s";

        String title = String.format(STYLE_YELLOW_BOLD + STYLE_SPACING + STYLE_RESET, "ID", "Name", "Email", "DOB", "Join Date");
        System.out.print(title);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        employeeService.getAllEmployee().forEach(employeeEntity -> {
            String content = String.format("\033[0;36m" + STYLE_SPACING + STYLE_RESET,
                    employeeEntity.getId(),
                    employeeEntity.getName(),
                    employeeEntity.getEmail(),
                    dateFormat.format(employeeEntity.getDateOfBirth()),
                    dateFormat.format(employeeEntity.getJoinedDate()));
            System.out.print(content);
        });
    }
}
