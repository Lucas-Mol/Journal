package com.journal.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Post.class)
public abstract class Post_ {

	public static volatile SingularAttribute<Post, Date> latestDate;
	public static volatile SingularAttribute<Post, Integer> id;
	public static volatile SingularAttribute<Post, Label> label;
	public static volatile SingularAttribute<Post, User> user;
	public static volatile SingularAttribute<Post, String> content;

	public static final String LATEST_DATE = "latestDate";
	public static final String ID = "id";
	public static final String LABEL = "label";
	public static final String USER = "user";
	public static final String CONTENT = "content";

}

