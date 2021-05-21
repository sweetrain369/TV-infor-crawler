package com.sweetrain.crawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



class TVInforCrawler {
	/* 필드 */
	Document doc;
	Map <String, String> channels_public = new HashMap<>(); 
	String ads_public_front = "https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=";
	String ads_public_back = "&key_depth3=";
	Elements cmm_boxs = null;
	String cur_time = "";
	String url = "";	
	Elements cm_tab_info_box = null;
	String characters = "";
	
	private String [] names= {"EBS", "SBS", "MBC", "KBS2", "KBS1", "tvN", "JTBC2", "E채널", "KBS joy", "SBS fun E", "XtvN", "SPOTV", "OCN", "OCN Movies", "Mnet", "JTBC2", "Tooniverse"};
	private String [] urls = {"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=15&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=14&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=13&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=12&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=12&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=872&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=874&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=886&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=880&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=882&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=185&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5900&key_depth2=125&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5800&key_depth2=178&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5800&key_depth2=187&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=6000&key_depth2=&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7800&key_depth2=&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=6600&key_depth2=&key_depth3="
		};

	
	/* --------------------------------- 오늘의 채널 편성표 ---------------------------- */
	String getCurTime() {
		Date time = new Date();
		SimpleDateFormat format_time = new SimpleDateFormat("HH:mm");
		cur_time = format_time.format(time);
		return cur_time;
	} 

	//////////////// 오늘 날짜 리턴 /////////////////
	String getDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format_date = new SimpleDateFormat("yyyyMMdd");
		String date = format_date.format(cal.getTime());
		
