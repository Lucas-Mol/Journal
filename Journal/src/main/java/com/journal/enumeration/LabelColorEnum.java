package com.journal.enumeration;

public enum LabelColorEnum {
	
	RED(1,"#FF0000"),
	GREEN(2,"#00FF00"),
	BLUE(3,"#0000FF");

	private Integer id;
	private String color;
	
	private LabelColorEnum(Integer id, String color) {
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


	public static LabelColorEnum getStatusIntimacaoById(Integer id) {
		for (LabelColorEnum labelColor: LabelColorEnum.values()) {
			if (id.equals(labelColor.getId())) {
				return labelColor;
			}
		}
		throw new IllegalArgumentException("No matching type for id " + id);
	}

}
