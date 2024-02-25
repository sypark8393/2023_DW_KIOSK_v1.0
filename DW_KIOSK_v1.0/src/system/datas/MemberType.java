package system.datas;

public class MemberType {
	public static final String FILE_NAME = "src//files//datas//member.txt";
	public static final String TMP_FILE_NAME = "src//files//datas//member_tmp.txt";
	
	public static final int INDEX_MEMBER_ID = 0;
	public static final int INDEX_POINT = 1;
	public static final int INDEX_BIRTH = 2;
	
	//필드명 추후 합의 필요
	//핸드폰번호와 생년월일 int형으로 선언할 시 맨 앞 0이 생략되버림 ex>09월 20일생 > 0920 > 920으로 출력
	private String memberId = null;	//핸드폰번호 
	private int point = 0;			//포인트
	private String birth = null;	//생년월일
	
	// 생성자
	public MemberType (String memberId, int point, String birth) {
		super();
		this.memberId=memberId;
		this.point=point;
		this.birth=birth;
	}

	//getter setter <= phoneNumber, point, dateOfBirth 선택
	public String getPhoneNumber() {
		return memberId;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.memberId = phoneNumber;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getbirth() {
		return birth;
	}

	public void setbirth(String birth) {
		this.birth = birth;
	}
	
	// 관리자 모드: 회원 목록 출력 메소드
	/* 메소드명: printAdminMember
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 관리자 모드에서 회원 목록을 양식에 맞게 출력한다. 
	 */
	public void printAdminMember() {
		System.out.print("      ");
		System.out.printf("%11s", this.memberId);
		System.out.print("\t");
		System.out.printf("%,6d", this.point);
		System.out.print("\t\t  ");
		System.out.printf("%4s", this.birth);
		
		System.out.println();
	}
}
