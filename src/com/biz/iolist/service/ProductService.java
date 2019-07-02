package com.biz.iolist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import com.biz.iolist.config.DBConnection;
import com.biz.iolist.dao.ProductDao;
import com.biz.iolist.model.ProductVO;

public class ProductService {

	SqlSession sqlSession = null;
	Scanner scan = null;
	ProductDao ProDao = null;

	public ProductService() {
		sqlSession = DBConnection.getSessionFactory().openSession(true);
		scan = new Scanner(System.in);
		ProDao = (ProductDao) sqlSession.getMapper(ProductDao.class);
	}

	public void viewProduct() {
		System.out.println("================================================================");
		System.out.println("상품정보 리스트");
		System.out.println("----------------------------------------------------------------");
		System.out.println("상품코드\t상품이름\t매입금액\t매출금액\t");
		System.out.println("----------------------------------------------------------------");
		List<ProductVO> proList = ProDao.selectAll();
		for (ProductVO vo : proList) {
			System.out.printf("%s\t", vo.getP_code());
			System.out.printf("%s\t", vo.getP_name());
			System.out.printf("%d\t", vo.getP_iprice());
			System.out.printf("%d\t\n", vo.getP_oprice());

		}
		System.out.println("----------------------------------------------------------------");
	}

	public boolean insertPRO() {
		List<ProductVO> pList = ProDao.selectAll();
		while (true) {
			System.out.print("상품코드 입력 >>");
			String strPcode = scan.nextLine();
			int intInspir = 0;
			for (ProductVO vo : pList) {
				if (vo.getP_code().equals(strPcode)) {
					System.out.println("중복된 값입니다.");
					intInspir += 1;
				}

			}
			if (intInspir > 0) {
				continue;
			}
			System.out.print("상품이름 입력 >>");
			String strProduct = scan.nextLine();
			System.out.print("매입금액 입력 >>");
			String strIprice = scan.nextLine();
			int intIprice = Integer.valueOf(strIprice);
			System.out.print("매출금맥 입력 >>");
			String strOprice = scan.nextLine();
			int intOprice = Integer.valueOf(strOprice);

			ProductVO vo = new ProductVO(strPcode, strProduct, intIprice, intOprice);

			if (ProDao.insert(vo) > 0) {
				System.out.println("입력 완료");
				return true;
			} else {
				System.out.println("입력 실패 ");
				return false;
			}
		}
		
	}

	public boolean updatePRO() {
		while (true) {
			System.out.println("=============");
			System.out.println("상품정보 변경");
			System.out.println("-------------");
			System.out.print("상품코드 입력 >>");
			String strPcode = scan.nextLine();
			ProductVO vo = ProDao.findByNum(strPcode);
			if (vo == null) {
				System.out.println("상품정보가 없습니다.");
				continue;
			}
			System.out.printf("상품이름 입력 %s>>", vo.getP_name());
			String strName = scan.nextLine();
			if (strName.isEmpty())
				strName = vo.getP_name();

			System.out.printf("매입금액 입력 %d>>", vo.getP_iprice());
			String strIprice = scan.nextLine();
			int intIprice = 0;
			if (strIprice.isEmpty())
				intIprice = vo.getP_iprice();
			else
				intIprice = Integer.valueOf(strIprice);
			System.out.printf("매출금액 입력 %d>>", vo.getP_oprice());
			String strOprice = scan.nextLine();
			int intOprice = 0;
			if (strOprice.isEmpty())
				intOprice = vo.getP_oprice();
			else
				intOprice = Integer.valueOf(intOprice);

			vo.setP_code(strPcode);
			vo.setP_name(strName);
			vo.setP_iprice(intIprice);
			vo.setP_oprice(intIprice);

			if (ProDao.update(vo) > 0) {
				System.out.println("업데이트 성공");
				return true;
			} else {
				System.out.println("업데이트 실패");
				return false;
			}
		}

	}



	public boolean deletePRO() {
		System.out.print("삭제할 거래 내역 >>");
		String strPcode = scan.nextLine();

		ProductVO vo = ProDao.findByNum(strPcode);
		System.out.println(vo);
		System.out.println("정말 삭제하겠습니까? (yes)");
		String strYes = scan.nextLine();
		if (strYes.equals("yes")) {
			if (ProDao.delete(strPcode) > 0) {
				System.out.println("삭제되었습니다.");
				return true;
			} else {
				System.out.println("삭제가 되지 않았습니다.");
				return false;
			}

		}
		return false;
	}

	public void mainStart() {

		while (true) {
			viewProduct();

			System.out.println("=================================");
			System.out.println("1.상품등록 2.상품변경 3.상품삭제 4.종료");
			System.out.print("업무 선택 >>");
			String strMenu = scan.nextLine();
			int intMenu = 0;
			try {
				intMenu = Integer.valueOf(strMenu);
			} catch (Exception e) {
				System.out.println("숫자만 입력하세요.");
				continue;
			}
			
			
			
			if (intMenu == 1)
				insertPRO();
			if (intMenu == 2)
				updatePRO();
			if (intMenu == 3)
				deletePRO();
			if (intMenu == 4)
				return;

		}
	}
}
