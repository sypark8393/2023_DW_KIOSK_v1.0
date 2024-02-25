package system.admin;

import java.util.Scanner;
import system.admin.datas.MenuList;
import system.datas.MenuType;

public class AdminMenuProcess {
	//필드
	private static AdminMenuProcess process = new AdminMenuProcess();

	// 생성자
	private AdminMenuProcess() {}
	
	// 메소드
	public static AdminMenuProcess getInstance() {
		return process;
	}
	/* 메소드명: start
	 * 파라미터: Scanner sc
	 * 		  sc: main()에서 생성한 스캐너 객체
	 * 반환값: 없음
	 * 기능 설명: 스캐너 객체를 통해 입력받은 answer의 값에 따라 메뉴에 대해 수행할 프로세스를 호출
	 * 		   
	 */
	public void start(Scanner sc) {
		// TODO Auto-generated method stub
		char answer;//세부 작업 번호
		while (true) {
			
			System.out.println("[메뉴]에 대해 수행할 세부 작업을 선택해주세요.");
			System.out.println("[1]전체출력 [2]메뉴ID로 조회 [3]상품 재고로 조회 [4]상품 재고 업데이트 [B]메인 메뉴로 돌아가기");
			System.out.print(" > ");
			String input = sc.next();
			sc.nextLine();
			if (input.length() > 1) {// 입력이 1글자보다 많은 경우
				System.out.println("입력이 1글자를 초과하셨습니다. 첫 번째 글자만 사용됩니다.");// 안내문 출력
			}
			answer = input.charAt(0);// answer는 input의 첫 번째 글자만 사용
			// B누르면 메인 메뉴로 돌아가기
			if (answer == 'B' || answer == 'b') {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			}
			
			MenuList menuList = new MenuList(sc);//menuList 객체에  Scanner 객체 sc 전달
												 
			int searchedStock = 0;
			switch (answer) {
			
			case '1': // 전체 출력
				menuList.printWholeMenu();//MenuList 클래스의 printWholeMenu메소드 실행
				break;
			case '2': // 메뉴ID로 조회
				System.out.println("조회할 메뉴 ID를 입력해주세요.");
				System.out.print(" > ");
				String searchedId = sc.nextLine();
				
				menuList.searchMenuById(MenuType.INDEX_MENU_ID, searchedId);//MenuList 클래스의 searchMenuById메소드 실행
				break;
			case '3': // 상품 재고로 조회
				System.out.println("조회할 상품의 재고 현황을 입력해주세요.");
				System.out.print(" > ");
				boolean check = false;//while문의 반복 제어 조건
				while (!check) {//check가 false이면 계속 반복
					try {
						searchedStock = Integer.parseInt(sc.next());
						check = true;//정수형 입력받으면 while문 탈출
					} catch (Exception e) {//정수형 외 입력받으면
						// TODO: handle exception
						System.out.println("재고는 숫자로만 입력 가능합니다. 다시 입력해주세요.");
						System.out.print(" > ");
						check = false;//while문 반복
					}

				}
				
				menuList.searchMenuByStock(MenuType.INDEX_STOCK, searchedStock);//MenuList 클래스의 searchMenuByStock메소드 실행
				break;
			case '4': //상품 재고 업데이트
				System.out.println("재고를 수정할 상품의 메뉴 ID를 입력해주세요.");
				System.out.print(" > ");
				String stockUpdateId = sc.nextLine();
				System.out.println();
				
				menuList.stockUpdate(MenuType.INDEX_MENU_ID, stockUpdateId);//MenuList 클래스의 stockUpdate메소드 실행
				break;
			default: // 1,2,3,4,B외의 선택지 입력받으면 출력
				System.out.printf("%s 선택지를 찾을 수 없습니다.\n", answer);
				System.out.println();

			}
		}
	}
}
		