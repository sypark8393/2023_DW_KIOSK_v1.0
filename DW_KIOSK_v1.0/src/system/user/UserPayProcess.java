package system.user;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import system.datas.*;

public class UserPayProcess {
	
	private final static String[] CARD_NAME_LIST = {"비씨", "국민", "하나", "삼성", "신한", "현대", "롯데", "씨티", "농협", "우리"}; // 제휴되어 있는 카드사 목록
	
	private Scanner sc;	// 스캐너 객체
	
	// 생성자
	public UserPayProcess(Scanner sc) {
		this.sc = sc;
	}
	
	// 11-2-1.회원 ID 입력 받기, 11-3-1.회원 ID 조회, 11-4-1.포인트 사용 금액 입력 받기
	/* 메소드명: userPoint
	 * 파라미터: int totalAmt
	 * 		  totalAmt: 포인트 사용 전 결제 금액
	 * 반환값: int discountAmt
	 * 		 discountAmt: 사용할 포인트 금액
	 * 기능 설명: 1)사용자로부터 회원 ID를 입력받아 회원 여부와 보유 포인트 금액을 조회한다.
	 * 		   2)보유 포인트가 사용 가능 금액(100p) 이상인 회원인 경우 사용할 포인트 금액을 입력받고, 사용할 포인트 금액이 유효한 경우에만 그 값을 반환한다.
	 */
	public int usePoint(int totalAmt) {
		int discountAmt;
		
		// 포인트를 사용할 회원 ID 입력받기
		System.out.println("\n포인트를 사용할 회원의 휴대전화 번호를 입력해주세요. (ex.01012345678)");
		System.out.print(" >> 010");
		String memberId = "010" + sc.nextLine(); // 회원 ID 입력
		
		// 입력받은 회원 ID가 올바르지 않은 경우
		if(memberId.length() != 11) {
			System.out.println("휴대전화 번호는 중간번호와 뒷번호를 하이픈(-)을 제외한 8자리로 입력해주세요.\n");
			return -1;
		}
		
		try {
			Integer.parseInt(memberId);
		}  catch(NumberFormatException e) {
			System.out.println("휴대전화 번호는 숫자로 입력해주세요.\n");
			return -1;
		}
		
		// 사용 가능한 포인트 조회
		int totalPointAmt = searchPointAmt(memberId);
		
		if(totalPointAmt == -1) {	// Exception 발생		
			return -1;
		} else if(totalPointAmt == -9999) {		// 회원 정보가 없는 경우
			System.out.println("회원 정보를 찾을 수 없습니다.\n");
			return -1;
		} else if(totalPointAmt < 100) {		// 포인트가 100p 미만
			System.out.printf("포인트는 최소 100p부터 사용 가능합니다.(현재 보유 포인트: %,dp)\n\n", totalPointAmt);
			return -1;
		}
		
		// 회원 정보 조회 결과 포인트가 100p 이상인 경우
		while(true) {
			// 사용할 포인트 금액 입력받기
			System.out.printf("\n현재 보유 포인트: %,d\n", totalPointAmt);
			System.out.println("사용하실 포인트 금액을 입력해주세요.(최소 100p 단위) (취소 [0])");
			System.out.print(" >> ");
			
			try {
				discountAmt = sc.nextInt(); // 사용할 포인트 금액
				sc.nextLine();
			} catch(InputMismatchException e) {	// 예외처리 - 정수 이외의 값
				System.out.println("사용하실 포인트 금액은 100p 이상의 정수로 입력해주세요. (최소 100p 단위)\n");
				sc.nextLine();
				return -1;
			}
			
			if(discountAmt == 0) {
				System.out.println("\n[포인트 사용이 취소되었습니다.]");
				return -1;
			}
			
			if(discountAmt < 100) {	// 최소 사용 금액인 100p를 만족하지 않는 경우
				System.out.println("사용하실 포인트 금액은 100p 이상의 정수로 입력해주세요. (최소 100p 단위)\n");
				
			} else if(discountAmt % 100 != 0) {	// 사용할 포인트 금액이 최소 100원 단위가 아닌 경우
				System.out.println("사용하실 포인트 금액은 최소 100p 단위로 입력해주세요.\n");
				
			} else if(discountAmt > totalPointAmt || discountAmt > totalAmt) {	// 사용할 포인트 금액이 현재 보유한 포인트 금액보다 크거나 결제 금액보다 큰 경우
				System.out.println("사용 가능한 포인트 금액을 초과하였습니다.");
				System.out.printf("사용하실 포인트 금액은 %,dp 이하로 입력해주세요\n\n", (totalAmt > totalPointAmt)? totalPointAmt: totalAmt);

			} else {	// 입력받은 포인트 금액만큼 사용이 가능한 경우
				UserMode.salesTotal.setDiscountId(memberId);
				return discountAmt;
			}
		}
	}
	
