package system.admin;

import java.util.Scanner;

import system.admin.datas.SalesDetailList;

public class AdminSalesDetailProcess {

	// 객체 생성
	private static AdminSalesDetailProcess process = new AdminSalesDetailProcess();
	
	// 생성자
	private AdminSalesDetailProcess() {}
	
	// 메소드
	public static AdminSalesDetailProcess getInstance() {
		return process;
	}

	/* 메소드명: start
	 * 파라미터: Scanner sc
	 * 		  sc: main()에서 생성한 스캐너 객체
	 * 반환값: 없음
	 * 기능 설명: 사용자로부터 입력받은 값(answer)에 따라 상세 매출 내역에 대해 수행할 작업 프로세스를 호출
	 */
	public void start(Scanner sc) {
		char answer;	// 세부 작업 번호
		String date;	// 매출일자
		while (true) {
			// 세부 작업 목록 출력 및 입력 처리
			System.out.println("\n[상세 매출 내역]에 대해 수행할 세부 작업을 선택해주세요.");
			System.out.println("[1]전체 출력 [2]주문번호로 조회 [3]유형(메뉴/포인트/쿠폰)으로 조회 [4]ID로 조회 [5]CSV 파일로 내보내기 [B]메인 메뉴로 돌아가기");
			System.out.print(" > ");
			String input = sc.next();
			sc.nextLine();
			if (input.length() > 1) {// 입력이 1글자보다 많으면
				System.out.println("입력이 1글자를 초과하셨습니다. 첫 번째 글자만 사용됩니다.");// 안내문 출력
			}
			answer = input.charAt(0);// answer는 input의 첫 번째 글자만 사용;

			// 뒤로 가기 - 작업 종류를 입력받는 단계로 돌아감
			if (answer == 'B' || answer == 'b') {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			}
			
			if(answer < '1' || answer > '5') {
				System.out.printf("%s 선택지를 찾을 수 없습니다.", answer);
				continue;
			}
			
			// 매출 내역을 조회할 날짜 입력받기
			System.out.println("매출일자를 입력해주세요.(YYYYMMDD 형식)");
			System.out.print(" > ");
			date = sc.nextLine();

			// SalesDetail 파일 처리를 위한 객체 생성
			SalesDetailList salesDetailData = new SalesDetailList(sc);
			salesDetailData.setFileName(date);
			System.out.println();

			switch (answer) {
			case '1': // 전체 출력
				salesDetailData.printWholeDatas();
				break;
				
			case '2': // 주문번호로 조회
				System.out.println("조회할 [주문번호]를 입력해주세요.");
				System.out.print(" > ");
				String orderNo = sc.nextLine();

				salesDetailData.searchDatas(SalesDetailList.INDEX_ORDER_NO, orderNo);
				break;

			case '3': // 유형(메뉴/포인트/쿠폰)으로 조회
				System.out.println("조회할 기준을 선택해주세요.");
				System.out.println("[1]메뉴 [2]포인트 [3]쿠폰");
				System.out.print(" > ");
				char type = sc.next().charAt(0);
				sc.nextLine();

				String strType = "";
				if (type == '1') strType = "메뉴";
				else if (type == '2') strType = "포인트";
				else if (type == '3') strType = "쿠폰";
				else {
					System.out.printf("%s 선택지를 찾을 수 없습니다.\n",type);
					break;
				}

				salesDetailData.searchDatas(SalesDetailList.INDEX_TYPE, strType);
				break;

			case '4': // ID로 조회
				System.out.println("조회할 [ID]를 입력해주세요.");
				System.out.print(" > ");
				String id = sc.nextLine();

				salesDetailData.searchDatas(SalesDetailList.INDEX_ID, id);
				break;

			case '5': // CSV 파일로 내보내기
				System.out.print("CSV 파일을 생성할 절대 경로를 입력하세요 > ");
				String path = sc.nextLine(); // 경로

				path = salesDetailData.toCsvFile(path);
				if (!path.equals("")) {
					System.out.println("CSV 파일이 정상적으로 생성되었습니다.");
					System.out.println("파일 경로: " + path);

					System.out.println("***CSV 파일 사용 전 함께 생성된 readme.txt 파일을 읽어보시기 바랍니다.");
				}
				break;
			}
		}
	}
}
