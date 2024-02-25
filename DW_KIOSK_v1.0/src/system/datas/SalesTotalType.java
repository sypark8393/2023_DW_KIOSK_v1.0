package system.datas;

import java.io.*;

public class SalesTotalType {
	private static final String FILE_DIRECTORY = "src//files//sales_datas//";

	public static final int INDEX_TYPE = 0;
	public static final int INDEX_WAIT_NO = 1;
	public static final int INDEX_TRANS_DT = 2;
	public static final int INDEX_ORDER_NO = 3;
	public static final int INDEX_TOTAL_AMT = 4;
	public static final int INDEX_SUPPLY_AMT = 5;
	public static final int INDEX_VAT = 6;
	public static final int INDEX_PAY_METHOD = 7;
	public static final int INDEX_DISCOUNT_ID = 8;
	public static final int INDEX_CARD_NAME = 9;
	public static final int INDEX_CARD_NO = 10;
	public static final int INDEX_CARD_QUOTA = 11;
	public static final int INDEX_AUTH_CODE = 12;
	public static final int INDEX_MEMBER_ID = 13;
	public static final int INDEX_REWARD_PTS = 14;
	public static final int INDEX_TOTAL_PTS = 15;
	
	private String fileName = "sales_total";
	
	private String type = " ";			// 유형 (매장 / 포장)
	private String waitNo = " ";		// 대기번호
	private String transDt = " ";		// 거래일시
	private String orderNo = " ";		// 주문번호
	private int totalAmt = 0;			// 거래 금액
	private int supplyAmt = 0;			// 공급가액
	private int vat = 0;				// 부가세
	private String payMethod = " ";		// 결제 방법
	private String discountId = " ";	// 포인트 전액 결제 건의 회원 ID 혹은 쿠폰 전액 결제 건의 쿠폰 ID
	private String cardName = " ";		// 카드사명
	private String cardNo = " ";		// 카드번호
	private String cardQuota = " ";		// 할부개월
	private String authCode = " ";		// 승인번호
	private String memberId = " ";		// 회원 ID
	private int rewardPts = 0;			// 적립 포인트
	private int totalPts = 0;			// 누적 포인트

	// getter & setter
	public String getFileName() {
		return fileName;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getWaitNo() {
		return waitNo;
	}
	
	public String getTransDt() {
		return transDt;
	}
	
	public void setTransDt(String transDt) {
		this.transDt = transDt;
		
		// 거래일시의 값이 설정되면 거래일자를 이용해 설정되는 파일명(fileName), 대기번호(waitNo), 주문번호(orderNo)의 값을 설정
		setFileNameNWaitNoNOrderNo();
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	
	public int getTotalAmt() {
		return totalAmt;
	}
	
	public void setTotalAmt(int totalAmt) {
		this.totalAmt = totalAmt;
		
		// 거래 금액이 설정되면 공급가액, 부가세는 자동으로 계산되어 설정됨
		supplyAmt = (int)(totalAmt * 0.9);
		vat = (int)(totalAmt * 0.1);
	}
	
	public int getSupplyAmt() {
		return supplyAmt;
	}
	
	public int getVat() {
		return vat;
	}
	
	public String getPayMethod() {
		return payMethod;
	}
	
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	public String getDiscountId() {
		return discountId;
	}
	
	public void setDiscountId(String discountId) {
		this.discountId = discountId;
	}
	
	public String getCardName() {
		return cardName;
	}
	
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	public String getCardNo() {
		return cardNo;
	}
	
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public String getCardQuota() {
		return cardQuota;
	}
	
	public void setCardQuota(String cardQuota) {
		this.cardQuota = cardQuota;
	}
	
	public String getAuthCode() {
		return authCode;
	}
	
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	public String getMemberId() {
		return memberId;
	}
	
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public int getRewardPts() {
		return rewardPts;
	}
	
	public void setRewardPts(int rewardPts) {
		this.rewardPts = rewardPts;
	}
	
	public int getTotalPts() {
		return totalPts;
	}
	
	public void setTotalPts(int totalPts) {
		this.totalPts = totalPts;
	}
	
	// 대기번호, 주문번호 설정
	/* 메소드명: setFileNameNWaitNoNOrderNo
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 현재 설정되어 있는 거래일시 정보에서 거래일자 정보를 추출하여 파일명(fileName), 대기번호(waitNo), 주문번호(orderNo) 설정
	 */
	public void setFileNameNWaitNoNOrderNo() {
		BufferedReader in;	// 입력 스트림
		
		String trnadDate = transDt.substring(0, 8);
		this.fileName = FILE_DIRECTORY + this.fileName + "(" + trnadDate + ").txt";
		
		int count = 1;
		
		try {
			in = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			
			// 거래일의 매출 데이터가 없는 경우 현재 주문이 거래일의 첫 주문이므로 대기번호는 0001
			
			waitNo = String.format("%04d", count);				// 대기번호
			orderNo = transDt.substring(0, 8) + '-' + waitNo;	// 주문번호 (거래일자 + 대기번호)
			
			return;
		}
		
		// 거래일의 매출 데이터를 확인해서 발생한 거래 건수를 카운트 -> 종료 후에는 현재 진행중인 건의 주문번호를 가지게 됨
		try {
			while (in.readLine() != null) {
				count++;
			}
			in.close();

		} catch (Exception e) {
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		
		waitNo = String.format("%04d", count);				// 대기번호
		orderNo = transDt.substring(0, 8) + '-'  + waitNo;	// 주문번호 (거래일자 + 대기번호)
	}
	
	// txt 파일 데이터 양식에 맞는 문자열로 변환하여 반환
	/* 메소드명: toDataTemplate
	 * 파라미터: 없음
	 * 반환값: String str
	 * 		 str: 실제 sales_total(YYYYMMDD).txt 파일에 작성되는 형식으로 구성된 한 줄의 문자열
	 * 기능 설명: 현재 객체가 가진 멤버 변수들의 값을 이용해 데이터 파일 형식에 맞는 한 줄의 문자열을 만들어 반환한다.
	 */
	public String toDataTemplate() {
		String str = "";
		str += type + "|" + waitNo + "|" + transDt + "|" + orderNo + "|";
		str += totalAmt + "|" + supplyAmt + "|" + vat + "|" + payMethod + "|";
		str += discountId + "|" + cardName + "|" + cardNo + "|" + cardQuota + "|";
		str += authCode + "|" + memberId + "|" + rewardPts + "|" + totalPts;
		
		return str;
	}
	
}
