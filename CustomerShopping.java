import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CustomerShopping {
	/**
	 * @param args
	 */
	class Transaction{
		String customer_name;
		String product_category;
		String item_description;
		double cost;
		
		@Override
		public String toString() {
			return customer_name + "|" + product_category + "|" + item_description + "|" + cost;
		}
	}
	
	private static final double SALES_TAX = 9.25/100;
	
	DecimalFormat format = new DecimalFormat("$00.00");
	
	//Stores all the transactions in an ArrayList
	ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	//Stores the HashMap(K,V)  => (Product Category, ArrayList)
	HashMap<String, ArrayList<Transaction>> transactionMap = new HashMap<String, ArrayList<Transaction>>();
	
	//Stores the HashMap(K, V) => (Customer Name, HashMap(K,V) (Product Category, ArrayList))
	HashMap<String, HashMap<String, ArrayList<Transaction>>> customersMap = new HashMap<String, HashMap<String, ArrayList<Transaction>>>(); 
	
	
	public void parseInput(String filename)
	{
		try
		{
			String temp = null;
			
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while((temp = br.readLine() )!= null)
			{
				Transaction t = new Transaction();
				StringTokenizer st = new StringTokenizer(temp, "|");
				t.customer_name = st.nextToken().toString();
				t.product_category = st.nextToken().toString();
				t.item_description = st.nextToken().toString();
				t.cost = Double.parseDouble(st.nextElement().toString());
				
				HashMap<String, ArrayList<Transaction>> tempCustomersMap = customersMap.get(t.customer_name);
				if(tempCustomersMap==null){
					tempCustomersMap = new HashMap<String, ArrayList<Transaction>>();
					customersMap.put(t.customer_name, tempCustomersMap);
				}
				ArrayList<Transaction> templist = tempCustomersMap.get(t.product_category);
				if(templist == null){
					templist = new ArrayList<Transaction>();
					tempCustomersMap.put(t.product_category, templist);
				}
				
				templist.add(t);
				
				
				ArrayList<Transaction> templist2 = transactionMap.get(t.product_category);
				if(templist2 == null){
					templist2 = new ArrayList<Transaction>();
					transactionMap.put(t.product_category, templist2);
				}
				templist2.add(t);
				
				transactions.add(t);
			}
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	
	public void report(boolean withSalesTax)
	{
		for(String customerName: customersMap.keySet())
		{
			System.out.println("Purchases by " + customerName + ": ");
			HashMap<String, ArrayList<Transaction>> categoriesMap = customersMap.get(customerName);
			if(categoriesMap!=null){
				for(String category: categoriesMap.keySet()){
					ArrayList<Transaction> transactions = categoriesMap.get(category);
					double sum = 0;
					for (Transaction transaction : transactions) {
						sum += transaction.cost;
					}
					System.out.println(category + ": " + format.format(sum*(withSalesTax?1+SALES_TAX:1)));
				}
				System.out.println();
			}
		}
	}
	
	public void reportCategories(boolean withSalesTax)
	{
		System.out.println("Total revenue by customer:");
		for(String customerName: customersMap.keySet())
		{
			HashMap<String, ArrayList<Transaction>> categoriesMap = customersMap.get(customerName);
			double sum = 0;
			if(categoriesMap!=null){
				for(String category: categoriesMap.keySet()){
					ArrayList<Transaction> transactions = categoriesMap.get(category);
					for (Transaction transaction : transactions) {
						sum += transaction.cost;
					}
				}
				System.out.println(customerName + " - " + format.format(sum*(withSalesTax?1+SALES_TAX:1)));
			}
		}
	}
	
	public void reportTransactions()
	{
		report(true);
		reportCategories(true);
	}
	
	public static void main(String[] args) {
		CustomerShopping obj = new CustomerShopping();
		String filename = "Transaction.txt";
		obj.parseInput(filename);
		System.out.println("Report 1:\n");
		obj.reportCategories(false);
		System.out.println("-------------------------------------\n");
		System.out.println("Report 2:\n");
		obj.report(false);
		System.out.println("-------------------------------------\n");
		System.out.println("Report 3:\n");
		obj.reportTransactions();
		
	}

}
