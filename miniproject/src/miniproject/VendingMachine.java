package miniproject;

import java.util.HashMap;
import java.util.Scanner;


public class VendingMachine {

			// ���� ��, ���� �׸�, �Ž��� ��, �ֹ� �޴� ����, �� ���� �ݾ�
	static int money, choiceNum, change = 0, stampCnt = 0, totalPrice = 0;
	static int Quantity; // ���� �޴� ���� ����

	// HashMap<key type, value type>
	// value Ÿ���� MenuList ��ü�� �־���
	static HashMap<Integer, MenuList> menuMap = new HashMap<Integer, MenuList>();  // <menuNum : menuList>
	static HashMap<Integer, int[]> orderMap = new HashMap<Integer, int[]>();  // <choiceNum : {price, Quantity}>
	static HashMap<String, Integer> stampMap = new HashMap<String, Integer>();  // <userPhoneNum : stampCnt>
	
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws InterruptedException {
		
		// �޴� �ʱ�ȭ     			 �޴�, ����, ���
		menuMap.put(1, new MenuList("�Ƹ޸�ī��", 1500, 10));
		menuMap.put(2, new MenuList("ī���", 2000, 10));
		menuMap.put(3, new MenuList("���ν����", 3000, 10));
		menuMap.put(4, new MenuList("���ڶ�", 2000, 0));
		menuMap.put(5, new MenuList("��ũƼ", 2500, 2));
		menuMap.put(6, new MenuList("���Ʈ������", 3000, 0));
		menuMap.put(7, new MenuList("������ ���", 0, 0));

		stampMap.put("01092593937", 11);
		stampMap.put("01045206591", 9);
		stampMap.put("01094823937", 5);
		
		// �޴� �ֹ�
		Order();
		
		// ���� �ϱ�  << �ڵ� ���� �ʿ�
		pay();
		
		// ���� ��� ���� -> ������ �̿�? �׳� ����?
//		useStamp();  ���� �� ȣ��


		// ���Ƥ��� -> �޴��� ���ڸ� �Է¹ް� �迭 �� �������� ������ ����ڽ��ϱ�?
//		saveStamp(); ���� �� ȣ��


	}


	////////////// �� ��� �Լ��� ���� //////////////////
	
	
	static String phoneNumCheck() {  // �޴��� ��ȣ �Է� ��ȿ�� �˻�
		
		String regExp = "^01(?:0|1|[6-9])[-]?(\\d{3}|\\d{4})[-]?(\\d{4})$";  // ���Խ����� �޴��� ��ȣ ���� �����
		String phoneNum; // ������ ����� �޴��� ��ȣ -> stampMap�� ����
		
		System.out.println("010-1234-5678�� ���·� �޴��� ��ȣ�� �Է��ϼ���.");
		System.out.print("�Է�>> ");
		phoneNum = sc.next();
		
		phoneNum = phoneNum.replace("-", "");
		
		System.out.println();
		
		
		while (!phoneNum.matches(regExp)) {
			System.out.println("��ȿ���� ���� ��ȣ�Դϴ�. �ٽ� �Է��ϼ���.");
			System.err.print("���Է�>> ");
			phoneNum = sc.next();
		}
		
		System.out.println(phoneNum +"��(��) �½��ϱ�?");
		System.out.print("1.��  2.�ƴϿ� >> ");
		choiceNum = sc.nextInt();
		
		validation(choiceNum, 2);
		
		while (choiceNum == 2) {
			System.out.print("�޴��� ��ȣ�� �ٽ� �Է��ϼ���>> ");
			phoneNum = sc.next();
			
			while (!phoneNum.matches(regExp)) {
				System.out.println("��ȿ���� ���� ��ȣ�Դϴ�. �ٽ� �Է��ϼ���.");
				System.err.print("���Է�>> ");
				phoneNum = sc.next();
			}
			
			System.out.println(phoneNum +"��(��) �½��ϱ�?");
			System.out.print("1.��  2.�ƴϿ� >> ");
			choiceNum = sc.nextInt();
			
			validation(choiceNum, 2);
		}
		System.out.println();
		return phoneNum;
	}
	
// ---------------------------------------------------------------

