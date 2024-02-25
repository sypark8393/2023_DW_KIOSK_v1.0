package system.user;

import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.*;

import system.datas.*;

public class UserMode {
	private boolean isNewOrder = true; // 지금 진행되는 프로세스가 어떤 주문 건의 최초 프로세스인지 확인하기 위한 변수

	static ArrayList<MenuType> menuList = new ArrayList<MenuType>();			// 메뉴 목록
	static ArrayList<Integer> categoryIndexList = new ArrayList<Integer>(); 	// 카테고리 인덱스 번호 리스트: 카테고리 출력 및 카테고리별 메뉴 선택에 활용
	static ArrayList<OrderedMenuType> cart = new ArrayList<OrderedMenuType>();	// 주문 목록
	
	static SalesTotalType salesTotal;														// 통합 매출 내역 처리를 위한 객체
	static ArrayList<SalesDetailType> salesDetailList = new ArrayList<SalesDetailType>();	// 상세 매출 내역 처리를 위한 리스트

	// 객체 생성
	private static UserMode user = new UserMode();

	// 생성자
	private UserMode() { }
	
	// 메소드
	public static UserMode getInstance() {
		return user;
	}

	// 사용자 모드
	/* 메소드명: start
	 * 파라미터: Scanner sc
	 * 		  sc: main()에서 생성한 스캐너 객체
	 * 반환값: boolean
	 * 		 true: 사용자가 주문을 취소했거나 주문이 정상적으로 완료된 경우
	 * 		 false: 결제 프로세스 이후 단계에서 '[B]나가기' 작업이 수행되어 메뉴 카테고리 출력 화면으로 돌아와야 하는 경우
	 * 기능 설명: 사용자 모드를 시작하고, 사용자 모드의 전체 프로세스를 수행하거나 관련 메소드를 호출한다.
	 */
	public boolean start(Scanner sc) {
		// ========================= 주문 프로세스 =========================
		UserOrderProcess orderProcess = new UserOrderProcess(sc);	// 주문 처리를 위한 프로세스 객체
		
		// 어떤 주문 건의 최초 프로세스일 때만 실행
		if(isNewOrder) {
			salesTotal = new SalesTotalType();
			salesTotal.setType(orderProcess.isHereOrGo());	// 3.매장 이용 방법 선택 받기 -> salesTotal 객체에 정보 저장
			System.out.println();
			
			if(!orderProcess.loadMenu()) return true;		// 메뉴 불러오기 - IOException 발생 시 메인으로 이동
			
			isNewOrder = false;								// 최초 프로세스가 끝났음을 명시
		}
		
		ORDER: while(true) {
			// 4. 메뉴 카테고리 목록 출력 및 사용자 입력받기
			System.out.println("주문하실 메뉴가 속한 카테고리의 번호를 입력해주세요.");
			orderProcess.printCategory();			// 4-1.메뉴 카테고리 목록 출력
			System.out.print(" >> ");
			String strCategoryNum = sc.nextLine();	// 4-2.사용자 입력받기
			
			if(strCategoryNum.equals("R") || strCategoryNum.equals("r")) {	// [R]주문취소
				System.out.println("\n[주문이 취소되었습니다.]");
				return true;
			} else if(cart.size() != 0 && (strCategoryNum.equals("P") || strCategoryNum.equals("p"))) {	// [P]결제
				break ORDER;
			}

			// *** 현재 버전에서는 카테고리 개수가 최대 9개 ***
			if(strCategoryNum.length() != 1) {	// 예외처리 - 2글자 이상의 문자열 혹은 2자리 이상의 숫자
				System.out.println("카테고리 번호는 화면에 출력된 카테고리 번호 중 하나의 값으로 입력해주세요.\n\n");
				continue;
			}

			int categoryNum; // 카테고리 번호
			try {
				categoryNum = Integer.parseInt(strCategoryNum);
				
				if(categoryNum < 1 || categoryNum > categoryIndexList.size()) {	// 예외처리 - 없는 카테고리
					System.out.println("카테고리 번호는 화면에 출력된 카테고리 번호 중 하나의 값으로 입력해주세요.\n\n");
					continue;
				}
			} catch(NumberFormatException e) {	// 예외처리 - 숫자, R, P를 제외한 문자
				System.out.println("카테고리 번호는 화면에 출력된 카테고리 번호 중 하나의 값으로 입력해주세요.\n\n");
				continue;
			}

			// 5. 카테고리별 메뉴 출력 및 사용자 입력받기
			int menuRealIndex; // 어떤 메뉴의 실제 메뉴 번호 (!= 카테고리에서 메뉴가 등장하는 순서)
			while(true) {
				System.out.println("\n주문하실 메뉴의 번호를 입력해주세요.  [B]나가기");
				int max = orderProcess.dealMenuByCategory(categoryNum);	// 5-1.카테고리별 메뉴 출력
				System.out.print(" >> ");
				String strMenuNum = sc.nextLine();	// 5-2.사용자 입력받기
				
				if(strMenuNum.equals("B") || strMenuNum.equals("b")) {	// [B]나가기
					System.out.println("\n[메뉴 카테고리 선택 화면으로 이동합니다.]\n");
					continue ORDER;
				}
				
				int menuNum;	// 메뉴 번호
				try {
					menuNum = Integer.parseInt(strMenuNum);
					
					if(menuNum < 1 || menuNum > max) { // 예외처리 - 올바르지 않은 값
						System.out.println("메뉴 번호는 화면에 출력된 메뉴 번호 중 하나의 값으로 입력해주세요.\n");
						continue;
					}
				} catch(NumberFormatException e) {	// 예외처리 - 올바르지 않은 값
					System.out.println("메뉴 번호는 화면에 출력된 메뉴 번호 중 하나의 값으로 입력해주세요.\n");
					continue;
				}

				menuRealIndex = categoryIndexList.get(categoryNum - 1) + menuNum - 1;	// 카테고리 내 첫번째 메뉴의 인텍스 값 + 사용자가 입력한 메뉴 번호 - 1
				
				if(menuList.get(menuRealIndex).getStock() == 0) { // 예외처리 - 재고가 0이라 번호를 표시하지 않은 메뉴 선택
					System.out.println("해당 메뉴는 현재 주문하실 수 없습니다.");
					System.out.println("메뉴 번호를 다시 입력해주세요.\n");
				} else { break;	}
				
			} // end of while: 5
			
			// 6.주문 수량 입력받기
			int orderQuantity = orderProcess.scanOrderQuantity(menuRealIndex);
			if(orderQuantity == 0) continue ORDER;	// 메뉴 선택 취소
			
			// 7.주문 목록에 메뉴 추가
			cart.add(new OrderedMenuType(menuRealIndex, menuList.get(menuRealIndex), orderQuantity));
			menuList.get(menuRealIndex).updateUserMenuStock(orderQuantity);	// 주문한 수량만큼 재고를 감소시킴
			
			while(true) {
				if(cart.size() == 0) {	// [C]수량 변경 작업으로 모든 메뉴가 삭제되어 주문목록에 메뉴가 없어진 경우 주문목록 출력X
					System.out.println("[출력할 주문 목록이 없어 메뉴 카테고리 선택 화면으로 이동합니다.]\n");
					continue ORDER;
				}
				
				orderProcess.printCart();	// 8.주문 목록 출력
				
				// 9-1.추가 옵션(추가 주문, 수량 변경), 결제 출력
				if(OrderedMenuType.getTOTAL_QUANTITY() < 50) // 현재 총 주문 수량이 50보다 작은 경우에만 추가 주문 옵션 제공
					System.out.print("[A]추가 주문  ");
				System.out.println("[C]수량 변경  [P]결제");
				System.out.print(" >> ");
				String strSelectTask = sc.nextLine();	// 9-2.사용자 입력받기

				if( (OrderedMenuType.getTOTAL_QUANTITY() < 50) && (strSelectTask.equals("A") || strSelectTask.equals("a"))) {	// [A]추가 주문
					System.out.println();
					System.out.print("추가 ");
					continue ORDER;

				} else if(strSelectTask.equals("C") || strSelectTask.equals("c")) {	// [C]수량 변경
					int orderMenuIndex = 0;
					
					System.out.println();
					while(true) {
						System.out.println("주문 목록에서 수량을 변경할 메뉴의 번호를 입력하세요."); // 9-C-1.수량을 변경할 메뉴 번호 입력 받기
						System.out.print(" >> ");
						
						try {
							orderMenuIndex = sc.nextInt();
							sc.nextLine();
							
							if(orderMenuIndex > 0 && orderMenuIndex <= cart.size()) break;
							
							System.out.println("올바른 메뉴 번호를 입력해주세요.\n");

						} catch (InputMismatchException e) {
							sc.nextLine();
							System.out.println("올바른 메뉴 번호를 입력해주세요.\n");
						}
						
						orderProcess.printCart();
					}
					
					orderMenuIndex -= 1;
					System.out.println();
					
					int changeQuantity;
					while(true) {
						System.out.println("변경할 수량을 입력하세요.  [0]메뉴 삭제  [기존 수량]변경 취소");	// 9-C-2.변경할 수량 입력 받기
						System.out.print(" >> ");
						
						try {
							changeQuantity = sc.nextInt();
							sc.nextLine();
							
							// 예외처리 - 음수
							if(changeQuantity < 0) System.out.println("변경할 수량은 0 이상의 정수로 입력해주세요.\n");
							else break;
							
						} catch(InputMismatchException e) {	// 예외처리 - 실수, 문자
							sc.nextLine();
							System.out.println("변경할 수량은 0 이상의 정수로 입력해주세요.\n");
						}
						
						orderProcess.printCart();
					}
					// 9-C-3.주문 수량 변경 처리
					orderProcess.changeOrderedMenuQuantity(orderMenuIndex, changeQuantity);
					
				} else if((strSelectTask.equals("P") || strSelectTask.equals("p"))) {	// [P]결제
					break ORDER;
					
				} else {
					System.out.println("입력값이 올바르지 않습니다.");
					System.out.println("다시 입력해주세요.\n");
				}
			} // end of while: 8~9
		} // end of while: Order
		
		
		// ========================= 결제 프로세스 =========================
		System.out.println("\n[결제 화면으로 이동합니다.]\n");
		UserPayProcess payProcess = new UserPayProcess(sc);

		// 10.결제 수단 입력받기
		int selectPayMethod;
		while(true) {
			System.out.println("결제 수단의 번호를 입력해주세요.");	// 10-1.결제 수단 목록 출력
			System.out.println("[1]신용카드  [2]간편결제-바코드  [B]결제 취소");
			System.out.print(" >> ");
			String strSelectPayMethod = sc.nextLine();	// 10-2.결제 수단 입력 받기
			
			if(strSelectPayMethod.equals("B") || strSelectPayMethod.equals("b")) {
				System.out.println("\n[메뉴 카테고리 선택 화면으로 이동합니다.]\n");
				return false;
			}
			
			if(!strSelectPayMethod.equals("1") && !strSelectPayMethod.equals("2")) { // 예외처리 - 1, 2 외의 값
				System.out.println("화면에 출력된 결제 수단의 번호 중 하나의 값으로 입력해주세요.\n\n");
				continue;
			}
			else {
				selectPayMethod = Integer.parseInt(strSelectPayMethod);
				break;
			}
		}

		// 11.포인트/쿠폰 사용 여부에 따른 결제 금액 차감
		int orgTotalAmt = OrderedMenuType.getTOTAL_AMT();	// 포인트/쿠폰 적용 전 결제 금액
		char discountType = 'N';			// 할인 종류(N: 할인 없음, P: 포인트, C: 쿠폰)
		String discountName = "";			// 할인 내용(포인트 사용, 쿠폰 할인)
		boolean isFullDiscount = false;		// 전액 할인 여부 (true: 전액 할인, false: 할인 없음/일부 할인)
		int discountAmt = 0;				// 할인 금액
		
		while(true) {
			discountAmt = 0;
			
			System.out.println("\n포인트/쿠폰을 사용하시겠습니까?");
			System.out.println("[1]포인트  [2]쿠폰  [3]미사용");
			System.out.print(" >> ");
			String strDiscountOption = sc.nextLine();	// 11-1.포인트/쿠폰 사용 여부 입력 받기
			
			if(strDiscountOption.equals("1")) { // 포인트 사용
				discountAmt = payProcess.usePoint(orgTotalAmt);	// 11-2-1.회원 ID 입력 받기, 11-3-1.회원 ID 조회, 11-4-1.포인트 사용 금액 입력 받기
				
				// 오류 (회원 ID 오류, Exception, 포인트 100p 미만, 회원 아님) & 포인트 사용 취소
				if(discountAmt == -1 || discountAmt == 0) continue;
				
				discountType = 'P';
				discountName = "포인트 사용";
				
				if(discountAmt == orgTotalAmt) isFullDiscount = true;	// 전액 포인트 사용
				break;
				
			} else if(strDiscountOption.equals("2")) { // 쿠폰 사용 {
				discountAmt = payProcess.useCoupon(orgTotalAmt); // 11-2-2.회원 ID 입력 받기, 11-3-2.회원 ID 조회, 11-4-2.포인트 사용 금액 입력 받기
				
				// 오류 (쿠폰 ID 오류, Exception, 사용 완료, 발급이력 없음) & 쿠폰 사용 취소
				if(discountAmt == -1 || discountAmt == 0) continue;
				
				discountType = 'C';
				discountName = "쿠폰 할인";
				if(discountAmt == orgTotalAmt) isFullDiscount = true;	// 전액 포인트 사용
				break;
				
			} else if(strDiscountOption.equals("3")) {	// 미사용
				break;
				
			} else {
				System.out.println("입력값이 올바르지 않습니다. [1], [2], [3]중 하나의 값으로 입력해주시기 바랍니다.\n");
			}
		}

		String discountId = salesTotal.getDiscountId();
		if(!isFullDiscount) salesTotal.setDiscountId(" ");	// 전액 포인트/쿠폰을 사용한 경우에만 discountId 정보를 유지

		// 12.주문 정보 출력
		System.out.println("\n주문정보를 확인해주세요.");
		payProcess.printOrderSheet(discountName, discountAmt); // 12-1.주문 정보 내용 출력
		
		while(true) {
			System.out.println("\n결제 진행 여부를 입력해주세요.");
			System.out.println("[1]결제 진행  [2]결제 취소");
			System.out.print(" >> ");
			
			int keepPay;
			try {
				keepPay = sc.nextInt(); // 12-2.결제 진행 여부 입력받기
				sc.nextLine();
			} catch(InputMismatchException e) {
				sc.nextLine();
				System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
				continue;
			}

			if(keepPay == 1) break; // [1]진행
			else if(keepPay == 2) {	// [2]취소
				System.out.println("\n[결제가 취소되었습니다.]");
				System.out.println("[메뉴 카테고리 선택 화면으로 이동합니다.]\n");
				return false;
			}
			
			System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
		}
		
		// 13.결제 금액이 0원이 아닌지 체크
		int newTotalAmt = orgTotalAmt - discountAmt;	// 최종 결제 금액
		
		if(newTotalAmt == 0) {	// 13-1.결제 금액이 0원 -> 승인 프로세스 건너 뜀
			Date today = new Date();
			Locale currentLocale = new Locale("KOREAN", "KOREA");
			String pattern = "yyyyMMddHHmmss";
			SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
			String transDt = formatter.format(today);	// 오늘 날짜로 거래일자 세팅
			
			salesTotal.setTransDt(transDt);
			salesTotal.setTotalAmt(newTotalAmt);
			
			if(selectPayMethod == 1) salesTotal.setPayMethod("신용카드");
			else if(selectPayMethod == 2) salesTotal.setPayMethod("간편결제");
		}
		
		if(newTotalAmt != 0) {	// 13-2.결제 금액이 0원이 아님 -> 승인 프로세스 진행
			// 14.결제 수단 정보, 할부 개월 입력받기
			String payMethod = (selectPayMethod == 1)? "카드 번호": "바코드 번호";	// selectPayMethod의 값이 '1'이면 신용카드, '2'이면 간편결제-바코드
			int checkLength = (selectPayMethod == 1)? 16: 24;					// 국내카드 기준 카드번호 16자리 / 카카오페이, 제로페이 기준 바코드 번호 24자리
			
			String payMethodNo = "";	// 카드번호 or 바코드 번호
			int cardQuota = 0;			// 할부 개월
			
			while(true) {
				System.out.printf("\n%s(%d자리)를 입력해주세요. ([B]결제 취소)\n", payMethod, checkLength);
				System.out.print(" >> ");
				payMethodNo = sc.nextLine();	// 14-1. 카드/바코드 번호 입력받기
								
				if(payMethodNo.equals("B") || payMethodNo.equals("b")) {
					System.out.println("\n[결제가 취소되었습니다.]");
					System.out.println("[메뉴 카테고리 선택 화면으로 이동합니다.]\n");
					return false;
				}

				// 길이가 맞지 않거나 숫자 외의 값이 입력된 경우
				if(payMethodNo.length() != checkLength || !payProcess.isDigitNo(payMethodNo)) {
					System.out.printf("%s가 올바르지 않습니다.\n", payMethod);
					System.out.println("다시 입력해주세요.\n");
					continue;
				}
				
				break;
			}
			
			if(newTotalAmt >= 50000) {	// 14-2. 할부 개월 입력받기 (결제 금액이 5만원 이상일 때)
				while(true) {
					System.out.println("\n할부개월을 입력해주세요.");
					System.out.println("[1]일시불  [2]2개월  [3]3개월  [B]결제 취소");
					System.out.print(" >> ");
					
					String strCardQuota = sc.nextLine();
					
					if(strCardQuota.equals("B") || strCardQuota.equals("b")) {
						System.out.println("\n[결제가 취소되었습니다.]");
						System.out.println("[메뉴 카테고리 선택 화면으로 이동합니다.]\n");
						return false;
					}
					
					if(!strCardQuota.equals("1") && !strCardQuota.equals("2") && !strCardQuota.equals("3")) {
						System.out.println("입력값이 올바르지 않습니다. [1], [2], [3], [B]중 하나의 값으로 입력해주시기 바랍니다.\n");
						continue;
					}
					
					cardQuota = Integer.parseInt(strCardQuota);
					break;
				}
			}
			if(cardQuota == 1) cardQuota = 0;	// 일시불의 할부개월은 0으로 처리

			// 15.승인 요청 및 승인 응답 수신
			try {
				String msg = "\n승인 요청 중...\n잠시만 기다려 주세요...\n";
				
				for(int i=0; i<msg.length(); i++) {
					System.out.print(msg.charAt(i));
					TimeUnit.MILLISECONDS.sleep(200);
				}
				TimeUnit.MILLISECONDS.sleep(300);
	
			} catch (InterruptedException e) {
				return false;
			}
			
			// 16.결제 성공 여부 체크
			if(!payProcess.goPay(payMethodNo, newTotalAmt, cardQuota)) {
				System.out.println("\n[승인 요청에 실패하였습니다.]");
				System.out.println("[메뉴 카테고리 선택 화면으로 이동합니다.]\n");
				return false; // 승인 실패 시 재시도가 가능하도록 카테고리로 돌아감
			}
		}
		
		//System.out.println("\n[승인이 완료되었습니다.]\n");

		
		// ========================= 포인트 적립 프로세스 =========================
		UserPointEarningProcess pointEarningProcess = new UserPointEarningProcess(sc);

		int pointEarnType = 0;
		String birth = "0000";

		if(discountType == 'N') { // 17.포인트/쿠폰 사용 여부 체크
			while(true) {
				// 18.포인트 적립 여부에 따라 휴대전화 번호 입력 받기
				System.out.println("\n포인트를 적립 여부를 입력해 주세요.");
				System.out.println("[1]적립  [2]미적립");
				System.out.print(" >> ");
				
				try {
					pointEarnType = sc.nextInt();	// 18-1.포인트 적립 여부 입력 받기
					sc.nextLine();
				} catch(InputMismatchException e) {	// 예외 처리 - 정수 이외의 값
					sc.nextLine();
					System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
					continue;
				}
				
				if(pointEarnType != 1 && pointEarnType != 2) {	// 1, 2 이외의 값
					System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
					continue;
				}
				
				if(pointEarnType == 1) {
					birth = pointEarningProcess.requestMemberInfo(); // 18-2.휴대전화 번호 입력 받기 (포인트를 적립하는 경우), 19.비회원과 생일 정보가 없는 회원에 대해 생일 정보 입력 받기
					
					if(birth.equals("")) {
						continue;
					}
					
					int rewardPts = (int)(newTotalAmt * UserPointEarningProcess.POINT_RATE);	// 적립할 포인트
					salesTotal.setRewardPts(rewardPts);

					int newTotalPts = rewardPts + salesTotal.getTotalPts();
					salesTotal.setTotalPts(newTotalPts);
					
					break;
				}
			}
		}
		
		
		// ========================= 데이터 처리 프로세스 =========================
		// 주문 및 결제가 완료된 상품들을 상세 매출 내역 데이터 처리를 위한 리스트에 올림
		for(OrderedMenuType orderedMenu : cart) {
			String orderNo = salesTotal.getOrderNo();
			String type = "M";
			String id = orderedMenu.getId();
			String name = orderedMenu.getName();
			int price = orderedMenu.getPrice();
			int quantity = orderedMenu.getQuantity();
			salesDetailList.add(new SalesDetailType(orderNo, type, id, name, price, quantity));
		}
		
		if(discountType == 'P')
			salesDetailList.add(new SalesDetailType(salesTotal.getOrderNo(), discountType + "", discountId, "포인트 사용", -discountAmt, 1));
		else if(discountType == 'C')
			salesDetailList.add(new SalesDetailType(salesTotal.getOrderNo(), discountType + "", discountId, "쿠폰 할인", -discountAmt, 1));
		
		UserUpdateDataProcess updateDataProcess = new UserUpdateDataProcess();

		// 20.데이터 처리
		updateDataProcess.updateMenuStock();	// 20-1. 메뉴별 재고 업데이트
		
		// 20-2. 포인트/쿠폰 사용 여부에 따른 포인트 금액/쿠폰 사용 여부 업데이트
		if(discountType == 'P')
			updateDataProcess.updateMemberPoint(discountId, discountAmt);
		else if (discountType == 'C')
			updateDataProcess.updateCouponUsed(discountId);
		
		if(pointEarnType == 1)	// 20-3. 회원 정보 업데이트 (적립 포인트, 생일 정보)
			updateDataProcess.updateMember(salesTotal.getMemberId(), salesTotal.getTotalPts(), birth);
		
		updateDataProcess.updateSalesTotal();	// 20-4. 통합 매출 업데이트
		updateDataProcess.updateSalesDetail();	// 20-5. 상세 매출 업데이트
		
		System.out.println("\n[주문이 완료되었습니다.]");
		
		
		// ========================= 대기번호표/영수증 출력 =========================
		UserPrintConfirmationProcess printConfirmation = new UserPrintConfirmationProcess();
		
		int selectReceipt;
		while(true) {
			System.out.println("\n영수증을 출력 여부를 입력해주세요.");
			System.out.println("[1]출력  [2]미출력");
			System.out.print(" >> ");	// 21.영수증 출력 여부 입력 받기
			
			try {
				selectReceipt = sc.nextInt();
				sc.nextLine();
				
				if(selectReceipt != 1 && selectReceipt != 2)
					System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
				else break;
				
			} catch(InputMismatchException e) {	// 예외처리 - 정수 이외의 값
				sc.nextLine();
				System.out.println("입력값이 올바르지 않습니다. [1] 또는 [2]로 입력해주시기 바랍니다.\n");
			}
		}
			
		printConfirmation.printExchangeTicket();
		if(selectReceipt == 1) printConfirmation.printWithReceipt();
		System.out.println();
		
		return true;
	}

	// 22.주문 목록 초기화
	/* 메소드명: reset
	 * 파라미터: 없음
	 * 반환값: boolean
	 * 		 true: 주문 정보가 정상적으로 삭제된 경우
	 * 		 false: 주문 정보의 삭제가 비정상적으로 종료된 경우
	 * 기능 설명: 취소되었거나 완료된 주문 건에 대한 정보(ex. 주문 목록)를 초기화한다.
	 */
	public boolean reset() {
		if (cart.size() != 0)
			OrderedMenuType.reset(); // 총 주문 수량, 총 금액 초기화

		menuList.clear();
		categoryIndexList.clear();
		cart.clear();
		salesTotal = null;
		salesDetailList.clear();

		isNewOrder = true;

		try {
			System.out.println("[잠시 후 메인 화면으로 이동합니다.]\n\n");
			TimeUnit.MILLISECONDS.sleep(600);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}

}