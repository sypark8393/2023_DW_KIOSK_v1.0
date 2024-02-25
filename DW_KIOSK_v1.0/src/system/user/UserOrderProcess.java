package system.user;

import java.util.*;
import java.io.*;
import system.datas.MenuType;
import system.datas.OrderedMenuType;

public class UserOrderProcess {
	
	private Scanner sc;	// 스캐너 객체
	
	// 생성자
	public UserOrderProcess(Scanner sc) {
		this.sc = sc;
	}
	
	// 3. 매장/포장
	/* 메소드명: isHereOrGo
	 * 파라미터: 없음
	 * 반환값: "매장": 매장 이용 방법으로 1을 선택한 경우
	 * 		 "포장": 매장 이용 방법으로 2를 선택한 경우
	 * 기능 설명: 사용자로부터 매장 이용 방법의 번호를 받은 후, 선택된 방법을 문자열(매장/포장)로 반환한다.
	 */
	public String isHereOrGo() {
		int selectType;
		
		while(true) {
			System.out.println("\n매장 이용 방법의 번호를 입력해주세요.");
			System.out.println("[1]매장  [2]포장");
			System.out.print(" >> ");

			try {
				selectType = sc.nextInt();
				sc.nextLine();
				
				if(selectType == 1) return "매장";
				if(selectType == 2) return "포장";
				
				if(selectType != 1 && selectType != 2)	// 예외처리: 1, 2를 제외한 숫자값을 입력한 경우
					System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
				
			} catch(InputMismatchException e) {			// 예외처리: 정수가 아닌 값을 입력한 경우
				sc.nextLine();
				System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
			}
		}
	}
	
	// 4-0. 메뉴 불러오기
	/* 메소드명: loadMenu
	 * 파라미터: 없음
	 * 반환값: boolean
	 * 		 true: menu.txt에서 데이터를 읽어와 리스트에 성공적으로 저장한 경우
	 * 		 false: menu.txt에서 데이터를 읽어오지 못한 경우
	 * 기능 설명: menu.txt에서 사용자에게 보여줄 메뉴 목록을 읽어와 리스트에 저장한다.
	 */
	public boolean loadMenu() {
		BufferedReader in;	// 입력 스트림
		
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MenuType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 메뉴 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.println("메뉴 데이터를 불러올 수 없습니다.");
			System.out.println("오류가 지속될 경우 관리자에게 문의해주세요.\n");
			return false;
			
		}
		
		String s;					// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;				// 구분자로 분리한 문자열이 저장될 임시 배열
		String categoryName = "";	// 카테고리명
		int index = 0;				// 새로운 카테고리명이 등장할 때의 인덱스값
		
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				// 매개변수로 전달할 값 세팅 및 객체 생성
				String id = tmp[MenuType.INDEX_MENU_ID];
				String category = tmp[MenuType.INDEX_CATEGORY];
				String name = tmp[MenuType.INDEX_NAME];
				int price = Integer.parseInt(tmp[MenuType.INDEX_PRICE]);
				int stock = Integer.parseInt(tmp[MenuType.INDEX_STOCK]);
				
				MenuType menu = new MenuType(id, category, name, price, stock);
				UserMode.menuList.add(menu);
				
				// 현재 가리키고 있는 위치의 카테고리명이 이전에 등장한 적이 없다면
				if(!categoryName.equals(category)) {
					UserMode.categoryIndexList.add(index);		// 현재 위치의 인덱스 값을 카테고리 인덱스 리스트에 추가
					categoryName = category;			// 현재 가라키고 있는 위치의 카테고리명을 별도 저장
				}
				