	static void useStamp(String phoneNum) {  // ������ ���
		
		if (!stampMap.containsKey(phoneNum)) {  // ���� ��ϵ� ��ȣ�� �ƴ� ���
			saveStamp(phoneNum);
			return;
		}
		
		if (choiceNum == 1) {
			System.out.println(phoneNum + "���� ������ �� ������ ������ " + stampMap.get(phoneNum) + "�� �Դϴ�.");

		}
		System.out.println();
		
		if (stampMap.get(phoneNum) < 10) {
			System.out.println("������ ������ ���ڶ��ϴ�.");
			return;
		}
		else if (stampMap.get(phoneNum) >= 10) {
			System.out.println("������ 10�� ��� �� �Ƹ޸�ī�� ������ ����� �����˴ϴ�.");
			System.out.println("�޴� ������ ���Ͻø� ���׸�ŭ �߰��� ������ �����մϴ�.");
			totalPrice -= 1500;
		}

		stampMap.put(phoneNum, stampMap.get(phoneNum)-10);
		System.out.println(phoneNum+"���� �������� 10�� �����Ǿ� ���� ������ �� " + stampMap.get(phoneNum) + "�� �Դϴ�.");
	}

 // ---------------------------------------------------------------

	static void saveStamp(String phoneNum) {  // ������ ����

		if (stampMap.containsKey(phoneNum)) {  // ������� phoneNum�� stampMap�� ���� �ϴ��� Ȯ��
			stampMap.put(phoneNum, stampMap.get(phoneNum) + stampCnt);
			System.out.println(phoneNum + "���� �������� " + stampCnt + "�� �߰��Ǿ�\n�� ������ ������ " + stampMap.get(phoneNum) +"�� �Դϴ�.");
		}
		else {  // ���� ��ϵ� ��ȣ�� �ƴ� ���
			System.out.println("��ϵ��� ���� ��ȣ�Դϴ�.");
			System.out.println("��ȣ�� ����Ͻðڽ��ϱ�?");
			System.out.print("1.��  2.�ƴϿ� >> ");
			choiceNum = sc.nextInt();
			System.out.println();

			validation(choiceNum, 2);
			
			if (choiceNum == 1) {
				stampMap.put(phoneNum, stampCnt);
				System.out.println("��ȣ�� ��ϵǾ����ϴ�.");
				System.out.println(phoneNum + "���� �������� " + stampCnt + "�� �߰��Ǿ�\n�� ������ ������ " + stampMap.get(phoneNum) +"�� �Դϴ�.");
			}
		}
		// �ٸ� ���
//		boolean flag = true;
//		stampMap.forEach((key, value)->{
//			if (key == phoneNum) {  // ������� phoneNum�� stampMap�� ���� �ϴ��� Ȯ��
//				stampMap.put(phoneNum, value += stampCnt);
//				System.out.println(phoneNum + "���� �������� " + stampCnt + "�� �߰��Ǿ� �� ������ ������ " + stampMap.get(phoneNum) +"�� �Դϴ�.");
//				flag = false;
//				return;
//			}
//		});
//		if (flag) {
//			stampMap.put(phoneNum, stampCnt);
//			System.out.println("��ȣ�� ��ϵǾ����ϴ�.");
//			System.out.println(phoneNum + "���� �������� " + stampCnt + "�� �߰��Ǿ� �� ������ ������ " + stampMap.get(phoneNum) +"�� �Դϴ�.");	
//		}
		}


// -------------------------------------------------------------------
	
	static void pay() throws InterruptedException {  // ���� ���
		
		/*
       ===== �ֹ��Ͻ� �޴� ======
       Ŀ��(1500��) x 1��
       �ֽ�(2000��) x 2��
       ========================
       �� ���� ������ 5500�� �Դϴ�. */
		System.out.println("====== �ֹ��Ͻ� �޴� ======");
		orderMap.forEach((key, value)->{
			System.out.println(menuMap.get(key).getMenu() + "(" + value[0] + "��)" +" x " + value[1] + "��");
			totalPrice += value[0] * value[1];
		});
		if (totalPrice==0) {
			System.out.println("�ֹ��Ͻ� �޴��� �����ϴ�.\n�ȳ��� ���ʽÿ�. (--)(__)*");
			System.out.println("========================");
			return;
		}
		System.out.println("========================");
		System.out.println();

		// ���⼭ ������ ���� or ���
		System.out.println("�������� �̿��Ͻðڽ��ϱ�?");
		System.out.print("1.���  2.����  3.�̿��������� >> ");
		choiceNum = sc.nextInt();
		System.out.println();

		validation(choiceNum, 3);
		
		if (choiceNum == 1) {
			String phoneNum = phoneNumCheck();
			useStamp(phoneNum);
		} else if (choiceNum == 2) {
			String phoneNum = phoneNumCheck();
			saveStamp(phoneNum);
		}

		System.out.println();
		System.out.println("�� ���� �ݾ��� " + totalPrice + "�� �Դϴ�.");
		
		if (totalPrice != 0) {
			System.out.print("���� �ݾ��� �Է��ϼ���>> ");
			money = sc.nextInt();
			
			System.out.println();
			
			// �ݾ��� ������ ���
			while (money < totalPrice) {
				System.out.println("�ݾ��� �����մϴ�. �ٽ� �Է��ϼ���.");
				System.err.print("���Է�>> ");
				money = sc.nextInt();
			}
			// �Ž����� ��ȯ
			if (money > totalPrice) {
				change = money - totalPrice;// �Ž��� ��
				System.out.println("�Ž����� " + change +"���� ��ȯ�˴ϴ�.");
			}
		}
		
		System.out.println("������ �Ϸ�Ǿ����ϴ�.\n");

		// ������ ���������� �̷������ ���
		System.out.println("�� ���� ������ ��\n");
		System.out.println("�층~��");
		Thread.sleep(1000);
		System.out.println("�ֹ��Ͻ� ���ᰡ ���Խ��ϴ�. \n�̿����ּż� �����մϴ�.");  // money == totalPrice
		System.out.print("(--)");
		Thread.sleep(500);
		System.out.print("(__)");
		Thread.sleep(500);
		System.out.print("(--)");
		Thread.sleep(500);
		System.out.print("(__)*");
		System.out.println();
	}
	
//-----------------------------------------------------------
	
