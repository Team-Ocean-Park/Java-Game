public class Player {

    double health;
    double money;

    public Player(User user){
        this.money = user.startingMoney;
        this.health = user.startingHealth;
    }
}
