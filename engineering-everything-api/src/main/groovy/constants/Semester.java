package constants;

/**
 * Created by GnyaniMac on 03/05/17.
 */
public enum  Semester {
    ONE(1),
    TWO(2),
    THREE(3);

    private int value;
    private Semester(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