	static void viewMenu() {  // �޴� ��� �Լ�
		
		System.out.println("========== �޴� ==========");
		// forEach(key, value)
		menuMap.forEach((key, value)->{
			if (key==7) {
				return;  // <<<<<<<<< break ��� return����!
			}
			if (value.getStock() == 0) {
				System.out.print(key + "."); 
				System.out.print(value.getMenu() + " " + value.getPrice() + "�� ");
				System.err.println("(������)");
			} else {
				System.out.print(key + "."); 
				System.out.println(value.getMenu() + " " + value.getPrice() + "�� (��� " + value.getStock() + "��)");
			}
		});
		System.out.println("=========================");
	}
	
// ------------------------------------------------------------------------
	
	static void Order() {  // �޴� �ֹ�
		
		System.out.println("�� ����� ���Ǳ��Դϴ�. ��");
		viewMenu();
		
		// �޴� ���� �ڵ�
		while (true) {      
			System.out.print("�޴��� ������>> ");
			choiceNum = sc.nextInt();
			
			// ��ȿ�� �˻�
			// .size() : ���� �׸� ����
			while (!menuMap.containsKey(choiceNum)) {
				System.out.println("�߸� �ԷµǾ����ϴ�. �ٽ� ����ּ���.");
				System.err.print("���Է�>> ");
				choiceNum = sc.nextInt();
			}
			
			if (choiceNum == 7) {
				adminCall();
				continue;
			}
			if (menuMap.get(choiceNum).getStock() == 0) {
				System.err.println("���� ��� �����ϴ�.");
				System.out.println("�ٸ� �޴��� �ֹ��Ͻðڽ��ϱ�?");
				System.out.print("1.��  2.�ƴϿ� >> ");
				int ans = sc.nextInt();

				validation(choiceNum, 2);
				
				if (ans==1) {
					continue;
				}
				else {
					break;
				}
			}
			
			System.out.print("������ �����ϼ���>> ");
			Quantity = sc.nextInt();
			
			
			while (menuMap.get(choiceNum).getStock() < Quantity) {
				System.err.println("���� ��� �����մϴ�.");
				System.out.println("���� ��� : " + menuMap.get(choiceNum).getStock() + "��");
				System.out.print("������ �ٽ� �����ϼ���>> ");
				Quantity = sc.nextInt();
			}
			
			// ��ȿ�� �˻�
			if (menuMap.get(choiceNum).getStock() == 0) {
				System.err.println("���� ��� �����ϴ�.");
				System.out.println("�ٸ� �޴��� �ֹ��Ͻðڽ��ϱ�?");
				System.out.print("1.��  2.�ƴϿ� >> ");
				if (sc.nextInt()==1) {
					continue;
				}
				else {
					break;
				}
			}
						
			// �Է¹��� �޴��� ��ȣ�� {����,����} ����
			orderMap.put(choiceNum, new int[] {menuMap.get(choiceNum).getPrice(), Quantity});
			stampCnt += Quantity;
			menuMap.get(choiceNum).setStock(menuMap.get(choiceNum).getStock()-Quantity); // ���� ������ŭ ��� --
			

			System.out.print("1.�޴��߰�  2.����  3.�ֹ���� >> ");
			choiceNum = sc.nextInt();

			// ��ȿ�� �˻�
			validation(choiceNum, 3);

			if (choiceNum == 2) {
				break;
			}
			///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// �ڵ� ���� : �޴��߰� �� �ֹ���� ������ ���ֹ��� �޴��� ��� ���󺹱͵��� ����
			else if (choiceNum == 3) {
				System.err.println("�ֹ��� ��ҵǾ����ϴ�.");
				System.out.println();
				
				// ��� ���󺹱� ��Ű��
				orderMap.forEach((key, value)->{
					MenuList menuInfo = menuMap.get(key);
					menuInfo.setStock(menuInfo.getStock()+value[1]);
				});
				// �ؽ��� ����
				orderMap.clear();
				
				viewMenu();
			}
		}
		System.out.println();
	}
	
// --------------------------------------------------------------------------
	
