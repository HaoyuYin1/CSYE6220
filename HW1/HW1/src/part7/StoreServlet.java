package part7;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StoreServlet extends HttpServlet {
	PrintWriter writer;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		writer = resp.getWriter();
		String store = req.getParameter("store");
		if(null != store) {
			switch (store) {
			case "book":
				displayBooks();
				writer.close();
				return;
			case "music":
				displayMusics();
				writer.close();
				return;
			case "computer":
				displayLaptops();
				writer.close();
				return;
			case "cart":
				List<Product> products = (List<Product>) req.getSession().getAttribute("products");
				displayCart(products);
				writer.close();
			default:
				break;
			}
		}else {
			displayBooks();
			writer.close();
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		writer = resp.getWriter();
		String action = req.getParameter("action");
		int count = 0;
		if(action.equals("add")) {
			String[] productNames = req.getParameterValues("product");
			List<Product> products = (List<Product>) req.getSession().getAttribute("products");
			if(products == null) {
				products = new ArrayList<Product>();
				req.getSession().setAttribute("products", products);
			}
			for (String string : productNames) {
				Product product = new Product();
				String name = string.substring(0, string.indexOf("[")).trim();
				float price = Float.parseFloat(string.substring(string.indexOf("$") + 1, string.indexOf("]")));
				product.setId(count++);
				product.setName(name);
				product.setPrice(price);
				products.add(product);
			}
			displayAdded(productNames);
			writer.close();
		}else if(action.equals("delete")) {
			String[] productIds = req.getParameterValues("product");
			List<Product> products = (List<Product>) req.getSession().getAttribute("products");
			List<Product> needDelete = new ArrayList<>();
			for(String str:productIds) {
				int index = Integer.parseInt(str);
				needDelete.add(products.get(index));
			}
			products.removeAll(needDelete);
			resp.sendRedirect("store?store=cart");
			
		}
		
	}
	
	public void displayAdded(String[] productNames) {
		writer.print("<html>");
		writer.print("<body>");
		writer.print("<div class='left-container' style='float:left; highwidth:200px'>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=book'>Book</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=music'>Music</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=computer'>Computer</a>\n" + 
				"        </div>  \n" + 
				"    </div>"+
				"	<div class='right-container' style='margin-left:210px'>\n");
		writer.print("<h4>The following items has been added to your shopping cart successful</h4>");
		for(String name: productNames) {
			writer.print("<p>-" + name + "</p>");
		}
		writer.print("<div>\n" + 
				"    <a href='store?store=book'>[Go to Books Page]</a>\n" + 
				"    <a href='store?store=music'>[Go to Music Page]</a>\n" + 
				"    <a href='store?store=computer'>[Go to Computer Page]</a>\n" + 
				"    <a href='store?store=cart'>[View Cart]</a>\n" + 
				"</div></div>");
		writer.print("</body>");
		writer.print("</html>");
	}
	
	public void displayCart(List<Product> products) {
		float total = 0;
		writer.print("<html>");
		writer.print("<body>");
		writer.print("<div class='left-container' style='float:left; highwidth:200px'>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=book'>Book</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=music'>Music</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=computer'>Computer</a>\n" + 
				"        </div>  \n" + 
				"    </div>");
		writer.print("<div class='right-container' style='margin-left:210px'>\n" + 
				"    <p>Cart</p>\n" + 
				"    <hr>\n");
		writer.print("<form action='store' method='post'>");
		writer.print("<input type='hidden' name='action' value='delete'>");
		if(products != null) {
			for(int i = 0; i < products.size(); i++) {
				Product product = products.get(i);
				writer.printf("<p><input type='checkbox' name='product' value='%d'> - %s [$%.2f] </p>", i, product.getName(), product.getPrice());
				total += product.getPrice();
			}
			writer.printf("<p>total price: %.2f</p>", total);
			writer.print("<input type='submit' value='delete select items'>");
		}
		writer.print("<div>\n" + 
				"    <a href='store?store=book'>[Go to Books Page]</a>\n" + 
				"    <a href='store?store=music'>[Go to Music Page]</a>\n" + 
				"    <a href='store?store=computer'>[Go to Computer Page]</a>\n" +  
				"</div></div>");
		writer.print("</body>");
		writer.print("</html>");
	}
	
	public void displayBooks() {
		writer.print("<html>");
		writer.print("<body>");
		writer.print("<div class='left-container' style='float:left; width:200px'>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=book'>Book</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=music'>Music</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=computer'>Computer</a>\n" + 
				"        </div>  \n" + 
				"    </div>");
		writer.print("<div class='right-container' style='margin-left:210px'>\n" + 
				"    <p>shop for book</p>\n" + 
				"    <hr>\n" + 
				"<a href='store?store=cart'>view cart</a>"+
				"    <form action='store' method='post'>\n" + 
				"		<input type='hidden' name='action' value='add'>" +
				"        <input type='checkbox' name='product' value='Java Servlet Programming [$29.95]' id=''>\n" + 
				"        Java Servlet Programming [$29.95] <br>\n" + 
				"        \n" + 
				"        <input type='checkbox' name='product' value='Oracle Database Programming [$48.95]' id=''>\n" + 
				"        Oracle Database Programming [$48.95] <br>\n" + 
				"        \n" + 
				"        <input type='checkbox' name='product' value='System Analysis and Design With UML [$14.95]' id=''>\n" + 
				"        System Analysis and Design With UML [$14.95] <br>\n" + 
				"        \n" + 
				"        <input type='checkbox' name='product' value='Object Oriented Software Engineering [$35.99]' id=''>\n" + 
				"        Object Oriented Software Engineering [$35.99] <br>\n" + 
				"        \n" + 
				"        <input type='checkbox' name='product' value='Java Web Service [$27.99]' id=''>\n" + 
				"        Java Web Service [$27.99] <br>\n" + 
				"        \n" + 
				"        <input type='submit' value='Add to cart'>\n" + 
				"    </form>\n" + 
				"</div>");
		writer.print("</body>");
		writer.print("</html>");
	}
	
	public void displayMusics() {
		writer.print("<html>");
		writer.print("<body>");
		writer.print("<div class='left-container' style='float:left; width:200px'>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=book'>Book</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=music'>Music</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=computer'>Computer</a>\n" + 
				"        </div>  \n" + 
				"    </div>");
		writer.print("    <div class='right-container' style='margin-left:210px'>\n" + 
				"        <p>shop for CDs</p>\n" + 
				"        <hr>\n" + 
				"<a href='store?store=cart'>view cart</a>"+
				"        <form action='store' method='post'>\n" + 
				"		<input type='hidden' name='action' value='add'>" +
				"            <input type='checkbox' name='product' value='I'm Going to Tell You a Secret by Madonna [$26.99]' id=''>\n" + 
				"            I'm Going to Tell You a Secret by Madonna [$26.99] <br>\n" + 
				"            \n" + 
				"            <input type='checkbox' name='product' value='Baby One More Time by Britney Spears [$10.95]' id=''>\n" + 
				"            Baby One More Time by Britney Spears [$10.95] <br>\n" + 
				"            \n" + 
				"            <input type='checkbox' name='product' value='Justified by Justin Timberlake [$9.97]' id=''>\n" + 
				"            Justified by Justin Timberlake [$9.97] <br>\n" + 
				"            \n" + 
				"            <input type='checkbox' name='product' value='Loose by Nelly Furtado [$13.98]' id=''>\n" + 
				"            Loose by Nelly Furtado [$13.98] <br>\n" + 
				"            \n" + 
				"            <input type='checkbox' name='product' value='Gold Digger by Kanye West [$27.99]' id=''>\n" + 
				"            Gold Digger by Kanye West [$27.99] <br>\n" + 
				"            \n" + 
				"            <input type='submit' value='Add to cart'>\n" + 
				"        </form>\n" + 
				"    </div>");
		writer.print("</body>");
		writer.print("</html>");
	}
	
	public void displayLaptops() {
		writer.print("<html>");
		writer.print("<body>");
		writer.print("<div class='left-container' style='float:left; width:200px'>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=book'>Book</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=music'>Music</a>\n" + 
				"        </div>\n" + 
				"        <div>\n" + 
				"            <a href='store?store=computer'>Computer</a>\n" + 
				"        </div>  \n" + 
				"    </div>");
		writer.print("<div class='right-container' style='margin-left:210px'>\n" + 
				"    <p>shop for CDs</p>\n" + 
				"    <hr>\n" + 
				"<a href='store?store=cart'>view cart</a>"+
				"    <form action='store' method='post'>\n" + 
				"		<input type='hidden' name='action' value='add'>" +
				"        <input type='checkbox' name='product' value='Apple MacBook Pro with 13.3\" Display [$1299.99]' id=''>\n" + 
				"        Apple MacBook Pro with 13.3' Display [$1299.99] <br>\n" + 
				"        \n" + 
				"        <input type='checkbox' name='product' value='Asus Laptop with Centrino 2 Black [$949.95]' id=''>\n" + 
				"        Asus Laptop with Centrino 2 Black [$949.95] <br>\n" + 
				"        \n" + 
				"        <input type='checkbox' name='product' value='HP Pavlion Laptop with Centrino 2 DV7 [$1199.95]' id=''>\n" + 
				"        HP Pavlion Laptop with Centrino 2 DV7 [$1199.95] <br>\n" + 
				"        \n" + 
				"        <input type='checkbox' name='product' value='Toshiba Satellite Laptop with Centrino 2-Copper [$899.99]' id=''>\n" + 
				"        Toshiba Satellite Laptop with Centrino 2-Copper [$899.99] <br>\n" + 
				"        \n" + 
				"        <input type='checkbox' name='product' value='Sony VAIO Laptop with Core 2 Duo Cosmopolitan Pink [$779.99]' id=''>\n" + 
				"        Sony VAIO Laptop with Core 2 Duo Cosmopolitan Pink [$779.99] <br>\n" + 
				"        \n" + 
				"        <input type='submit' value='Add to cart'>\n" + 
				"    </form>\n" + 
				"</div>");
		writer.print("</body>");
		writer.print("</html>");
	}
	
	
}

class Product {
	int id;
	String name;
	float price;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
}
