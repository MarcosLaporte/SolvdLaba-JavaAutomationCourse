package entitiesDAO;

public class MultipleIdFieldsException extends Exception {
    public MultipleIdFieldsException() {
        super("Entity has multiple ID fields.");
    }

    public MultipleIdFieldsException(String message) {
        super(message);
    }
}
