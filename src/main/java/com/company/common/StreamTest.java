package com.company.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class StreamTest {
	public static void main(String[] args) throws Exception {
//		
//		FileReader fr = new FileReader("C:\\temp\\hello.txt");
//		int c;
//		while( (c = fr.read()) != -1 ){
//			System.out.println((char)c);
//		}
//		fr.close();
		
		// Buffer 필터 추가하면 작업 속도 개선됨
		
		BufferedInputStream br = new BufferedInputStream( new FileInputStream("C:\\temp\\julmi.jpg"));
		BufferedOutputStream bw = new BufferedOutputStream( new FileOutputStream("C:\\temp\\julmi2.jpg")); // 파일 복사 기능
		//String line;
		int cnt; // 글자수
		while(true) {
			byte[] b = new byte[100];
			cnt = br.read(b);
			if(cnt == -1) break;
			bw.write(b);
		}
		br.close();
		bw.close();
	}
}
