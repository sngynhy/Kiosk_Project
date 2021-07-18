package miniproject;

import java.util.HashMap;
import java.util.Scanner;


public class VendingMachine {

			// 넣은 돈, 선택 항목, 거스름 돈, 주문 메뉴 개수, 총 결제 금액
	static int money, choiceNum, change = 0, stampCnt = 0, totalPrice = 0;
	static int Quantity; // 동일 메뉴 선택 개수

	// HashMap<key type, value type>
	// value 타입을 MenuList 객체로 넣어줌
	static HashMap<Integer, MenuList> menuMap = new HashMap<Integer, MenuList>();  // <menuNum : menuList>
	static HashMap<Integer, int[]> orderMap = new HashMap<Integer, int[]>();  // <choiceNum : {price, Quantity}>
	static HashMap<String, Integer> stampMap = new HashMap<String, Integer>();  // <userPhoneNum : stampCnt>
	
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws InterruptedException {
		
		// 메뉴 초기화     			 메뉴, 가격, 재고량
		menuMap.put(1, new MenuList("아메리카노", 1500, 10));
		menuMap.put(2, new MenuList("카페라떼", 2000, 10));
		menuMap.put(3, new MenuList("아인슈페너", 3000, 10));
		menuMap.put(4, new MenuList("초코라떼", 2000, 0));
		menuMap.put(5, new MenuList("밀크티", 2500, 2));
		menuMap.put(6, new MenuList("요거트스무디", 3000, 0));
		menuMap.put(7, new MenuList("관리자 모드", 0, 0));

		stampMap.put("01092593937", 11);
		stampMap.put("01045206591", 9);
		stampMap.put("01094823937", 5);
		
		// 메뉴 주문
		Order();
		
		// 결제 하기  << 코드 수정 필요
		pay();
		
		// 결제 방법 선택 -> 스탬프 이용? 그냥 결제?
//		useStamp();  결제 시 호출


		// 스탬ㅁ프 -> 휴대폰 뒷자리 입력받고 배열 내 존재하지 않으면 만들겠습니까?
//		saveStamp(); 결제 시 호출


	}


	////////////// 각 기능 함수로 구현 //////////////////
	
	
	static String phoneNumCheck() {  // 휴대폰 번호 입력 유효성 검사
		
		String regExp = "^01(?:0|1|[6-9])[-]?(\\d{3}|\\d{4})[-]?(\\d{4})$";  // 정규식으로 휴대폰 번호 패턴 만들기
		String phoneNum; // 스탬프 사용자 휴대폰 번호 -> stampMap에 저장
		
		System.out.println("010-1234-5678의 형태로 휴대폰 번호를 입력하세요.");
		System.out.print("입력>> ");
		phoneNum = sc.next();
		
		phoneNum = phoneNum.replace("-", "");
		
		System.out.println();
		
		
		while (!phoneNum.matches(regExp)) {
			System.out.println("유효하지 않은 번호입니다. 다시 입력하세요.");
			System.err.print("재입력>> ");
			phoneNum = sc.next();
		}
		
		System.out.println(phoneNum +"이(가) 맞습니까?");
		System.out.print("1.예  2.아니오 >> ");
		choiceNum = sc.nextInt();
		
		validation(choiceNum, 2);
		
		while (choiceNum == 2) {
			System.out.print("휴대폰 번호를 다시 입력하세요>> ");
			phoneNum = sc.next();
			
			while (!phoneNum.matches(regExp)) {
				System.out.println("유효하지 않은 번호입니다. 다시 입력하세요.");
				System.err.print("재입력>> ");
				phoneNum = sc.next();
			}
			
			System.out.println(phoneNum +"이(가) 맞습니까?");
			System.out.print("1.예  2.아니오 >> ");
			choiceNum = sc.nextInt();
			
			validation(choiceNum, 2);
		}
		System.out.println();
		return phoneNum;
	}
	
// ---------------------------------------------------------------

