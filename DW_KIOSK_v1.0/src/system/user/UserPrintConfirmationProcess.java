package system.user;

import system.datas.SalesDetailType;

public class UserPrintConfirmationProcess {
	// 기본 상점 정보
	private static final String STORE_NAME = "대우커피 상봉역점";					// 상호
	private static final String STORE_LICENSE_NO = "123-45-67890";			// 사업자번호
	private static final String CEO = "홍길동";								// 대표자명
	private static final String TEL = "02-000-0000";						// 전화번호
	private static final String ADDRESS = "서울특별시 중랑구 망우로 291 상봉빌딩 6층";	// 주소
	
	/* 메소드명: printExchangeTicket
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 대기번호표를 출력한다.
	 */
	public void printExchangeTicket() {
		System.out.println();
		System.out.println("┌───────────────────────────────────────┐");
		System.out.println("│\t\t\t\t\t│");
		System.out.println("│ [교환권]\t\t\t\t│");
		System.out.printf("│ %s\t\t\t\t\t│\n", UserMode.salesTotal.getType());
		System.out.printf("│ %s\t\t\t│\n", toTransDtTemplate(UserMode.salesTotal.getTransDt()));
		System.out.println("│ =====================================	│");
		System.out.printf("│ 대기번호: %s\t\t\t\t│\n", UserMode.salesTotal.getWaitNo());
		System.out.println("│ =====================================	│");
		System.out.println("│ 메뉴명\t\t\t\t   수량\t│");
		System.out.println("│ -------------------------------------	│");
		
		for(SalesDetailType salesDetail : UserMode.salesDetailList) {
			if(salesDetail.getType() == "C" || salesDetail.getType() == "P")
				continue;
			
			System.out.print("│ " + salesDetail.getName());
			
			printNameTap(salesDetail.getName());
			System.out.printf("   %2d\t│\n", salesDetail.getQuantity());
		}
		
		System.out.println("│ =====================================	│");
		System.out.println("│\t\t\t\t\t│");
		System.out.println("│ 메뉴 수령 시 카운터에서 교환권을 보여주시기 바랍니다.	│");
		System.out.println("│\t\t\t\t\t│");
		System.out.println("└───────────────────────────────────────┘");
	}
	
	/* 메소드명: printWithReceipt
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 영수증을 출력한다.
	 */
	public void printWithReceipt() {
		System.out.println("┌───────────────────────────────────────────────────────┐");
		System.out.println("│\t\t\t\t\t\t\t│");
		System.out.println("│ [영수증]\t\t\t\t\t\t│");
		System.out.printf("│ %s\t\t\t\t\t\t\t│\n", UserMode.salesTotal.getType());
		System.out.printf("│ %s\t\t\t\t\t│\n", toTransDtTemplate(UserMode.salesTotal.getTransDt()));
		System.out.println("│ =====================================================\t│");
		System.out.printf("│ 상호: %s\t\t\t\t\t│\n", STORE_NAME);
		System.out.printf("│ 사업자번호: %s\t\t\t\t\t│\n", STORE_LICENSE_NO);
		System.out.printf("│ 대표: %s\t\t\t\t\t\t│\n", CEO);
		System.out.printf("│ TEL: %s\t\t\t\t\t│\n", TEL);
		System.out.printf("│ 주소: %s\t\t\t│\n", ADDRESS);
		System.out.println("│\t\t\t\t\t\t\t│");
		System.out.printf("│ #%s\t\t\t\t\t│\n", UserMode.salesTotal.getOrderNo());
		System.out.println("│ =====================================================\t│");
		System.out.println("│ 메뉴명\t\t\t\t     단가　  수량\t    금액\t│");
		System.out.println("│ -----------------------------------------------------\t│");

		for(SalesDetailType salesDetail : UserMode.salesDetailList) {
			System.out.print("│ " + salesDetail.getName());
			printNameTap(salesDetail.getName());
			System.out.printf("%,8d   %2d  %,8d\t│\n", salesDetail.getPrice(), salesDetail.getQuantity(), salesDetail.getTotalPrice());
		}
	
		System.out.println("│ =====================================================\t│");
		System.out.printf("│ 합계금액\t\t\t\t\t    　　%,8d\t│\n", UserMode.salesTotal.getTotalAmt());
		System.out.printf("│ 공급가액\t\t\t\t\t    　　%,8d\t│\n", UserMode.salesTotal.getSupplyAmt());
		System.out.printf("│ 부가가치세\t\t\t\t    　　%,8d\t│\n", UserMode.salesTotal.getVat());
		System.out.println("│ =====================================================\t│");
		System.out.printf("│ 결제방법\t\t\t\t\t\t　%s\t│\n", UserMode.salesTotal.getPayMethod());
		
		if(UserMode.salesTotal.getTotalAmt() != 0) {
			System.out.printf("│ 카드사명\t\t\t\t\t\t　　　%s\t│\n", UserMode.salesTotal.getCardName());
			System.out.printf("│ 카드번호\t\t\t\t    　　%s\t│\n", UserMode.salesTotal.getCardNo());
			System.out.printf("│ 할부개월\t\t\t\t\t\t  　　%s\t│\n", UserMode.salesTotal.getCardQuota());
			System.out.printf("│ 승인번호\t\t\t\t\t    　　%s\t│\n", UserMode.salesTotal.getAuthCode());
		}
		System.out.println("│ =====================================================\t│");

		if(!UserMode.salesTotal.getMemberId().equals(" ")) {
			System.out.println("│ [포인트 적립]\t\t\t\t\t\t│");
			System.out.printf("│ 적립포인트\t\t\t\t　　　　　%,8d\t│\n", UserMode.salesTotal.getRewardPts());
			System.out.printf("│ 누적포인트\t\t\t\t　　　　　%,8d\t│\n", UserMode.salesTotal.getTotalPts());
			System.out.println("│ =====================================================\t│");
		}
		
		System.out.println("│\t\t\t\t\t\t\t│");
		System.out.println("│ 결제방법 변경은 결제일로부터 30일 내 카드와 영수증을 지참하시어 해당 매장에\t│");
		System.out.println("│ 방문하여 주십시오.\t\t\t\t\t\t│");
		System.out.println("│\t\t\t\t\t\t\t│");
		System.out.println("└───────────────────────────────────────────────────────┘");
	}
	
