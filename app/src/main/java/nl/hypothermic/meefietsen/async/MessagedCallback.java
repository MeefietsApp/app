package nl.hypothermic.meefietsen.async;

/**
 * Callback interface for deserialization
 */
public interface MessagedCallback<T extends Object> {

	void onAction(T val, String msg);

}