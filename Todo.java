import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author DIVAKARAN
 * @version 1.0
 */

public class Todo 
{
	private static final String USAGE = "Usage :-\n" + "$ ./todo add \"todo item\"  # Add a new todo\n"
			+ "$ ./todo ls               # Show remaining todos\n" + "$ ./todo del NUMBER       # Delete a todo\n"
			+ "$ ./todo done NUMBER      # Complete a todo\n" + "$ ./todo help             # Show usage\n"
			+ "$ ./todo report           # Statistics";
	private static final String TODO = "todo.txt";
	private static final String DONE = "done.txt";
	/**
	 * 
	 * @param item
	 * @return added value
	 */
	private static String add(String item) 
	{
		addDataInFile(TODO, new String[] { item }, true);
		return "Added todo: \"" + item + "\"";
	}

	private static void ls() 
	{
		String[] items = getDataFromFile(TODO);
		if (items.length == 0) 
		{
			System.out.println("There are no pending todos!");
		} 
		else 
		{
			for (int i = items.length - 1; i >= 0; i--) 
			{
				System.out.printf("[%d] %s\n", i + 1, items[i]);
			}
		}
	}
	/**
	 * 
	 * @param itemNo
	 * @return deleted todo item no
	 */
	private static String del(String itemNo) 
	{
		String[] items = getDataFromFile(TODO);
		int index = Integer.parseInt(itemNo);
		if (index > items.length || index <= 0) 
		{
			return "Error: todo #" + itemNo + " does not exist. Nothing deleted.";
		}

		for (int i = 0; i < items.length - 1; i++) 
		{
			if (i < index - 1) 
			{
				continue;
			}
			items[i] = items[i + 1];
		}

		addDataInFile(TODO, Arrays.copyOf(items, items.length - 1), false);
		return "Deleted todo #" + itemNo;
	}
	/**
	 * 
	 * @param itemNo
	 * @return item no value
	 */
	private static String done(String itemNo) 
	{
		String[] items = getDataFromFile(TODO);
		int index = Integer.parseInt(itemNo);
		if (index > items.length || index <= 0) 
		{
			return "Error: todo #" + itemNo + " does not exist.";
		}

		String dataString = items[index - 1];
		for (int i = 0; i < items.length - 1; i++) 
		{
			if (i < index - 1) 
			{
				continue;
			}
			items[i] = items[i + 1];
		}

		addDataInFile(DONE, new String[] { dataString }, true);
		addDataInFile(TODO, Arrays.copyOf(items, items.length - 1), false);
		return "Marked todo #" + itemNo + " as done.";
	}

	private static void help() 
	{
		System.out.println(USAGE);
	}

	private static void report() 
	{
		int pending = getDataFromFile(TODO).length;
		int completed = getDataFromFile(DONE).length;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		System.out.printf("%s Pending : %d Completed : %d", formatter.format(date), pending, completed);
	}
	/**
	 * 
	 * @param file
	 * @return items
	 * @throws Exception
	 */
	private static String[] getDataFromFile(String file) 
	{
		ArrayList<String> dataList = new ArrayList<>();
		BufferedReader br = null;
		try 
		{
			File f = new File(file);
			if (!f.exists())
				f.createNewFile();
			br = new BufferedReader(new FileReader(file));
			String temp;
			while ((temp = br.readLine()) != null) 
			{
				dataList.add(temp);
			}

		} 
		catch (Exception ex) 
		{
			System.out.println("Problem to open \"todo.txt\".");
		} 
		finally 
		{
			if (br != null) 
			{
				try 
				{
					br.close();
				} 
				catch (IOException ex) 
				{

				}
			}
		}

		String[] items = new String[dataList.size()];
		for (int i = 0; i < items.length; i++) 
		{
			items[i] = dataList.get(i);
		}

		return items;
	}
	/**
	 * 
	 * @param file
	 * @param items
	 * @param append
	 * @throws Exception
	 */
	private static void addDataInFile(String file, String[] items, boolean append) 
	{
		try (PrintWriter pw = new PrintWriter(new FileWriter(file, append))) 
		{
			if (file == DONE) 
			{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				for (String item : items) 
				{
					pw.println(String.format("x %s %s", formatter.format(date), item));
				}
			} 
			else 
			{
				for (String item : items) 
				{
					pw.println(item);
				}
			}
		} 
		catch (Exception ex) 
		{
			System.out.println("Problem to open \"done.txt\".");
		}
	}

	public static void main(String[] args) 
	{
		try 
		{
			if (args.length == 0) 
			{
				help();
			} 
			else 
			{
				String option = args[0];
				if (option.equals("add")) 
				{
					if (args.length == 2)
						System.out.println(add(args[1]));
					else
						System.out.println("Error: Missing todo string. Nothing added!");
				}
				else if (option.equals("ls")) 
				{
					ls();
				}
				else if (option.equals("del")) 
				{
					if (args.length == 2)
						System.out.println(del(args[1]));
					else
						System.out.println("Error: Missing NUMBER for deleting todo.");
				} 
				else if (option.equals("done")) 
				{
					if (args.length == 2)
						System.out.println(done(args[1]));
					else
						System.out.println("Error: Missing NUMBER for marking todo as done.");
				} 
				else if (option.equals("help")) 
				{
					help();
				} 
				else if (option.equals("report")) 
				{
					report();
				} 
				else 
				{
					System.out.println("You enter wrong option.\n");
				}
			}
		} 
		catch (Exception dhvakr) 
		{
			System.out.println("Somthing went wrong.\n");
			help();
		}
	}
}
