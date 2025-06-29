import java.io.*;
import java.util.*;

class Student {
    private String id;
    private String name;
    private int age;
    private String grade;

    public Student(String id, String name, int age, String grade) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGrade() { return grade; }

    @Override
    public String toString() {
        return String.format("| %-5s | %-20s | %-3s | %-5s |", id, name, age, grade);
    }
}

public class StudentManagementSystem {
    private static final String FILE_NAME = "students.txt";
    private static List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        loadStudentsFromFile();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            clearScreen();
            System.out.println("╔══════════════════════════════════╗");
            System.out.println("║    STUDENT MANAGEMENT SYSTEM     ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ 1. Add Student                   ║");
            System.out.println("║ 2. Delete Student                ║");
            System.out.println("║ 3. Update Student                ║");
            System.out.println("║ 4. Search Student                ║");
            System.out.println("║ 5. Display All Students          ║");
            System.out.println("║ 6. Clear All Data                ║");
            System.out.println("║ 7. Exit                          ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Enter your choice (1-7): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent(scanner);
                        break;
                    case 2:
                        deleteStudent(scanner);
                        break;
                    case 3:
                        updateStudent(scanner);
                        break;
                    case 4:
                        searchStudent(scanner);
                        break;
                    case 5:
                        displayAllStudents();
                        break;
                    case 6:
                        clearAllData();
                        break;
                    case 7:
                        saveStudentsToFile();
                        System.out.println("\nExiting... Thank you!");
                        System.exit(0);
                    default:
                        System.out.println("❌ Invalid choice. Please enter 1-7.");
                        pressEnterToContinue(scanner);
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input. Please enter a number (1-7).");
                scanner.nextLine(); // Clear invalid input
                pressEnterToContinue(scanner);
            }
        }
    }

    // ================== ADD STUDENT ================== //
    private static void addStudent(Scanner scanner) {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║          ADD NEW STUDENT         ║");
        System.out.println("╚══════════════════════════════════╝");

        String id;
        while (true) {
            System.out.print("Enter Student ID: ");
            id = scanner.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("❌ ID cannot be empty. Try again.");
                continue;
            }
            if (isDuplicateId(id)) {
                System.out.println("❌ This ID already exists. Try another.");
            } else {
                break;
            }
        }

        String name;
        while (true) {
            System.out.print("Enter Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("❌ Name cannot be empty. Try again.");
            } else {
                break;
            }
        }

        int age;
        while (true) {
            try {
                System.out.print("Enter Age: ");
                age = Integer.parseInt(scanner.nextLine());
                if (age <= 0) {
                    System.out.println("❌ Age must be positive. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid age. Please enter a number.");
            }
        }

        String grade;
        while (true) {
            System.out.print("Enter Grade (e.g., A, B, C): ");
            grade = scanner.nextLine().trim().toUpperCase();
            if (grade.isEmpty()) {
                System.out.println("❌ Grade cannot be empty. Try again.");
            } else {
                break;
            }
        }

        students.add(new Student(id, name, age, grade));
        System.out.println("\n✅ Student added successfully!");
        pressEnterToContinue(scanner);
    }

    // ================== DELETE STUDENT ================== //
    private static void deleteStudent(Scanner scanner) {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║          DELETE STUDENT         ║");
        System.out.println("╚══════════════════════════════════╝");

        if (students.isEmpty()) {
            System.out.println("❌ No students found.");
            pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine().trim();

        boolean removed = students.removeIf(student -> student.getId().equalsIgnoreCase(id));
        if (removed) {
            System.out.println("\n✅ Student deleted successfully!");
        } else {
            System.out.println("\n❌ Student not found.");
        }
        pressEnterToContinue(scanner);
    }

    // ================== UPDATE STUDENT ================== //
    private static void updateStudent(Scanner scanner) {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║          UPDATE STUDENT         ║");
        System.out.println("╚══════════════════════════════════╝");

        if (students.isEmpty()) {
            System.out.println("❌ No students found.");
            pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine().trim();

        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                System.out.println("\nCurrent Details:");
                System.out.println(student);

                System.out.println("\nEnter new details (leave blank to keep old value):");

                System.out.print("New Name [" + student.getName() + "]: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) name = student.getName();

                int age;
                while (true) {
                    try {
                        System.out.print("New Age [" + student.getAge() + "]: ");
                        String ageInput = scanner.nextLine().trim();
                        if (ageInput.isEmpty()) {
                            age = student.getAge();
                            break;
                        }
                        age = Integer.parseInt(ageInput);
                        if (age <= 0) {
                            System.out.println("❌ Age must be positive. Try again.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid age. Please enter a number.");
                    }
                }

                System.out.print("New Grade [" + student.getGrade() + "]: ");
                String grade = scanner.nextLine().trim().toUpperCase();
                if (grade.isEmpty()) grade = student.getGrade();

                students.set(students.indexOf(student), new Student(id, name, age, grade));
                System.out.println("\n✅ Student updated successfully!");
                pressEnterToContinue(scanner);
                return;
            }
        }
        System.out.println("\n❌ Student not found.");
        pressEnterToContinue(scanner);
    }

    // ================== SEARCH STUDENT ================== //
    private static void searchStudent(Scanner scanner) {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║          SEARCH STUDENT         ║");
        System.out.println("╚══════════════════════════════════╝");

        if (students.isEmpty()) {
            System.out.println("❌ No students found.");
            pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter Student ID to search: ");
        String id = scanner.nextLine().trim();

        boolean found = false;
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                System.out.println("\nStudent found:");
                System.out.println("+──────+──────────────────────+─────+──────+");
                System.out.println("| ID   | Name                 | Age | Grade|");
                System.out.println("+──────+──────────────────────+─────+──────+");
                System.out.println(student);
                System.out.println("+──────+──────────────────────+─────+──────+");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("\n❌ Student not found.");
        }
        pressEnterToContinue(scanner);
    }

    // ================== DISPLAY ALL STUDENTS ================== //
    private static void displayAllStudents() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║        ALL STUDENT RECORDS       ║");
        System.out.println("╚══════════════════════════════════╝");

        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("+──────+──────────────────────+─────+──────+");
            System.out.println("| ID   | Name                 | Age | Grade|");
            System.out.println("+──────+──────────────────────+─────+──────+");
            for (Student student : students) {
                System.out.println(student);
            }
            System.out.println("+──────+──────────────────────+─────+──────+");
            System.out.println("Total Students: " + students.size());
        }
        pressEnterToContinue(new Scanner(System.in));
    }

    // ================== CLEAR ALL DATA ================== //
    private static void clearAllData() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n⚠️ Are you sure? This will delete ALL records! (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y")) {
            students.clear();
            new File(FILE_NAME).delete();
            System.out.println("\n✅ All data cleared successfully!");
        } else {
            System.out.println("\nOperation cancelled.");
        }
        pressEnterToContinue(scanner);
    }

    // ================== HELPER METHODS ================== //
    private static boolean isDuplicateId(String id) {
        return students.stream().anyMatch(student -> student.getId().equalsIgnoreCase(id));
    }

    private static void pressEnterToContinue(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50)); // Fallback for unsupported terminals
        }
    }

    // ================== FILE HANDLING ================== //
    private static void loadStudentsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    students.add(new Student(data[0], data[1], Integer.parseInt(data[2]), data[3]));
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet (first run)
        }
    }

    private static void saveStudentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student student : students) {
                writer.write(student.getId() + "," + student.getName() + "," + student.getAge() + "," + student.getGrade());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving data to file.");
        }
    }
}