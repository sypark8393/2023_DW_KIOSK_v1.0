package system.datas;

// 사용자 모드에서 주문 목록을 구현하기 위한 클래스
public class OrderedMenuType {
	private static int TOTAL_QUANTITY = 0;	// 현재 주문 목록에 있는 상품의 총 수량
	private static int TOTAL_AMT = 0;		// 현재 주문 목록에 있는 상품의 총 금액

	private int realMenuIndex;	// 메뉴 리스트에서 해당 메뉴의 실제 위치를 나타내는 인덱스
	private String id;			// 메뉴 ID
	private String name;		// 메뉴명
	private int price;			// 단가
	private int quantity;		// 주문 수량
	private int total;			// 해당 메뉴의 금액
	
	// 생성자
	public OrderedMenuType(int realMenuIndex, MenuType menu, int quantity) {
		this.realMenuIndex = realMenuIndex;
		
		this.id = menu.getId();
		this.name = menu.getName();
		this.price = menu.getPrice();
		
		this.quantity = quantity;
		total = price * quantity;	// 단가(prce)와 주문 수량(quantity) 정보를 이용해 금액 설정
		
		TOTAL_QUANTITY += quantity;	// 주문 수량만큼 총 주문 수량 증가
		TOTAL_AMT += total;			// 주문한 메뉴의 금액만큼 총 금액 증가
	}

	// getter
	public static int getTOTAL_QUANTITY() {
		return TOTAL_QUANTITY;
	}
	
	public static int getTOTAL_AMT() {
		return TOTAL_AMT;
	}
	
	public int getRealMenuIndex() {
		return realMenuIndex;
	}
	
	public String getId() {
		return id;
	}
	
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}
	
	// 수량 업데이트
	/* 메소드명: updateQuantity
	 * 파라미터: int difference
	 * 		  difference: 수량의 변경량 (새로운 주문 수량 - 기존 주문 수량)
	 * 반환값: 없음
	 * 기능 설명: 수량의 변경량만큼 수량 관련 멤버 변수(TOTAL_QUANTITY, quantity), 금액 관련 멤버 변수(TOTAL_AMT, total)의 값을 업데이트한다.
	 */
	public void updateQuantity(int difference) {
//		System.out.println("==========> 추가된 수량: " + difference);
//		System.out.println("==========> quantity: " + quantity + " ==> "+ (quantity+difference));
//		System.out.println("==========> total: " + total + " ==> " + (total + difference * price));
//		System.out.println("==========> TOTAL_AMT: " + TOTAL_AMT + " ==> "+ (TOTAL_AMT + difference * price));
//		System.out.println("==========> TOTAL_QUANTITY: " + TOTAL_QUANTITY + " ==> "+ (TOTAL_QUANTITY+difference));
		
		TOTAL_QUANTITY += difference;
		quantity += difference;
		
		TOTAL_AMT += difference * price;
		total += difference * price;
	}

	// 주문 목록 출력 양식
	/* 메소드명: printCartTemplate
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 사용자 모드에서 주문 목록을 양식에 맞게 출력한다. 
	 */
	public void printCartTemplate() {
		System.out.printf("%-20s\t%,8d   %2d  %,8d\n", name, price, quantity, total);
	}
	
	// 총 주문 수량, 총 금액 초기화
	/* 메소드명: reset
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 주문을 취소하거나 주문이 완료되는 경우 static 변수(TOTAL_QUANTITY, TOTAL_AMT)의 값을 0으로 초기화한다.
	 */
	public static void reset() {
		TOTAL_QUANTITY = 0;
		TOTAL_AMT = 0;
	}
}
