package com.journal.enumeration;

public enum ColorEnum {
	
	RED(0, "#DF2231"),
	GREEN(1,"#839F18"),
	BLUE(2, "#1E9AFF"),
	YELLOW(3, "#FFD700"),
	PINK(4, "#FF006F"),
	PURPLE(5, "#873cbb"),
	ORANGE(6, "#F97613"),
	BROWN(7, "#654321"),
	GOLD(8, "radial-gradient(ellipse farthest-corner at right bottom, #FEDB37 0%, #FDB931 8%, #9f7928 30%, #8A6E2F 40%, transparent 80%)," +
						"radial-gradient(ellipse farthest-corner at left top, #FFFFFF 0%, #FFFFAC 8%, #D1B464 25%, #5d4a1f 62.5%, #5d4a1f 100%)"),
	WHITE(9, "#FAFAFA");

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
