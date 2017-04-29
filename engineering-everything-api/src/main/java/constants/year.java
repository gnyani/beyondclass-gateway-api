package constants;

/**
 * Created by GnyaniMac on 29/04/17.
 */
public enum year {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private int value;
    private year(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
