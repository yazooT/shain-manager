package dataObject;

/**
 * 性別を取り扱う列挙型
 *
 */
public enum Gender {
	MALE("男性"), FEMALE("女性");

	public String text;

	private Gender(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
