package org.nsy.test;

import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import org.junit.Test;
import org.nsy.test.Math;

import lombok.extern.log4j.Log4j;


@Log4j
public class LamdaTests {
	
	/**
	 * @FunctionalInterface 사용
	 * 
	 * 구현해야 할 추상 메소드가 하나만 정의된 인터페이스(2개의 메소드는 안됨)
	 */
	@Test
	public void LamdaTest() {
			Math plusLamda = (first, second) -> first + second;
			
			log.info(plusLamda.Calc(4, 2));		// 6
			
			
			Math minusLambda = (first, second) -> first - second;
			   
			System.out.println(minusLambda.Calc(4, 2));	//2
	}
	
	/**
	 * IntFunction<R>
	 * 
	 * int 값의 인수를 받아들이고 결과를 생성하는 함수
	 */
	@Test
	public void intFunctionTest() {
		IntFunction insum = (x) -> x+1;
		
		log.info(insum.apply(3));	//4
		
	}
	
	/**
	 * BinaryOperator<T>
	 * 
	 * 동일한 유형의 두 피연산자에 대한 연산을 나타내며 피연산자와 동일한 유형의 결과를 생성
	 * 
	 * https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html(다양한 인터페이스 목록)
	 */
	@Test
	public void binaryOperTest() {
		BinaryOperator stringSum = (x, y) -> x + " " + y;
		
		log.info(stringSum.apply("Welcome", "korea"));
	}
	
	/**
	 * Stream API를 이용한 간단한 짝수 판별
	 * 
	 * 예) example.stream().filter(x -> x < 2).count
	 * 
	 *  stream() <- 스트림생성
     *
	 *	filter < - 중간 연산 (스트림 변환) - 연속에서 수행 가능합니다.
     *
	 *	count <- 최종 연산 (스트림 사용) - 마지막에 단 한 번만 사용 가능합니다.
	 * 
	 */
	@Test
	public void streamTest() {
		IntStream.range(1, 11)
				.filter(i -> i%2 == 0)
				.forEach(System.out::println); //2 4 6 8 10
	}
	
	/**
	 *  0~1000까지의 값 중 500이상이며 짝수이면서 5의 배수인 수의 합을 구하라
	 */
	@Test
	public void streamTest2() {
		log.info( 
				IntStream.range(0, 1001)			//범위
						.skip(500)					//500이상
						.filter(i -> i%2 == 0)		//짝수
						.filter(i -> i%5 == 0)		//5의배수
						.sum()						//합
		);
	}
}
