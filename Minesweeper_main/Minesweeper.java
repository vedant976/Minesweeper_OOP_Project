import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.sound.sampled.*;

public class Minesweeper 
{
    private class MineTile extends JButton
    {
        int r;
        int c;

        public MineTile(int r, int c)
        {
            this.r=r;
            this.c=c;
        }
    }
    public class SoundPlayer 
    {
        public static void playSound(String soundFilePath) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
    JFrame frame = new JFrame("MINESWEEPER");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    int mineCount=10;
    int tileSize=70;
    int numRows=8;
    int numCols=numRows;
    int boardWidth=numCols * tileSize;
    int boardHeight=numRows * tileSize;
    

    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;
    private boolean soundEnabled = true;
    private Timer gameTimer;
    private int elapsedTime = 0;
    private boolean timerStarted = false;
    private String difficultyLevel = "CUSTOM";

    Minesweeper(int rows, int cols, int mines, boolean soundEnabled, String difficultyLevel)
    {
        this.numRows = rows;
        this.numCols = cols;
        this.mineCount = mines;
        this.boardWidth = numCols * tileSize;
        this.boardHeight = numRows * tileSize;
        this.board = new MineTile[numRows][numCols];
        this.soundEnabled = soundEnabled;
        this.difficultyLevel = difficultyLevel;
        mineList = new ArrayList<>();
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("MINESWEEPER: "+Integer.toString(mineCount));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        /*boardPanel.setBackground(Color.GREEN);*/
        frame.add(boardPanel);

        for(int r=0; r<numRows; r++)
        {
            for(int c=0; c<numCols; c++)
            {
                MineTile tile=new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
                //tile.setText("💣");
                tile.addMouseListener(new MouseAdapter() 
                {               
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        if(gameOver)
                        {
                            return;
                        }
                        if (!timerStarted) {
                            startTimer();
                            timerStarted = true;
                        }
                        MineTile tile = (MineTile)e.getSource(); 

                        //left click
                        if(e.getButton() == MouseEvent.BUTTON1)  //left click to reveal tile
                        {
                            SoundPlayer.playSound("C:\\Users\\Hp\\Desktop\\MineSweeper\\src\\sounds\\Menu Selection Click.wav");
                            if(tile.getText()=="")
                            {
                                if(mineList.contains(tile))
                                {
                                    revealMines();
                                }
                                else
                                {
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        }
                        else if(e.getButton()==MouseEvent.BUTTON3)   //right click to flag tile
                        {
                            SoundPlayer.playSound("C:\\Users\\Hp\\Desktop\\MineSweeper\\src\\sounds\\item_pickup.wav");
                            if(tile.getText()=="" && tile.isEnabled())
                            {
                                tile.setText("🚩");
                            }
                            else if(tile.getText()=="🚩")
                            {
                                tile.setText("");
                            }
                        }
                    }                    
                });
                boardPanel.add(tile);
            }
        }
        frame.setVisible(true);
        setMines();
    }
    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            elapsedTime++;
            textLabel.setText("Time: " + elapsedTime + "s | Mines: " + mineCount);
        });
        gameTimer.start();
    }
    
    void setMines()
    {
        /*mineList = new ArrayList<MineTile>();
        mineList.add(board[2][2]);
        mineList.add(board[2][7]);
        mineList.add(board[7][1]);
        mineList.add(board[3][4]);
        mineList.add(board[1][1]);*/
        int mineLeft = mineCount;
        while(mineLeft > 0)
        {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if(!mineList.contains(tile))
            {
                mineList.add(tile);
                mineLeft-=1;
            }
        }
        
    }

    void revealMines()
    {
        if (gameTimer != null) gameTimer.stop();
        if (soundEnabled) SoundPlayer.playSound("C:\\Users\\Hp\\Desktop\\MineSweeper\\src\\sounds\\8bit_bomb_explosion.wav");
        for(int i=0; i<mineList.size(); i++)
        {
            MineTile tile=mineList.get(i);
            tile.setText("💣");
        }
        gameOver=true;
        textLabel.setText("GAME OVER");
        StatsManager.incrementGamesPlayed();
    }

    void checkMine(int r, int c)
    {
        if(r<0 || r>=numRows || c<0 || c>=numCols)
        {
            return;
        }
        MineTile tile = board[r][c];
        if(!tile.isEnabled())
        {
            return;
        }

        tile.setEnabled(false);
        tilesClicked+=1;
        int minesFound=0;

        //counting mines around tiles
        // top 3
        minesFound+=countMine(r-1, c-1); //check top left tile
        minesFound+=countMine(r-1, c); //check top tile
        minesFound+=countMine(r-1, c+1); //check top right tile

        //left and right
        minesFound+=countMine(r, c-1); //left tile
        minesFound+=countMine(r, c+1); //right tile

        //bottom 3
        minesFound+=countMine(r+1, c-1); //check bottom left tile
        minesFound+=countMine(r+1, c); //check bottom tile
        minesFound+=countMine(r+1, c+1); //check bottom right tile

        if(minesFound>0)
        {
            tile.setText(Integer.toString(minesFound));
        }
        else
        {
            tile.setText("");
            //cascading check
            // top 3
            checkMine(r-1, c-1); //check top left tile
            checkMine(r-1, c); //check top tile
            checkMine(r-1, c+1); //check top right tile

            //left and right
            checkMine(r, c-1); //left tile
            checkMine(r, c+1); //right tile

            //bottom 3
            checkMine(r+1, c-1); //check bottom left tile
            checkMine(r+1, c); //check bottom tile
            checkMine(r+1, c+1); //check bottom right tile
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            if (gameTimer != null) gameTimer.stop();

            StatsManager.incrementGamesPlayed(); 
            StatsManager.incrementGamesWon();    
            
            // Update high score
            HighScoreManager.updateHighScore(difficultyLevel, elapsedTime);
            int bestTime = HighScoreManager.getHighScore(difficultyLevel);
            
            textLabel.setText("MINES CLEARED, YOU WIN!");
            if (soundEnabled) SoundPlayer.playSound("C:\\Users\\Hp\\Desktop\\MineSweeper\\src\\sounds\\Won!.wav");
            
            saveHighScore(difficultyLevel, elapsedTime);

            JOptionPane.showMessageDialog(frame,
                "You Win!\nTime: " + elapsedTime + "s\nBest Time: " + bestTime + "s",
                "Victory!",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveHighScore(String difficulty, int timeInSeconds) 
    {
        try (FileWriter writer = new FileWriter("C:\\Users\\Hp\\Desktop\\MineSweeper\\src\\highscores.txt", true))
        { // true = append mode
            writer.write(difficulty + ": " + timeInSeconds + "s\n");
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    int countMine(int r, int c)
    {
        if(r<0 || r>=numRows || c<0 || c>=numCols)
        {
            return 0;
        }
        if(mineList.contains(board[r][c]))
        {
            return 1;
        }
        return 0;
    }
}
