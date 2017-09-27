import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Block {
	public String actionType;
	public String action;
	public ArrayList<String> parametersArrayList;
	Map<String, ArrayList<String>> data;

	public Block() {

		this.parametersArrayList = new ArrayList<>();
		this.data = new HashMap<String, ArrayList<String>>();
	}

}
