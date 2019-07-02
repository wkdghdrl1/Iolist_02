package com.biz.iolist.exec;

import com.biz.iolist.service.ProductService;

public class ProEx_02 {

		public static void main(String[] args) {
			
			ProductService ps = new ProductService();
			ps.viewProduct();
			ps.updatePRO();
		}
}	
