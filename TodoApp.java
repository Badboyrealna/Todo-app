import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoApp {

    // A small class that represents one task
    static class Task {
        String title;
        boolean done;

        Task(String title) {
            this.title = title;
            this.done = false;
        }

        @Override
        public String toString() {
            return (done ? "[x] " : "[ ] ") + title;
        }
    }

    private static final List<Task> tasks = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Path SAVE_FILE = Paths.get("tasks.txt");

private static void saveTasks() {
    List<String> lines = new ArrayList<>();
    for (Task t : tasks) {
        lines.add((t.done ? "1" : "0") + "|" + t.title);
    }
    try {
        Files.write(SAVE_FILE, lines);
    } catch (IOException e) {
        System.out.println("Could not save tasks: " + e.getMessage());
    }
}

private static void loadTasks() {
    if (!Files.exists(SAVE_FILE)) return;
    try {
        for (String line : Files.readAllLines(SAVE_FILE)) {
            String[] parts = line.split("\\|", 2);
            if (parts.length == 2) {
                Task t = new Task(parts[1]);
                t.done = parts[0].equals("1");
                tasks.add(t);
            }
        }
    } catch (IOException e) {
        System.out.println("Could not load tasks: " + e.getMessage());
    }
}

    public static void main(String[] args) {
        System.out.println("=== Welcome to My To-Do App ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readNumber("Choose an option: ");

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    showTasks();
                    break;
                case 3:
                    completeTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    showStats();
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye! Keep getting things done.");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Add task");
        System.out.println("2. Show tasks");
        System.out.println("3. Mark task as done");
        System.out.println("4. Delete task");
        System.out.println("5. Show progress");
        System.out.println("0. Exit");
    }

    private static void addTask() {
        System.out.print("Enter task name: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Task name can't be empty.");
            return;
        }
        tasks.add(new Task(title));
        System.out.println("Task added!");
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet. Add one!");
            return;
        }
        System.out.println("\nYour tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    private static void completeTask() {
        showTasks();
        if (tasks.isEmpty()) return;

        int index = readNumber("Task number to complete: ") - 1;
        if (isValidIndex(index)) {
            tasks.get(index).done = true;
            System.out.println("Nice work! Task completed.");
        } else {
            System.out.println("That task doesn't exist.");
        }
    }

    private static void deleteTask() {
        showTasks();
        if (tasks.isEmpty()) return;

        int index = readNumber("Task number to delete: ") - 1;
        if (isValidIndex(index)) {
            Task removed = tasks.remove(index);
            System.out.println("Deleted: " + removed.title);
        } else {
            System.out.println("That task doesn't exist.");
        }
    }

    private static void showStats() {
        int total = tasks.size();
        long completed = tasks.stream().filter(t -> t.done).count();
        int percent = total == 0 ? 0 : (int) (completed * 100 / total);

        System.out.println("Total: " + total + " | Done: " + completed
                + " | Progress: " + percent + "%");
    }

    private static boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    // Keeps asking until the user types a valid number
    private static int readNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}
