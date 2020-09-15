package com.example.demo;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserControllerTest {
   
	@Test
	public void before() {
		System.out.println("Before");
	}

	@Test
	public void after() {
		System.out.println("After");
	}

	@Test
	public static void beforeClass() {
		System.out.println("Before Class");
	}

	@Test
	public static void afterClass() {
		System.out.println("After Class");
	}
}