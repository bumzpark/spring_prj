package skcc.nexcore.client.application.base;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * VO (Value Object) 객체에 대한 상위 클래스. VO 객체들의 비교 기능과 Reflection을 이용한 객체 정보 추출 기능을
 * 공통적으로 적용
 * 
 */
public class BaseVO implements Serializable {
	private static final long serialVersionUID = 8295608545574798629L;

	/**
	 * VO 객체의 내부 속성을 문자열로 변환해서 리턴
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * VO 객체의 내부 속성 값들이 일치하는지 확인
	 */
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	/**
	 * HashCode를 생성해서 리턴
	 */
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}