	static void adminCall() {  // ������ ���

		int password;  // �н����� == -555
		do {
			System.out.print("��й�ȣ�� �Է��ϼ���>> ");
			password = sc.nextInt();
		} while (password != -555);

		System.out.println();

		// ������ ��� ����
		String[] admin = {"1.�޴� ����", "2.���� ����", "3.��� �߰�"};

		System.out.println("������ ����Դϴ�.");

		// �޴� ���� �� ���ݵ� ���� �ʿ�
		// ���� �׸��� ���������� ���� �����ϵ���
		boolean flag = true;
		while (flag) {
			System.out.println("==================");
			for (int i=0; i<admin.length; i++) {
				System.out.println(admin[i]);
			}
			System.out.println("==================");

			System.out.print("������ �׸��� �����ϼ���>> ");
			choiceNum = sc.nextInt();
			
			validation(choiceNum, 3);

			if (choiceNum==1) {
				menuChange();
			} else if (choiceNum==2) {
				priceChange();
			} else if (choiceNum==3) {
				addStock();
			}
			
			System.out.println("\n�� ������ �׸��� �ֽ��ϱ�?");
			System.out.print("1.��   2.�ƴϿ�  >> ");
			choiceNum = sc.nextInt();
			System.out.println();

			validation(choiceNum, 2);
			
			if (choiceNum == 2) {
				System.out.println("������ ��带 �����մϴ�.\n");
				viewMenu();
				break;
			}
		}
		System.out.println();
	}

// -------------------------------------------------------------------
		
	static void addStock() {
		
		viewMenu();
		
		// ��� �߰� �ڵ�
		System.out.print("��� �߰��� �޴��� �����ϼ���>> ");
		choiceNum = sc.nextInt();
		
		System.out.print("�߰��� ����� �Է��ϼ���>> ");
		Quantity = sc.nextInt();
		
		// �Է¹��� �޴� ��ȣ�� ��� �߰� (������ �ִ� ����� �÷�����)
		Quantity += menuMap.get(choiceNum).getStock();
		menuMap.get(choiceNum).setStock(Quantity);
		
		
		System.out.println();
		menuMap.forEach((menuNum, menu)->{
			System.out.print(menuNum + "."); 
			System.out.println(menu.getMenu() + "(" + menu.getStock() + "��)");
		});
		
		System.out.println("\n< ��� �߰� �Ϸ� >");
	}


	static void priceChange() {

		viewMenu();
		
		// ���� ���� �ڵ�
		System.out.print("������ ������ �޴��� �����ϼ���>> ");
		choiceNum = sc.nextInt();
		validation(choiceNum, 6);
		System.out.print("�󸶷� �����Ͻðڽ��ϱ�?>> ");
		int price = sc.nextInt();
		
		menuMap.get(choiceNum).setPrice(price);
		
		System.out.print("< ���� ���� �Ϸ� >");
	}

	static void menuChange() {

		viewMenu();
		
		// �޴� ���� �ڵ�
		System.out.print("������ �޴��� �����ϼ���>> ");
		choiceNum = sc.nextInt();
		validation(choiceNum, 6);
		System.out.print("� �޴��� �����Ͻðڽ��ϱ�?>> ");
		String menu = sc.next();
		System.out.print("������ �޴��� ������ �Է����ּ���>> ");
		int price = sc.nextInt();		
		
		menuMap.get(choiceNum).setMenu(menu);
		menuMap.get(choiceNum).setPrice(price);
		
		System.out.println("< �޴� ���� �Ϸ� >");
	}
	
	// �Է¹��� �� ��ȿ�� �˻�
	static void validation(int choiceNum, int maxNum) {

		while (choiceNum < 1 || maxNum < choiceNum) {
			System.out.println("�߸� �Է��ϼ̽��ϴ�. �ٽ� �Է����ּ���.");
			System.err.print("���Է�>> ");
			choiceNum = sc.nextInt();
		}
	}
	
}