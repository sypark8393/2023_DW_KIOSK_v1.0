package system.admin.datas;

import java.io.*;
import java.util.*;
import system.datas.MenuType;

public class MenuList {
	private Scanner sc=null;
	
	//생성자
	public MenuList(Scanner sc) {
		this.sc=sc;
	}
	
	/* 메소드명: printWholeMenu
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: MenuType 클래스에 지정된 경로에 존재하는 src//files//datas//menu.txt파일의 내용 전체 출력
	 */
	public void printWholeMenu() {
		ArrayList<MenuType> menuList=new ArrayList<MenuType>();//MenuType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		
		//파일을 읽어올 수 있는동안
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리
				//tmp 배열에 저장된 값들을 MenuType 열거형의 생성자에 전달
				MenuType menu = new MenuType(tmp[MenuType.INDEX_MENU_ID], tmp[MenuType.INDEX_CATEGORY], tmp[MenuType.INDEX_NAME],
						Integer.parseInt(tmp[MenuType.INDEX_PRICE]), Integer.parseInt(tmp[MenuType.INDEX_STOCK]));
				//생성된 menu를 menuList에 저장
				menuList.add(menu);
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		//출력
		System.out.println("현재 저장되어 있는 메뉴 리스트를 출력합니다.");
		System.out.println("======================================================================");
		System.out.println("       메뉴ID       카테고리                   메뉴명           가격     재고");
		System.out.println("======================================================================");
		for (int i = 0; i < menuList.size(); i++) {
			menuList.get(i).printAdminMenu();
		}
		System.out.println("======================================================================");
		
	}
	
