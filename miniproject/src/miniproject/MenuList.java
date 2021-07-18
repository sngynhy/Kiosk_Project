package miniproject;

public class MenuList {

	String menu;
	int price;
	int stock;
	
	public MenuList(String menu, int price, int stock) {
		this.menu = menu;
		this.price = price;
		this.stock = stock;
	}
	
	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	

}