	// 11-3-1.회원 ID 조회
	/* 메소드명: searchPointAmt
	 * 파라미터: String memberId
	 * 		  memberId: 회원 ID(휴대전화 번호)
	 * 반환값: int totalPointAmt
	 * 		 totalPointAmt: 회원이 보유하고 있는 포인트 금액
	 * 기능 설명: 회원 ID를 이용하여 회원 정보를 조회한 후, 회원이 경우 보유하고 있는 포인트 금액을 반환한다.
	 */
	private int searchPointAmt(String memberId) {
		BufferedReader in;	// 입력 스트림
		
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MemberType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 메뉴 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.println("\n회원 데이터를 불러올 수 없습니다.");
			System.out.println("오류가 지속될 경우 관리자에게 문의해주세요.\n");
			return -1;
		}
		
		String s;						// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;					// 구분자로 분리한 문자열이 저장될 임시 배열
		int totalPointAmt = -9999;		// 포인트 금액 변수 (초기값: -9999)
		
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				if(tmp[MemberType.INDEX_MEMBER_ID].equals(memberId)) {
					//System.out.println("회원 조회에 성공하였습니다.");
					totalPointAmt = Integer.parseInt(tmp[MemberType.INDEX_POINT]);
					break;
				}
			}
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("\n비정상적인 오류로 포인트 조회가 불가합니다.");
			System.out.println("오류가 지속될 경우 관리자에게 문의해주세요.\n");
			return -1;
			
		}
		
		return totalPointAmt;
		
	}
	
	// 11-2-2.쿠폰 ID 입력 받기, 11-3-2.쿠폰 ID 조회, 11-4-2.쿠폰 사용 여부 입력 받기
	/* 메소드명: useCoupon
	 * 파라미터: int totalAmt
	 * 		   totalAmt: 쿠폰 사용 전 결제 금액
	 * 반환값: int totalCouponAmt
	 * 		 totalCouponAmt: 실제 적용되는 쿠폰 금액
	 * 기능 설명: 1)사용자로부터 쿠폰 ID를 입력받아 쿠폰의 발급 여부와 상태(사용 가능/사용 불가)를 조회한다.
	 * 		   2)정상적으로 발급되어 사용 가능한 쿠폰이라면 쿠폰 사용 여부를 입력받아 실제 적용되는 쿠폰 금액을 반환한다.
	 */
	public int useCoupon(int totalAmt) {
		// 쿠폰 ID 입력 받기
		System.out.println("\n사용할 쿠폰 ID를 입력해주세요. (ex.C01012345678xxx)");
		System.out.print(" >> C010");
		String couponId = "C010" + sc.nextLine(); // 쿠폰 ID 입력
		
		// 입력받은 쿠폰 ID가 올바르지 않은 경우
		if(couponId.length() != 15) {
			System.out.println("쿠폰 ID가 올바르지 않습니다.\n");
			return -1;
		}
		
		// 쿠폰 금액 조회
		int totalCouponAmt = searchCouponAmt(couponId);
		
		if(totalCouponAmt == -1) {				// Exception 발생
			return -1;
		} else if(totalCouponAmt == 0) {		// 포인트가 0p
			System.out.println("이미 사용된 쿠폰입니다.\n");
			return -1;
		} else if(totalCouponAmt == -9999) {	// 회원 정보가 없는 경우
			System.out.println("쿠폰 ID가 유효하지 않습니다.\n");
			return -1;
		} else {	// 사용 가능한 쿠폰인 경우
			while(true) {
				System.out.printf("\n쿠폰 금액: %,d\n", totalCouponAmt);
				System.out.print("쿠폰 사용 여부를 선택해주세요.");
				if(totalCouponAmt > totalAmt) System.out.print("(쿠폰 사용 후 차액은 반환되지 않습니다.)");
				System.out.println();
				System.out.println("[1]사용  [2]사용 취소");
				System.out.print(" >> ");
				
				int selectUse;
				try {
					selectUse = sc.nextInt();
					sc.nextLine();
					
				} catch(InputMismatchException e) {	// 예외처리: 1, 2를 제외한 숫자값을 입력한 경우
					sc.nextLine();
					System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
					continue;
				}
				
				if(selectUse == 1) {
					UserMode.salesTotal.setDiscountId(couponId);
					totalCouponAmt = (totalCouponAmt > totalAmt)? totalAmt: totalCouponAmt;	// 쿠폰 금액이 결제 금액보다 큰 경우 총 할인 금액은 결제 금액과 동일하게 변경
					return totalCouponAmt;
				}
				
				if(selectUse == 2) {
					System.out.println("\n[쿠폰 사용이 취소되었습니다.]");
					return 0;
				}
				
				System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
			}
			
		}
	}
	
	// 11-3-2.쿠폰 ID 조회
	/* 메소드명: searchCouponAmt
	 * 파라미터: String couponId
	 * 		  couponId: 쿠폰 ID
	 * 반환값: int couponAmt
	 * 		 couponAmt: 쿠폰 금액
	 * 기능 설명: 쿠폰 ID를 이용하여 쿠폰을 조회한 후, 사용 가능한 쿠폰인 경우 쿠폰 금액을 반환한다.
	 */
	private int searchCouponAmt(String couponId) {
		BufferedReader in;	// 입력 스트림
		
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 메뉴 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.println("\n쿠폰 데이터를 불러올 수 없습니다.");
			System.out.println("오류가 지속될 경우 관리자에게 문의해주세요.\n");
			return -1;
		}
		
		String s;					// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;				// 구분자로 분리한 문자열이 저장될 임시 배열
		int couponAmt = -9999;		// 포인트 금액 변수 (초기값: -9999)
		
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				if(tmp[CouponType.INDEX_COUPON_ID].equals(couponId)) {
					//System.out.println("쿠폰 조회에 성공하였습니다.");
					
					// 사용된 적이 없는 쿠폰이면 그 금액으로 세팅하고, 사용된 적이 있으면 0으로 세팅
					couponAmt = (tmp[CouponType.INDEX_USED].equals("N"))? Integer.parseInt(tmp[CouponType.INDEX_PRICE]): 0;
					
					break;
				}
			}
			
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("\n비정상적인 오류로 포인트 조회가 불가합니다.");
			System.out.println("오류가 지속될 경우 관리자에게 문의해주세요.\n");
			return -1;
		}
		
		return couponAmt;

	}

	// 12-1.주문 정보 내용 출력
	/* 메소드명: printOrderSheet
	 * 파라미터: String discountName, int discountAmt
	 * 		  discountName: 할인 내용(포인트 사용/쿠폰 할인)
	 * 		  discountAmt: 할인 금액
	 * 반환값: 없음
	 * 기능 설명: 할인 내용과 할인 금액이 받은 후, 내용이 적용된 최종 주문 정보 출력한다.
	 */
	public void printOrderSheet(String discountName, int discountAmt) {
		System.out.println("===============================================");
		System.out.println("메뉴명                         단가   수량       금액");
		System.out.println("-----------------------------------------------");
		
		for(OrderedMenuType menu : UserMode.cart) {
			menu.printCartTemplate();
		}
		
		if(discountAmt != 0)
			System.out.printf("%-20s\t%,8d   %2d  %,8d\n", discountName, -discountAmt, 1, -discountAmt);
		
		System.out.println("===============================================");
		System.out.printf("합계:\t\t\t\t       %,8d\n", OrderedMenuType.getTOTAL_AMT()-discountAmt);
		System.out.println("===============================================");
	}
	
	// 카드/바코드 번호 숫자 체크
	/* 메소드명: isDigitNo
	 * 파라미터: String payMethodNo
	 * 		  payMethodNo: 결제 수단 정보(카드 번호 혹은 바코드 번호)
	 * 반환값: boolean
	 * 		 true: 문자열의 모든 문자가 숫자인 경우
	 * 		 false: 문자열에 숫자가 아닌 값이 1개라도 있는 경우
	 * 기능 설명: 파라미터로 받은 결제 수단 번호가 숫자로만 구성되어 있는지 검사한다.
	 */
	public boolean isDigitNo(String payMethodNo) {
		for(int i=0; i<payMethodNo.length(); i++) {
			if(!Character.isDigit(payMethodNo.charAt(i)))
				return false;
		}
		
		return true;
	}
	
	// 16. 승인 요청 및 응답 수신
	/* 메소드명: goPay
	 * 파라미터: String payMethodNo, int totalAmt, int cardQuota
	 * 		  payMethodNo: 결제 수단 정보(카드 번호 혹은 바코드 번호)
	 * 		  totalAmt: 결제 금액
	 * 		  cardQuota: 할부 개월
	 * 반환값: boolean
	 * 		 true: 승인에 성공한 경우
	 * 		 false: 승인에 실패한 경우
	 * 기능 설명: 파라미터로 받은 값을 이용해 제휴사로 승인 요청을 진행하고 승인 응답을 수신한다.
	 */
	public boolean goPay(String payMethodNo, int totalAmt, int cardQuota) {
		/* 실제 결제 API가 연동되어 있지 않기 때문에
		 * 무조건 승인에 성공한 것으로 처리(return true;)하고
		 * 제휴사 응답값(카드사명, 카드번호(바코드 결제), 승인번호)은 난수를 이용해 임의 값으로 세팅한다.
		 */
		
		Random random = new Random();	// 난수 생성을 위한 Random 객체
		
		// 거래 일시
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

		String transDt = formatter.format(today);
		UserMode.salesTotal.setTransDt(transDt);

		// 거래 금액
		UserMode.salesTotal.setTotalAmt(totalAmt);
		
		// 카드사명
		int size = CARD_NAME_LIST.length;
		String cardName = CARD_NAME_LIST[random.nextInt(size)];	// 제휴되어 있는 카드사 중 하나
		UserMode.salesTotal.setCardName(cardName);
		
		// 카드번호 - 신용카드 결제이면 입력받은 카드번호에 마스킹 처리, 바코드 결제면 임의 생성
		String cardNo;
		if(payMethodNo.length() == 16) { // 신용카드: 0000 0000 0000 0000 => 0000 00** **** 0000
			cardNo = payMethodNo.substring(0, 6) + "******" + payMethodNo.substring(12);
			UserMode.salesTotal.setPayMethod("신용카드");
		} else { // 바코드 결제: 바코드를 통해 사전에 등록된 카드로 결제를 진행되고 제휴사에서는 그 카드번호를 반환해준다고 가정
			cardNo = String.format("%06d", random.nextInt(1000000)) + "******" + String.format("%04d", random.nextInt(10000));
			UserMode.salesTotal.setPayMethod("간편결제");
		}
		UserMode.salesTotal.setCardNo(cardNo);
		
		// 할부개월
		UserMode.salesTotal.setCardQuota(String.format("%02d", cardQuota));
		
		// 승인번호 - 8자리 임의값
		UserMode.salesTotal.setAuthCode(String.format("%08d", random.nextInt(100000000)));
		
		return true;
	}
	
}