				index++;
			}
			
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("데이터 입출력에 실패하였습니다.");
			System.out.println("관리자에게 문의해 주세요.");
			return false;
			
		}
		
		return true;
	}
	
	// 4-1. 메뉴 카테고리 목록 출력
	/* 메소드명: printCategory
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 리스트에 저장된 메뉴 목록을 바탕으로 메뉴의 카테고리 목록을 출력한다.
	 */
	public void printCategory() {
		int count = 0;			// 한 줄에 4개씩 출력하기 위한 카운트 변수
		int categoryNum = 0;	// 카테고리 번호
		
		System.out.println("==========================================================");
		for(int i : UserMode.categoryIndexList) {
			System.out.printf("[%d]%-6s\t", ++categoryNum, UserMode.menuList.get(i).getCategory());
			count++;
			
			// 한 줄에 4개의 카테고리가 출력되었으면 개행처리 후 한 줄에 cnt 변수 초기화
			if(count == 4) {
				System.out.println();
				count = 0;
			}
		}
		
		System.out.printf("[R]%-6s", "주문취소");
		
		// 한 개 이상의 상품이 주문 목록에 있는 경우 결제 버튼 제공
		if(UserMode.cart.size() > 0) {
			System.out.print("\t[P]결제");
		}
		System.out.println("\n==========================================================");
	}
	
	// 5-1. 카테고리별 메뉴 출력
	/* 메소드명: dealMenuByCategory
	 * 파라미터: int categoryIndex
	 * 		  categoryIndex: 사용자가 선택한 메뉴 카테고리의 번호
	 * 반환값: int numOfMenu
	 * 		 numOfMenu: 사용자가 선택한 메뉴 카테고리에 있는 메뉴의 개수
	 * 기능 설명: 사용자가 선택한 메뉴 카테고리에 속한 메뉴를 출력하고, 그 개수를 반환한다.
	 */
	public int dealMenuByCategory(int categoryIndex) {
		int numOfMenu = 0;
		int start = UserMode.categoryIndexList.get(categoryIndex - 1); // 선택된 카테고리 내 첫 번째 메뉴의 실제 인덱스 값
		
		// 사용자가 선택한 카테고리의 마지막 메뉴의 실제 인덱스 값 + 1
		int end; 
		if(categoryIndex != UserMode.categoryIndexList.size()) end = UserMode.categoryIndexList.get(categoryIndex);
		else end = UserMode.menuList.size(); 

		System.out.println("======================================");
		System.out.println("번호  메뉴명                          가격");
		System.out.println("======================================");
		
		for(int i=start; i<end; i++) {
			numOfMenu++;
			
			// 출력될 메뉴의 재고가 0인 경우 번호 출력하지 않음
			if(UserMode.menuList.get(i).getStock() == 0) {
				System.out.print("[--] ");
			} else {
				System.out.printf("[%02d] ", numOfMenu);
			}
			
			UserMode.menuList.get(i).printUserMenu();
			
		}
		System.out.println("======================================");
		
		return numOfMenu;
	}
	
	// 6. 메뉴 수량 입력받기
	/* 메소드명: scanOrderQuantity
	 * 파라미터: int menuRealIndex
	 * 		  menuRealIndex: 사용자가 선택한 리스트의 몇 번째에 저장되어 있는지 나타내는 값
	 * 반환값: int orderQuantity
	 * 		 orderQuantity: 사용자로부터 입력받은 주문 수량
	 * 기능 설명: 사용자로부터 주문할 수량을 입력받은 뒤, 값이 유효한 경우에만 그 값을 반환한다.
	 */
	public int scanOrderQuantity(int menuRealIndex) {
		int orderQuantity = 0;
		
		while(true) {
			System.out.println("\n수량을 입력해주세요.  [0]취소");
			System.out.print(" >> ");
			
			try {
				orderQuantity = sc.nextInt();
				sc.nextLine();
			} catch(InputMismatchException e) {
				sc.nextLine();
				System.out.println("주문 수량은 0 이상의 정수로 입력해주세요.\n");
				continue;
			}

			if(orderQuantity == 0) { // 주문 수량이 0이면 메뉴 선택 취소
				System.out.println("\n[메뉴 선택이 취소되었습니다.]\n");
				return 0;
			}
			
			if(orderQuantity < 0) {	// 예외처리 - 0 미만의 값
				System.out.println("주문 수량은 0 이상의 정수로 입력해주세요.\n");
			} else if(orderQuantity > 50 - OrderedMenuType.getTOTAL_QUANTITY()) {	// 예외처리 - 최대 주문 수량 초과
				System.out.printf("최대 주문 가능 수량인 50을 초과하였습니다.(현재 총 주문 수량: %d)\n", OrderedMenuType.getTOTAL_QUANTITY());
				System.out.println("다시 입력해주세요.\n");
			} else if(orderQuantity > UserMode.menuList.get(menuRealIndex).getStock()) {	// 예외처리 - 선택한 메뉴의 재고 부족
				System.out.printf("해당 메뉴는 현재 최대 %d개까지 주문하실 수 있습니다.\n", UserMode.menuList.get(menuRealIndex).getStock());
				System.out.println("수량을 다시 입력해주세요.\n");
			} else {
				break;
			}
		}
		
		return orderQuantity;
	}
	
	// 8. 주문 목록 출력
	/* 메소드명: printCart
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 주문 목록을 출력한다.
	 */
	public void printCart() {
		int orderMenuIndex = 0;
		
		System.out.println("\n=======================================================");
		System.out.println("번호   메뉴명                           단가   수량       금액");
		System.out.println("-------------------------------------------------------");
		
		for(OrderedMenuType menu : UserMode.cart) {
			System.out.printf("[%02d] ", ++orderMenuIndex);
			menu.printCartTemplate();
		}
		System.out.println("=======================================================");
		System.out.printf("합계:\t\t\t\t\t       %,8d\n", OrderedMenuType.getTOTAL_AMT());
		System.out.println("=======================================================");
	}

	// 9-C. 주문 수량 변경 처리
	/* changeOrderedMenuQuantity
	 * 파라미터: int orderMenuIndex, int changeQuantity
	 * 		  orderMenuIndex: 주문 수량을 변경할 메뉴가 주문 목록 중 몇 번째에 있는지 나타내는 값
	 * 		  changeQuantity: 변경 후에 적용될 주문 수량
	 * 반환값: 없음
	 * 기능 설명: 주문 수량을 변경할 메뉴의 수량을 입력받은 값으로 변경 가능한지 확인하고,
	 * 		   변경이 가능한 경우에만 수량 변경 후 메뉴 재고와 주문 목록의 수량, 금액 정보를 업데이트한다.
	 */
	public void changeOrderedMenuQuantity(int orderMenuIndex, int changeQuantity) {

		int orgQuantity = UserMode.cart.get(orderMenuIndex).getQuantity();			// 수량을 변경할 메뉴의 원래 주문 수량
		int difference = changeQuantity - orgQuantity;								// 수량의 변경량
		int stock = UserMode.menuList.get(UserMode.cart.get(orderMenuIndex).getRealMenuIndex()).getStock();	// 수량을 변경할 현재 메뉴의 재고

		if(changeQuantity == 0) { 		// 메뉴 빼기
			difference = -orgQuantity;
			
			UserMode.cart.get(orderMenuIndex).updateQuantity(difference); // 총 주문 수량, 총 금액 업데이트
			UserMode.menuList.get(UserMode.cart.get(orderMenuIndex).getRealMenuIndex()).updateUserMenuStock(difference); // 재고 업데이트
			UserMode.cart.remove(orderMenuIndex); // 주문 목록에서 메뉴 삭제
			System.out.println("\n[선택하신 메뉴가 주문 목록에서 삭제되었습니다.]");
			
		} else if(difference == 0) { // 수량 변경 작업 취소
			System.out.println("\n[수량 변경 작업이 취소되었습니다.]");
			
		} else if(difference > 50 - OrderedMenuType.getTOTAL_QUANTITY()) {	// 변경 후의 수량이 최대 주문 수량을 초과하는 경우
			System.out.printf("최대 주문 수량인 50개를 초과하였습니다.(현재 총 주문 수량: %d)\n", OrderedMenuType.getTOTAL_QUANTITY());
			
		} else if(difference > stock) { // 해당 메뉴의 재고가 부족해지는 경우
			System.out.printf("해당 메뉴는 현재 최대 %d개까지 추가하실 수 있습니다.\n", stock);
			
		} else {
			UserMode.cart.get(orderMenuIndex).updateQuantity(difference);	// 주문 목록 수량 업데이트
			UserMode.menuList.get(UserMode.cart.get(orderMenuIndex).getRealMenuIndex()).updateUserMenuStock(difference);	// 재고 업데이트
			System.out.println("\n[수량이 변경되었습니다.]");
		}
	
	}
	
}
