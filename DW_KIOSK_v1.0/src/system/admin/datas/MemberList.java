package system.admin.datas;

import java.io.*;
import java.util.*;

import system.datas.MemberType;

public class MemberList {
	private Scanner sc=null;
	
	//생성자
	public MemberList(Scanner sc) {
		this.sc=sc;
	}
	
	/* 메소드명: printWholeMember
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: MemberType 클래스에 지정된 경로에 존재하는 src//files//datas//member.txt파일의 내용 전체 출력
	 */
	public void printWholeMember() {
		ArrayList<MemberType> memberList=new ArrayList<MemberType>();//MemberType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MemberType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("회원 파일을 찾을 수 없습니다.");
			return;
		}
		
		try {
			while ((s = in.readLine()) != null) {
				// System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				MemberType member = new MemberType(tmp[MemberType.INDEX_MEMBER_ID], 
						Integer.parseInt(tmp[MemberType.INDEX_POINT]), tmp[MemberType.INDEX_BIRTH]);

				memberList.add(member);
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		//출력
		System.out.println("현재 저장되어 있는 회원 목록을 출력합니다.");
		System.out.println("=================================================");
		System.out.println("         회원ID             포인트           생일");
		System.out.println("=================================================");
		for (int i = 0; i < memberList.size(); i++) {
			memberList.get(i).printAdminMember();
		}
		System.out.println("=================================================");
	}
	
	/* 메소드명: searchMemberById
	 * 파라미터: MemberType.INDEX_MEMBER_ID, searchedMemberId
	 * 반환값: 없음
	 * 기능 설명: MemberType 클래스에 지정된 경로에 존재하는 src//files//datas//member.txt파일의 내용 중
	 * 		   searchedMemberId와 MemberType.INDEX_MEMBER_ID가 동일한 회원 목록을 찾아 출력
	 */
	public void searchMemberById(int index,String keyword) {
		ArrayList<MemberType> memberList=new ArrayList<MemberType>();//MemberType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;//searchedMemberId와 MemberType.INDEX_MEMBER_ID가 동일한 회원 목록 발견했는지 확인할 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MemberType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("회원 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[MemberType.INDEX_MEMBER_ID].equals(keyword)) {//searchedMemberId와 MemberType.INDEX_MEMBER_ID가 동일한 회원 목록 발견
					found=true;
					MemberType member = new MemberType(tmp[MemberType.INDEX_MEMBER_ID], 
							Integer.parseInt(tmp[MemberType.INDEX_POINT]),tmp[MemberType.INDEX_BIRTH]);

					memberList.add(member);
				}

			}
			in.close();
			if (found==false) {//searchedMemberId와 MemberType.INDEX_MEMBER_ID가 동일한 회원 목록 발견하지 못하면
				System.out.printf("회원 ID가 %s인 회원을 찾을 수 없습니다.\n",keyword);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
			
		}
		if (found==true) {//searchedMemberId와 MemberType.INDEX_MEMBER_ID가 동일한 회원 목록 발견했으면 출력
			System.out.printf("회원 ID가 %s인 회원 목록입니다.\n",keyword);
			System.out.println("=================================================");
			System.out.println("         회원ID             포인트           생일");
			System.out.println("=================================================");
			for (int i = 0; i < memberList.size(); i++) {
				memberList.get(i).printAdminMember();
			}
			System.out.println("=================================================");
		}
	}
	
	/* 메소드명: searchMemberByBirth
	 * 파라미터: MemberType.INDEX_BIRTH, searchedBirth
	 * 반환값: 없음
	 * 기능 설명: MemberType 클래스에 지정된 경로에 존재하는 src//files//datas//member.txt파일의 내용 중
	 * 		   searchedBirth와 MemberType.INDEX_BIRTH가 동일한 회원 목록을 찾아 출력
	 */
	public void searchMemberByBirth(int index,String keyword) {
		ArrayList<MemberType> memberList=new ArrayList<MemberType>();//MemberType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;//earchedBirth와 MemberType.INDEX_BIRTH가 동일한 회원 목록 발견했는지 확인할 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MemberType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("회원 파일을 찾을 수 없습니다.");
			return;
		}
		
		try {
			while ((s = in.readLine()) != null) {
//				System.out.println(s);

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[MemberType.INDEX_BIRTH].equals(keyword)) {//earchedBirth와 MemberType.INDEX_BIRTH가 동일한 회원 목록 발견
					found=true;
					MemberType member = new MemberType(tmp[MemberType.INDEX_MEMBER_ID], 
							Integer.parseInt(tmp[MemberType.INDEX_POINT]),tmp[MemberType.INDEX_BIRTH]);

					memberList.add(member);
				}

			}
			in.close();
			if (found==false) {//earchedBirth와 MemberType.INDEX_BIRTH가 동일한 회원 목록 발견하지 못하면
				System.out.printf("생일이 %s인 회원을 찾을 수 없습니다.\n",keyword);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {//earchedBirth와 MemberType.INDEX_BIRTH가 동일한 회원 목록 발견했으면 출력
			System.out.printf("생일이 %s인 회원 목록입니다.\n",keyword);
			System.out.println("=================================================");
			System.out.println("         회원ID             포인트           생일");
			System.out.println("=================================================");
			for (int i = 0; i < memberList.size(); i++) {
				memberList.get(i).printAdminMember();
			}
			System.out.println("=================================================");
		}
	}

}
