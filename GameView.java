import javax.swing.JOptionPane;

public class GameView {

    private BoardLayersListener bll; 

    public GameView() {

    }

    public void initWindow() {
        this.bll = new BoardLayersListener();
        this.bll.setVisible(true);

        JOptionPane.showInputDialog(this.bll , "How many players?"); 
    }
}
