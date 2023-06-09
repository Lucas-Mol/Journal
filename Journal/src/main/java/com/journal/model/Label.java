package com.journal.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.journal.enumeration.ColorEnum;


@Entity
@Table(name = "tb_label")
public class Label implements Serializable{
	private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "color")
	private ColorEnum color;
	
	public Label() {}

	public Label(String name, ColorEnum color) {
		super();
		this.name = name;
		this.color = color;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ColorEnum getColor() {
		return color;
	}
	
	public void setColor(ColorEnum colorEnum) {
		this.color = colorEnum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Label other = (Label) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
