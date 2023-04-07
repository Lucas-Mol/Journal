package com.journal.enumeration;

public enum ColorEnum {
	
	RED(0, "#A50F69"),
	GREEN(1,"#444725"),
	BLUE(2, "#1E9AFF"),
	YELLOW(3, "#FFE600");

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


	public static ColorEnum getColorById(Integer id) {
		for (ColorEnum labelColor: ColorEnum.values()) {
			if (id.equals(labelColor.getId())) {
				return labelColor;
			}
		}
		throw new IllegalArgumentException("No matching type for id " + id);
	}

}
