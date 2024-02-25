package system.admin;

import java.util.Scanner;

public class AdminMode {
	private static final String PASSWORD = "123456";	// 관리자 패스워드

	private boolean exit = false; // 종료 여부(true: 관리자 모드 종료, false: 관리자 모든 종료 아님)
	
	// 객체 생성
	private static AdminMode admin = new AdminMode();
	
	// 생성자
	private AdminMode() {}
	
	// 메소드
	public static AdminMode getInstance() {
		return admin;
	}
	
	// 관리자 모드 시작
	/* 메소드명: start
	 * 파라미터: Scanner sc
	 * 		  sc: main()에서 생성한 스캐너 객체
	 * 반환값: 없음
	 * 기능 설명: 관리자 모드를 시작하고, 스캐너 객체를 통해 입력받은 answer의 값에 따라 수행할 프로세스를 불러온다.
	 */
	public void start(Scanner sc) {
		char answer = 0;

		System.out.println("관리자 모드를 시작합니다.");
		
		while(true) {
			// 작업을 수행할 파일 목록 출력 및 파일 종류 입력 처리
			System.out.println("\n작업을 수행할 파일을 선택해주세요.");
			System.out.println("[1]메뉴 [2]회원 [3]쿠폰 [4]통합 매출 내역 [5]상세 매출 내역 [B]관리자 모드 나가기 [E]시스템 종료");
			System.out.print(" > ");
			String input = sc.next();
			sc.nextLine();
			
			if (input.length()>1) {//입력이 1글자보다 많으면
				System.out.println("입력이 1글자를 초과하셨습니다. 첫 번째 글자만 사용됩니다.");//안내문 출력
			}	
			answer=input.charAt(0);//answer는 input의 첫 번째 글자만 사용
			
			// 초기 화면으로 돌아가기
			if(answer == 'B' || answer == 'b') {
				System.out.println("관리자 모드를 종료합니다.");
				return;
			}

			// 시스템 종료
			if(answer == 'E' || answer == 'e') {
				exit = true;
				return;
			}
			
			//실행할 프로세스 선택
			switch(answer) {	
			case '1': // 메뉴
				System.out.println("메뉴입니다.");
				System.out.println();
				AdminMenuProcess adminMenuProcess = AdminMenuProcess.getInstance();//메뉴 프로세스 호출
				adminMenuProcess.start(sc);
				break;
			case '2': // 회원 
				System.out.println("회원입니다.");
				System.out.println();
				AdminMemberProcess adminMemberProcess = AdminMemberProcess.getInstance();//회원 프로세스 호출
				adminMemberProcess.start(sc);
				break;
			case '3': // 쿠폰 
				System.out.println("쿠폰입니다.");
				System.out.println();
				AdminCouponProcess adminCouponProcess = AdminCouponProcess.getInstance();//쿠폰 프로세스 호출
				adminCouponProcess.start(sc);
				break;
			case '4': // 통합 매출 내역
				System.out.println("통합 매출 내역입니다.");
				System.out.println();
				AdminSalesTotalProcess salesTotalProcess = AdminSalesTotalProcess.getInstance();//통합 매출 내역 프로세스 호출
				salesTotalProcess.start(sc);
				break;
			case '5': // 상세 매출 내역
				System.out.println("상세 매출 내역입니다.");
				System.out.println();
				AdminSalesDetailProcess salesDetailProcess = AdminSalesDetailProcess.getInstance();//상세 매출 내역 프로세스 호출
				salesDetailProcess.start(sc);
				break;
			default: // 1,2,3,4,5,B,E외의 선택지 입력받으면 실행
				System.out.printf("%s 선택지를 찾을 수 없습니다. 다시 입력해주세요.",answer);
				System.out.println();
				break;
				
			}
		}	
	}
	
	// 전달받은 패스워드가 사전에 설정된 관리자 비밀번호가 맞는지 검사
	/* 메소드명: adminCheck
	 * 파라미터: String password
	 * 		  password: 사용자로부터 입력받은 관리자 패스워드
	 * 반환값: boolean
	 * 		 true: 관리자 패스워드가 일치하는 경우
	 * 		 false: 관리자 패스워드가 일치하지 않는 경우
	 * 기능 설명: 사용자로부터 입력받은 관리자 패스워가 사전에 설정된 관리자 패스워드와 일치하는지 체크한다.
	 */
	public boolean adminCheck(String password) {
		if(password.equals(this.PASSWORD))
			return true;
		else
			return false;
	}
	
	// 시스템 종료 여부
	public boolean isExit() {
		return exit;
	}
}
