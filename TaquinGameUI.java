import javax.swing.*;
//import java.awt.Queue;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class TaquinGameUI extends JFrame implements ActionListener {
    private JPanel panel,taquinPanel;
    private JRadioButton bfsRadioBtn, dfsRadioBtn;
    private JTextArea textArea;
    private JButton newGameBtn;
   
    public TaquinGameUI() {
        super("Taquin Game");

        panel = new JPanel(new GridLayout(0, 2));
        add(panel, BorderLayout.CENTER);

        // Create the components for the first column
        JPanel column1 = new JPanel(new BorderLayout());
        JLabel configLabel = new JLabel("Initial Configuration:");
        column1.add(configLabel, BorderLayout.NORTH);

        // Add the initial configuration of the Taquin game
        taquinPanel = new JPanel(new GridLayout(3, 3));
        int[][] taquinConfig = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JLabel tile = new JLabel("" + taquinConfig[i][j], SwingConstants.CENTER);
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                taquinPanel.add(tile);
            }
        }
        column1.add(taquinPanel, BorderLayout.CENTER);

        // Add the radio buttons for the search algorithms
        JPanel radioPanel = new JPanel(new GridLayout(2, 1));
        bfsRadioBtn = new JRadioButton("Breadth First Search", true);
        dfsRadioBtn = new JRadioButton("Depth First Search");
        ButtonGroup group = new ButtonGroup();
        group.add(bfsRadioBtn);
        group.add(dfsRadioBtn);
        radioPanel.add(bfsRadioBtn);
        radioPanel.add(dfsRadioBtn);
        column1.add(radioPanel, BorderLayout.SOUTH);

        // Add the first column to the panel
        panel.add(column1);

        // Create the components for the second column
        JPanel column2 = new JPanel(new BorderLayout());
        JLabel outputLabel = new JLabel("Output:");
        column2.add(outputLabel, BorderLayout.NORTH);

        // Add the text area for the output
        textArea = new JTextArea(10, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        column2.add(scrollPane, BorderLayout.CENTER);

        // Add the new game button
        newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(this);
        column2.add(newGameBtn, BorderLayout.SOUTH);

        // Add the second column to the panel
        panel.add(column2);

        // Set the window properties
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameBtn) {
            // Generate a random configuration for the Taquin game
            int[][] taquinConfig = generateRandomConfig();

            
            
            //int[][] startMatrix = {{1, 2, 0}, {3, 4, 5}, {6, 7, 8}};
            int[][] startMatrix=taquinConfig;

            updateTaquinPanel(startMatrix);
            // Update the UI with the new configuration
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    sb.append(taquinConfig[i][j]).append(" ");

                }
                sb.append("\n");
            }
            textArea.append(sb.toString());

            int[][] targetMatrix = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
            State startState = new State(startMatrix);
            State targetState = new State(targetMatrix);
        
            System.out.println("Initial State:");
            startState.printMatrix();
            if(bfsRadioBtn.isSelected())
            {
                System.out.println("\nSolving using BFS:");
                textArea.append("\nSolving using BFS:\n");
                boolean bfsResult = bfs(startState, targetState);
                if (bfsResult) {
                    System.out.println("BFS Solution found!");
                    textArea.append("BFS Solution found!\n");
                } else {
                    System.out.println("BFS Solution not found.");
                    textArea.append("BFS Solution not found.\n");
                }
            }
            if(dfsRadioBtn.isSelected())
            {
                System.out.println("\nSolving using DFS:");
                textArea.append("\nSolving using DFS:\n");
                boolean dfsResult = dfs(startState, targetState);
                if (dfsResult) {
                    System.out.println("DFS Solution found!");
                    textArea.append("DFS Solution found!\n");
                } else {
                    System.out.println("DFS Solution not found.");
                    textArea.append("DFS Solution not found.\n");
                }
            }
            
        
            
            
           
        }
    
    }
     final int[] dx = {1, -1, 0, 0};
     final int[] dy = {0, 0, 1, -1};
     class State {
        int[][] matrix;
        int x;
        int y;

        public State(int[][] matrix) {
            this.matrix = matrix;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (matrix[i][j] == 0) {
                        x = i;
                        y = j;
                        return;
                    }
                }
            }
        }

        public int[][] getMatrix() {
            return matrix;
        }
        public int getX() { return x;}
        public int getY() { return y;}
        public void printMatrix() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
        }

        public State move(int nx, int ny) {
        int[][] newMatrix = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
        }
        newMatrix[x][y] = newMatrix[nx][ny];
        newMatrix[nx][ny] = 0;
        return new State(newMatrix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Arrays.deepEquals(matrix, state.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }
}
     boolean dfs(State start, State end) {
        Stack<State> stack = new Stack<>();
        Set<State> visited = new HashSet<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            State state = stack.pop();
            System.out.println("Current state:");
            state.printMatrix();
            // Update the UI with the new configuration
            textArea.append("Current state:\n");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    sb.append(state.getMatrix()[i][j]).append(" ");

                }
                sb.append("\n");
            }
            textArea.append(sb.toString());
            textArea.append("\n");


            if (state.equals(end)) {
                return true;
            }
            visited.add(state);
            for (int i = 0; i < 4; i++) {
                int nx = state.x + dx[i];
                int ny = state.y + dy[i];
                if (nx < 0 || nx >= 3 || ny < 0 || ny >= 3) {
                    continue;
                }
                State next = state.move(nx, ny);
                if (!visited.contains(next)) {
                    stack.push(next);
                }
            }
        }
        return false;
    }
     boolean bfs(State start, State end) {
        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();
        queue.offer(start);
        while (!queue.isEmpty()) {
            State state = queue.poll();
            System.out.println("Current state:");
            state.printMatrix();
            // Update the UI with the new configuration
            textArea.append("Current state:\n");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    sb.append(state.getMatrix()[i][j]).append(" ");

                }
                sb.append("\n");
            }
            textArea.append(sb.toString());
            textArea.append("\n");

            if (state.equals(end)) {
                return true;
            }
            visited.add(state);
            for (int i = 0; i < 4; i++) {
                int nx = state.x + dx[i];
                int ny = state.y + dy[i];
                if (nx < 0 || nx >= 3 || ny < 0 || ny >= 3) {
                    continue;
                }
                State next = state.move(nx, ny);
                if (!visited.contains(next)) {
                    queue.offer(next);
                }
            }
        }
        return false;
    }
    
    private int[][] generateRandomConfig() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 0};
        int[][] config = new int[3][3];
        int index = 0;
        for (int i = 0; i < config.length; i++) {
            for (int j = 0; j < config[i].length; j++) {
                index = (int) (Math.random() * values.length);
                config[i][j] = values[index];
                values[index] = values[values.length - 1];
                values = Arrays.copyOf(values, values.length - 1);
            }
        }
        return config;
    }
    public boolean isGoalState(int[][] puzzle) {
        int count = 1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (puzzle[row][col] != count % 9) {
                    return false;
                }
                count++;
            }
        }
        return true;
    }
    
        
   
    private void updateTaquinPanel(int[][] taquinConfig) {
        JPanel taquinPanel2 = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(taquinConfig[i][j]==0)
                {
                    JLabel tile = new JLabel("", SwingConstants.CENTER);
                    tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    taquinPanel2.add(tile);
                    
                }
                else{
                    JLabel tile = new JLabel("" + taquinConfig[i][j], SwingConstants.CENTER);
                    tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    taquinPanel2.add(tile);
                }
                
            }
        }

        taquinPanel.removeAll();
        taquinPanel.add(taquinPanel2);
        taquinPanel.revalidate();
        taquinPanel.repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaquinGameUI());
    }
}
