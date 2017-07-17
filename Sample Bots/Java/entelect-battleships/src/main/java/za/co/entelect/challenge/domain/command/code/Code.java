package za.co.entelect.challenge.domain.command.code;

public enum Code {
    DO_NOTHING(0),
    FIRESHOT(1),
    DOUBLE_SHOT_VERTICAL(2),
    DOUBLE_SHOT_HORIZONTAL(3),
    CORNER_SHOT(4),
    CROSS_SHOT_DIAGONAL(5),
    CROSS_SHOT_HORIZONTAL(6),
    SEEKER_MISSILE(7);

    private final int value;
    private Code(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
