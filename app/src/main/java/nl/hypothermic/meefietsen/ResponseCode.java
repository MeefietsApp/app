package nl.hypothermic.meefietsen;

public enum ResponseCode {

    // Let op: niet voor authenticatie!! Alleen standaard responscodes

    SUCCESS(1),
    ERR_GENERIC(0),
    ERR_NOT_ENOUGH_ARGS(-1),
    ERR_ARGS_FORMAT(-2),
    ERR_PERMISSIONS(-9),

    INTERNAL_ERR_GENERIC(-101),
    INTERNAL_ERR_NOT_AUTH(-102),
    INTERNEL_ERR_NOT_SET(-103),

    ;private int value;

    ResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
