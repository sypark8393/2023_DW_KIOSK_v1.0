package system.user;

import java.io.*;
import java.util.Scanner;

import system.datas.MemberType;

public class UserPointEarningProcess {
	
	public static final int[] LAST_DAY_OF_MONTH = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; // 월별 마지막 날짜
	public static final double POINT_RATE = 0.01; // 포인트 적립 백분율
	
	private Scanner sc;	// 스캐너 객체
	
	// 생성자
	public UserPointEarningProcess(Scanner sc) {
		this.sc = sc;
	}

	// 18-2.휴대전화 번호 입력 받기 (포인트를 적립하는 경우)
	// 19.비회원과 생일 정보가 없는 회원에 대해 생일 정보 입력 받기
	/* 메소드명: requestMemberInfo
	 * 파라미터: 없음
	 * 반환값: String birth
	 * 		 birth: 회원의 생일 정보
	 * 기능 설명: 포인트를 적립할 회원의 ID(휴대전화 번호)를 입력받은 후,
	 * 		   비회원이거나 생일 정보가 없는 회원에 한해 생일 정보를 입력받아 반환한다.
	 */
	public String requestMemberInfo() {
		String memberId = " ";	// 회원 ID (휴대전화 번호)
		String birth;			// 생일 정보
		int totalPts = 0;		// 현재 누적 포인트
		boolean isAllDigit;		// 8자리 모두 숫자인 경우에 true
		
		System.out.println();
		while(true) {
			isAllDigit = true;
			
			System.out.println("포인트를 적립하실 휴대전화 번호를 입력해 주세요. (ex.01012345678)");
			System.out.print(" >> 010");
			memberId = sc.nextLine();
			
			// 휴대전화 번호가 8(010 제외)자리가 아닌 경우
			if(memberId.length() != 8) {
				System.out.println("휴대전화 번호는 중간번호와 뒷번호를 하이픈(-)을 제외한 8자리로 입력해주세요.\n\n");
				continue;
			}
			
			for(int i=0; i<8; i++) {
				char c = memberId.charAt(i);
				if(!Character.isDigit(c)) {
					System.out.println("휴대전화 번호는 숫자만 입력해 주세요.\n\n");
					isAllDigit = false;
					break;
				}
			}
			
			if(!isAllDigit) continue;
			
			memberId = "010" + memberId;
			UserMode.salesTotal.setMemberId(memberId);
			break;
		} // end of while: 휴대전화 번호 입력받기	
	
	
		// 19-1.회원 여부 및 생일 정보 유무 체크
		String memberInfo = checkMemberInfo(memberId);
		
		if(memberInfo.equals("Exception"))  return "";

		if(memberInfo.equals("")) totalPts = 0;	// 회원이 아니면 현재 누적 포인트는 0p
		else { // 회원이면 포인트 정보 가져옴
			String[] tmp = memberInfo.split("\\|");
			totalPts = Integer.parseInt(tmp[MemberType.INDEX_POINT]);
		}

		birth = returnBirth(memberInfo);
		if(birth.equals("0000")) { // 생일 정보가 없으면 -> 받아야 됨
			System.out.println();
			
			while(true) {
				System.out.println("마케팅 정보 수신에 동의하시는 경우 생일을 입력해주세요. (ex. 0101)");
				System.out.println("[미동의] 입력 시, 각종 이벤트 및 이용자 맞춤형 상품 추천 등의 서비스 제공이 제한될 수 있습니다.");
				System.out.print(" >> ");
				birth = sc.nextLine();	// 생일 정보 입력받기
				
				if(birth.equals("미동의")) {
					birth = "0000";
					break;
				}
				
				if(birth.length() != 4) {	// MMDD 형식 X
					System.out.println("생일은 월 2자리, 일 2자리를 합쳐 4자리로 입력해주세요.\n\n");
					continue;
					
				} else if(!isValidBirth(birth)) {	// 20-1. 생일 유효성 검증
					System.out.println("유효하지 않은 날짜입니다.");
					System.out.println("다시 입력해주세요.\n\n");
					continue;
					
				} else break;	// 올바른 생일 정보
			}
		}

		UserMode.salesTotal.setMemberId(memberId);
		UserMode.salesTotal.setTotalPts(totalPts);
		
		return birth;
	}
	
