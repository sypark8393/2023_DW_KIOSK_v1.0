package system.main;

import java.io.File;

// 파일 체크
public class FileCheck {
	
	// 필수 데이터 경로
	private static final String MENU_FILE = "src//files//datas//menu.txt";
	private static final String MEMBER_FILE = "src//files//datas//member.txt";
	private static final String COUPON_FILE = "src//files//datas//coupon.txt";

	// 1.주문 처리 파일 존재여부 체크
	/* 메소드명: IsFilesExist
	 * 파라미터: 없음
	 * 반환값: boolean
	 * 		 true: 파일이 모두 존재
	 * 		 false: 파일이 1개라도 존재하지 않음
	 * 기능 설명: 주문 처리 파일이 모두 존재하는지 검사한다.
	 */
	public static boolean isFilesExist() {
		boolean check = true;
		
		if(!(new File(MENU_FILE).exists())) {
			check = false;
		}
		if(!(new File(MEMBER_FILE).exists())) {
			check = false;
		}
		if(!(new File(COUPON_FILE).exists())) {
			check = false;
		}
		
		return check;
	}
}
