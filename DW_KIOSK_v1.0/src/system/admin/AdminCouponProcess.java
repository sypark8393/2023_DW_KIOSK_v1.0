package system.admin;


import java.util.Scanner;
import system.admin.datas.CouponList;
import system.datas.CouponType;

public class AdminCouponProcess {

	// 필드
	private static AdminCouponProcess process = new AdminCouponProcess();

	// 생성자
	private AdminCouponProcess() {}

	// 메소드
	public static AdminCouponProcess getInstance() {
		return process;
	}
	
	/* 메소드명: start
	 * 파라미터: Scanner sc
	 * 		  sc: main()에서 생성한 스캐너 객체
	 * 반환값: 없음
	 * 기능 설명: 스캐너 객체를 통해 입력받은 answer의 값에 따라 쿠폰에 대해 수행할 프로세스를 호출
	 */
	public void start(Scanner sc) {
		char answer;//세부 작업 번호
		
		while(true) {
			
			System.out.println("[쿠폰]에 대해 수행할 세부 작업을 선택해주세요.");
			System.out.println("[1]전체출력 [2]쿠폰 ID로 조회 [3]쿠폰금액으로 조회 [4]사용 여부로 조회 [B]메인 메뉴로 돌아가기");
			System.out.print(" > ");
			String input = sc.next();
			sc.nextLine();
			if (input.length()>1) {//입력이 1글자보다 많으면
				System.out.println("입력이 1글자를 초과하셨습니다. 첫 번째 글자만 사용됩니다.");//안내문 출력
			}	
			answer=input.charAt(0);//answer는 input의 첫 번째 글자만 사용
			
			//B누르면 메인 메뉴로 돌아가기
			if(answer == 'B' || answer == 'b') {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			}
			CouponList couponList=new CouponList(sc);
			int searchedPrice=0;
			switch (answer) {
			case '1': // 쿠폰 목록 전체 출력
				couponList.printWholeCoupon();//couponList클래스의 printWholeCoupon 메소드 호출
				break;
			case '2': // 쿠폰 ID로 조회
				System.out.println("조회할 [쿠폰 ID]를 입력해주세요.");
				System.out.print(" > ");
				String searchedId = sc.nextLine();
				
				couponList.searchCouponById(CouponType.INDEX_COUPON_ID, searchedId);//couponList클래스의 searchCouponById 메소드 호출
				break;
			case '3': // 쿠폰금액으로 조회
				System.out.println("조회할 쿠폰금액 현황을 입력해주세요.");
				System.out.print(" > ");
				boolean check=false;//while문 반복 제어 변수
				while (!check) {
					try {
						searchedPrice = Integer.parseInt(sc.next());
						check=true;//정수 입력받으면 while문 탈출
					} catch (Exception e) {//정수형 외 입력받으면
						// TODO: handle exception
						System.out.println("쿠폰 금액은 숫자만 입력 가능합니다. 다시 입력해주세요.");
						System.out.print(" > ");
						check=false;//while문 반복
					}
				}
			
				couponList.searchCouponByPrice(CouponType.INDEX_PRICE,searchedPrice);//couponList클래스의 searchCouponByPrice 메소드 호출
				break;
			case '4': // 사용 여부로 조회
				System.out.println("조회할 기준을 선택해주세요.");
				System.out.println("[1]Y [2]N");
				System.out.print(" > ");
				char type = sc.next().charAt(0);
				
				System.out.println();
				
				String searchedUsed = sc.nextLine();
				if (type == '1') searchedUsed = "Y";
				else if (type == '2') searchedUsed = "N";
				
				couponList.searchCouponByUsed(CouponType.INDEX_USED, searchedUsed);//couponList클래스의 searchCouponByUsed 메소드 호출
				break;
			default: // 1, 2, 3, 4, B외의 입력 받으면
				System.out.printf("%s 선택지를 찾을 수 없습니다.",answer);
				break;
			}
		}
	}
}