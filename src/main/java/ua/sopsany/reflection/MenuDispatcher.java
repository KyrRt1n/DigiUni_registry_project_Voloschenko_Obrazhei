package ua.sopsany.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sopsany.auth.Role;
import ua.sopsany.auth.User;
import ua.sopsany.utils.InputHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuDispatcher {

    private static final Logger log = LoggerFactory.getLogger(MenuDispatcher.class);
    private final InputHandler input;

    public MenuDispatcher(InputHandler input) {
        this.input = input;
    }

    public void runMenu(Class<?> menuClass, User currentUser, String title) {
        List<Method> menuMethods = findMenuMethods(menuClass, currentUser);

        if (menuMethods.isEmpty()) {
            System.out.println("No menu items available for your role.");
            return;
        }

        while (true) {
            System.out.println("\n--- " + title + " ---");
            for (int i = 0; i < menuMethods.size(); i++) {
                MenuItem ann = menuMethods.get(i).getAnnotation(MenuItem.class);
                System.out.println((i + 1) + ". " + ann.label());
            }
            System.out.println("0. Go back");

            int choice = input.readInt("Select option", 0, menuMethods.size());
            if (choice == 0) return;

            Method chosen = menuMethods.get(choice - 1);
            invokeMenuMethod(chosen);
        }
    }

    private List<Method> findMenuMethods(Class<?> clazz, User currentUser) {
        List<Method> result = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            MenuItem annotation = method.getAnnotation(MenuItem.class);
            if (annotation == null) continue; // пропускаємо методи без @MenuItem

            if (!hasAccess(currentUser, annotation.role())) continue;

            result.add(method);
        }

        result.sort(Comparator.comparingInt(m -> m.getAnnotation(MenuItem.class).order()));
        return result;
    }

    private boolean hasAccess(User user, Role required) {
        if (user == null) return false;
        int userLevel = roleLevel(user.getRole());
        int requiredLevel = roleLevel(required);
        return userLevel >= requiredLevel;
    }

    private int roleLevel(Role role) {
        return switch (role) {
            case USER -> 1;
            case MANAGER -> 2;
            case ADMIN -> 3;
        };
    }

    private void invokeMenuMethod(Method method) {
        try {
            method.setAccessible(true);
            method.invoke(null);
            log.info("Menu item '{}' executed successfully",
                    method.getAnnotation(MenuItem.class).label());
        } catch (Exception e) {
            log.error("Failed to invoke menu method '{}': {}", method.getName(), e.getMessage(), e);
            System.out.println("Error executing menu action: " + e.getMessage());
        }
    }
}