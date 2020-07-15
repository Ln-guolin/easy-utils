package cn.soilove.utils;


import com.alibaba.excel.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证
 * @author chenguolin
 *
 */
public class RegexUtils {
	
	public static boolean doMatch(String regex,String txt){
		return match(regex, txt);
	}
	
	public static String getString(String value,String reg){
		Matcher matcher = getMatcher(value,reg);
		String result ="";
		while (matcher.find()) {
			result =  matcher.group(0);
		}
		return result;
	}
	
	public static String getString(String value,String reg,int index){
		Matcher matcher = getMatcher(value,reg);
		String result ="";
		while (matcher.find()) {
			result =  matcher.group(index);
		}
		return result;
	}
	
	
	private static Matcher getMatcher(String value,String reg){
		Pattern pattern = Pattern.compile(reg);  
	    return  pattern.matcher(value.trim());
	}
	

	/**
	 * 执行器
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * 验证验证输入字母和数字
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isLetterAndNumber(String str) {
		String regex = "^[A-Z|a-z|0-9]+$";
		return match(regex, str);
	}
	/**
	 * 验证验证输入字母
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isLetter(String str) {
		String regex = "^\\d*+[A-Z|a-z]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字母和数字，减号，下划线
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isLetterAndNumberAndOther(String str) {
		String regex = "^[A-Z|a-z|0-9|\\-|\\_]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字母和中文
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isLetterAndChinese(String str) {
		String regex = "^[A-Z|a-z|\u4e00-\u9fa5]+$";
		return match(regex, str);
	}
	
	/**
	 * 验证验证输入字母和中文、数字
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isLetterORNumberOrChinese(String str) {
		String regex = "^[A-Z|a-z|0-9|\u4e00-\u9fa5]+$";
		return match(regex, str);
	}

	/**
	 * 验证输入汉字
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isChinese(String str) {
		String regex = "^[\u4e00-\u9fa5]+$";
		return match(regex, str);
	}

	/**
	 * 验证输入数字(整数)
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isNumber(String str) {
		String regex = "^[0-9]+$";
		return match(regex, str);
	}
	/**
	 * 校验输入参数是否存在一位小数点的浮点型数字
	 * @param target
	 * @return
	 */
	public static boolean isOneScaleNumber(String target){
		if(StringUtils.isEmpty(target)){
			return false;
		}

		try{
			Float f = Float.parseFloat(target);
			if(f > 100 || f < 0){
				return false;
			}

		}catch(Exception ex){
			return false;
		}
		String regex = "^[0-9]+(.[0-9]{1})?$";
		return match(regex, target);
	}
	/**
	 * 验证输入浮点数字
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isDoubleNumber(String str) {
		String regex = "^([0-9]+\\d*+[.][0-9]|[0-9])+$";
		return match(regex, str);
	}

	/**
	 * 验证邮箱
	 * 
	 * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isEmail(String str) {
		String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		return match(regex, str);
	}
	
	/**
	 * 是否存在特殊字符
	 * 
	 * @param str 待验证的字符串
	 * @return 
	 */
	public static boolean IsNotTS(String str) {
		String regex = "^[^|~|$|\"|'|<|>|&|?|*|~|{|}|[|]|+|#|‘|%|…|￥|`|·|—|=|【|】|;|’]+$";
		
		try {
			return match(regex, str);
		} catch (Exception e) {
			return false;
		}
		
	}
	/**
	 * 验证是否为合法ip
	 * @param str 待验证的ip字符串
	 * @return
	 */
	public static boolean isIp(String str) {
		String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		
		try {
			return match(regex, str);
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 手机验证
	 * @param phone
	 * @return 如果是符合的规则的手机,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isPhone(String phone){
//		String regex = "^0?(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])[0-9]{8}$";
		String regex = "^0?1(2|3|4|5|6|7|8|9)[0-9]{9}$";
		return match(regex, phone);
	}
	
	/**
	 * 密码验证，字母和数字组成
	 * 
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isPassword(String str) {
		String regex = "^[A-Z|a-z|0-9]+$";
		return match(regex, str);
	}
	/**
	 * 验证码格式验证，6位数字
	 * @param str	待验证的字符串
	 * @return	如果符合格式，返回<b>true</b>,否则返回<b>false</b>
	 */
	public static boolean isVcode(String str) {
		String regex = "^\\d{6}$";
		return match(regex, str);
	}
	
	/**
	 * 验证是否是整数
	 * @return
	 */
	public static boolean isRoundNum(String str) {
		String regex = "^\\-?\\d*$";
		return match(regex, str);
	}

	/**
	 * 邮箱校验
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isMail(String value) {
		String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		return match(regex, value);
	}
	
	/**
	 * 密码的设定为6-20位字母、数字下划线组成
	 */
	public static boolean isLoginPwd(String str) {
		String regex =  "[A-Za-z0-9_]+" ;
		return match(regex, str);
	}

	/**
	 * 过滤字符，仅保留数字、字母和中文
	 * @param str
	 * @return
	 */
	public static String replaceHasLetterORNumberOrChinese(String str){
		return str.replaceAll("[^A-Z|a-z|0-9|\u4e00-\u9fa5]", "");
	}
}
