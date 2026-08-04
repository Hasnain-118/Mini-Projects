import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TicTacToeGame {
    
    public static boolean checkWin(char[][] board, char currentPlayer) {
        for (int i = 0; i < 3; i++) {
            if ((board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) ||
                (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer)) {
                return true;
            }
        }
        
        if ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
            (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)) {
            return true;
        }
        
        return false;
    }
    
    public static void recordMatchHistory(String result) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("match_history.txt", true))) {
            writer.println("Match: " + result);
        } catch (IOException e) {
            System.err.println("Error: Unable to save match history.");
        }
    }
    
    public static void displayBoard(char[][] board) {
        System.out.println("\nCurrent Board:");
        for (int i = 0; i < 3; i++) {
            System.out.println(" " + board[i][0] + " | " + board[i][1] + " | " + board[i][2]);
            if (i < 2) {
                System.out.println("---|---|---");
            }
        }
        System.out.println();
    }
    
    public static char[][] initializeBoard() {
        char[][] board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        return board;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char playAgain;
        
        System.out.println("\n=================================");
        System.out.println("     WELCOME TO TIC-TAC-TOE     ");
        System.out.println("=================================");
        
        do {
            char[][] board = initializeBoard();
            boolean gameOver = false;
            char currentPlayer = 'X';
            int moves = 0;
            
            displayBoard(board);
            
            while (moves < 9) {
                System.out.println("\n=== PLAYER " + currentPlayer + "'S TURN ===");
                
                int row, col;
                try {
                    System.out.print("Enter row (0-2): ");
                    row = scanner.nextInt();
                    System.out.print("Enter column (0-2): ");
                    col = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter numeric values between 0 and 2.");
                    scanner.nextLine();
                    continue;
                }
                
                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    System.out.println("Invalid input! Please enter numbers between 0 and 2.");
                    continue;
                }
                
                if (board[row][col] == ' ') {
                    board[row][col] = currentPlayer;
                    moves++;
                    
                    displayBoard(board);
                    
                    if (checkWin(board, currentPlayer)) {
                        System.out.println(" Player " + currentPlayer + " wins! ");
                        recordMatchHistory("Player " + currentPlayer + " wins!");
                        gameOver = true;
                        break;
                    }
                    
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                } else {
                    System.out.println("Cell already taken! Please choose an empty cell.");
                }
            }
            
            if (!gameOver) {
                System.out.println(" Game Draw! ");
                recordMatchHistory("Game Draw");
            }
            
            System.out.print("\nDo you want to play again? (y/Y): ");
            playAgain = scanner.next().charAt(0);
            
        } while (playAgain == 'y' || playAgain == 'Y');
        
        System.out.println("\nThank you for playing Tic-Tac-Toe!");
        System.out.println("=================================");
        scanner.close();
    }
}
