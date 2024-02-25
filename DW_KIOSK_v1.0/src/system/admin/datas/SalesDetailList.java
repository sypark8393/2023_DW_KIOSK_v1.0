package system.admin.datas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class SalesDetailList {
	
	private static final String FILE_DIRECTORY = "src//files//sales_datas//";
	
	public static final int INDEX_ORDER_NO = 0;
	public static final int INDEX_TYPE = 1;
	public static final int INDEX_ID = 2;
	public static final int INDEX_NAME = 3;
	public static final int INDEX_UNIT_PRICE = 4;
	public static final int INDEX_QUANTITY = 5;
	public static final int INDEX_PRICE = 6;
	
	private static final HashMap<String, String> TYPES = new HashMap<String, String>() { {put("메뉴", "M"); put("포인트", "P"); put("쿠폰", "C"); } };
	
	private String date = null;
	private String fileName = "sales_detail";

	private Scanner sc = null;
	
	// 생성자
	public SalesDetailList(Scanner sc) {
		this.sc = sc;
	}
		
	// 파일명 설정
	/* 메소드명: setFileName
	 * 파라미터: String date
	 * 		  date: 매출일자
	 * 반환값: 없음
	 * 기능 설명: 파리미터로 받은 매출일자 정보를 이용해 사용할 상세 매출 내역 파일의 이름을 설정한다.
	 */
	public void setFileName(String date) {
		// src//files//sales_datas//sales_detail(YYYYMMDD).txt
		
		this.date = date;
		fileName = FILE_DIRECTORY + fileName + "(" + date + ").txt";
	}
	
	// 날짜 반환
	/* 메소드명: toDateFormat
	 * 파라미터: 없음
	 * 반환값: "YYYY년 MM월 DD일"
	 * 기능 설명: 현재 객체에 저장되어 있는 매출일자(YYYYMMDD) 정보를 "YYYY년 MM월 DD일" 형식으로 변환하여 출력한다.
	 */
	public String toDateFormat() {
		// YYYY년 MM월 DD일 형식으로 반환
		if (date.length()!=8) {
			return date;
		}
		return date.substring(0, 4) + "년 " + date.substring(4, 6) + "월 " + date.substring(6) + "일";
	}
	
	// 전체 출력
	/* 메소드명: printWholeDatas
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: src//files//sales_datas에 존재하는 상세 매출 내역 파일(src//files//sales_datas//sales_detail(YYYYMMDD).txt) 중
	 * 		   YYYYMMDD가 AdminSalesDetailProcess-55에서 입력받은 date와 동일한 파일 내용 전체 출력
	 */
	public void printWholeDatas() {
		ArrayList<String[]> salesDetalList = new ArrayList<String[]>();
		BufferedReader in;	// 입력 스트림
		
		String s;				// 파일에서 읽어온 값(한 줄)이 저장될 변수
		String[] tmp;			// 읽어온 값을 구분자로 분리하여 저장할 배열
	
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(fileName));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {  // 상세 매출 내역 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.printf("%s의 상세 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
			return;
		}
		
		// 파일에서 읽어온 값이 있는 동안
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");
				salesDetalList.add(tmp);
			}
			
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		
		// 파일은 존재하나 파일 안에 내용이 없는 경우
		if(salesDetalList.size() == 0) {
			System.out.println("출력할 내용이 없습니다.");
			return;
		}
		
		// 출력 안내 문구
		System.out.printf("%s의 상세 매출 내역 %d건 전체를 출력합니다.\n", toDateFormat(), salesDetalList.size());
		
		int totalPage = (salesDetalList.size() % 15 != 0)? salesDetalList.size() / 15 + 1: salesDetalList.size() / 15;	// 한 번에 15건씩 출력한다고 가정할 때의 총 페이지 수
		int pointer = 0; // 리스트에 저장되어 있는 각 요소에 접근하기 위한 포인터
		
		// 모든 페이지 내용을 출력하기 위해 반복
		for(int currentPage=0; currentPage<totalPage; currentPage++) {
			System.out.printf("(%d / %d)\n", (currentPage+1), totalPage);	// (현재 페이지 / 전체 페이지 수)
			System.out.println("===========================================================================================");
			System.out.println("주문번호         유형  ID               내용                                    단가   수량     금액");
			System.out.println("===========================================================================================");
			
			// 리스트에 저장되어 있는 요소를 15개씩 출력
			for( ; pointer<15*(currentPage+1); pointer++) {
				if(pointer == salesDetalList.size())
					break;
				
				System.out.println(toPrintTemplate(salesDetalList.get(pointer)));	// 출력 양식에 맞게 출력

			}
			System.out.println("===========================================================================================");
			
			if(currentPage != (totalPage -1)) {
				System.out.print("다음 페이지를 확인하시려면 [Enter]를 입력해주세요\n");
				sc.nextLine();
			}
			
		}
	}
	
	// 조회
	/* 메소드명: searchDatas
	 * 파라미터: int index, String keyword
	 * 		  index: 칼럼의 인덱스 (ex. 2: 두 번째 칼럼)
	 * 		  keyword: 조회할 내용
	 * 반환값: 없음
	 * 기능 설명: 1. src//files//sales_datas에 존재하는 상세 매출 내역 파일(src//files//sales_datas//sales_detail(YYYYMMDD).txt) 중
	 * 			  YYYYMMDD가 AdminSalesDetailProcess-55에서 입력받은 date와 동일한 파일 찾기
	 * 		   2. 1.에서 찾은 파일의 상세 매출 내역 목록 중 AdminSalesDetailProcess에서 입력받은 keyword가 index에 배정된 값과 같은 
	 * 			  상세 매출 내역을 찾아서 출력
	 */
	public void searchDatas(int index, String keyword) {
		ArrayList<String[]> salesDetalList = new ArrayList<String[]>();
		BufferedReader in;	// 입력 스트림
		
		String s;				// 파일에서 읽어온 값(한 줄)이 저장될 변수
		String[] tmp;			// 읽어온 값을 구분자로 분리하여 저장할 배열
		String type = keyword; 	// 유형으로 조회하는 경우 유형의 종류(메뉴, 포인트, 쿠폰)
		
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(fileName));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {  // 상세 매출 내역 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.printf("%s의 상세 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
			return;
		}
		
		// 유형으로 조회하는 경우 각 유형에 따른 코드값(메뉴:M, 포인트:P, 쿠폰:C)으로 키워드 변경
		if(index == INDEX_TYPE) {
			keyword = TYPES.get(type);
		}
		
		// 파일에서 읽어온 값이 있는 동안
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");
				
				if(tmp[index].equals(keyword)) {	// index번째 값이 키워드가 일치하는 경우에만 리스트에 추가
					salesDetalList.add(tmp);
				}
			}
			
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생하였습니다.");
			return;

		}
			
		// 파일은 존재하나 파일 안에 내용이 없는 경우
		if(salesDetalList.size() == 0) {
			System.out.println("조회된 내용이 없습니다.");
			return;
		}
		
		// 조회 기준 별 출력 안내 문구
		if(index == INDEX_ORDER_NO) {
			System.out.printf("%s의 상세 매출 내역 중 주문번호가 [%s]인 데이터가 %d건 조회되었습니다.\n", toDateFormat(), keyword, salesDetalList.size());
		} else if(index == INDEX_TYPE) {
			System.out.printf("%s의 상세 매출 내역 중 유형이 [%s]인 데이터가 %d건 조회되었습니다.\n", toDateFormat(), type, salesDetalList.size());
		} else if(index == INDEX_ID) {
			System.out.printf("%s의 상세 매출 내역 중 ID가 [%s]인 데이터가 %d건 조회되었습니다.\n", toDateFormat(), keyword, salesDetalList.size());
		}
		
		int totalPage = (salesDetalList.size() % 15 != 0)? salesDetalList.size() / 15 + 1: salesDetalList.size() / 15;	// 한 번에 15건씩 출력한다고 가정할 때의 총 페이지 수
		int pointer = 0; // 리스트에 저장되어 있는 각 요소에 접근하기 위한 포인터

		// 모든 페이지 내용을 출력하기 위해 반복
		for(int currentPage=0; currentPage<totalPage; currentPage++) {
			System.out.printf("(%d / %d)\n", (currentPage+1), totalPage);	// (현재 페이지 / 전체 페이지 수)
			System.out.println("===========================================================================================");
			System.out.println("주문번호         유형  ID               내용                                    단가   수량     금액");
			System.out.println("===========================================================================================");

			for( ; pointer<15*(currentPage+1); pointer++) {
				if(pointer == salesDetalList.size())
					break;
				
				System.out.println(toPrintTemplate(salesDetalList.get(pointer)));	// 출력 양식에 맞게 출력

			}
			System.out.println("===========================================================================================");
			
			if(currentPage != (totalPage -1)) {
				System.out.print("다음 페이지를 확인하시려면 [Enter]를 입력해주세요\n");
				sc.nextLine();
			}
			
		}
	}

	// 매개변수로 받은 문자열 배열을 출력 양식의 문자열로 변환 후 반환
	/* 메소드명: toPrintTemplate
	 * 파라미터: String[] datas
	 * 		  datas: 상세 매출 내역 파일에서 읽어온 한 줄의 문자열
	 * 반환값: String str
	 * 		 str: 상세 매출 내역의 각 데이터를 출력 양식에 맞게 변환한 문자열
	 * 기능 설명: 구분자(|)로 구분되어 있는 상세 매출 내역 데이터를 받아 출력 양식에 맞는 형태로 변환하여 반환한다.
	 */
	private String toPrintTemplate(String[] datas) {
		String str = "";
		
		str += datas[INDEX_ORDER_NO];
		str += "  " + datas[INDEX_TYPE];
		str += "   " + String.format("%-15s", datas[INDEX_ID]);
		str += "  " + String.format("%-30s", datas[INDEX_NAME]);
		str += "\t" + String.format("%,5d", Integer.parseInt(datas[INDEX_UNIT_PRICE]));
		str += "  " + String.format("%3d", Integer.parseInt(datas[INDEX_QUANTITY]));
		str += "  " + String.format("%,7d", Integer.parseInt(datas[INDEX_PRICE]));

		return str;
	}

	// CSV 파일로 내보내기
	/* 메소드명: toCsvFile
	 * 파라미터: String path
	 * 		  path: 상세 매출 내역 파일을 csv로 내보내기할 경로
	 * 반환값: String csvFileName
	 * 		 csvFileName: csv파일이 정상적으로 생성되었을 때의 파일 위치
	 * 기능 설명: csv 파일을 내보내기할 경로를 받아 해당 위치의 상세 매출 내역의 csv파일의 생성한다.
	 */
	public String toCsvFile(String path) {
		BufferedReader in;	// 입력 스트림
		PrintWriter out;	// 출력 스트림
		
		String csvFileName = Paths.get(path,"sales_detail(" + date + ").csv").toString(); // 경로에 파일명-sales_total(날짜).csv- 추가
		
		try {
			in = new BufferedReader(new FileReader(fileName)); // 파일 내용을 읽어오기 위한 스트림
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.printf("%s의 상세 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
			
			// 오류 발생 시 생성중이던 csv 파일은 삭제
			File csvFile = new File(csvFileName);
			csvFile.delete();
			
			return "";
		}
		
		try {
			out = new PrintWriter(new FileWriter(csvFileName));	// 파일 내용을 출력하기 위한 스트림
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			System.out.println("경로가 올바르지 않습니다.");
			
			// 오류 발생 시 생성중이던 csv 파일은 삭제
			File csvFile = new File(csvFileName);
			csvFile.delete();
			
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생하였습니다.");
			
			// 오류 발생 시 생성중이던 csv 파일은 삭제
			File csvFile = new File(csvFileName);
			csvFile.delete();
			
			return "";
		}
		
		// 상단 라벨 내용 출력
		String label = "주문번호,유형,ID,내용,단가,수량,금액";
		out.println(label);
		
		String s;		// 파일에서 읽어온 값(한 줄)이 저장될 변수
		String[] tmp;	// 읽어온 값을 구분자로 분리하여 저장할 배열
		
		// 파일에서 읽어온 값이 있는 동안
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");		// 문자열을 구분자로 분리하고
				s = String.join(",", tmp);	// csv 파일의 구분자인 쉼표(,)로 다시 통합
			
				out.println(s);				// 쉼표로 통합된 문자열을 파일에 출력
			}
			
			// 스트림 닫기
			in.close();
			out.close();
			
		} catch (IOException e) {
			System.out.println("입출력 오류가 발생하였습니다.");

			// 오류 발생 시 생성중이던 csv 파일은 삭제
			File csvFile = new File(csvFileName);
			csvFile.delete();
			
			return "";
		}

		//  readme.txt 파일(csv 파일 사용 시 안내사항) 관련 처리
		Path orgGuideFilePath = Paths.get(FILE_DIRECTORY,"readme.txt"); // readme.txt 파일 경로
		Path newGuideFilePath = Paths.get(path, "readme.txt");			// 새로 생설될 readme.txt 파일 경로
		
		try {
			Files.copy(orgGuideFilePath, newGuideFilePath, StandardCopyOption.REPLACE_EXISTING);	// 파일 복사
			
		} catch (FileNotFoundException e) {	// readme.txt 파일이 없는 경우
			System.out.println("readme.txt 파일 경로가 올바르지 않습니다.");
			return "";
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생하여 readme.txt 파일이 정상적으로 생성되지 않았습니다.");
			return "";
			
		}

		return csvFileName;
	}
}
