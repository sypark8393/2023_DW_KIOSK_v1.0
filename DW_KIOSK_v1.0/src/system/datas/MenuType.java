package system.datas;

public class MenuType {
	public static final String FILE_NAME = "src//files//datas//menu.txt";
	public static final String TMP_FILE_NAME = "src//files//datas//menu_tmp.txt";
	
	public static final int INDEX_MENU_ID = 0;
	public static final int INDEX_CATEGORY = 1;
	public static final int INDEX_NAME = 2;
	public static final int INDEX_PRICE = 3;
	public static final int INDEX_STOCK = 4;
	
	private String id = null;
	private String category = null;
	private String name = null;
	private int price = 0;
	private int stock = 0;
	
	// 생성자
	public MenuType(String id, String category, String name, int price, int stock) {
		super();
		this.id = id;
		this.category = category;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}
	
	// getter & setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	// 관리자 모드: 메뉴 목록 출력 메소드
	/* 메소드명: printAdminMenu
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 관리자 모드에서 메뉴 목록을 양식에 맞게 출력한다. 
	 */
	public void printAdminMenu() {
		System.out.printf("%12s", this.id);
		System.out.print("\t");
		System.out.printf("%6s", this.category);
		System.out.print("\t");
		System.out.printf("%25s", this.name);
		System.out.print("\t");
		System.out.printf("%,4d", this.price);
		System.out.print("\t");
		System.out.printf("%4d", this.stock);
		System.out.println();
	}

	// 사용자 모드: 메뉴 목록 출력 메소드 (메뉴명, 가격만 출력)
	/* 메소드명: printUserMenu
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 사용자 모드에서 메뉴 목록을 양식에 맞게 출력한다. 
	 */
	public void printUserMenu() {
		System.out.printf("%-20s", this.name);
		System.out.print("\t");
		System.out.printf("%,6d", this.price);
		System.out.print("\t");
		System.out.println();
	}
	
	// 사용자가 주문을 진행하는 동안 변동되는 재고량 처리
	/* 메소드명: updateUserMenuStock
	 * 파라미터: int quantity
	 * 		  quantity: 사용자로부터 입력받은 주문 수량
	 * 반환값: 없음
	 * 기능 설명: 사용자가 주문을 진행하는 동안 발생한 수량 변경 사항에 대해 메뉴 재고량을 변경한다.
	 */
	public void updateUserMenuStock(int quantity) {
		stock -= quantity;
	}
	
	// txt 파일 데이터 양식에 맞는 문자열로 변환하여 반환
	/* 메소드명: toDataTemplate
	 * 파라미터: 없음
	 * 반환값: String str
	 * 		 str: 실제 menu.txt 파일에 작성되는 형식으로 구성된 한 줄의 문자열
	 * 기능 설명: 현재 객체가 가진 멤버 변수들의 값을 이용해 데이터 파일 형식에 맞는 한 줄의 문자열을 만들어 반환한다.
	 */
	public String toDataTemplate() {
		String str = "";
		str += id + "|" + category + "|" + name + "|" + price + "|" + stock;
		
		return str;
	}
}