	static void useStamp(String phoneNum) {  // 스탬프 사용
		
		if (!stampMap.containsKey(phoneNum)) {  // 만약 등록된 번호가 아닐 경우
			saveStamp(phoneNum);
			return;
		}
		
		if (choiceNum == 1) {
			System.out.println(phoneNum + "님의 적립된 총 스탬프 개수는 " + stampMap.get(phoneNum) + "개 입니다.");

		}
		System.out.println();
		
		if (stampMap.get(phoneNum) < 10) {
			System.out.println("스탬프 개수가 모자랍니다.");
			return;
		}
		else if (stampMap.get(phoneNum) >= 10) {
			System.out.println("스탬프 10개 사용 시 아메리카노 한잔이 무료로 제공됩니다.");
			System.out.println("메뉴 변경을 원하시면 차액만큼 추가로 결제가 가능합니다.");
			totalPrice -= 1500;
		}

		stampMap.put(phoneNum, stampMap.get(phoneNum)-10);
		System.out.println(phoneNum+"님의 스탬프가 10개 차감되어 남은 개수는 총 " + stampMap.get(phoneNum) + "개 입니다.");
	}

 // ---------------------------------------------------------------

	static void saveStamp(String phoneNum) {  // 스탬프 적립

		if (stampMap.containsKey(phoneNum)) {  // 사용자의 phoneNum가 stampMap에 존재 하는지 확인
			stampMap.put(phoneNum, stampMap.get(phoneNum) + stampCnt);
			System.out.println(phoneNum + "님의 스탬프가 " + stampCnt + "개 추가되어\n총 스탬프 개수는 " + stampMap.get(phoneNum) +"개 입니다.");
		}
		else {  // 만약 등록된 번호가 아닐 경우
			System.out.println("등록되지 않은 번호입니다.");
			System.out.println("번호를 등록하시겠습니까?");
			System.out.print("1.예  2.아니오 >> ");
			choiceNum = sc.nextInt();
			System.out.println();

			validation(choiceNum, 2);
			
			if (choiceNum == 1) {
				stampMap.put(phoneNum, stampCnt);
				System.out.println("번호가 등록되었습니다.");
				System.out.println(phoneNum + "님의 스탬프가 " + stampCnt + "개 추가되어\n총 스탬프 개수는 " + stampMap.get(phoneNum) +"개 입니다.");
			}
		}
		// 다른 방식
//		boolean flag = true;
//		stampMap.forEach((key, value)->{
//			if (key == phoneNum) {  // 사용자의 phoneNum가 stampMap에 존재 하는지 확인
//				stampMap.put(phoneNum, value += stampCnt);
//				System.out.println(phoneNum + "님의 스탬프가 " + stampCnt + "개 추가되어 총 스탬프 개수는 " + stampMap.get(phoneNum) +"개 입니다.");
//				flag = false;
//				return;
//			}
//		});
//		if (flag) {
//			stampMap.put(phoneNum, stampCnt);
//			System.out.println("번호가 등록되었습니다.");
//			System.out.println(phoneNum + "님의 스탬프가 " + stampCnt + "개 추가되어 총 스탬프 개수는 " + stampMap.get(phoneNum) +"개 입니다.");	
//		}
		}


// -------------------------------------------------------------------
	
