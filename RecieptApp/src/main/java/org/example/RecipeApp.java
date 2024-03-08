package org.example;
import java.sql.*;
import java.util.Scanner;

public class RecipeApp {
    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/receiptdb"; //Тут надо заменить на вашу строку подключения
        String username = "root";
        String password = "";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
        return conn;
    }

    public void insertRecipe(String title, String ingredients, String instructions) {
        String sql = "INSERT INTO recipes(title, ingredients, instructions) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, ingredients);
            pstmt.setString(3, instructions);
            pstmt.executeUpdate();
            System.out.println("Recipe added successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listRecipes() {
        String sql = "SELECT id, title, ingredients, instructions FROM recipes";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("title") + "\t" +
                        rs.getString("ingredients") + "\t" +
                        rs.getString("instructions"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRecipe(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int deletedRows = pstmt.executeUpdate();
            if (deletedRows > 0)
                System.out.println("Recipe deleted successfully!");
            else
                System.out.println("No recipe found with the given ID.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        RecipeApp app = new RecipeApp();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Recipe Application");
        while (true) {
            System.out.println("1. Add Recipe\n2. List Recipes\n3. Delete Recipe\n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter title:");
                    String title = scanner.nextLine();
                    System.out.println("Enter ingredients:");
                    String ingredients = scanner.nextLine();
                    System.out.println("Enter instructions:");
                    String instructions = scanner.nextLine();

                    app.insertRecipe(title, ingredients, instructions);
                    break;
                case 2:
                    app.listRecipes();
                    break;
                case 3:
                    app.listRecipes();
                    System.out.println("Enter the ID of the recipe to delete:");
                    int idToDelete = scanner.nextInt();
                    app.deleteRecipe(idToDelete);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}
