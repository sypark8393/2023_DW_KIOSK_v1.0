package system.main;

import system.admin.AdminMode;
import system.user.UserMode;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AdminMode admin = AdminMode.getInstance();	// 관리자 모드
		UserMode user = UserMode.getInstance();		// 사용자 모드
		
		Scanner sc = new Scanner(System.in);		// 스캐너
		
		String answer;
		
		// 1.주문 처리를 위한 필수 파일의 존재 여부 검사: 모든 파일이 존재하지 않을 경우 시스템 종료
		if(!FileCheck.isFilesExist()) {
			System.out.println("데이터 오류로 요청하신 작업을 처리할 수 없습니다.");
			System.out.println("오류가 지속될 경우 관리자에게 문의해주세요.");
			
			sc.close();	// 스캐너 닫기
			return;		// 시스템 종료
		}
		
		// 2.입력된 요청에 따라 사용자 모드 혹은 관리자 모드를 호출 
		while(true) {
			System.out.println("시작하려면 [시작]을 입력해주세요.");
			System.out.print(" >> ");
			answer = sc.nextLine();
		
			if(answer.equals("시작")) {	// 사용자 모드
				while(!user.start(sc));	// 주문 취소 혹은 주문 종료인 경우에만 탈출
				user.reset();			// 이전 주문 정보 초기화

			} else if(admin.adminCheck(answer)) {	// 관리자 모드
				admin.start(sc);
				
				if(admin.isExit()) {
					System.out.println("[관리자 권한으로 시스템을 종료합니다.]");
					sc.close();	// 스캐너 닫기
					return;
				}

			} else { //예외처리 : 시작 외의 값은 X
				System.out.println("입력값이 올바르지 않습니다.");
				System.out.println("다시 입력해주세요.\n\n");
				
			}
		}

	}

}