	/* 메소드명: toTransDtTemplate
	 * 파라미터: String transDt
	 * 		  transDt: 거래 일시
	 * 반환값: String
	 * 기능 설명: YYYYMMDDHHMMss 형식의 문자열을 받아 YYYY-MM-DD HH:MM:ss 형식으로 변환하여 반환
	 */
	public String toTransDtTemplate(String transDt) {
		return transDt.substring(0, 4) + "-" + transDt.substring(4, 6) + "-" + transDt.substring(6, 8)
				+ " " + transDt.substring(8, 10) + ":" + transDt.substring(10, 12) + ":" + transDt.substring(12, 14);
	}
	
	/* 메소드명: printNameTap
	 * 파라미터: String name
	 * 		  name: 메뉴명
	 * 반환값: 없음
	 * 기능 설명: 메뉴명에서 반각 문자(영문, 숫자)와 전각 문자(한글)의 개수에 따라 탭의 개수를 다르게 지정하여 함께 출력
	 * 		   (전각 문자와 반각 문자의 폭이 달라 전체 문자 개수를 기준으로 탭의 개수를 다르게 할 때 출력 양식이 깨지는 문제를 해결하기 위한 기능)
	 */
	public void printNameTap(String name) {
		double totalCharLength;	// 전체 길이
		int halfCharCnt = 2;	// 반각문자 개수
		int wholeCharCnt = 0;	// 전각문자 개수
		boolean check = false;	// 반각2+전각1, 반각1+전각2 케이스일 때 true
		
		for(int i=0; i<name.length(); i++) {
			if (name.charAt(i) >= ' ' && name.charAt(i) <= '~') halfCharCnt++;	// 반각문자이면 카운트
			else wholeCharCnt++;
		}
		
		totalCharLength = halfCharCnt / 4 + wholeCharCnt / 3;
		halfCharCnt %= 4;
		wholeCharCnt %= 3;

		if(halfCharCnt >= 2 && wholeCharCnt >= 1) {	// 반각2 + 전각1 케이스
			totalCharLength++;
			halfCharCnt -= 2;
			wholeCharCnt -= 1;
			check = true;
		} else if(halfCharCnt >= 1 && wholeCharCnt >= 2) {	// 반각1 + 전각2 케이스
			totalCharLength++;
			halfCharCnt -= 1;
			wholeCharCnt -= 2;
			check = true;
		}
		
		totalCharLength = totalCharLength * 4 + 0.25 * halfCharCnt + 0.33 * wholeCharCnt;

		if(check && totalCharLength%8 == 0) System.out.print(" ");;
		for(int i=0; i<4-totalCharLength/8; i++) {
			System.out.print("\t");
		}
	}
}
