package org.zerock.sample;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import lombok.ToString;

//Setter 주입
//생성자 주입
//필드 주입

@Component
@ToString
@RequiredArgsConstructor
public class Restaurant {
	
	
	private final Chef chef;
	
}
