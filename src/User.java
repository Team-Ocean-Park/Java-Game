public class User {

    private Screen screen;

    Player player;

    int startingMoney = 25;
    int startingHealth = 10;

    public User(Screen screen) {
        this.screen = screen;

        this.screen.scene = 0; //sets the scene to main menu
    }

    public  void createPlayer(){
        this.player =  new Player(this);
    }
}
