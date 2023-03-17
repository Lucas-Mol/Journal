package com.journal.enumeration;

public enum ColorEnum {
	
	RED(1, "#A50F69"),
	GREEN(2,"#444725"),
	BLUE(3, "#1E9AFF"),
	YELLOW(4, "#FFE600");

	private Integer id;
	private String color;
	
	private ColorEnum(Integer id, String color) {
		this.id = id;
		this.color = color;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


	public static ColorEnum getStatusIntimacaoById(Integer id) {
		for (ColorEnum labelColor: ColorEnum.values()) {
			if (id.equals(labelColor.getId())) {
				return labelColor;
			}
		}
		throw new IllegalArgumentException("No matching type for id " + id);
	}

}
