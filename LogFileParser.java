import java.io.File;
import java.util.Scanner;

public class LogFileParser {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			System.out.print("Enter the path to the log file: " );
			String input = scanner.next();
			File file = new File(input);
			if (!file.exists())
			{
				System.out.println("File Does Not Exist.");
				continue;
			}
			else {
				String extension = "";
				int i = file.getName().lastIndexOf('.');
				if (i >= 0) {
					extension = file.getName().substring(i+1);
					if (!extension.equals("txt"))
					{
						System.out.println("Not a text file.");
						continue;
					}
				}
				System.out.flush();
				LogFileParser obj = new LogFileParser();
				LogFile lfile = new LogFile(file.getPath());
				//obj.parseLog(lfile); // Function to parse the log file.
				System.out.println("*****************|"+ " FileName:" + file.getName() + " | Size:" + lfile.fileSizeInBytes + " bytes" +"|****************");
				//obj.serializeToJSON(lfile); // Function to serialize the data-structure into a JSON Object.
				System.out.println();

				System.out.println("Do you have another file? (Y/N) : ");
				input = scanner.next();
				if (input.equals("n") || input.equals("N")){
					System.out.println("***************** THANK YOU ****************");
					scanner.close();
					break;	
				}
				else
					System.out.flush();
			}	
			
		}
	}
}


