package nl.hypothermic.meefietsen.async;

/**
 * Callback interface for deserialization
 */
public interface GenericCallback<T extends Object> {

	void onAction(T val);

}