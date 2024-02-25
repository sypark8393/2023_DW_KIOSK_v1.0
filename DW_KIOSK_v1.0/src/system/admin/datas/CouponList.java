package system.admin.datas;

import java.io.*;
import java.util.*;

import system.datas.CouponType;

public class CouponList {
	private Scanner sc = null;
	
	//생성자
	public CouponList(Scanner sc) {
		this.sc=sc;
	}
	
	/* 메소드명: printWholeCoupon
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: CouponType 클래스에 지정된 경로에 존재하는 src//files//datas//coupon.txt파일의 내용 전체 출력
	 */
	public void printWholeCoupon() {
		ArrayList<CouponType> couponList=new ArrayList<CouponType>();//CouponType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(CouponType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		
		//파일을 읽어올 수 있는동안
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				CouponType coupon = new CouponType(tmp[CouponType.INDEX_COUPON_ID], Integer.parseInt(tmp[CouponType.INDEX_PRICE]), tmp[CouponType.INDEX_USED]);

				couponList.add(coupon);
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		//출력
		System.out.println("현재 저장되어 있는 메뉴 리스트를 출력합니다.");
		System.out.println("====================================================");
		System.out.println("	  쿠폰ID		        쿠폰금액        사용여부");
		System.out.println("====================================================");
		for (int i = 0; i < couponList.size(); i++) {
			couponList.get(i).printAdminCoupon();
		}
		System.out.println("====================================================");
		
	}
	
	/* 메소드명: searchCouponById
	 * 파라미터: CouponType.INDEX_COUPON_ID, searchedId
	 * 반환값: 없음
	 * 기능 설명: CouponType 클래스에 지정된 경로에 존재하는 src//files//datas//coupon.txt파일의 내용 중
	 * 		   searchedId와 CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록 출력
	 */
	public void searchCouponById(int index, String keyword) {
		ArrayList<CouponType> couponList=new ArrayList<CouponType>();//CouponType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;//searchedId와 CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록을 발견했는지 확인할 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(CouponType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[CouponType.INDEX_COUPON_ID].equals(keyword)) {//searchedId와 CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록을 발견
					found=true;
					CouponType coupon = new CouponType(tmp[CouponType.INDEX_COUPON_ID], Integer.parseInt(tmp[CouponType.INDEX_PRICE]), tmp[CouponType.INDEX_USED]);

					couponList.add(coupon);
				}

			}
			in.close();
			if (found==false) {//searchedId와 CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록을 발견하지 못하면
				System.out.printf("쿠폰 ID가 %s인 쿠폰을 찾을 수 없습니다.\n",keyword);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {//searchedId와 CouponType.INDEX_COUPON_ID가 동일한 쿠폰 목록을 발견했으면 출력 시작
			System.out.printf("쿠폰 ID가 %s인 메뉴 목록입니다.\n",keyword);
			System.out.println("====================================================");
			System.out.println("	  쿠폰ID		        쿠폰금액        사용여부");
			System.out.println("====================================================");
			for (int i = 0; i < couponList.size(); i++) {
				couponList.get(i).printAdminCoupon();
			}
			System.out.println("====================================================");
		}
	}
	
	/* 메소드명: searchCouponByPrice
	 * 파라미터: CouponType.INDEX_PRICE,searchedPrice
	 * 반환값: 없음
	 * 기능 설명: CouponType 클래스에 지정된 경로에 존재하는 src//files//datas//coupon.txt파일의 내용 중
	 * 		   searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록 출력
	 */
	public void searchCouponByPrice(int index,int keyword) {
		ArrayList<CouponType> couponList=new ArrayList<CouponType>();//CouponType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;//searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록을 발견했는지 확인할 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(CouponType.FILE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				int PriceInt = Integer.parseInt(tmp[CouponType.INDEX_PRICE]);

				if (keyword == PriceInt) {//searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록을 발견
					found=true;
					CouponType coupon = new CouponType(tmp[CouponType.INDEX_COUPON_ID], Integer.parseInt(tmp[CouponType.INDEX_PRICE]), tmp[CouponType.INDEX_USED]);

					couponList.add(coupon);
				}
			}
			in.close();
			if (found==false) {//searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록을 발견하지 못하면
				System.out.printf("쿠폰 금액이 %d인 쿠폰을 찾을 수 없습니다.\n",keyword);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {//searchedPrice와 CouponType.INDEX_PRICE가 동일한 쿠폰 목록을 발견했으면 출력 시작
			System.out.printf("현재 쿠폰 금액이 %s인 목록입니다.\n",keyword);
			System.out.println("====================================================");
			System.out.println("	  쿠폰ID		        쿠폰금액        사용여부");
			System.out.println("====================================================");
			for (int i = 0; i < couponList.size(); i++) {
				couponList.get(i).printAdminCoupon();
			}
			System.out.println("====================================================");
		}
	}
	
	/* 메소드명: searchCouponByUsed
	 * 파라미터: CouponType.INDEX_USED, searchedUsed
	 * 반환값: 없음
	 * 기능 설명: CouponType 클래스에 지정된 경로에 존재하는 src//files//datas//coupon.txt파일의 내용 중
	 * 		   searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 출력
	 */
	public void searchCouponByUsed(int index,String keyword) {
		ArrayList<CouponType> couponList=new ArrayList<CouponType>();//CouponType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;// searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 발견했는지 확인할 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(CouponType.FILE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[CouponType.INDEX_USED].equals(keyword)) {// searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 발견
					found=true;
					CouponType coupon = new CouponType(tmp[CouponType.INDEX_COUPON_ID], Integer.parseInt(tmp[CouponType.INDEX_PRICE]), tmp[CouponType.INDEX_USED]);

					couponList.add(coupon);
				}
			}
			in.close();
			if (found==false) {// searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 발견하지 못하면
				System.out.printf("사용 여부가 %s인 쿠폰을 찾을 수 없습니다.\n",keyword);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {// searchedUsed와 CouponType.INDEX_USED가 동일한 쿠폰 목록 발견했으면 출력 시작
			System.out.printf("현재 사용 여부가 %s인 목록입니다.\n",keyword);
			System.out.println("====================================================");
			System.out.println("	  쿠폰ID		        쿠폰금액        사용여부");
			System.out.println("====================================================");
			for (int i = 0; i < couponList.size(); i++) {
				couponList.get(i).printAdminCoupon();
			}
			System.out.println("====================================================");
		}
	}

}