	static void pay() throws InterruptedException {  // 결제 기능
		
		/*
       ===== 주문하신 메뉴 ======
       커피(1500원) x 1개
       주스(2000원) x 2개
       ========================
       총 결제 가격은 5500원 입니다. */
		System.out.println("====== 주문하신 메뉴 ======");
		orderMap.forEach((key, value)->{
			System.out.println(menuMap.get(key).getMenu() + "(" + value[0] + "원)" +" x " + value[1] + "개");
			totalPrice += value[0] * value[1];
		});
		if (totalPrice==0) {
			System.out.println("주문하신 메뉴가 없습니다.\n안녕히 가십시오. (--)(__)*");
			System.out.println("========================");
			return;
		}
		System.out.println("========================");
		System.out.println();

		// 여기서 스탬프 적립 or 사용
		System.out.println("스탬프를 이용하시겠습니까?");
		System.out.print("1.사용  2.적립  3.이용하지않음 >> ");
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
		System.out.println("총 결제 금액은 " + totalPrice + "원 입니다.");
		
		if (totalPrice != 0) {
			System.out.print("결제 금액을 입력하세요>> ");
			money = sc.nextInt();
			
			System.out.println();
			
			// 금액이 부족한 경우
			while (money < totalPrice) {
				System.out.println("금액이 부족합니다. 다시 입력하세요.");
				System.err.print("재입력>> ");
				money = sc.nextInt();
			}
			// 거스름돈 반환
			if (money > totalPrice) {
				change = money - totalPrice;// 거스름 돈
				System.out.println("거스름돈 " + change +"원이 반환됩니다.");
			}
		}
		
		System.out.println("결제가 완료되었습니다.\n");

		// 결제가 정상적으로 이루어졌을 경우
		System.out.println("♣ 음료 제조중 ♣\n");
		System.out.println("띠링~♪");
		Thread.sleep(1000);
		System.out.println("주문하신 음료가 나왔습니다. \n이용해주셔서 감사합니다.");  // money == totalPrice
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
	
	static void viewMenu() {  // 메뉴 출력 함수
		
		System.out.println("========== 메뉴 ==========");
		// forEach(key, value)
		menuMap.forEach((key, value)->{
			if (key==7) {
				return;  // <<<<<<<<< break 대신 return으로!
			}
			if (value.getStock() == 0) {
				System.out.print(key + "."); 
				System.out.print(value.getMenu() + " " + value.getPrice() + "원 ");
				System.err.println("(재고없음)");
			} else {
				System.out.print(key + "."); 
				System.out.println(value.getMenu() + " " + value.getPrice() + "원 (재고량 " + value.getStock() + "개)");
			}
		});
		System.out.println("=========================");
	}
	
// ------------------------------------------------------------------------
	
	static void Order() {  // 메뉴 주문
		
		System.out.println("♣ 음료수 자판기입니다. ♣");
		viewMenu();
		
		// 메뉴 선택 코드
		while (true) {      
			System.out.print("메뉴를 고르세요>> ");
			choiceNum = sc.nextInt();
			
			// 유효성 검사
			// .size() : 맵의 항목 개수
			while (!menuMap.containsKey(choiceNum)) {
				System.out.println("잘못 입력되었습니다. 다시 골라주세요.");
				System.err.print("재입력>> ");
				choiceNum = sc.nextInt();
			}
			
			if (choiceNum == 7) {
				adminCall();
				continue;
			}
			if (menuMap.get(choiceNum).getStock() == 0) {
				System.err.println("현재 재고가 없습니다.");
				System.out.println("다른 메뉴로 주문하시겠습니까?");
				System.out.print("1.예  2.아니오 >> ");
				int ans = sc.nextInt();

				validation(choiceNum, 2);
				
				if (ans==1) {
					continue;
				}
				else {
					break;
				}
			}
			
			System.out.print("수량을 선택하세요>> ");
			Quantity = sc.nextInt();
			
			
			while (menuMap.get(choiceNum).getStock() < Quantity) {
				System.err.println("현재 재고가 부족합니다.");
				System.out.println("현재 재고량 : " + menuMap.get(choiceNum).getStock() + "개");
				System.out.print("수량을 다시 선택하세요>> ");
				Quantity = sc.nextInt();
			}
			
			// 유효성 검사
			if (menuMap.get(choiceNum).getStock() == 0) {
				System.err.println("현재 재고가 없습니다.");
				System.out.println("다른 메뉴로 주문하시겠습니까?");
				System.out.print("1.예  2.아니오 >> ");
				if (sc.nextInt()==1) {
					continue;
				}
				else {
					break;
				}
			}
						
			// 입력받은 메뉴의 번호와 {가격,수량} 저장
			orderMap.put(choiceNum, new int[] {menuMap.get(choiceNum).getPrice(), Quantity});
			stampCnt += Quantity;
			menuMap.get(choiceNum).setStock(menuMap.get(choiceNum).getStock()-Quantity); // 구매 수량만큼 재고량 --
			

			System.out.print("1.메뉴추가  2.결제  3.주문취소 >> ");
			choiceNum = sc.nextInt();

			// 유효성 검사
			validation(choiceNum, 3);

			if (choiceNum == 2) {
				break;
			}
			///<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			// 코딩 수정 : 메뉴추가 후 주문취소 했을때 선주문된 메뉴의 재고량 원상복귀되지 않음
			else if (choiceNum == 3) {
				System.err.println("주문이 취소되었습니다.");
				System.out.println();
				
				// 재고량 원상복귀 시키기
				orderMap.forEach((key, value)->{
					MenuList menuInfo = menuMap.get(key);
					menuInfo.setStock(menuInfo.getStock()+value[1]);
				});
				// 해쉬맵 비우기
				orderMap.clear();
				
				viewMenu();
			}
		}
		System.out.println();
	}
	
// --------------------------------------------------------------------------
	
	static void adminCall() {  // 관리자 모드

		int password;  // 패스워드 == -555
		do {
			System.out.print("비밀번호를 입력하세요>> ");
			password = sc.nextInt();
		} while (password != -555);

		System.out.println();

		// 관리자 모드 시작
		String[] admin = {"1.메뉴 변경", "2.가격 변경", "3.재고 추가"};

		System.out.println("관리자 모드입니다.");

		// 메뉴 변경 시 가격도 변경 필요
		// 관리 항목을 연속적으로 선택 가능하도록
		boolean flag = true;
		while (flag) {
			System.out.println("==================");
			for (int i=0; i<admin.length; i++) {
				System.out.println(admin[i]);
			}
			System.out.println("==================");

			System.out.print("관리할 항목을 선택하세요>> ");
			choiceNum = sc.nextInt();
			
			validation(choiceNum, 3);

			if (choiceNum==1) {
				menuChange();
			} else if (choiceNum==2) {
				priceChange();
			} else if (choiceNum==3) {
				addStock();
			}
			
			System.out.println("\n더 관리할 항목이 있습니까?");
			System.out.print("1.예   2.아니오  >> ");
			choiceNum = sc.nextInt();
			System.out.println();

			validation(choiceNum, 2);
			
			if (choiceNum == 2) {
				System.out.println("관리자 모드를 종료합니다.\n");
				viewMenu();
				break;
			}
		}
		System.out.println();
	}

// -------------------------------------------------------------------
		
	static void addStock() {
		
		viewMenu();
		
		// 재고 추가 코드
		System.out.print("재고를 추가할 메뉴를 선택하세요>> ");
		choiceNum = sc.nextInt();
		
		System.out.print("추가할 재고량을 입력하세요>> ");
		Quantity = sc.nextInt();
		
		// 입력받은 메뉴 번호의 재고량 추가 (기존에 있던 재고량에 플러스됨)
		Quantity += menuMap.get(choiceNum).getStock();
		menuMap.get(choiceNum).setStock(Quantity);
		
		
		System.out.println();
		menuMap.forEach((menuNum, menu)->{
			System.out.print(menuNum + "."); 
			System.out.println(menu.getMenu() + "(" + menu.getStock() + "개)");
		});
		
		System.out.println("\n< 재고 추가 완료 >");
	}


	static void priceChange() {

		viewMenu();
		
		// 가격 변경 코드
		System.out.print("가격을 변경할 메뉴를 선택하세요>> ");
		choiceNum = sc.nextInt();
		validation(choiceNum, 6);
		System.out.print("얼마로 변경하시겠습니까?>> ");
		int price = sc.nextInt();
		
		menuMap.get(choiceNum).setPrice(price);
		
		System.out.print("< 가격 변경 완료 >");
	}

	static void menuChange() {

		viewMenu();
		
		// 메뉴 변경 코드
		System.out.print("변경할 메뉴를 선택하세요>> ");
		choiceNum = sc.nextInt();
		validation(choiceNum, 6);
		System.out.print("어떤 메뉴로 변경하시겠습니까?>> ");
		String menu = sc.next();
		System.out.print("변경할 메뉴의 가격을 입력해주세요>> ");
		int price = sc.nextInt();		
		
		menuMap.get(choiceNum).setMenu(menu);
		menuMap.get(choiceNum).setPrice(price);
		
		System.out.println("< 메뉴 변경 완료 >");
	}
	
	// 입력받은 값 유효성 검사
	static void validation(int choiceNum, int maxNum) {

		while (choiceNum < 1 || maxNum < choiceNum) {
			System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			System.err.print("재입력>> ");
			choiceNum = sc.nextInt();
		}
	}
	
}