package system.user;

import java.io.*;
import system.datas.*;

public class UserUpdateDataProcess {

	// 22-1. 메뉴 재고 업데이트
	/* 메소드명: updateMenuStock
	 * 파라미터: 없음
	 * 반환값: boolean
	 * 		 true: 정상적으로 메뉴 재고가 업데이트 된 경우
	 * 		 false: 메뉴 재고 업데이트에 실패한 경우
	 * 기능 설명: 사용자의 주문 처리를 통해 변동된 재고 정보를 이용하여 메뉴 재고를 업데이트한다.
	 */
	public boolean updateMenuStock() {
		PrintWriter out;	// 출력 스트림
		
		try {
			out = new PrintWriter(new FileWriter(MenuType.FILE_NAME));	// 출력 스트림 생성
			
			for(MenuType menu : UserMode.menuList) {
				out.println(menu.toDataTemplate());
			}
			
			out.close(); // 스트림 닫기
						
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return false;
			
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생했습니다.");
			return false;
		}

		return true;
	}
	
	// 22-2-1. 사용한 포인트 업데이트
	/* 메소드명: updateMemberPoint
	 * 파라미터: String discountId, int discountAmt
	 * 		  discountId: 포인트를 사용한 회원 ID
	 * 		  discountID: 포인트 사용 금액
	 * 반환값: boolean
	 * 		 true: 정상적으로 회원 포인트 업데이트에 성공한 경우
	 * 		 false: 회원 포인트 업데이트에 실패한 경우
	 * 기능 설명: 포인트 사용 시, 포인트를 사용한 회원 ID와 사용한 포인트 금액을 받아 회원 포인트 정보를 업데이트한다.
	 */
	public boolean updateMemberPoint(String discountId, int discountAmt) {
		BufferedReader in;	// 입력 스트림
		PrintWriter out;	// 출력 스트림
		
		try {
			in = new BufferedReader(new FileReader(MemberType.FILE_NAME));
			out = new PrintWriter(new FileWriter(MemberType.TMP_FILE_NAME));
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("회원 파일을 찾을 수 없습니다.");
			return false;
			
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생했습니다.");
			return false;
		}
		
		String s;
		String[] tmp;
		
		//파일을 읽어올 수 있는동안
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if(tmp[MemberType.INDEX_MEMBER_ID].equals(discountId)) {
					
					int newPointAmt = Integer.parseInt(tmp[MemberType.INDEX_POINT]) - discountAmt;
					tmp[MemberType.INDEX_POINT] = newPointAmt + "";

					s = String.join("|", tmp);
				}
				out.println(s);
			}
			