		return date;
	}
	
	////////////////// (채널, URL) MAP 함수 만들어주는 함수 //////////////////////
	Map<String, String> makeChansAdsList(String[] names, String[] urls) {
		for(int i=0; i<names.length; i++) {
			channels_public.put(names[i], urls[i]);
		}
		return channels_public;
	}

	
	////////////////// 사용자가 입력한 채널과 매칭하여 url 반환 ////////////////////////////
	String matchInputChans(String channel) {
		channels_public = makeChansAdsList(names, urls);
		if (channels_public.containsKey(channel)) {
			////////////////////// 입력 channel의 url의 반환 ////////////////////////
			return channels_public.get(channel);
		}
		return "채널 없음";
	}
	


	///////////////// 수집, 정제 후 채널 편성 정보 보여주는 함수 /////////////////////////
	String showChannelSchedule(String channel, String date) {
		String infor = "";
		url = matchInputChans(channel) + date;
		try {
			while(true) {
				String cur_time = getCurTime(); // 현재 시간
				System.out.println(url);
				doc = Jsoup.connect(url).get();
				
				cmm_boxs = doc.select("div.cmm_boxs");
				for(Element e: cmm_boxs.select("ol li")) {
					Elements flag_box = e.select("span.flag_box");
					flag_box.remove();
					Elements time=e.select("p.time");

					
					
					String today = getDate();

					if (today.equals(date)) {

					//////////////// 미래에 방송할 프로그램할 보여주기 (이미 방송한 프로그램 제거) ///////////////
						if(time.text().compareTo(cur_time)>0) {
							if(e.className().equals("list on")) continue;
							infor += (e.text());
							infor += "\n";
						}
						if(e.className().equals("list on")) { 
							Elements bar_wrap=e.select("div.bar_wrap");
							bar_wrap.remove();
							infor += (e.text()) + "- !!!!! 현재 방송 중 !!!!!! ";
							infor += "\n";
						}
					}
					
					//////////////// 사용자가 원하는 날짜와 오늘 날짜가 같지 않으면 정제할 필요가 없음 //////////
					else {
						infor += (e.text());
						infor += "\n";
					}
					
				}
				return infor;
			}
		}
		catch (IOException e) {
				return "error";
			}
	}
	
	
	
	/* --------------------------------- 프로그램 등장인물 함수 ---------------------------- */
	String showCharactersInfor(String program) {
		int per = 0;
		url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + program + "%EC%B6%9C%EC%97%B0%EC%A7%84";
		try {
			doc = Jsoup.connect(url).get();
			cm_tab_info_box = doc.select("div.cm_tab_info_box");
			// 예능 프로그램은 strong 태그에 연예인 이름이 있고, span 태그에 진행, 출연 단어가 있음
			// 드라마는 strong 태그에 역할 이름이 있고, span 배우 이름이 있기 때문에 필요한 코드 
			for(Element e:cm_tab_info_box.select("ul li div.title_box")) {
				Elements strong = e.select("strong");
				Elements span = e.select("span");
				/////////////////////// 드라마일 때 ////////////////////////////
				if(span.text().contains("진행")||span.text().contains("출연")) 
					characters += "' " + strong.text() + " '";
				/////////////////////// 예능일 때 /////////////////////////////
				else
					characters += "' " + span.text() + " '"; 
				
				///////////////////// 4명까지만 보여주기 ///////////////////////
				if (per > 4) break;
				System.out.println();
				per += 1;
			}
		characters += "👨‍👨\n\n"; 
		characters += "등이 출연합니다~ 👨‍👨‍👦‍👦 ‍ \n 어때요?! 더더욱 " + program + "이 보고 싶으시죠 ?? 🤭😁🧐";
			
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		return characters;
	
}



/* --------------------------------- 프로그램 정보 함수 ---------------------------- */
	String showProgramInfor(String program) {
		String program_infor = "";
		Elements cm_info_box = null;
		try {
			String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + program;
			doc = Jsoup.connect(url).get();
			cm_info_box = doc.select("div.cm_info_box");
			
			for(Element e:cm_info_box.select("div.detail_info div")) {
				if(e.hasClass("text_expand")&&e.hasClass("_ellipsis")&&e.hasClass("_img_ellipsis")) {
						Elements button=e.select("button");
						button.remove();
						program_infor = e.text();
						program_infor += " \n\n";
						program_infor += "함께 만나 보실래요 ??? 🌝🌞☺ 👉🏼👈🏼👀😊🤗";
					} 
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return program_infor;
	}



	/* --------------------------------- 프로그램 편성표 함수 ---------------------------- */
	String showProgramSchedule(String program) {
		
		Document doc;
		Elements cm_content_wrap = null;

		String str = "";
		String[] date=new String[100];
		String[] channel=new String[100];
		
		
		try {
			url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + program + "%ED%8E%B8%EC%84%B1%ED%91%9C";
			doc = Jsoup.connect(url).get();
			cm_content_wrap =doc.select("div.pack_group");
			int j=0;
			for(Element e:cm_content_wrap.select("div.table_scroll_wrap")) {// 전체
				if(e.hasClass("_scroll")) {
					for(Element e_channel:e.select("div.table_top_area>table.cm_table>thead>tr>th")) { // 채널
							if(channel[e_channel.elementSiblingIndex()]==null) channel[e_channel.elementSiblingIndex()]=e_channel.text();
							else channel[e_channel.elementSiblingIndex()]+=e_channel.text();
							//System.out.println(channel[e_channel.elementSiblingIndex()]+e_channel.elementSiblingIndex());
						}
						for(Element e_date:e.select("div.table_body_area>table.cm_table>tbody>tr")) {
							Elements number=e_date.select("span.number");
							number.remove();
							Element first=e_date.firstElementSibling();
							//System.out.println(first);
							for(Element f:first.select("td.align_left")) {
								if(date[f.elementSiblingIndex()]==null) date[f.elementSiblingIndex()]=f.text();
								else date[f.elementSiblingIndex()]+=f.text();

							}
							j+=1;
							if(j==1) break;
						}	
					}	
				}
			str = "오늘의 " + program + " 편성표를 알려드릴게요 !! 👩🏿‍💻 \n\n";
			for(int i=0; date[i]!=null; i++) {
				if(!date[i].equals("")) str += "🔊" + channel[i] + "🔊" + "\n" + "⏰" + date[i] + "⏰"+"\n";
				//System.out.println(channel[i]+"-"+date[i]);
			} return str;
		}
		catch(Exception e) {
			return "error";
		}
		
	}

}

	