	// 19-1-1.회원 여부 체크
	/* 메소드명: checkMemberInfo
	 * 파라미터: String memberId
	 * 		  memberId: 회원 ID(휴대전화 번호)
	 * 반환값: String s
	 * 		 s: member.txt에서 회원 ID로 조회된 한 줄의 내용
	 * 기능 설명: 파라미터로 받은 회원 ID로 회원을 조회하여, 조회된 내용이 있을 때 그 내용을 반환한다.
	 */
	private String checkMemberInfo(String memberId) {
		BufferedReader in;	// 입력 스트림
		
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MemberType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 메뉴 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.println("\n회원 데이터를 불러올 수 없습니다.");
			System.out.println("오류가 지속될 경우 관리자에게 문의해주세요.\n");
			return "Exception";
		}
		
		String s;						// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;					// 구분자로 분리한 문자열이 저장될 임시 배열
		boolean isExist = false;
		
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				// 회원 조회에 성공하면 해당 회원의 정보를 반환
				if(tmp[MemberType.INDEX_MEMBER_ID].equals(memberId)) {
					isExist = true;
					break;
				}
			}
			
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("비정상적인 오류로 포인트 조회가 불가합니다.");
				System.out.println("관리자에게 문의해 주세요.");
				return "Exception";
				
		}
		
		return isExist? s: "";
	}
	
	// 19-2.생일 정보 유무 체크
	/* 메소드명: returnBirth
	 * 파라미터: String memberInfo
	 * 		  memberInfo: member.txt에서 읽어온 회원 정보 한 줄
	 * 반환값: String tmp[MemberType.INDEX_BIRTH]
	 * 		 tmp[MemberType.INDEX_BIRTH]: 생일 정보(4자리)
	 * 기능 설명: 한 줄의 회원 정보를 받아 그 중 생일 정보만 반환한다.
	 */
	private String returnBirth(String memberInfo) {
		String[] tmp;			// 구분자로 분리한 문자열이 저장될 임시 배열
		
		if(memberInfo.equals("")) {
			return "0000";
		}
		
		tmp = memberInfo.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
		
		if(tmp[MemberType.INDEX_BIRTH].equals("0000")) {
			return "0000";
		}

		return tmp[MemberType.INDEX_BIRTH];
	}
	
	// 생일 유효성 검증
	/* 메소드명: isValidBirth
	 * 파라미터: String birth
	 * 		  birth: MMDD 형식으로 작성된 4자리의 생일 정보
	 * 반환값: boolean
	 * 		 true: 실제 존재하는 생일인 경우
	 * 		 false: 존재할 수 없는 생일이거나 숫자 이외의 값이 포함된 경우 (ex.9999)
	 * 기능 설명: 문자열로 작성된 생일 정보를 받아 실제 존재하는 날짜인지 검사한다.
	 */
	private boolean isValidBirth(String birth) {
		try {
			int month = Integer.parseInt(birth.substring(0, 2));	// 월
			int day = Integer.parseInt(birth.substring(2));			// 일
			
			// 입력된 월(month)의 값이 1~12 범주를 벗어나는 경우
			if(month < 1 || month > 12)
				return false;
			
			// 입력된 일(day)의 값이 일의 범위를 초과하는 경우: 유효하지 않은 날짜
			if(day < 1 || day > LAST_DAY_OF_MONTH[month-1])
				return false;
			
		} catch(NumberFormatException e) {	// 예외처리 - Integer.parseInt() 오류
			return false;
		}

		return true;
	}
	
}