			// 스트림 닫기
			in.close();
			out.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return false;
		}
		
		// 구버전 파일 삭제
		File file = new File(MemberType.FILE_NAME);
		file.delete();

		// 새로운 파일명을 설정
		file = new File(MemberType.TMP_FILE_NAME);
		file.renameTo(new File(MemberType.FILE_NAME));
		
		return true;
	}
	
	// 22-2-2. 사용한 쿠폰 업데이트
	/* 메소드명: updateCouponused
	 * 파라미터: String discountId
	 * 		  discountId: 사용한 쿠폰 ID
	 * 반환값: boolean
	 * 		 true: 정상적으로 쿠폰 사용 여부 업데이트에 성공한 경우
	 * 		 false: 쿠폰 사용 여부 업데이트에 실패한 경우
	 * 기능 설명: 쿠폰 사용 시, 사용한 쿠폰 ID를 받아 쿠폰 사용 여부를 업데이트한다.
	 */
	public boolean updateCouponUsed(String discountId) {
		BufferedReader in;	// 입력 스트림
		PrintWriter out;	// 출력 스트림
		
		try {
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));
			out = new PrintWriter(new FileWriter(CouponType.TMP_FILE_NAME));
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("쿠폰 파일을 찾을 수 없습니다.");
			return false;
			
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생했습니다.");
			return false;
		}
		
		String s;
		String[] tmp;
		
		//파일을 읽어올 수 있는동안
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리
				
				if(tmp[CouponType.INDEX_COUPON_ID].equals(discountId)) {
					tmp[CouponType.INDEX_USED] = "Y";
					
					s = String.join("|", tmp);
				}
				out.println(s);
			}
			
			// 스트림 닫기
			in.close();
			out.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return false;
		}
		
		// 구버전 파일 삭제
		File file = new File(CouponType.FILE_NAME);
		file.delete();

		// 새로운 파일명을 설정
		file = new File(CouponType.TMP_FILE_NAME);
		file.renameTo(new File(CouponType.FILE_NAME));
		
		return true;
	}
	
	// 22-3. 회원 정보 업데이트 (포인트 적립, 생일 정보 업데이트)
	/* 메소드명: updateMember
	 * 파라미터: String memberId, int point, String birth
	 * 		  memberId: 회원 ID
	 * 		  point: 누적 포인트 금액
	 * 		  birth: 생일 정보
	 * 반환값: boolean
	 * 		 true: 정상적으로 회원 정보 업데이트에 성공한 경우
	 * 		 false: 회원 정보 업데이트에 실패한 경우
	 * 기능 설명: 포인트 적립을 선택한 경우 회원 ID, 누적 포인트 금액, 생일 정보를 받아 회원 정보를 업데이트한다.
	 */
	public boolean updateMember(String memberId, int point, String birth) {
		BufferedReader in;	// 입력 스트림
		PrintWriter out;	// 출력 스트림
		
		String s;
		String[] tmp;
		boolean isUpdate = false;
		
		try {
			in = new BufferedReader(new FileReader(MemberType.FILE_NAME));
			out = new PrintWriter(new FileWriter(MemberType.TMP_FILE_NAME));
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("회원 파일을 찾을 수 없습니다.");
			return false;
			
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생했습니다.");
			return false;
		}
		
		//파일을 읽어올 수 있는동안
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if(tmp[MemberType.INDEX_MEMBER_ID].equals(memberId)) {
					isUpdate = true;
					
					tmp[MemberType.INDEX_POINT] = point + "";
					tmp[MemberType.INDEX_BIRTH] = birth;
					s = String.join("|", tmp);
				}
				out.println(s);
			}
			
			// 신규 등록 케이스
			if(!isUpdate) out.println(memberId + "|" + point + "|" + birth);
			
			// 스트림 닫기
			in.close();
			out.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return false;
		}
		
		// 구버전 파일 삭제
		File file = new File(MemberType.FILE_NAME);
		file.delete();

		// 새로운 파일명을 설정
		file = new File(MemberType.TMP_FILE_NAME);
		file.renameTo(new File(MemberType.FILE_NAME));

		return true;
	}
	
	// 22-4. 통합 매출 내역 업데이트
	/* 메소드명: updateSalesTotal
	 * 파라미터: 없음
	 * 반환값: boolean
	 * 		 true: 통합 매출 내역 작성에 성공한 경우
	 * 		 false: 통합 매출 내역 작성에 실패한 경우
	 * 기능 설명: 주문이 완료된 거래 건에 대한 통합 매출 정보(주문 번호, 거래 시간, 거래 금액 등)을 통합 매출 내역 파일에 작성한다.
	 */
	public boolean updateSalesTotal() {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(UserMode.salesTotal.getFileName(), true));
			out.println(UserMode.salesTotal.toDataTemplate());
			out.close();
			
			//System.out.println(UserMode.salesTotal.toDataTemplate());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// 22-5. 상세 매출 내역 업데이트
	/* 메소드명: updateSalesDetail
	 * 파라미터: 없음
	 * 반환값: boolean
	 * 		 true: 상세 매출 내역 작성에 성공한 경우
	 * 		 false: 상세 매출 내역 작성에 실패한 경우
	 * 기능 설명: 주문이 완료된 거래 건에 대한 상세 매출 정보(주문 상품, 상품별 수량 등)을 상세 매출 내역 파일에 작성한다.
	 */
	public boolean updateSalesDetail() {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(UserMode.salesDetailList.get(0).getFileName(), true));
			
			for(SalesDetailType salesDetail : UserMode.salesDetailList) {
				out.println(salesDetail.toDataTemplate());
				
				//System.out.println(salesDetail.toDataTemplate());
			}

			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
