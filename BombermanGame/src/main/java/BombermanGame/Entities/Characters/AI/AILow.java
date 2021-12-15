package BombermanGame.Entities.Characters.AI;

public class AILow extends AIEnemy {
    public AILow() {
        super(false);
    }

    @Override
    public void calcDangerDistance() {}

    @Override
    public int calculateDirection() {
        return random.nextInt(4);
    }
}