	/* 메소드명: searchMenuById
	 * 파라미터: MenuType.INDEX_MENU_ID, searchedId
	 * 반환값: 없음
	 * 기능 설명: MenuType 클래스에 지정된 경로에 존재하는 src//files//datas//menu.txt파일에서 
	 * 		   searchedId와 MenuType.INDEX_MENU_ID가 동일한 메뉴를 찾아 출력
	 */
	public void searchMenuById(int index, String keyword) {
		ArrayList<MenuType> menuList=new ArrayList<MenuType>();//MenuType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;// searchedId와 MenuType.INDEX_MENU_ID가 동일한 메뉴를 발견했는지 확인하는 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[MenuType.INDEX_MENU_ID].equals(keyword)) {//searchedId와 MenuType.INDEX_MENU_ID가 동일한 메뉴를 발견
					found=true;
					MenuType menu = new MenuType(tmp[MenuType.INDEX_MENU_ID], tmp[MenuType.INDEX_CATEGORY], tmp[MenuType.INDEX_NAME],
							Integer.parseInt(tmp[MenuType.INDEX_PRICE]), Integer.parseInt(tmp[MenuType.INDEX_STOCK]));

					menuList.add(menu);
				}

			}
			in.close();
			if (found==false) {//searchedId와 MenuType.INDEX_MENU_ID가 동일한 메뉴를 발견하지 못하면
				System.out.printf("메뉴 ID가 %s인 메뉴를 찾을 수 없습니다.\n",keyword);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {////searchedId와 MenuType.INDEX_MENU_ID가 동일한 메뉴를 발견했으면 출력 시작
			System.out.printf("메뉴 ID가 %s인 메뉴 목록입니다.\n",keyword);
			System.out.println("======================================================================");
			System.out.println("       메뉴ID       카테고리                   메뉴명           가격     재고");
			System.out.println("======================================================================");
			for (int i = 0; i < menuList.size(); i++) {
				menuList.get(i).printAdminMenu();
			}
			System.out.println("======================================================================");
		}
	}
	
	/* 메소드명: searchMenuByStock
	 * 파라미터: MenuType.INDEX_STOCK, searchedStock
	 * 반환값: 없음
	 * 기능 설명: MenuType 클래스에 지정된 경로에 존재하는 src//files//datas//menu.txt파일에서 
	 * 		   searchedStock과 MenuType.INDEX_STOCK이 동일한 메뉴를 찾아 출력
	 */
	public void searchMenuByStock(int index,int keyword) {
		ArrayList<MenuType> menuList=new ArrayList<MenuType>();//MenuType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		String s;
		String[] tmp;
		boolean found=false;//searchedStock과 MenuType.INDEX_STOCK이 동일한 발견했는지 확인하는 변수
		
		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				int stockInt = Integer.parseInt(tmp[MenuType.INDEX_STOCK]);

				if (keyword == stockInt) {//searchedStock과 MenuType.INDEX_STOCK이 동일한 발견
					found=true;
					MenuType menu = new MenuType(tmp[MenuType.INDEX_MENU_ID], tmp[MenuType.INDEX_CATEGORY], tmp[MenuType.INDEX_NAME],
							Integer.parseInt(tmp[MenuType.INDEX_PRICE]), Integer.parseInt(tmp[MenuType.INDEX_STOCK]));

					menuList.add(menu);
				}
			}
			in.close();
			if (found==false) {//searchedStock과 MenuType.INDEX_STOCK이 동일한 발견하지 못하면
				System.out.printf("재고가 %d인 메뉴를 찾을 수 없습니다.\n",keyword);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {//searchedStock과 MenuType.INDEX_STOCK이 동일한 발견하면 출력 시작
			System.out.printf("현재 재고가 %s인 상품 목록입니다.\n",keyword);
			System.out.println("======================================================================");
			System.out.println("       메뉴ID       카테고리                   메뉴명           가격     재고");
			System.out.println("======================================================================");
			for (int i = 0; i < menuList.size(); i++) {
				menuList.get(i).printAdminMenu();
			}
			System.out.println("======================================================================");
		}
	}
	
	/* 메소드명: stockUpdate
	 * 파라미터: MenuType.INDEX_MENU_ID, stockUpdateId
	 * 반환값: 없음
	 * 기능 설명: MenuType 클래스에 지정된 경로에 존재하는 src//files//datas//menu.txt파일에서 
	 * 		   stockUpdateId과 MenuType.INDEX_MENU_ID이 동일한 메뉴를 찾아 stock을 stockUpdate의 값으로 변경
	 */
	public void stockUpdate(int index,String keyword) {
		
		BufferedReader in;	// 입력 스트림
		PrintWriter out;
		String s;
		String[] tmp;
		boolean check=false;//while문 반복 제어 조건
		boolean found=false;//stockUpdateId과 MenuType.INDEX_MENU_ID이 동일한 메뉴를 발견했는지 확인하는 변수
		int stockUpdate=0;
		System.out.println("변경할 재고 수량을 입력해 주세요.");
		System.out.print(" > ");
		
		while (!check) {//check가 false면 계속 반복
			try {
				stockUpdate = Integer.parseInt(sc.next());
				
				check=true;//정수 입력받으면 while문 탈출
			} catch (Exception e) {//정수형 외 입력받으면
				// TODO: handle exception
				System.out.println("숫자만 입력 가능합니다.다시 입력해주세요.");
				System.out.print(" > ");
				check=false;//while문 반복
			}
		}
		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			out = new PrintWriter(new FileWriter(MenuType.TMP_FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("파일 경로를 찾을 수 없습니다.");
			return;
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생했습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리
				if (tmp[MenuType.INDEX_MENU_ID].equals(keyword)) { //stockUpdateId과 MenuType.INDEX_MENU_ID이 동일한 메뉴를 발견
					found=true;
					System.out.print(s);
					//재고 업데이트
					System.out.printf(" ===> 재고 %d로 설정 ===> ", stockUpdate);
					tmp[MenuType.INDEX_STOCK] = stockUpdate + "";
					s = String.join("|", tmp); // 구분자 기준으로 문자열 합침
					System.out.print(s+"\n");
				}

				out.println(s); // 파일에 쓰기
			}
			
			in.close();
			out.close();
			if (found==false) {//stockUpdateId과 MenuType.INDEX_MENU_ID이 동일한 메뉴를 발견하지 못하면
				System.out.printf("메뉴 ID가 %s인 메뉴를 찾을 수 없습니다.\n",keyword);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		File file = new File(MenuType.FILE_NAME);
		file.delete();

		// 새로운 파일명을 술정
		file = new File(MenuType.TMP_FILE_NAME);
		file.renameTo(new File(MenuType.FILE_NAME));
		
	}
}	