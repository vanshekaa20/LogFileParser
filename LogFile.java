import java.util.ArrayList;
import java.util.Date;

public class LogFile {
	// Log File Properties
	public String filePath;
	public String fileName;
	public Date DateCreated;
	public long fileSizeInBytes;

	public ArrayList<Block> blocks;

	public LogFile(String filePath) {
		this.filePath = filePath;
		this.blocks = new ArrayList<>();
	}

}
