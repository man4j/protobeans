package org.protobeans.hibernate.entity;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

@MappedSuperclass
@TypeDefs(@TypeDef(name = "json", typeClass = JsonStringType.class))
public class BaseEntity { /*empty*/ }
