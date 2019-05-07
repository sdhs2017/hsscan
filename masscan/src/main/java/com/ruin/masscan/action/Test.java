package com.ruin.masscan.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		String str = "192";
//        String pattern = "(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}";

        
        String pattern="^[1-9]\\d*$";
        
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
//        System.out.println(m.matches());
        if (m.find()) {
			System.out.println(m.group(0));
		}
	}

}
