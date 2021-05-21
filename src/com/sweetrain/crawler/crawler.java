package com.sweetrain.crawler;


public class crawler {
	public static void main(String[] args) {
		com.sweetrain.crawler.TVInforCrawler c1 = new com.sweetrain.crawler.TVInforCrawler(); // 채널 편성 정보
		//System.out.println(c1.showProgramSchedule("런닝맨"));
		//System.out.println(c1.showCharactersInfor("태양의 후예"));
		//System.out.println(c1.showProgramInfor("태양의 후예"));
		System.out.println(c1.showChannelSchedule("SBS", "20210522"));
		
	}

}



