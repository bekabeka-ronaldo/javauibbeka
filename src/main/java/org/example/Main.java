package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    //  Енгізу
    static class Input {
        private static final Scanner SC = new Scanner(System.in);

        static String text(String prompt) {
            System.out.print(prompt);
            return SC.nextLine().trim();
        }

        static int integer(String prompt) {
            while (true) {
                try {
                    return Integer.parseInt(text(prompt));
                } catch (NumberFormatException e) {
                    System.out.println("Қате: бүтін сан енгізіңіз.");
                }
            }
        }
    }

    // Студенттер
    static class StudentDao {
        static void add(String name, String group, String email, int year) {
            String sql = "INSERT INTO students (full_name, group_name, email, enrollment_year) VALUES (?, ?, ?, ?)";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, group);
                ps.setString(3, email);
                ps.setInt(4, year);
                System.out.println("Қосылған жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void showAll() {
            query("SELECT id, full_name, group_name, email, enrollment_year FROM students ORDER BY id", null);
        }

        static void search(String text) {
            query("SELECT id, full_name, group_name, email, enrollment_year FROM students WHERE full_name ILIKE ? ORDER BY id",
                    "%" + text + "%");
        }

        private static void query(String sql, String param) {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                if (param != null) ps.setString(1, param);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.printf("%-4s %-25s %-8s %-25s %-6s%n", "ID", "Аты-жөні", "Топ", "Email", "Жыл");
                    boolean any = false;
                    while (rs.next()) {
                        any = true;
                        System.out.printf("%-4d %-25s %-8s %-25s %-6d%n", rs.getInt("id"), rs.getString("full_name"),
                                rs.getString("group_name"), rs.getString("email"), rs.getInt("enrollment_year"));
                    }
                    if (!any) System.out.println("(жазба табылмады)");
                }
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void update(int id, String name, String group, String email) {
            String sql = "UPDATE students SET full_name = ?, group_name = ?, email = ? WHERE id = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, group);
                ps.setString(3, email);
                ps.setInt(4, id);
                System.out.println("Өзгертілген жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void delete(int id) {
            String sql = "DELETE FROM students WHERE id = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, id);
                System.out.println("Жойылған жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }
    }

    //  Оқытушылар
    static class TeacherDao {
        static void add(String name, String department, String email) {
            String sql = "INSERT INTO teachers (full_name, department, email) VALUES (?, ?, ?)";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, department);
                ps.setString(3, email);
                System.out.println("Қосылған жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void showAll() {
            query("SELECT id, full_name, department, email FROM teachers ORDER BY id", null);
        }

        static void search(String text) {
            query("SELECT id, full_name, department, email FROM teachers WHERE full_name ILIKE ? ORDER BY id",
                    "%" + text + "%");
        }

        private static void query(String sql, String param) {
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                if (param != null) ps.setString(1, param);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.printf("%-4s %-25s %-20s %-25s%n", "ID", "Аты-жөні", "Кафедра", "Email");
                    boolean any = false;
                    while (rs.next()) {
                        any = true;
                        System.out.printf("%-4d %-25s %-20s %-25s%n", rs.getInt("id"), rs.getString("full_name"),
                                rs.getString("department"), rs.getString("email"));
                    }
                    if (!any) System.out.println("(жазба табылмады)");
                }
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void update(int id, String name, String department, String email) {
            String sql = "UPDATE teachers SET full_name = ?, department = ?, email = ? WHERE id = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, department);
                ps.setString(3, email);
                ps.setInt(4, id);
                System.out.println("Өзгертілген жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void delete(int id) {
            String sql = "DELETE FROM teachers WHERE id = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, id);
                System.out.println("Жойылған жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }
    }

    // Пәндер
    static class SubjectDao {
        static void add(String name, int credits, int teacherId) {
            String sql = "INSERT INTO subjects (name, credits, teacher_id) VALUES (?, ?, ?)";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, credits);
                ps.setInt(3, teacherId);
                System.out.println("Қосылған жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void showAll() {
            query("", null);
        }

        static void search(String text) {
            query("WHERE s.name ILIKE ?", "%" + text + "%");
        }

        private static void query(String where, String param) {
            String sql = "SELECT s.id, s.name, s.credits, t.full_name AS teacher "
                    + "FROM subjects s LEFT JOIN teachers t ON s.teacher_id = t.id " + where + " ORDER BY s.id";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                if (param != null) ps.setString(1, param);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.printf("%-4s %-28s %-8s %-25s%n", "ID", "Пән", "Кредит", "Оқытушы");
                    boolean any = false;
                    while (rs.next()) {
                        any = true;
                        System.out.printf("%-4d %-28s %-8d %-25s%n", rs.getInt("id"), rs.getString("name"),
                                rs.getInt("credits"), rs.getString("teacher"));
                    }
                    if (!any) System.out.println("(жазба табылмады)");
                }
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void update(int id, String name, int credits, int teacherId) {
            String sql = "UPDATE subjects SET name = ?, credits = ?, teacher_id = ? WHERE id = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, credits);
                ps.setInt(3, teacherId);
                ps.setInt(4, id);
                System.out.println("Өзгертілген жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void delete(int id) {
            String sql = "DELETE FROM subjects WHERE id = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, id);
                System.out.println("Жойылған жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }
    }

    // Бағалар
    static class GradeDao {
        static void add(int studentId, int subjectId, int grade) {
            String sql = "INSERT INTO grades (student_id, subject_id, grade) VALUES (?, ?, ?)";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, studentId);
                ps.setInt(2, subjectId);
                ps.setInt(3, grade);
                System.out.println("Қосылған жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void showAll() {
            query("", null);
        }

        static void search(String text) {
            query("WHERE st.full_name ILIKE ?", "%" + text + "%");
        }

        private static void query(String where, String param) {
            String sql = "SELECT g.id, st.full_name AS student, sb.name AS subject, g.grade, g.grade_date "
                    + "FROM grades g JOIN students st ON g.student_id = st.id "
                    + "JOIN subjects sb ON g.subject_id = sb.id " + where + " ORDER BY g.id";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                if (param != null) ps.setString(1, param);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.printf("%-4s %-25s %-25s %-6s %-12s%n", "ID", "Студент", "Пән", "Баға", "Күні");
                    boolean any = false;
                    while (rs.next()) {
                        any = true;
                        System.out.printf("%-4d %-25s %-25s %-6d %-12s%n", rs.getInt("id"), rs.getString("student"),
                                rs.getString("subject"), rs.getInt("grade"), rs.getDate("grade_date"));
                    }
                    if (!any) System.out.println("(жазба табылмады)");
                }
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void update(int id, int grade) {
            String sql = "UPDATE grades SET grade = ? WHERE id = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, grade);
                ps.setInt(2, id);
                System.out.println("Өзгертілген жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }

        static void delete(int id) {
            String sql = "DELETE FROM grades WHERE id = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, id);
                System.out.println("Жойылған жазба саны: " + ps.executeUpdate());
            } catch (SQLException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }
    }

    //Мәзір
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== УНИВЕРСИТЕТ: басты мәзір ===");
            System.out.println("1. Студенттер");
            System.out.println("2. Оқытушылар");
            System.out.println("3. Пәндер");
            System.out.println("4. Бағалар");
            System.out.println("0. Шығу");
            int choice = Input.integer("Таңдаңыз: ");
            switch (choice) {
                case 1 -> studentsMenu();
                case 2 -> teachersMenu();
                case 3 -> subjectsMenu();
                case 4 -> gradesMenu();
                case 0 -> {
                    System.out.println("Бағдарлама аяқталды.");
                    return;
                }
                default -> System.out.println("Қате таңдау.");
            }
        }
    }

    private static int crudMenu(String title) {
        System.out.println("\n--- " + title + " ---");
        System.out.println("1. Қосу (Create)");
        System.out.println("2. Барлығын көрсету (Read)");
        System.out.println("3. Іздеу (Read)");
        System.out.println("4. Өзгерту (Update)");
        System.out.println("5. Жою (Delete)");
        System.out.println("0. Артқа");
        return Input.integer("Таңдаңыз: ");
    }

    private static void studentsMenu() {
        while (true) {
            switch (crudMenu("Студенттер")) {
                case 1 -> StudentDao.add(Input.text("Аты-жөні: "), Input.text("Топ: "),
                        Input.text("Email: "), Input.integer("Қабылданған жыл: "));
                case 2 -> StudentDao.showAll();
                case 3 -> StudentDao.search(Input.text("Аты бойынша іздеу: "));
                case 4 -> StudentDao.update(Input.integer("Студент ID: "), Input.text("Жаңа аты-жөні: "),
                        Input.text("Жаңа топ: "), Input.text("Жаңа email: "));
                case 5 -> StudentDao.delete(Input.integer("Студент ID: "));
                case 0 -> { return; }
                default -> System.out.println("Қате таңдау.");
            }
        }
    }

    private static void teachersMenu() {
        while (true) {
            switch (crudMenu("Оқытушылар")) {
                case 1 -> TeacherDao.add(Input.text("Аты-жөні: "), Input.text("Кафедра: "), Input.text("Email: "));
                case 2 -> TeacherDao.showAll();
                case 3 -> TeacherDao.search(Input.text("Аты бойынша іздеу: "));
                case 4 -> TeacherDao.update(Input.integer("Оқытушы ID: "), Input.text("Жаңа аты-жөні: "),
                        Input.text("Жаңа кафедра: "), Input.text("Жаңа email: "));
                case 5 -> TeacherDao.delete(Input.integer("Оқытушы ID: "));
                case 0 -> { return; }
                default -> System.out.println("Қате таңдау.");
            }
        }
    }

    private static void subjectsMenu() {
        while (true) {
            switch (crudMenu("Пәндер")) {
                case 1 -> SubjectDao.add(Input.text("Пән атауы: "), Input.integer("Кредит: "),
                        Input.integer("Оқытушы ID: "));
                case 2 -> SubjectDao.showAll();
                case 3 -> SubjectDao.search(Input.text("Атауы бойынша іздеу: "));
                case 4 -> SubjectDao.update(Input.integer("Пән ID: "), Input.text("Жаңа атауы: "),
                        Input.integer("Жаңа кредит: "), Input.integer("Жаңа оқытушы ID: "));
                case 5 -> SubjectDao.delete(Input.integer("Пән ID: "));
                case 0 -> { return; }
                default -> System.out.println("Қате таңдау.");
            }
        }
    }

    private static void gradesMenu() {
        while (true) {
            switch (crudMenu("Бағалар")) {
                case 1 -> GradeDao.add(Input.integer("Студент ID: "), Input.integer("Пән ID: "),
                        Input.integer("Баға (0-100): "));
                case 2 -> GradeDao.showAll();
                case 3 -> GradeDao.search(Input.text("Студент аты бойынша іздеу: "));
                case 4 -> GradeDao.update(Input.integer("Баға ID: "), Input.integer("Жаңа баға (0-100): "));
                case 5 -> GradeDao.delete(Input.integer("Баға ID: "));
                case 0 -> { return; }
                default -> System.out.println("Қате таңдау.");
            }
        }
    }
}